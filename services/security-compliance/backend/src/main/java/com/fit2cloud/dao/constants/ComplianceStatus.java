package com.fit2cloud.dao.constants;

import com.baomidou.mybatisplus.annotation.EnumValue;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2022/12/15  17:00}
 * {@code @Version 1.0}
 * {@code @注释: 检查状态}
 */
public enum ComplianceStatus {
    /**
     * 合规
     */
    COMPLIANCE(1),

    /**
     * 不合规
     */
    NOT_COMPLIANCE(2);

    @EnumValue
    private Integer code;

    ComplianceStatus(Integer code) {
        this.code = code;
    }
}
