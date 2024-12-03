package com.relic.retry.observer;

/**
 * IObserver
 * <p>
 * 此组件的所有观察者的父接口
 *
 * @author wxl
 * @version v1.0.0
 * @since 2020-09-22 17:25:561
 */
public interface IObserver {

    /**
     * 通知
     *
     * @param message 消息体
     */
    void notice(Message message);

}
