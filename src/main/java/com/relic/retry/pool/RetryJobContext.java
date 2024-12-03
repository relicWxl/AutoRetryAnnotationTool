package com.relic.retry.pool;

import com.alibaba.fastjson.JSONObject;
import com.relic.retry.mybatis.pagehelper.PageHelperUtil;
import com.relic.retry.pool.observer.service.QueueIsEmptyObserverContext;
import com.relic.retry.service.RetryService;
import com.relic.retry.service.RetryServiceFacade;
import com.relic.retry.util.AssertUtil;
import com.relic.retry.util.CalculateUtil;
import com.relic.retry.util.SerializationUtil;
import com.relic.retry.SpringContext;
import com.relic.retry.pojo.consts.StateEnum;
import com.relic.retry.pojo.dto.RetryJobTypeDTO;
import com.relic.retry.pojo.model.RetryJobDO;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * RetryJobContext
 * <p>
 * 重试任务策略模式+模版方法模式实现类
 *
 * @author wxl
 */
@Slf4j
@Service
public class RetryJobContext {

    private final RetryService retryService;

    private final QueueIsEmptyObserverContext observerContext;

    private final RetryServiceFacade retryServiceFacade;

    public RetryJobContext(RetryService retryService, QueueIsEmptyObserverContext observerContext,
                           RetryServiceFacade retryServiceFacade) {
        this.retryService = retryService;
        this.observerContext = observerContext;
        this.retryServiceFacade = retryServiceFacade;
    }

    /**
     * 创建线程任务
     */
    public void createJob(String lockOwnerId) {
        PageHelperUtil.executeService(1, 200, () -> retryService.listAllJob(lockOwnerId),
                list -> {
                    List<RetryJobDO> canExecuteRetryJobDOList =
                            Optional.ofNullable(list).orElse(Collections.emptyList())
                                    .stream().filter(this::canExecute).collect(Collectors.toList());
                    // 更新任务状态为WAITING
                    canExecuteRetryJobDOList.forEach(job -> {
                        job.setState(StateEnum.WAITING);
                        // 在任务创建的时候，把当前节点的lockOwnerId传递进去，且在更新state=WAITING时就加上
                        job.setLockOwnerId(lockOwnerId);
                    });
                    List<RetryJobDO> waitingRetryJobDOList = retryService.updateRetryJob(canExecuteRetryJobDOList);
                    // 将成功更新状态为WAITING的数据放到线程池中运行
                    waitingRetryJobDOList.forEach(
                            job -> RetryJobThreadPool.execute(new RetryJob(this, observerContext, job)));
                });
    }

    /**
     * 重试方法的模版方法实现
     *
     * @param job 重试任务库表对象
     */
    public void retry(RetryJobDO job) {
        AssertUtil.notNull(job, "所需重试的job为空");
        AssertUtil.notNull(job.getType(), "job type为空. job:{}", job);
        AssertUtil.notNull(job.getParam(), "job中的参数为空. job:{}", job);
        if (!canExecute(job)) {
            // 避免因为任务阻塞，导致这块任务实际执行的时间点其实已经超过了限制
            log.warn("当前job不满足执行条件，重试请求被拒绝。job:{}", job);
            return;
        }
        // 获取数据变更的锁，并更新状态为RUNNING
        if (!retryService.tryLock(job)) {
            return;
        }
        boolean result = false;
        try {
            if (retryServiceFacade.containsServiceType(job.getType())) {
                result = retryServiceFacade.getService(job.getType()).retry(job);
            } else {
                RetryJobTypeDTO typeDTO = JSONObject.parseObject(job.getType(), RetryJobTypeDTO.class);
                final Object[] deserialize = SerializationUtil.deserialize(job.getParam());
                Class<?> clazz = Class.forName(typeDTO.getClassName());
                int paramNameListSize = typeDTO.getParamClassNameList().size();
                Class<?>[] classArray = new Class[paramNameListSize];
                for (int i = 0; i < paramNameListSize; i++) {
                    classArray[i] = Class.forName(typeDTO.getParamClassNameList().get(i));
                }
                Object service = SpringContext.getBean(clazz);
                Method method;
                if (null != service) {
                    method = service.getClass().getDeclaredMethod(typeDTO.getMethodName(), classArray);
                    result = (boolean) method.invoke(service, deserialize);
                } else {
                    method = clazz.getDeclaredMethod(typeDTO.getMethodName(), classArray);
                    result = (boolean) method.invoke(clazz, deserialize);
                }
            }
        } catch (Exception e) {
            log.error("job: {} 重试失败", job, e);
        }
        if (result) {
            // 成功就删除
            retryService.delete(job.getBh());
        } else {
            // 重试次数+1
            job.setRetriesNumber(job.getRetriesNumber() + 1);
            // 下一次可执行时间更新
            job.setNextRunDateTime(CalculateUtil.calculateNextRunDateTime(job.getNextRunDateTime(),
                    job.getIntervalInMinutes(), job.getIntervalMultiple(), job.getRetriesNumber()));
            retryService.updateRetryJob(job);
        }
    }

    private boolean canExecute(RetryJobDO job) {
        try {
            checkStartAndEndTime(job);
            // 是否超过最大重试次数限制（sql里面已经限制了，这里只是二次校验）
            return job.getRetriesNumber() < job.getMaxRetries();
        } catch (Exception e) {
            log.error("当前任务校验失败，无法被执行！job: {}", job, e);
            return false;
        }
    }

    void checkStartAndEndTime(RetryJobDO job) {
        // 任务执行时间
        LocalDateTime localDateTime = LocalDateTime.now();
        LocalTime localTime = localDateTime.toLocalTime();
        LocalTime jobStartTime = job.getJobStartTime();
        LocalTime jobEndTime = job.getJobEndTime();
        if (jobStartTime.isBefore(jobEndTime)) {
            AssertUtil.isTrue(localTime.isAfter(jobStartTime) && localTime.isBefore(jobEndTime),
                    "当前任务不满足开始时间或不满足结束时间，job:{}, now:{}", job, localDateTime);
        }
        if (jobStartTime.isAfter(jobEndTime)) {
            LocalDateTime jobStartLocalDateTime = localDateTime.toLocalDate().atTime(job.getJobStartTime());
            LocalDateTime jobEndLocalDateTime = localDateTime.plusDays(1L).toLocalDate().atTime(job.getJobEndTime());
            AssertUtil.isTrue(
                    localDateTime.isAfter(jobStartLocalDateTime) && localDateTime.isBefore(jobEndLocalDateTime),
                    "当前任务不满足开始时间或不满足结束时间，job:{}, now:{}", job, localDateTime);
        }
    }

}
