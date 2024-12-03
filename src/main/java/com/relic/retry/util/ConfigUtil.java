package com.relic.retry.util;

import com.relic.retry.config.IRetryConfig;
import com.relic.retry.SpringContext;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ConfigUtil {

    private static final class RetryConfigHolder {
        final static IRetryConfig retryConfig = SpringContext.getBean(IRetryConfig.class);
    }

    /**
     * 获取表名
     */
    public String getTableName() {
        return RetryConfigHolder.retryConfig.getSchemeName() + "." + RetryConfigHolder.retryConfig.getTableName();
    }

}
