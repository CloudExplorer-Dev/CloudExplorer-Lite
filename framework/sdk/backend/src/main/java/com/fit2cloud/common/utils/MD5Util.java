package com.fit2cloud.common.utils;

import org.apache.commons.lang3.StringUtils;

import java.security.MessageDigest;

/**
 * 加密解密工具
 *
 * @author kun.mo
 */
public class MD5Util {

    private static final String UTF_8 = "UTF-8";

    private static final char[] HEX_DIGITS = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    /**
     * MD5加密
     *
     * @param src 要加密的串
     * @return 加密后的字符串
     */
    public static String md5(String src) {
        return md5(src, UTF_8);
    }

    /**
     * MD5加密
     *
     * @param src     要加密的串
     * @param charset 加密字符集
     * @return 加密后的字符串
     */
    public static String md5(String src, String charset) {
        try {
            byte[] strTemp = StringUtils.isEmpty(charset) ? src.getBytes() : src.getBytes(charset);
            MessageDigest mdTemp = MessageDigest.getInstance("MD5");
            mdTemp.update(strTemp);

            byte[] md = mdTemp.digest();
            int j = md.length;
            char[] str = new char[j * 2];
            int k = 0;

            for (byte byte0 : md) {
                str[k++] = HEX_DIGITS[byte0 >>> 4 & 0xf];
                str[k++] = HEX_DIGITS[byte0 & 0xf];
            }

            return new String(str);
        } catch (Exception e) {
            throw new RuntimeException("MD5 encrypt error:", e);
        }
    }

}
