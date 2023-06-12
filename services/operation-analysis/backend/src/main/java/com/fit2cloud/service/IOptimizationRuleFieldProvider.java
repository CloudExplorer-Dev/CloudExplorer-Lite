package com.fit2cloud.service;

import com.fit2cloud.dto.optimization.OptimizationRuleField;

import java.util.List;

/**
 * 资源类型优化字段接口
 *
 * @author jianneng
 * @date 2023/5/29 17:16
 **/
public interface IOptimizationRuleFieldProvider {


    /**
     * 得到虚拟机优化字段列表
     *
     * @return {@link List }<{@link OptimizationRuleField }>
     */
    List<OptimizationRuleField> getVmOptimizationRuleFieldList();

}
