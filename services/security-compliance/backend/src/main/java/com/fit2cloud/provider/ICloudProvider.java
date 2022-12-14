package com.fit2cloud.provider;

import com.fit2cloud.es.entity.ResourceInstance;
import com.fit2cloud.provider.constants.ProviderConstants;
import com.fit2cloud.provider.entity.InstanceSearchField;

import java.util.Arrays;
import java.util.List;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2022/12/5  15:41}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
public interface ICloudProvider {
    /**
     * 获取云服务器列表
     *
     * @param req 请求参数 包括认证对象
     * @return 云服务器列表
     */
    List<ResourceInstance> listEcsInstance(String req);

    /**
     * 获取实例可查询字段
     *
     * @return 实例可查询字段
     */
    List<InstanceSearchField> listEcsInstanceSearchField();

    /**
     * 根据供应商获取对应云平台处理器
     *
     * @param platform 供应商
     * @return 处理器
     */
    static Class<? extends ICloudProvider> of(String platform) {
        return (Class<? extends ICloudProvider>) Arrays.stream(ProviderConstants.values()).filter(providerConstants -> providerConstants.name().equals(platform)).findFirst().orElseThrow(() -> new RuntimeException("不支持的云平台")).getCloudProvider();
    }
}
