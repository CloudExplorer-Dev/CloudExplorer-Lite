package com.fit2cloud.base.entity.json_entity;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2023/5/25  16:50}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
@Data
public class PackagePriceBillingPolicy {
    /**
     * 名称
     */
    private String name;
    /**
     * 字段
     */
    private List<PolicyField> billPolicyFields;

    /**
     * 按需
     */
    private BigDecimal onDemand;

    /**
     * 包年包月
     */
    private BigDecimal monthly;

    @Data
    public static class PolicyField {
        /**
         * 字段
         */
        private String field;

        /**
         * 数量
         */
        private int number;
    }
}
