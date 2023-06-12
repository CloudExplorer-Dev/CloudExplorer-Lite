package com.fit2cloud.constants;

import com.fit2cloud.dto.optimization.OptimizationRuleField;
import com.fit2cloud.service.IOptimizationRuleFieldProvider;

import java.util.List;
import java.util.function.Function;


/**
 * 优化策略资源类型
 *
 * @author jianneng
 */

public enum ResourceTypeConstants {
    /**
     * 云服务器
     */
    VIRTUAL_MACHINE("云服务器", IOptimizationRuleFieldProvider::getVmOptimizationRuleFieldList);

    /**
     * 描述
     */
    private final String label;

    /**
     * 获取可优化字段
     */
    private final Function<IOptimizationRuleFieldProvider, List<OptimizationRuleField>> optimizationFieldList;

    ResourceTypeConstants(String label, Function<IOptimizationRuleFieldProvider, List<OptimizationRuleField>> optimizationFieldList) {
        this.optimizationFieldList = optimizationFieldList;
        this.label = label;
    }

    public Function<IOptimizationRuleFieldProvider, List<OptimizationRuleField>> getOptimizationRuleFieldList() {
        return optimizationFieldList;
    }

    public String getLabel() {
        return label;
    }
}
