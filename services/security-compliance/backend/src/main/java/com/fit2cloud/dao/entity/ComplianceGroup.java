package com.fit2cloud.dao.entity;

import lombok.Data;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2023/2/10  14:45}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
@Data
public class ComplianceGroup {
    /**
     * 分组key
     */
    private String key;
    /**
     * 合规数量
     */
    private int complianceCount;
    /**
     * 不合规数量
     */
    private int notComplianceCount;
}
