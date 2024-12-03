package com.relic.retry.util;

import java.util.Collection;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

public final class AssertUtil {
    public static void isTrue(boolean expression, String messagePattern, Object... argArray) {
        isNotTrue(!expression, messagePattern, argArray);
    }

    public static void isNotTrue(boolean expression, String messagePattern, Object... argArray) {
        if (expression) {
            throw new IllegalArgumentException(MessageFormatterUtil.format(messagePattern, argArray));
        }
    }

    public static void notNull(Object object, String messagePattern, Object... argArray) {
        notNull(object, MessageFormatterUtil.format(messagePattern, argArray));
    }

    public static void notNull(Object object, String message) {
        if (object == null) {
            throw new IllegalArgumentException(message);
        } else if (object instanceof String && StringUtils.isEmpty(object.toString())) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void empty(Collection<?> collection, String messagePattern, Object... argArray) {
        empty(collection, MessageFormatterUtil.format(messagePattern, argArray));
    }

    public static void empty(Collection<?> collection, String message) {
        if (CollectionUtils.isNotEmpty(collection)) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void notEmpty(Collection<?> collection, String messagePattern, Object... argArray) {
        notEmpty(collection, MessageFormatterUtil.format(messagePattern, argArray));
    }

    public static void notEmpty(Collection<?> collection, String message) {
        if (CollectionUtils.isEmpty(collection)) {
            throw new IllegalArgumentException(message);
        }
    }

    private AssertUtil() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}
