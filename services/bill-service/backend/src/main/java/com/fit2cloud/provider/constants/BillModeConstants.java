package com.fit2cloud.provider.constants;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2022/10/25  3:21 PM}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
public enum BillModeConstants {
    /**
     * 按需
     */
    ON_DEMAND("按需"),
    /**
     * 包年包月
     */
    MONTHLY("包年包月"),
    /**
     * 其他
     */
    OTHER("其他");
    private String message;

    BillModeConstants(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
