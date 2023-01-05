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

    /**
     * 获取redis客户端
     *
     * @param regionId 区域id
     * @return redis客户端
     */
    public com.aliyun.r_kvstore20150101.Client getRedisClient(String regionId) {
        com.aliyun.teaopenapi.models.Config config = new com.aliyun.teaopenapi.models.Config()
                .setAccessKeyId(getAccessKeyId())
                .setAccessKeySecret(getAccessKeySecret())
                .setRegionId(regionId);
        config.endpoint = "r-kvstore.aliyuncs.com";
        try {
            return new com.aliyun.r_kvstore20150101.Client(config);
        } catch (Exception e) {
            throw new Fit2cloudException(1000, "获取客户端失败");
        }
    }

    /**
     * 获取mongodb客户端
     *
     * @param regionId 区域
     * @return MongoDB客户端
     */
    public com.aliyun.dds20151201.Client getMongodbClient(String regionId) {
        com.aliyun.teaopenapi.models.Config config = new com.aliyun.teaopenapi.models.Config()
                .setAccessKeyId(getAccessKeyId())
                .setAccessKeySecret(getAccessKeySecret())
                .setRegionId(regionId);
        config.endpoint = "mongodb.aliyuncs.com";
        try {
            return new com.aliyun.dds20151201.Client(config);
        } catch (Exception e) {
            throw new Fit2cloudException(1000, "获取客户端失败");
        }
    }

    /**
     * 获取Rds客户端 -- MySQL,SQLServer,PostgreSQL,MariaDB
     *
     * @param regionId 区域
     * @return Rds客户端
     */
    public com.aliyun.rds20140815.Client getRdsClient(String regionId) {
        com.aliyun.teaopenapi.models.Config config = new com.aliyun.teaopenapi.models.Config()
                .setAccessKeyId(getAccessKeyId())
                .setAccessKeySecret(getAccessKeySecret())
                .setRegionId(regionId);
        config.endpoint = "rds.aliyuncs.com";
        try {
            return new com.aliyun.rds20140815.Client(config);
        } catch (Exception e) {
            throw new Fit2cloudException(1000, "获取客户端失败");
        }
    }

    /**
     * 获取 elasticsearch 客户端
     *
     * @param regionId 区域
     * @return elasticsearch 客户端
     */
    public com.aliyun.elasticsearch20170613.Client getElasticSearchClient(String regionId) {
        com.aliyun.teaopenapi.models.Config config = new com.aliyun.teaopenapi.models.Config()
                .setAccessKeyId(getAccessKeyId())
                .setAccessKeySecret(getAccessKeySecret())
                .setRegionId(regionId);
        // 访问的域名
        config.endpoint = "elasticsearch." + regionId + ".aliyuncs.com";
        try {
            return new com.aliyun.elasticsearch20170613.Client(config);
        } catch (Exception e) {
            throw new Fit2cloudException(1000, "获取客户端失败");
        }

    }
}
