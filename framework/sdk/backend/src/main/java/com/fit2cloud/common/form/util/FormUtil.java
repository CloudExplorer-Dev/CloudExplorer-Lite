package com.fit2cloud.common.form.util;

import com.fit2cloud.common.form.annotaion.From;
import com.fit2cloud.common.form.vo.Form;
import com.fit2cloud.common.platform.bill.impl.AliBill;
import com.fit2cloud.common.utils.JsonUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.commons.lang3.reflect.MethodUtils;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2022/10/11  2:15 PM}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
public class FormUtil {
    public static List<Form> toForm(Class<?> clazz) {
        Field[] fieldsWithAnnotation = FieldUtils.getFieldsWithAnnotation(clazz, From.class);
        return Arrays.stream(fieldsWithAnnotation).map(field -> {
            field.setAccessible(true);
            From annotation = field.getAnnotation(From.class);
            Map<String, Object> map = new HashMap<>();
            map.put("defaultValue", annotation.defaultValue());
            map.put("inputType", annotation.inputType());
            map.put("label", annotation.label());
            map.put("required", annotation.required());
            map.put("description", annotation.description());
            map.put("valueField", annotation.valueField());
            map.put("textField", annotation.textField());
            map.put("field", field.getName());
            map.put("relationShows", annotation.relationShows());
            map.put("relationTrigger", annotation.relationTrigger());
            if (StringUtils.isNotEmpty(annotation.method())) {
                map.put("method", annotation.method());
                map.put("clazz", annotation.clazz().getName());
            }
            String jsonString = JsonUtil.toJSONString(map);
            return JsonUtil.parseObject(jsonString, annotation.inputType().getFormClass());
        }).collect(Collectors.toList());
    }

    /**
     * 执行class函数
     *
     * @param clazz  需要执行的类
     * @param method 执行函数
     * @param params 参数
     * @return 执行返回值
     */
    public static Object exec(String clazz, String method, Map<String, Object> params) {
        try {
            return MethodUtils.invokeMethod(Class.forName(clazz).getConstructor().newInstance(), true, method, JsonUtil.toJSONString(params));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
