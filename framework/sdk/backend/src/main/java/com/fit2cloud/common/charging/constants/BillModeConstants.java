package com.fit2cloud.common.charging.constants;

import com.baomidou.mybatisplus.annotation.EnumValue;

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
    MONTHLY("包年包月");
    @EnumValue
    private final String message;

    BillModeConstants(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
