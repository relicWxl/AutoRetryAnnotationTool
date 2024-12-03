package com.relic.retry.exeception;

import com.relic.retry.service.IRetryService;
import com.relic.retry.util.MessageFormatterUtil;

/**
 * RetryJobAlreadyExistException
 * <p>
 * 重试任务已经存在异常
 * <p>
 * 以下场景会触发此异常：
 * 1. 多个{@link IRetryService#getType()}返回了相同的key
 *
 * @author wxl
 * @version v1.0.0
 */
public class RetryJobAlreadyExistException extends RuntimeException {

    public RetryJobAlreadyExistException(final String messagePattern, final Object... argArray) {
        super(MessageFormatterUtil.format(messagePattern, argArray));
    }
}
