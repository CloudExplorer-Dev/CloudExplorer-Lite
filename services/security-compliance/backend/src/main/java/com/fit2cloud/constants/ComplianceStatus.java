package com.fit2cloud.constants;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2022/12/15  17:00}
 * {@code @Version 1.0}
 * {@code @注释: 检查状态}
 */
public enum ComplianceStatus {
    COMPLIANCE(1, "合规"),

    NOT_COMPLIANCE(2, "不合规");
    /**
     * 提示
     */
    private String message;
    /**
     * code
     */
    private Integer code;

    ComplianceStatus(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
