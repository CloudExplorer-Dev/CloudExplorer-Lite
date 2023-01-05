package com.fit2cloud.provider.entity;

import lombok.Data;
import org.apache.commons.collections4.keyvalue.DefaultKeyValue;

import java.util.Arrays;
import java.util.List;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2022/12/7  14:25}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
@Data
public class InstanceSearchField {
    /**
     * es对应字段
     */
    private String field;
    /**
     * 提示
     */
    private String label;
    /**
     * 字段类型
     */
    private InstanceFieldType fieldType;
    /**
     * 枚举类型的所有枚举
     */
    private List<DefaultKeyValue<String, Object>> options;

    /**
     * 其他类型构造器
     *
     * @param label     描述
     * @param field     字段
     * @param fieldType 字段类型
     */
    public InstanceSearchField(String label, String field, InstanceFieldType fieldType) {
        this.label = label;
        this.fieldType = fieldType;
        this.field = field;
        if (fieldType.equals(InstanceFieldType.Enum)) {
            throw new RuntimeException("枚举类型不适用于当前构造器");
        }
    }

    /**
     * 枚举类型构造器
     *
     * @param label     描述
     * @param field     字段
     * @param fieldType 字段类型
     * @param options   字段value枚举
     */
    public InstanceSearchField(String label, String field, InstanceFieldType fieldType, List<DefaultKeyValue<String, Object>> options) {
        if (!fieldType.equals(InstanceFieldType.Enum) && !fieldType.equals(InstanceFieldType.ArrayEnum)) {
            throw new RuntimeException("当前构造器只适用于枚举类型");
        }
        this.label = label;
        this.fieldType = fieldType;
        this.field = field;
        this.options = options;
    }

}
