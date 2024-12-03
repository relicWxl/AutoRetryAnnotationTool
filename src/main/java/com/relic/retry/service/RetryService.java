//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.relic.retry.service;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.relic.retry.aop.annotation.RetryJobAnnotation;
import com.relic.retry.dao.RetryMapper;
import com.relic.retry.pojo.consts.StateEnum;
import com.relic.retry.pojo.dto.InitRetryJobParam;
import com.relic.retry.pojo.dto.PageResult;
import com.relic.retry.pojo.dto.RetryJobQuery;
import com.relic.retry.pojo.model.RetryJobDO;
import com.relic.retry.pojo.vo.RetryJobVO;
import com.relic.retry.util.*;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

@Service
public class RetryService {
    private static final Logger log = LoggerFactory.getLogger(RetryService.class);
    private final RetryMapper retryMapper;
    private final RetryServiceFacade retryServiceFacade;

    public RetryService(RetryMapper retryMapper, RetryServiceFacade retryServiceFacade) {
        this.retryMapper = retryMapper;
        this.retryServiceFacade = retryServiceFacade;
    }

    public void initRetryJob(RetryJobAnnotation annotation, String type, String param, long durationTime) {
        if (!this.existJob(param, type, StateEnum.NEW)) {
            LocalTime jobStartTime = LocalTime.parse(annotation.jobStartTime());
            LocalTime jobEndTime = LocalTime.parse(annotation.jobEndTime());
            Integer order = annotation.order();
            LocalDateTime now = LocalDateTime.now();
            RetryJobDO job = RetryJobDO.builder().bh(UuidUtil.getUuid()).param(param).order(order).type(type).jobStartTime(jobStartTime).jobEndTime(jobEndTime).intervalInMinutes(annotation.intervalInMinutes()).intervalMultiple(annotation.intervalMultiple()).nextRunDateTime(CalculateUtil.calculateNextRunDateTime(now, annotation.intervalInMinutes(), annotation.intervalMultiple(), (Integer)null)).maxRetries(annotation.maxRetries()).cjsj(now).zhgxsj(now).lastRunDurationMilliSecond(durationTime).build();
            this.insert(job);
        }
    }

    public void initRetryJob(Object[] paramObjects, String type, Long lastRunDurationMilliSecond) {
        this.initRetryJob(InitRetryJobParam.builder().paramObjects(paramObjects).type(type).lastRunDurationMilliSecond(lastRunDurationMilliSecond).build());
    }

    public void initRetryJob(InitRetryJobParam initRetryJobParam) {
        AssertUtil.notNull(initRetryJobParam.getParamObjects(), "参数实例不可为空");
        AssertUtil.notNull(initRetryJobParam.getType(), "任务类型标识不可为空");
        AssertUtil.notNull(initRetryJobParam.getLastRunDurationMilliSecond(), "任务运行时间不可为空");
        AssertUtil.notNull(initRetryJobParam.getJobStartTime(), "任务开始时间不可为空");
        AssertUtil.notNull(initRetryJobParam.getJobEndTime(), "任务结束时间不可为空");
        AssertUtil.notNull(initRetryJobParam.getOrder(), "任务排序不可为空");
        AssertUtil.notNull(initRetryJobParam.getMaxRetries(), "最大重试次数不可为空");
        AssertUtil.notNull(initRetryJobParam.getIntervalInMinutes(), "间隔重试时间（单位：分钟）不可为空");
        AssertUtil.notNull(initRetryJobParam.getIntervalMultiple(), "间隔倍数不可为空");
        AssertUtil.isTrue(this.retryServiceFacade.containsServiceType(initRetryJobParam.getType()), "任务类型标识【{}】无对应实现类，初始化失败。请确认com.thunisoft.zhsg.retry.service.IRetryService实现类中有声明此字符串，且实现类需要注册到Spring IOC容器中", new Object[]{initRetryJobParam.getType()});
        String param = SerializationUtil.serialize(initRetryJobParam.getParamObjects());
        if (!this.existJob(param, initRetryJobParam.getType(), StateEnum.NEW)) {
            LocalDateTime now = LocalDateTime.now();
            RetryJobDO job = RetryJobDO.builder().bh(UuidUtil.getUuid()).param(param).order(initRetryJobParam.getOrder()).type(initRetryJobParam.getType()).jobStartTime(initRetryJobParam.getJobStartTime()).jobEndTime(initRetryJobParam.getJobEndTime()).intervalInMinutes(initRetryJobParam.getIntervalInMinutes()).intervalMultiple(initRetryJobParam.getIntervalMultiple()).nextRunDateTime(CalculateUtil.calculateNextRunDateTime(now, initRetryJobParam.getIntervalInMinutes(), initRetryJobParam.getIntervalMultiple(), 1)).maxRetries(initRetryJobParam.getMaxRetries()).cjsj(now).zhgxsj(now).lastRunDurationMilliSecond(initRetryJobParam.getLastRunDurationMilliSecond()).build();
            this.insert(job);
        }
    }

    public int insert(RetryJobDO job) {
        AssertUtil.notNull(job, "job为空，插入失败");
        job.setTypeHex(MD5Util.encrypt(job.getType()));
        job.setParamHex(MD5Util.encrypt(job.getParam()));
        job.setState(StateEnum.NEW);
        return this.retryMapper.insert(job);
    }

    public int delete(String bh) {
        AssertUtil.notNull(bh, "编号为空，无法执行");
        return this.retryMapper.deleteByBh(bh);
    }

    public List<RetryJobDO> listAllJob(String lockOwnerId) {
        LocalDateTime now = LocalDateTime.now();
        String localDate = now.format(DateTimeFormatter.ISO_LOCAL_DATE);
        String localTime = now.format(DateTimeFormatter.ISO_LOCAL_TIME);
        return this.retryMapper.list(lockOwnerId, now, localDate, localTime);
    }

    public PageResult<RetryJobVO> pageQuery(RetryJobQuery retryJobQuery) {
        List<RetryJobVO> resultList = new ArrayList<>();
        int pageNum = retryJobQuery.getPageNum();
        int pageSize = StringUtils.isBlank(retryJobQuery.getParam()) ? retryJobQuery.getPageSize() : Math.max(retryJobQuery.getPageSize(), 200);

        PageInfo pageResult;
        do {
            PageHelper.startPage(pageNum, pageSize);
            pageResult = new PageInfo(this.retryMapper.query(retryJobQuery));
            resultList.addAll((Collection<? extends RetryJobVO>) pageResult.getList().stream().map(job -> {
                RetryJobDO retryJobDO = (RetryJobDO) job; // 确保类型转换正确
                RetryJobVO jobVO = RetryJobVO.builder()
                        .bh(retryJobDO.getBh())
                        .type(retryJobDO.getType())
                        .order(retryJobDO.getOrder())
                        .retriesNumber(retryJobDO.getRetriesNumber())
                        .maxRetries(retryJobDO.getMaxRetries())
                        .intervalInMinutes(retryJobDO.getIntervalInMinutes())
                        .intervalMultiple(retryJobDO.getIntervalMultiple())
                        .jobStartTime(retryJobDO.getJobStartTime())
                        .jobEndTime(retryJobDO.getJobEndTime())
                        .nextRunDateTime(retryJobDO.getNextRunDateTime())
                        .zhgxsj(retryJobDO.getZhgxsj())
                        .cjsj(retryJobDO.getCjsj())
                        .params(SerializationUtil.deserialize(retryJobDO.getParam()))
                        .build();
                return jobVO;
            }).filter(vo -> {
                RetryJobVO value = (RetryJobVO) vo; // 确保类型转换正确
                return StringUtils.isBlank(retryJobQuery.getParam()) || Arrays.stream(value.getParams()).anyMatch(param -> {
                    return JSONObject.toJSONString(param).contains(retryJobQuery.getParam());
                });
            }).collect(Collectors.toList()));
            ++pageNum;
            ++pageSize;
        } while (StringUtils.isNotBlank(retryJobQuery.getParam()) && resultList.size() < retryJobQuery.getPageSize() * retryJobQuery.getPageNum() && pageResult.isHasNextPage());

        if (StringUtils.isNotBlank(retryJobQuery.getParam())) {
            resultList = resultList.stream()
                    .skip((long) (retryJobQuery.getPageNum() - 1) * (long) retryJobQuery.getPageSize())
                    .limit((long) retryJobQuery.getPageSize())
                    .collect(Collectors.toList());
        }
        return PageResult.builder().query(retryJobQuery).dataList((List)resultList).hasNextPage(pageResult.isHasNextPage()).build();
    }

    public int updateRetryJob(RetryJobDO job) {
        AssertUtil.notNull(job, "job为空，更新失败");
        AssertUtil.notNull(job.getBh(), "job.getBh()为空，更新失败");
        return this.updateRetryJob(Collections.singletonList(job)).size();
    }

    public List<RetryJobDO> updateRetryJob(List<RetryJobDO> retryJobDOList) {
        if (CollectionUtils.isEmpty(retryJobDOList)) {
            return Collections.emptyList();
        } else {
            AssertUtil.isTrue(retryJobDOList.stream().noneMatch((temp) -> {
                return StringUtils.isEmpty(temp.getBh());
            }), "存在编号为空的数据，更新失败。retryJobDOList：{}", new Object[]{retryJobDOList});
            LocalDateTime now = LocalDateTime.now();
            return (List)retryJobDOList.stream().map((job) -> {
                job.setZhgxsj(now);

                try {
                    if (this.retryMapper.updateByBh(job, now) == 1) {
                        return job;
                    }
                } catch (DuplicateKeyException var4) {
                    log.debug("尝试更新失败. job: {}", job, var4);
                }

                return null;
            }).filter(Objects::nonNull).collect(Collectors.toList());
        }
    }

    public boolean tryLock(RetryJobDO job) {
        AssertUtil.notNull(job, "job为空，获取数据锁失败");
        AssertUtil.notNull(job.getBh(), "job.getBh()为空，获取数据锁失败");
        AssertUtil.notNull(job.getLockOwnerId(), "job.getLockOwnerId()为空，获取数据锁失败");
        String interval = "interval '" + CalculateUtil.calculateLockInterval(job.getLastRunDurationMilliSecond(), 20000L) + " millisecond'";
        return this.retryMapper.tryLock(job, interval, LocalDateTime.now());
    }

    private boolean existJob(String param, String type, StateEnum state) {
        return !CollectionUtils.isEmpty(this.retryMapper.listByParamAndTypeAndState(RetryJobDO.builder().paramHex(MD5Util.encrypt(param)).typeHex(MD5Util.encrypt(type)).state(state).build()));
    }
}
