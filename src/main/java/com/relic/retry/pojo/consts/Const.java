package com.relic.retry.pojo.consts;

/**
 * 常量参数.
 *
 * @author wxl
 */
public interface Const {

    /**
     * 默认锁的间隔时间不小于20s.
     */
    long DEFAULT_LAST_RUN_DURATION_MILLI_SECOND = 20000;

    /**
     * 默认的年月日时分秒格式化字符串.
     */
    String DEFAULT_LOCAL_DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

}
