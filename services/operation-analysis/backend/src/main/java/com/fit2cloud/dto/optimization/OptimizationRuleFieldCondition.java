package com.fit2cloud.dto.optimization;

import lombok.Data;


/**
 * 描述：优化规则规则条件
 * @author jianneng
 */
@Data
public class OptimizationRuleFieldCondition {
    /**
     * 条件key
     */
    private String field;
    /**
     * es字段
     */
    private boolean esField;
    /**
     * 比较器
     */
    private String compare;
    /**
     * 条件值
     */
    private String value;
}
