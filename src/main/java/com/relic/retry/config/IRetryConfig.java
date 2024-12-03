package com.relic.retry.config;

/**
 * RetryConfig
 * <p>
 * 重试任务配置项
 *
 * @author wxl
 * @version v1.0.0
 */
public interface IRetryConfig {

    /**
     * 获取模式名称
     *
     * @return 模式名
     */
    String getSchemeName();

    /**
     * 获取表名
     *
     * @return 表名
     */
    default String getTableName() {
        return "t_retry";
    }
}
