package com.fit2cloud.dao.constants;

import com.baomidou.mybatisplus.annotation.EnumValue;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2023/2/8  11:53}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
public enum ResourceType {
    /**
     * 合规
     */
    COMPLIANCE(1),
    /**
     * 漏洞
     */
    LEAK(2);

    @EnumValue
    private final int code;

    ResourceType(int code) {
        this.code = code;
    }
}
