package com.fit2cloud.dto.optimization;

import com.fit2cloud.constants.OptimizationRuleConditionTypeConstants;
import lombok.Data;

import java.util.List;


/**
 * 描述：优化规则
 * @author jianneng
 */
@Data
public class OptimizationRule {

    /**
     * 树
     */
    private List<OptimizationRule> children;
    /**
     * 条件
     */
    private List<OptimizationRuleFieldCondition> conditions;
    /**
     * 类型
     */
    private OptimizationRuleConditionTypeConstants conditionType;

}
