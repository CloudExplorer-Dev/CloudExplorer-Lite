package com.fit2cloud.provider.constants;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2023/8/7  14:00}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
public enum CostFieldConstants {

    officialAmount("官网价", "cost.officialAmount"),
    payableAmount("应付金额", "cost.payableAmount"),
    cashAmount("现金支付", "cost.cashAmount"),
    couponAmount("代金券支付", "cost.couponAmount");

    /**
     * 描述
     */
    private final String message;
    /**
     * 字段
     */
    private final String field;

    CostFieldConstants(String message, String field) {
        this.message = message;
        this.field = field;
    }

    public String getField() {
        return field;
    }

    public String getMessage() {
        return message;
    }
}
