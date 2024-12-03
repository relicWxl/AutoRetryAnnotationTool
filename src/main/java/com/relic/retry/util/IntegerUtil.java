package com.relic.retry.util;

public final class IntegerUtil {
    public static int defaultIfNonPositive(Integer num, int defaultNum) {
        return null != num && num > 0 ? num : defaultNum;
    }

    private IntegerUtil() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}