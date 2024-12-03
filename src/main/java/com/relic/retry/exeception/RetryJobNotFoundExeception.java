package com.relic.retry.exeception;

import com.thunisoft.zhsg.utils.MessageFormatterUtil;

/**
 * RetryJobNotFoundExeception
 * <p>
 * 重试任务不存在异常
 * <p>
 * 以下场景会触发此异常：
 * 1. 根据任务key获取任务实现类，获取失败时
 *
 * @author wxl
 * @version v1.0.0
 */
public class RetryJobNotFoundExeception extends RuntimeException {

    public RetryJobNotFoundExeception(final String messagePattern, final Object... argArray) {
        super(MessageFormatterUtil.format(messagePattern, argArray));
    }
}
