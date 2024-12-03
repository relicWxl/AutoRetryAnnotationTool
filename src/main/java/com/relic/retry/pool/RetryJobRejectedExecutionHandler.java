package com.relic.retry.pool;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * RetryJobRejectedExecutionHandler
 * <p>
 * 重试任务线程池的拒绝策略，满了以后需要从map中删除标记位，便于这个任务下次继续运行
 *
 * @author wxl
 * @version v1.0.0
 */
class RetryJobRejectedExecutionHandler implements RejectedExecutionHandler {
    @Override
    public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
        PoolUtil.remove(r);
    }
}
