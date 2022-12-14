package com.fit2cloud.dao.constants;

import com.baomidou.mybatisplus.annotation.EnumValue;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2022/12/8  16:36}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
public enum RiskLevel {
    /**
     * 高
     */
    HIGH(1),
    /**
     * 中
     */
    MIDDLE(0),
    /**
     * 低
     */
    LOW(-1);
    @EnumValue
    private final int code;

    RiskLevel(int code) {
        this.code = code;
    }
}
