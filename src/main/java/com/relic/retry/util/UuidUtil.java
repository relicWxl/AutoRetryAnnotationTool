package com.relic.retry.util;

import java.util.UUID;

/**
 * UuidUtil
 * <p>
 * 提供uuid生成方法
 *
 * @author wxl
 * @version v1.0.0
 * @since 2020-09-17 17:36:642
 */
public class UuidUtil {

    /**
     * @return uuid
     */
    public static String getUuid() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}
