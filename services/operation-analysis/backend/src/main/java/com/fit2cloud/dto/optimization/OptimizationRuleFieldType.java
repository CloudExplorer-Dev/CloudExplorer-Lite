package com.fit2cloud.dto.optimization;

import java.util.Arrays;
import java.util.List;

import static com.fit2cloud.dto.optimization.OptimizationRuleFieldCompare.*;


/**
 * 描述：优化字段类型
 * @author jianneng
 */
public enum OptimizationRuleFieldType {
    /**
     * 枚举
     */
    Enum(EQ, NOT_EQ),
    /**
     * 字符串
     */
    String(EQ, NOT_EQ),
    /**
     * 数字
     */
    Number(GT, GE, LT, LE, EQ);

    /**
     * 字段对比器
     */
    private final List<OptimizationRuleFieldCompare> compares;

    OptimizationRuleFieldType(OptimizationRuleFieldCompare... compares) {
        this.compares = Arrays.stream(compares).toList();
    }

    public List<OptimizationRuleFieldCompare> getCompares() {
        return compares;
    }
}
