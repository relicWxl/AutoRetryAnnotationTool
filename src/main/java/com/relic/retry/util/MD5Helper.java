package com.relic.retry.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import org.apache.commons.codec.binary.Hex;

public final class MD5Helper {
    public static String encrypt(String str) throws NoSuchAlgorithmException {
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        md5.update(str.getBytes());
        byte[] abResult = md5.digest();
        return byte2hex(abResult);
    }

    public static String byte2hex(byte[] data) {
        return data == null ? null : new String(Hex.encodeHex(data));
    }

    private MD5Helper() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}
