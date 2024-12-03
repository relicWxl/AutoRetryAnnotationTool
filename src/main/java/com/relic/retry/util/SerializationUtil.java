package com.relic.retry.util;

import java.io.Serializable;
import lombok.experimental.UtilityClass;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.SerializationUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * Java序列化工具类.
 *
 * @author wxl
 */
@UtilityClass
public class SerializationUtil {

    /**
     * 将可序列化的对象序列化为字符串.
     * <p>
     * 需要实现{@link Serializable}接口，以声明支持序列化
     * </p>
     *
     * @param obj 可序列化的对象.
     * @return 经序列化后的字节数组转为base64的字符串.
     */
    public String serialize(Serializable obj) {
        if (obj != null) {
            byte[] bytes = SerializationUtils.serialize(obj);
            return bytesToString(bytes);
        }
        return StringUtils.EMPTY;
    }

    /**
     * 将字符串反序列化为对象.
     *
     * @param str 使用{@link #serialize(Serializable)}方法获取的字符串.
     * @return 对象
     */
    public Object[] deserialize(String str) {
        if (StringUtils.isEmpty(str)) {
            throw new IllegalArgumentException("The byte[] must not be null");
        }
        return SerializationUtils.deserialize(stringToByte(str));
    }

    private String bytesToString(byte[] bytes) {
        // base64编码
        return Base64.encodeBase64String(bytes);
    }

    private byte[] stringToByte(String str) {
        // base64解码
        return Base64.decodeBase64(str);
    }

}
