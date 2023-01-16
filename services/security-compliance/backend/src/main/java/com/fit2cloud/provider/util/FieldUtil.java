package com.fit2cloud.provider.util;

import com.fit2cloud.constants.ResourceTypeConstants;
import com.fit2cloud.provider.entity.InstanceFieldType;
import com.fit2cloud.provider.entity.InstanceSearchField;

import java.util.List;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2022/12/8  10:47}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
public class FieldUtil {
    /**
     * 添加字段前缀
     *
     * @param prefixField         字段前缀
     * @param instanceSearchField 实例字段搜索对象
     * @return 实例对象搜索对象
     */
    public static InstanceSearchField appendPrefixField(String prefixField, InstanceSearchField instanceSearchField) {
        instanceSearchField.setField(prefixField + instanceSearchField.getField());
        return instanceSearchField;
    }

    /**
     * 添加字段前缀
     *
     * @param prefixField          字段前缀
     * @param instanceSearchFields 实例字段搜索对象
     * @return 添加前缀后的对象
     */
    public static List<InstanceSearchField> appendPrefixField(String prefixField, List<InstanceSearchField> instanceSearchFields) {
        return instanceSearchFields.stream().map(instanceSearchField -> appendPrefixField(prefixField, instanceSearchField)).toList();
    }

    public static List<InstanceSearchField> appendPrefixField(String platform, ResourceTypeConstants resourceTypeConstants, List<InstanceSearchField> instanceSearchFields) {
        return appendPrefixField("instance" + platform + "_" + resourceTypeConstants.name() + ".", instanceSearchFields);
    }
}
