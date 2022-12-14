package com.fit2cloud.provider.impl.aliyun.entity.credential;

import com.aliyun.ecs20140526.Client;
import com.aliyun.teaopenapi.models.Config;
import com.fit2cloud.common.exception.Fit2cloudException;
import com.fit2cloud.common.provider.impl.aliyun.entity.credential.AliyunBaseCredential;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2022/12/6  11:22}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
public class AliSecurityComplianceCredential extends AliyunBaseCredential {

    /**
     * 根据区域获取客户端
     *
     * @param regionId 区域id
     * @return 阿里云ecs客户端
     */
    public Client getEcsClient(String regionId) {
        Config config = new Config()
                .setAccessKeyId(getAccessKeyId())
                .setAccessKeySecret(getAccessKeySecret())
                .setRegionId(regionId);
        try {
            Client client = new Client(config);
            return client;
        } catch (Exception e) {
            throw new Fit2cloudException(1000, "获取客户端失败");
        }
    }
}
