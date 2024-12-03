package com.relic.retry.pool.observer.service;

import java.util.List;

import com.relic.retry.observer.Message;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;

import com.relic.retry.SpringContext;
import com.relic.retry.observer.IObserver;
import com.relic.retry.pool.observer.IQueueIsEmptyObserver;

import lombok.extern.slf4j.Slf4j;

/**
 * QueueIsEmptyObserverContext
 * <p>
 * 任务池队列为空通知者上下文实现类，主要负责在接收到通知以后调用各个消息接收者
 *
 * @author wxl
 * @version v1.0.0
 */
@Slf4j
@Service
@DependsOn({"retrySpringContext"})
public class QueueIsEmptyObserverContext {

    private volatile List<IQueueIsEmptyObserver> observerList;

    /**
     * 给所有接收队列为空的observer发送通知
     *
     * @param message 通知消息，无内容体
     */
    public void notice(Message message) {
        if (null == observerList) {
            synchronized (QueueIsEmptyObserverContext.class) {
                if (null == observerList) {
                    observerList = SpringContext.getBeanListOfType(IQueueIsEmptyObserver.class);
                }
            }
        }
        for (IObserver observer : observerList) {
            try {
                observer.notice(message);
            } catch (Exception e) {
                log.error("通知发送失败，message: {}, observer: {}", message, observer, e);
            }
        }
    }

}
