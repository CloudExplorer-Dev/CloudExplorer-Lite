package com.fit2cloud.dto.optimization;

import lombok.Data;
import org.apache.commons.collections4.keyvalue.DefaultKeyValue;

import java.util.List;


/**
 * 描述：优化字段
 * @author jianneng
 */
@Data
public class OptimizationRuleField {
    /**
     * 对应字段
     */
    private String field;
    /**
     * es字段
     */
    private boolean esField;
    /**
     * 提示
     */
    private String label;
    /**
     * 字段类型
     */
    private OptimizationRuleFieldType fieldType;
    /**
     * 单位
     */
    private String unit;
    /**
     * 枚举类型的所有枚举
     */
    private List<DefaultKeyValue<String, Object>> options;

    /**
     * 枚举类型构造器
     *
     * @param label     描述
     * @param field     字段
     * @param fieldType 字段类型
     * @param esField   ES字段
     * @param unit      字段单位
     */
    public OptimizationRuleField(String label, String field, boolean esField, String unit, OptimizationRuleFieldType fieldType) {
        this.label = label;
        this.fieldType = fieldType;
        this.field = field;
        this.esField = esField;
        this.unit = unit;
    }

    /**
     * 枚举类型构造器
     *
     * @param label     描述
     * @param field     字段
     * @param fieldType 字段类型
     * @param options   字段value枚举
     * @param esField   ES字段
     * @param unit      字段单位
     */
    public OptimizationRuleField(String label, String field, boolean esField, String unit, OptimizationRuleFieldType fieldType, List<DefaultKeyValue<String, Object>> options) {
        if (!fieldType.equals(OptimizationRuleFieldType.Enum)) {
            throw new RuntimeException("当前构造器只适用于枚举类型");
        }
        this.label = label;
        this.fieldType = fieldType;
        this.field = field;
        this.esField = esField;
        this.unit = unit;
        this.options = options;
    }

}
