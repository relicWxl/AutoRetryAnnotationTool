//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.relic.retry.pool;

import com.relic.retry.util.UuidUtil;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

final class PoolUtil {
    private static final Map<Integer, String> JOB_MAP = new ConcurrentHashMap(256);

    PoolUtil() {
    }

    static boolean exist(Runnable retryJob) {
        return JOB_MAP.containsKey(retryJob.hashCode());
    }

    static void add(Runnable retryJob) {
        JOB_MAP.put(retryJob.hashCode(), UuidUtil.getUuid());
    }

    static void remove(Runnable retryJob) {
        JOB_MAP.remove(retryJob.hashCode());
    }
}
