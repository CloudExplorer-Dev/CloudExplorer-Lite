package com.fit2cloud.constants;

import com.fit2cloud.es.entity.ResourceInstance;
import com.fit2cloud.provider.ICloudProvider;
import com.fit2cloud.provider.entity.InstanceSearchField;
import io.reactivex.rxjava3.functions.BiFunction;

import java.util.List;
import java.util.function.Function;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2022/12/6  14:03}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
public enum ResourceTypeConstants {
    /**
     * 云服务器
     */
    ECS("云服务器", ICloudProvider::listEcsInstance, ICloudProvider::listEcsInstanceSearchField);
    /**
     * 提示
     */
    private final String message;
    /**
     * 获取资源执行器
     */
    private BiFunction<ICloudProvider, String, List<ResourceInstance>> exec;

    /**
     * 获取查询子弹
     */
    private Function<ICloudProvider, List<InstanceSearchField>> listInstanceSearchField;

    ResourceTypeConstants(String message, BiFunction<ICloudProvider, String, List<ResourceInstance>> exec, Function<ICloudProvider, List<InstanceSearchField>> listInstanceSearchField) {
        this.exec = exec;
        this.listInstanceSearchField = listInstanceSearchField;
        this.message = message;
    }

    public Function<ICloudProvider, List<InstanceSearchField>> getListInstanceSearchField() {
        return listInstanceSearchField;
    }

    public BiFunction<ICloudProvider, String, List<ResourceInstance>> getExec() {
        return exec;
    }

    public String getMessage() {
        return message;
    }
}
