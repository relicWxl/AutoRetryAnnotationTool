//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.relic.retry.pool;

import com.relic.retry.observer.Message;
import com.relic.retry.pool.observer.service.QueueIsEmptyObserverContext;
import com.relic.retry.pojo.model.RetryJobDO;

import java.util.Objects;

final class RetryJob implements Runnable {
    private final RetryJobContext jobContext;
    private final QueueIsEmptyObserverContext observerContext;
    private final RetryJobDO job;

    RetryJob(RetryJobContext jobContext, QueueIsEmptyObserverContext observerContext, RetryJobDO job) {
        this.jobContext = jobContext;
        this.observerContext = observerContext;
        this.job = job;
    }

    public void run() {
        this.jobContext.retry(this.job);
        if (RetryJobThreadPool.isQueueEmpty()) {
            this.observerContext.notice(Message.builder().lockOwnerId(this.job.getLockOwnerId()).build());
        }

        PoolUtil.remove(this);
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o != null && this.getClass() == o.getClass()) {
            RetryJob retryJob = (RetryJob)o;
            return Objects.equals(this.job.getBh(), retryJob.job.getBh());
        } else {
            return false;
        }
    }

    public int hashCode() {
        return Objects.hash(new Object[]{this.job.getBh()});
    }
}
