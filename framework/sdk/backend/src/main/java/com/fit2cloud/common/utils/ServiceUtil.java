package com.fit2cloud.common.utils;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.beans.BeansException;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @Author:张少虎
 * @Date: 2022/10/7  5:53 PM
 * @Version 1.0
 * @注释:
 */
@Component
public class ServiceUtil implements ApplicationContextAware {
    private final static String httpPrefix = "https://";
    private static DiscoveryClient discoveryClient;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        ServiceUtil.discoveryClient = applicationContext.getBean(DiscoveryClient.class);
    }

    /**
     * 获取所有的服务
     *
     * @return 获取所有服务
     */
    public static Set<String> getServices() {
        return new HashSet<>(discoveryClient.getServices());
    }

    /**
     * 获取所有服务,可根据服务名称排除
     *
     * @param excludes 需要排除的服务
     * @return 排除后的服务
     */
    public static Set<String> getServices(String... excludes) {
        return getServices().stream().filter(server -> !Arrays.asList(excludes).contains(server)).collect(Collectors.toSet());
    }

    /**
     * 获取除开gateway以外的服务
     *
     * @return 除开gateway以外的服务
     */
    public static Set<String> getServicesExcludeGateway() {
        return getServices("gateway");
    }

    public static Set<String> getServicesExcludeGatewayAndIncludeSelf(String selfService) {
        Set<String> services = getServices("gateway");
        services.add(selfService);
        return services;
    }

    /**
     * 根据服务名称获取所有的实例
     *
     * @param serviceId 服务名称
     * @return 所有实例
     */
    public static List<ServiceInstance> getInstances(String serviceId) {
        return discoveryClient.getInstances(serviceId);
    }

    /**
     * 随机获取一个实例
     *
     * @param serviceId 服务名称
     * @return 实例
     */
    public static ServiceInstance getRandomInstance(String serviceId) {
        List<ServiceInstance> instances = getInstances(serviceId);
        if (CollectionUtils.isNotEmpty(instances)) {
            return instances.get(RandomUtils.nextInt(0, instances.size()));
        }
        throw new RuntimeException("没有运行的实例");
    }

    /**
     * 获取请求连接
     *
     * @param module  服务名称
     * @param apiPath 接口path
     * @return httpUrl
     */
    public static String getHttpUrl(String module, String apiPath) {
        return String.format(httpPrefix + "%s/%s/%s", module, module, apiPath);
    }

}
