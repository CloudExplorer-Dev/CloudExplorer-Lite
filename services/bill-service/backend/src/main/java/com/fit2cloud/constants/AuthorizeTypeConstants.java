package com.fit2cloud.constants;

import com.baomidou.mybatisplus.annotation.EnumValue;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2022/11/8  9:55 AM}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
public enum AuthorizeTypeConstants {
    /**
     * 组织
     */
    ORGANIZATION(0),
    /**
     * 工作空间
     */
    WORKSPACE(1);

    @EnumValue
    private final int code;

    AuthorizeTypeConstants(int code) {
        this.code = code;
    }
}
