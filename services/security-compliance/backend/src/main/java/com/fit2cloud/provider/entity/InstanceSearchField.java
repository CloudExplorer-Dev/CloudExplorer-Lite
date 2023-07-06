package com.fit2cloud.provider.entity;

import com.fit2cloud.constants.ResourceTypeConstants;
import lombok.Data;
import org.apache.commons.collections4.keyvalue.DefaultKeyValue;

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
        if (!fieldType.equals(InstanceFieldType.Enum) && !fieldType.equals(InstanceFieldType.ArrayEnum) && !fieldType.equals(InstanceFieldType.NestedArrayEnum)) {
            throw new RuntimeException("当前构造器只适用于枚举类型");
        }
        this.label = label;
        this.fieldType = fieldType;
        this.field = field;
        this.options = options;
    }

    /**
     * 重置当前es字段
     *
     * @param platform     供应商
     * @param resourceType 资源类型
     * @return 实例对象下的属性
     */
    public InstanceSearchField resetInstanceField(String platform, ResourceTypeConstants resourceType) {
        this.field = "instance." + platform + "_" + resourceType.name() + "." + this.field;
        return this;
    }

    /**
     * 重置当前除去实例对象以外的过滤对象
     *
     * @param platform     供应商
     * @param resourceType 资源类型
     * @return 实例查询对象
     */
    public InstanceSearchField resetFilterObjField(String platform, ResourceTypeConstants resourceType) {
        this.field = "filterObj." + platform + "_" + resourceType.name() + "." + this.field;
        return this;
    }

    /**
     * 重置过滤数组对象
     *
     * @param platform     供应商
     * @param resourceType 资源类型
     * @return 实例查询对象
     */
    public InstanceSearchField resetFilterArrayField(String platform, ResourceTypeConstants resourceType, String filterField) {
        return resetFilterArrayField(platform, resourceType, filterField, true);
    }

    /**
     * 重置过滤数组对象
     *
     * @param platform     供应商
     * @param resourceType 资源类型
     * @return 实例查询对象
     */
    public InstanceSearchField resetFilterArrayField(String platform, ResourceTypeConstants resourceType, String filterField, Boolean appendKeyword) {
        this.field = "filterArray." + platform + "_" + resourceType.name() + "_" + filterField + "." + (this.field.endsWith(".keyword") ? this.field : appendKeyword ? this.field + ".keyword" : this.field);
        return this;
    }
}
