package com.relic.retry.util;

import java.security.NoSuchAlgorithmException;

import org.apache.commons.lang3.StringUtils;


import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

/**
 * MD5工具类.
 *
 * @author wxl
 */
@Slf4j
@UtilityClass
public class MD5Util {

    /**
     * 将传入的字符串计算为MD5值.
     *
     * @param str 字符串
     * @return 字符串对应的MD5
     */
    public String encrypt(String str) {
        try {
            return MD5Helper.encrypt(str);
        } catch (NoSuchAlgorithmException e) {
            log.warn("尝试计算【{}】的md5值失败，将返回空字符串", str);
        }
        return StringUtils.EMPTY;
    }

}
