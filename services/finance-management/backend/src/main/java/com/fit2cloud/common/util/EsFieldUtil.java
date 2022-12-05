package com.fit2cloud.common.util;

import com.fit2cloud.common.exception.Fit2cloudException;
import com.fit2cloud.constants.ErrorCodeConstants;
import com.fit2cloud.es.entity.CloudBill;
import org.apache.commons.collections4.keyvalue.DefaultKeyValue;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.springframework.data.elasticsearch.annotations.MultiField;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class EsFieldUtil {

    /**
     * 获取分组字段
     *
     * @param groupField 实例对象字段
     * @return 聚合字段
     */
    public static String getGroupKeyByField(String groupField) {
        if (groupField.startsWith("tags.") || groupField.startsWith("orgTree.")) {
            return groupField + "." + org.springframework.data.elasticsearch.annotations.FieldType.Keyword.getMappedName();
        }
        Field field = FieldUtils.getField(CloudBill.class, groupField, true);
        if (field.isAnnotationPresent(org.springframework.data.elasticsearch.annotations.Field.class)) {
            if (field.getAnnotation(org.springframework.data.elasticsearch.annotations.Field.class).type().equals(org.springframework.data.elasticsearch.annotations.FieldType.Keyword)) {
                return field.getName();
            }
        } else if (field.isAnnotationPresent(MultiField.class)) {
            MultiField annotation = field.getAnnotation(MultiField.class);
            return Arrays.stream(annotation.otherFields()).filter(a -> a.type().equals(org.springframework.data.elasticsearch.annotations.FieldType.Keyword)).findFirst().map(f -> {
                return field.getName() + "." + f.suffix();
            }).orElseThrow(() -> new Fit2cloudException(ErrorCodeConstants.BILL_VIEW_UNSUPPORTED_GROUP_FIELD.getCode(), ErrorCodeConstants.BILL_VIEW_UNSUPPORTED_GROUP_FIELD.getMessage(new Object[]{groupField})));
        }
        throw new Fit2cloudException(ErrorCodeConstants.BILL_VIEW_UNSUPPORTED_GROUP_FIELD.getCode(), ErrorCodeConstants.BILL_VIEW_UNSUPPORTED_GROUP_FIELD.getMessage(new Object[]{groupField}));
    }

    /**
     * 获取标签字段
     *
     * @param properties mapping properties
     * @param parentName 传null 用于递归 可以传开头
     * @return 获取es下面的字段
     */
    private static List<String> getEsField(Map<String, Map<String, Object>> properties, String parentName) {
        return properties.entrySet().stream().map((t) -> {
            Map<String, Object> value = t.getValue();
            if (value.get("type").toString().equals(co.elastic.clients.elasticsearch._types.mapping.FieldType.Text.jsonValue())) {
                return List.of(StringUtils.isEmpty(parentName) ? t.getKey() : parentName + "." + t.getKey());
            } else {
                return getEsField((Map<String, Map<String, Object>>) value.get("properties"), StringUtils.isEmpty(parentName) ? t.getKey() : parentName + "." + t.getKey());
            }
        }).flatMap(List::stream).toList();
    }

    /**
     * 查询
     *
     * @param mapping    esMapping
     * @param parentName 父名称
     * @return 子字段
     */
    public static List<DefaultKeyValue<String, String>> getChildEsField(Map<String, Object> mapping, String parentName) {
        Map<String, Map<String, Object>> properties = (Map<String, Map<String, Object>>) mapping.get("properties");
        if (properties.containsKey(parentName)) {
            return getEsField((Map<String, Map<String, Object>>) properties.get(parentName).get("properties"), null).stream().map(field -> new DefaultKeyValue<>(field, parentName + "." + field)).toList();
        }
        return new ArrayList<>();
    }

}
