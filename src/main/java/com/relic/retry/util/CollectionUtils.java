package com.relic.retry.util;

import java.util.Collection;

public final class CollectionUtils {
    public static boolean isNotEmpty(Collection<?> coll) {
        return !isEmpty(coll);
    }

    public static boolean isEmpty(Collection<?> coll) {
        return coll == null || coll.isEmpty();
    }

    private CollectionUtils() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}
