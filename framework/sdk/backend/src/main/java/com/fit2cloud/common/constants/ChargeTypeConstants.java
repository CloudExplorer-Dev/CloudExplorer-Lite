package com.fit2cloud.common.constants;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2023/6/16  16:25}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
public enum ChargeTypeConstants {
    POSTPAID("PostPaid", "按量付费"),
    PREPAID("PrePaid", "包年包月");
    private final String code;
    private final String message;

    ChargeTypeConstants(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
