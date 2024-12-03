//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.relic.retry.task;

import com.relic.retry.util.UuidUtil;
import com.relic.retry.pool.RetryJobContext;

import javax.annotation.Resource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class RetryTask {
    @Resource
    private RetryJobContext retryJobContext;
    private final String lockOwnerId = UuidUtil.getUuid();

    public RetryTask() {
    }

    @Scheduled(
            fixedDelayString = "${retry.schedule.fixed-delay:300000}"
    )
    public void execute() {
        this.retryJobContext.createJob(this.lockOwnerId);
    }
}
