package com.fit2cloud.common.charging.constants;

import com.fit2cloud.common.charging.entity.InstanceState;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2023/5/29  9:58}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
public enum BillingInstanceStateConstants {
    /**
     * 未创建
     */
    NOT_CREATED('0'),

    /**
     * 运行中
     */
    IN_OPERATION('1'),

    /**
     * 已停止
     */
    STOPPED('2'),

    /**
     * 未知
     */
    UNKNOWN('3');

    private char code;

    public char getCode() {
        return this.code;
    }

    public InstanceState.State getState() {
        return new InstanceState.State(this.code);
    }

    BillingInstanceStateConstants(char code) {
        this.code = code;
    }
}
