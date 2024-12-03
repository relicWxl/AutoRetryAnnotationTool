package com.relic.retry.pool.observer.impl;

import javax.annotation.Resource;

import com.relic.retry.observer.Message;
import org.springframework.stereotype.Service;

import com.relic.retry.pool.RetryJobContext;
import com.relic.retry.pool.observer.IQueueIsEmptyObserver;

import lombok.extern.slf4j.Slf4j;

/**
 * QueueIsEmptyObServerImpl
 * <p>
 * 任务队列为空通知者实现类，负责当任务池为空时，新增新的任务到任务池中
 *
 * @author wxl
 * @version v1.0.0
 */
@Slf4j
@Service("queueIsEmptyObServerImpl")
public class QueueIsEmptyObServerImpl implements IQueueIsEmptyObserver {

    @Resource
    private RetryJobContext retryJobContext;

    @Override
    public void notice(Message message) {
        // 当任务已经为空时，及时同步数据库中的任务以便于线程不会空闲，任务不会积压
        retryJobContext.createJob(message.getLockOwnerId());
    }
}
