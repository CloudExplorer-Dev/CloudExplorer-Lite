package com.fit2cloud.common.form.util;

import com.fit2cloud.autoconfigure.PluginsContextHolder;
import com.fit2cloud.common.form.annotaion.FormConfirmInfo;
import com.fit2cloud.common.form.annotaion.FormGroupInfo;
import com.fit2cloud.common.form.annotaion.FormStepInfo;
import com.fit2cloud.common.form.vo.FormObject;
import com.fit2cloud.common.utils.JsonUtil;
import com.fit2cloud.common.utils.SpringUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.commons.lang3.reflect.MethodUtils;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2022/10/11  2:15 PM}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
public class FormUtil {
    public static <T> FormObject toForm(Class<T> clazz) {

        FormGroupInfo[] groupAnnotations = clazz.getAnnotationsByType(FormGroupInfo.class);
        Map<Integer, Map<String, Object>> groupAnnotationMap = Arrays.stream(groupAnnotations)
                .filter((groupAnnotation) -> groupAnnotation.group() > 0)
                .collect(Collectors.toMap(FormGroupInfo::group, annotation -> {
                    Map<String, Object> objectMap = new HashMap<>();
                    objectMap.put("group", annotation.group());
                    objectMap.put("name", annotation.name());
                    objectMap.put("description", annotation.description());
                    objectMap.put("inline", annotation.inline());
                    return objectMap;
                }));

        FormStepInfo[] stepAnnotations = clazz.getAnnotationsByType(FormStepInfo.class);
        Map<Integer, Map<String, Object>> stepAnnotationMap = Arrays.stream(stepAnnotations)
                .filter((stepAnnotation) -> stepAnnotation.step() > 0)
                .collect(Collectors.toMap(FormStepInfo::step, annotation -> {
                    Map<String, Object> objectMap = new HashMap<>();
                    objectMap.put("step", annotation.step());
                    objectMap.put("name", annotation.name());
                    objectMap.put("description", annotation.description());
                    return objectMap;
                }));

        FormConfirmInfo[] confirmAnnotations = clazz.getAnnotationsByType(FormConfirmInfo.class);
        Map<Integer, Map<String, Object>> confirmAnnotationMap = Arrays.stream(confirmAnnotations)
                .collect(Collectors.toMap(FormConfirmInfo::group, annotation -> {
                    Map<String, Object> objectMap = new HashMap<>();
                    objectMap.put("group", annotation.group());
                    objectMap.put("name", annotation.name());
                    objectMap.put("description", annotation.description());
                    objectMap.put("items", annotation.items());
                    return objectMap;
                }));

        boolean hasGroup = groupAnnotationMap.size() > 0, hasStep = stepAnnotationMap.size() > 0, hasConfirm = confirmAnnotationMap.size() > 0;

        Field[] fieldsWithAnnotation = FieldUtils.getFieldsWithAnnotation(clazz, com.fit2cloud.common.form.annotaion.Form.class);
        AtomicInteger atomicInteger = new AtomicInteger(0);
        FormObject formObject = new FormObject().setForms(Arrays.stream(fieldsWithAnnotation).map(field -> {
            field.setAccessible(true);
            com.fit2cloud.common.form.annotaion.Form annotation = field.getAnnotation(com.fit2cloud.common.form.annotaion.Form.class);
            Map<String, Object> map = new HashMap<>();
            map.put("defaultValue", annotation.defaultValue());
            map.put("defaultJsonValue", annotation.defaultJsonValue());
            map.put("inputType", annotation.inputType());
            map.put("attrs", annotation.attrs());
            map.put("label", annotation.label());
            map.put("leftLabel", annotation.leftLabel());
            map.put("hideLabel", annotation.hideLabel());
            map.put("required", annotation.required());
            map.put("description", annotation.description());
            map.put("valueField", annotation.valueField());
            map.put("textField", annotation.textField());
            map.put("baseTextField", annotation.baseTextField());
            map.put("formatTextField", annotation.formatTextField());
            map.put("unit", annotation.unit());
            map.put("field", field.getName());
            map.put("relationShows", annotation.relationShows());
            map.put("relationShowValues", annotation.relationShowValues().length == 0 ? null : annotation.relationShowValues());
            map.put("footerLocation", annotation.footerLocation());
            map.put("relationTrigger", annotation.relationTrigger());
            map.put("propsInfo", JsonUtil.parseObject(annotation.propsInfo()));
            map.put("index", atomicInteger.getAndIncrement());
            map.put("regexp", annotation.regexp());
            map.put("regexpDescription", annotation.regexpDescription());
            map.put("regexList", JsonUtil.parseArray(annotation.regexList()));
            map.put("encrypted", annotation.encrypted());
            if (StringUtils.isNotEmpty(annotation.pluginId())) {
                map.put("pluginId", annotation.pluginId());
            }
            if (StringUtils.isNotEmpty(annotation.execModule())) {
                map.put("execModule", annotation.execModule());
            }
            if (StringUtils.isNotEmpty(annotation.method())) {
                map.put("method", annotation.method());
                map.put("clazz", annotation.clazz().getName());
                map.put("serviceMethod", annotation.serviceMethod());
            }
            if (hasStep) {
                map.put("step", stepAnnotationMap.get(annotation.step()) != null ? annotation.step() : 0);
            }
            if (hasGroup) {
                map.put("group", groupAnnotationMap.get(annotation.group()) != null ? annotation.group() : 0);
            }
            if (hasConfirm) {
                map.put("confirmGroup", confirmAnnotationMap.get(annotation.confirmGroup()) != null ? annotation.confirmGroup() : -1);
                map.put("confirmSpecial", annotation.confirmSpecial());
                map.put("confirmPosition", annotation.confirmPosition());
                map.put("confirmItemSpan", annotation.confirmItemSpan() > 0 ? annotation.confirmItemSpan() : 1);
            }
            if (StringUtils.isNotBlank(annotation.extraInfo())) {
                map.put("extraInfo", annotation.extraInfo());
            }
            if (StringUtils.isNotBlank(annotation.hint())) {
                map.put("hint", annotation.hint());
            }

            String jsonString = JsonUtil.toJSONString(map);
            return JsonUtil.parseObject(jsonString, annotation.inputType().getFormClass());
        }).toList());

        if (hasStep) {
            formObject.setStepAnnotationMap(stepAnnotationMap);
        }
        if (hasGroup) {
            formObject.setGroupAnnotationMap(groupAnnotationMap);
        }
        if (hasConfirm) {
            formObject.setConfirmAnnotationMap(confirmAnnotationMap);
        }

        return formObject;
    }

    /**
     * 执行class函数
     *
     * @param clazz  需要执行的类
     * @param method 执行函数
     * @param params 参数
     * @return 执行返回值
     */
    public static Object exec(String clazz, boolean serviceMethod, String method, String pluginId, Map<String, Object> params) {
        try {
            if (serviceMethod) {
                return MethodUtils.invokeMethod(SpringUtil.getBean(Class.forName(clazz)), true, method, JsonUtil.toJSONString(params));
            } else {
                if (StringUtils.isNotEmpty(pluginId)) {
                    Class<?> aClass = PluginsContextHolder.pluginManager.getPluginClassLoader(pluginId).loadClass(clazz);
                    return MethodUtils.invokeMethod(aClass.getConstructor().newInstance(), true, method, JsonUtil.toJSONString(params));
                } else {
                    return MethodUtils.invokeMethod(Class.forName(clazz).getConstructor().newInstance(), true, method, JsonUtil.toJSONString(params));
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
