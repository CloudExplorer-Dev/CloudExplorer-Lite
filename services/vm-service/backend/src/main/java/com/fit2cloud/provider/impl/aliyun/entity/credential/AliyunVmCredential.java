package com.fit2cloud.provider.impl.aliyun.entity.credential;

import com.aliyun.ecs20140526.Client;
import com.aliyun.teaopenapi.models.Config;
import com.fit2cloud.common.exception.Fit2cloudException;
import com.fit2cloud.common.platform.credential.Credential;
import com.fit2cloud.common.platform.credential.impl.AliCredential;
import lombok.Data;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * @Author:张少虎
 * @Date: 2022/9/20  11:47 AM
 * @Version 1.0
 * @注释:
 */
@Data
public class AliyunVmCredential extends AliCredential implements Credential {

    private static Map<String, Region> regionCache = new ConcurrentHashMap<String, Region>();

    public Client getClient() {
        Config config = new Config()
                .setAccessKeyId(getAccessKeyId())
                .setAccessKeySecret(getAccessKeySecret())
                .setEndpoint("ecs.aliyuncs.com");
        try {
            Client client = new Client(config);
            return client;
        } catch (Exception e) {
            throw new Fit2cloudException(1000, "获取客户端失败");
        }
    }

    /**
     * 根据区域获取客户端
     *
     * @param regionId
     * @return
     */
    public Client getClientByRegion(String regionId) {
        if (regionCache.size() == 0) {
            regionCache = this.regions().stream().collect(Collectors.toMap(Region::getRegionId, v -> v, (k1, k2) -> k1));
        }
        String endpoint = regionCache.get(regionId) == null ? "ecs.aliyuncs.com" : regionCache.get(regionId).getEndpoint();
        Config config = new Config()
                .setAccessKeyId(getAccessKeyId())
                .setAccessKeySecret(getAccessKeySecret())
                .setEndpoint(endpoint);
        try {
            Client client = new Client(config);
            return client;
        } catch (Exception e) {
            throw new Fit2cloudException(1000, "获取客户端失败");
        }
    }

    /**
     * 获取云监控客户端
     *
     * @param regionId
     * @return
     */
    public com.aliyun.cms20190101.Client getCmsClientByRegion(String regionId) {
        if (regionCache.size() == 0) {
            regionCache = this.regions().stream().collect(Collectors.toMap(Region::getRegionId, v -> v, (k1, k2) -> k1));
        }
        String endpoint = regionCache.get(regionId) == null ? "ecs.aliyuncs.com" : regionCache.get(regionId).getEndpoint();
        Config config = new Config()
                .setAccessKeyId(getAccessKeyId())
                .setAccessKeySecret(getAccessKeySecret())
                .setEndpoint(endpoint.replaceAll("ecs", "metrics"));
        try {
            com.aliyun.cms20190101.Client client = new com.aliyun.cms20190101.Client(config);
            return client;
        } catch (Exception e) {
            throw new Fit2cloudException(1000, "获取客户端失败");
        }
    }

    public com.aliyun.bssopenapi20171214.Client getBssClient() {
        Config config = new Config()
                .setAccessKeyId(getAccessKeyId())
                .setAccessKeySecret(getAccessKeySecret())
                .setEndpoint("business.aliyuncs.com");
        com.aliyun.bssopenapi20171214.Client client = null;
        try {
            client = new com.aliyun.bssopenapi20171214.Client(config);
            return client;
        } catch (Exception e) {
            throw new Fit2cloudException(1000, "获取客户端失败");
        }
    }
}
