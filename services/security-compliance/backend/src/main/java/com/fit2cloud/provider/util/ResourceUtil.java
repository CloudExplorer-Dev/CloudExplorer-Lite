package com.fit2cloud.provider.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fit2cloud.common.utils.JsonUtil;
import com.fit2cloud.constants.ResourceTypeConstants;
import com.fit2cloud.es.entity.ResourceInstance;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import java.util.*;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2022/12/28  20:30}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
public class ResourceUtil {
    /**
     * 获取实例名称
     *
     * @param resourceType 资源类型
     * @param platform     云平台
     * @return 资源名称
     */
    public static String getResourceName(String resourceType, String platform) {
        return platform + "_" + resourceType;
    }

    public static ResourceInstance toResourceInstance(String platform, ResourceTypeConstants resourceTypeConstants, String resourceId, String resourceName, Map<String, List<?>> filterArray, Object instance) {
        ResourceInstance resourceInstance = toResourceInstance(platform, resourceTypeConstants, resourceId, resourceName, instance, null, null);
        resourceInstance.setFilterArray(resetFilterArray(platform, resourceTypeConstants, filterArray));
        return resourceInstance;
    }

    /**
     * 重置 filterArray数据
     *
     * @param platform              云平台
     * @param resourceTypeConstants 资源类型
     * @param filterArray           嵌套Array过滤
     * @return 重置后的Array
     */
    private static Map<String, List<Object>> resetFilterArray(String platform, ResourceTypeConstants resourceTypeConstants, Map<String, List<?>> filterArray) {
        HashMap<String, List<Object>> filterArrayMap = new HashMap<>();
        for (Map.Entry<String, List<?>> stringListEntry : filterArray.entrySet()) {
            filterArrayMap.put(platform + "_" + resourceTypeConstants.name() + "_" + stringListEntry.getKey(), CollectionUtils.isNotEmpty(stringListEntry.getValue()) ? stringListEntry.getValue().stream().map(s -> (Object) s).toList() : List.of());
        }
        return filterArrayMap;
    }

    public static ResourceInstance appendFilterArray(String platform, ResourceTypeConstants resourceTypeConstants, String resourceField, ResourceInstance instance, List<?> filterArray) {
        HashMap<String, List<Object>> filterArrayMap = new HashMap<>();
        filterArrayMap.put(platform + "_" + resourceTypeConstants.name() + "_" + resourceField, CollectionUtils.isNotEmpty(filterArray) ? filterArray.stream().map(s -> (Object) s).toList() : List.of());
        instance.setFilterArray(filterArrayMap);
        return instance;
    }

    /**
     * 将云平台实例转换为系统资源实例对象
     *
     * @param platform              云平台
     * @param resourceTypeConstants 实例类型
     * @param resourceId            资源id
     * @param resourceName          资源名称
     * @param instance              实例对象
     * @return 系统实例对象
     */
    public static ResourceInstance toResourceInstance(String platform, ResourceTypeConstants resourceTypeConstants, String resourceId, String resourceName, Object instance) {
        return toResourceInstance(platform, resourceTypeConstants, resourceId, resourceName, instance, null, null);
    }

    /**
     * 将云平台实例转换为系统资源实例对象
     *
     * @param platform              云平台
     * @param resourceTypeConstants 实例类型
     * @param resourceId            资源id
     * @param resourceName          资源名称
     * @param instance              实例对象
     * @return 系统实例对象
     */
    public static ResourceInstance toResourceInstance(String platform, ResourceTypeConstants resourceTypeConstants, String resourceId, String resourceName, Object instance, String otherDataKey, Object otherData) {
        ResourceInstance resourceInstance = new ResourceInstance();
        resourceInstance.setResourceType(resourceTypeConstants.name());
        resourceInstance.setPlatform(platform);
        resourceInstance.setResourceId(resourceId);
        resourceInstance.setResourceName(resourceName);
        resourceInstance.setId(UUID.randomUUID().toString().replace("-", ""));
        HashMap<String, Object> instanceTypeData = new HashMap<>();

        if (Objects.nonNull(otherData)) {
            HashMap<String, Object> instanceData = new HashMap<>(JsonUtil.parseObject(JsonUtil.toJSONString(instance), new TypeReference<Map<String, Object>>() {
            }));
            if (StringUtils.isNotEmpty(otherDataKey)) {
                instanceData.put(otherDataKey, JsonUtil.parseObject(JsonUtil.toJSONString(otherData), Map.class));
                instanceTypeData.put(getResourceName(resourceTypeConstants.name(), platform), instanceData);
            } else {
                instanceData.putAll(JsonUtil.parseObject(JsonUtil.toJSONString(otherData), new TypeReference<Map<String, Object>>() {
                }));
                instanceTypeData.put(getResourceName(resourceTypeConstants.name(), platform), instanceData);
            }
        } else {
            instanceTypeData.put(getResourceName(resourceTypeConstants.name(), platform), JsonUtil.parseObject(JsonUtil.toJSONString(instance), Map.class));
        }
        resourceInstance.setInstance(instanceTypeData);
        return resourceInstance;
    }

    /**
     * 转换对象为Map
     *
     * @param instance 需要转换的对象
     * @return 转换后的Map
     */
    public static Map<String, Object> objectToMap(Object instance) {
        return JsonUtil.parseObject(JsonUtil.toJSONString(instance), new TypeReference<>() {
        });
    }

    /**
     * 转换对象为Map
     *
     * @param instance 需要转换的对象
     * @return 转换后的Map
     */
    public static Map<String, Object> objectsToMap(Object... instance) {
        return Arrays.stream(instance).filter(Objects::nonNull).map(ResourceUtil::objectToMap).reduce(new HashMap<>(), (pre, next) -> {
            pre.putAll(next);
            return pre;
        });
    }


    /**
     * 将对象转换为字符串
     *
     * @param o 需要转换的对象
     * @return 对象字符串
     */
    public static String toString(Object o) {
        if (Objects.isNull(o)) {
            return "N/A";
        }
        return o.toString();
    }
}
