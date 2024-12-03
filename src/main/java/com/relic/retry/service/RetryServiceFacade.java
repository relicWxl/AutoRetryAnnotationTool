package com.relic.retry.service;

import com.relic.retry.SpringContext;
import com.relic.retry.exeception.RetryJobAlreadyExistException;
import com.relic.retry.exeception.RetryJobNotFoundExeception;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;

/**
 * 针对{@link IRetryService}封装的门面类.
 *
 * @author wxl
 */
@Slf4j
@Service
@DependsOn({"retrySpringContext"})
public class RetryServiceFacade {

    /**
     * 原型模式-初始化所有实现{@link IRetryService}接口的实现类
     */
    private static final Map<String, IRetryService> MAP = new ConcurrentHashMap<>();

    @PostConstruct
    public void init() {
        final List<IRetryService> retryJobList = SpringContext.getBeanListOfType(IRetryService.class);
        log.debug("Retry Job 初始化开始");
        for (IRetryService retryJob : retryJobList) {
            String key = retryJob.getType();
            if (MAP.containsKey(key)) {
                throw new RetryJobAlreadyExistException("任务标识:{}存在多个实现类，重试任务创建失败", key);
            }
            MAP.put(key, retryJob);
        }
        log.debug("Retry Job 初始化结束，当前共有{}种实现", retryJobList.size());
    }

    /**
     * 判断当前应用中是否包含任务类型标识对应的实现类.
     *
     * @param type 任务类型标识，与{@link IRetryService#getType()}一一对应
     * @return 包含返回true，否则返回false
     */
    public boolean containsServiceType(String type) {
        return MAP.containsKey(type);
    }

    /**
     * 根据任务类型标识返回对应的实现类
     *
     * @param type 任务类型标识
     * @return 存在则返回对应实现类，否则抛出异常{@link RetryJobNotFoundExeception}
     */
    public IRetryService getService(String type) {
        if (MAP.containsKey(type)) {
            return MAP.get(type);
        }
        throw new RetryJobNotFoundExeception("当前类型无对应实现：{}", type);
    }

}
