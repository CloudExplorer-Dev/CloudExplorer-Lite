package com.fit2cloud.provider.impl.aliyun.entity.credential;

import com.aliyun.ecs20140526.Client;
import com.aliyun.teaopenapi.models.Config;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fit2cloud.common.exception.Fit2cloudException;
import com.fit2cloud.common.platform.credential.Credential;
import com.fit2cloud.common.platform.credential.impl.AliCredential;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
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
     * @param regionId
     * @return
     */
    public Client getClientByRegion(String regionId) {
        if(regionCache.size()==0){
            regionCache = this.regions().stream().collect(Collectors.toMap(Region::getRegionId,v->v,(k1,k2)->k1));
        }
        String endpoint = regionCache.get(regionId)==null?"ecs.aliyuncs.com":regionCache.get(regionId).getEndpoint();
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

}
