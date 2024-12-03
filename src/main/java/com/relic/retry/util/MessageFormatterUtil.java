package com.relic.retry.util;

import org.slf4j.helpers.MessageFormatter;

public final class MessageFormatterUtil {
    public static String format(String messagePattern, Object... argArray) {
        return MessageFormatter.arrayFormat(messagePattern, argArray).getMessage();
    }

    private MessageFormatterUtil() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}