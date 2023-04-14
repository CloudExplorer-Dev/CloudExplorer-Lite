package com.fit2cloud.provider.util;

import com.fit2cloud.common.constants.PlatformConstants;
import com.fit2cloud.constants.ResourceTypeConstants;
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

    /**
     * 添加实例instance字段前缀
     *
     * @param platform              供应商
     * @param resourceTypeConstants 资源类型
     * @param instanceSearchFields  实例查询字段
     * @return 添加前缀后的字段
     */
    public static List<InstanceSearchField> appendPrefixField(PlatformConstants platform, ResourceTypeConstants resourceTypeConstants, List<InstanceSearchField> instanceSearchFields) {
        return appendPrefixField("instance." + platform.name() + "_" + resourceTypeConstants.name() + ".", instanceSearchFields);
    }
}
