package com.relic.retry.pool;

import java.util.concurrent.*;

/**
 * RetryJobThreadPoll
 * <p>
 * 重试任务线程池
 * <p>
 * 该线程池具有以下特性：
 * 1. 持续运行：线程池支持在队列全部完成，通过观察者模式发送通知让生产者添加任务。此特性主要适用于单次任务执行时间较短的场景；
 * 2. 动态扩展：线程池支持定时增加任务，当任务数量大于当前队列所能接受的范围后，动态增加线程加大此任务池的吞吐量。此特性主要适用于单次任务执行时间较长的场景；
 * 3. 任务去重：线程池中的任务可根据业务编号进行去重，业务编号的值需要根据业务进行确认。
 *
 * @author wxl
 * @version v1.0.0
 */
final class RetryJobThreadPool {
    // 核心线程数为1，如果当前重试任务并不多，则尽可能保持为1
    private static final int corePoolSize = 1;
    // 最大线程数为8，当队列已满时，线程池可动态扩展线程数量最多至8个
    private static final int maximumPoolSize = 8;
    // 线程生存时间为10分钟，当空闲时间达到10分钟后，线程池的总线程数将恢复为4
    private static final long keepAliveTime = 10;
    private static final TimeUnit unit = TimeUnit.MINUTES;
    // 阻塞队列大小为200
    private static final BlockingQueue<Runnable> workQueue = new ArrayBlockingQueue<>(200);
    // 基于Spring的DefaultThreadFactory改写的线程工厂，修改了线程池名称，便于排查问题
    private static final ThreadFactory threadFactory = new DefaultThreadFactory();
    // 拒绝策略采用丢弃，当队列和线程数都已经达到最大时候，丢弃任务
    private static final RejectedExecutionHandler handler = new RetryJobRejectedExecutionHandler();
    // 线程池，采用饿汉式
    private static final ThreadPoolExecutor executor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory, handler);


    /**
     * 提交线程任务， 并根据ywId进行去重
     *
     * @param retryJob 重试任务
     */
    static void execute(RetryJob retryJob) {
        if (PoolUtil.exist(retryJob)) {
            return;
        }
        PoolUtil.add(retryJob);
        executor.execute(retryJob);
    }

    /**
     * 当前任务队列是否为空
     *
     * @return 任务队列为空 返回true， 否则返回false
     */
    static boolean isQueueEmpty() {
        return workQueue.isEmpty();
    }


}
