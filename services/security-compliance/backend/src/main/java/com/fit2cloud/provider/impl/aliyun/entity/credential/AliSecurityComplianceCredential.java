package com.fit2cloud.provider.impl.aliyun.entity.credential;

import com.aliyun.auth.credentials.Credential;
import com.aliyun.auth.credentials.provider.StaticCredentialProvider;
import com.aliyun.ecs20140526.Client;
import com.aliyun.sdk.service.oss20190517.AsyncClient;
import com.aliyun.teaopenapi.models.Config;
import com.fit2cloud.common.exception.Fit2cloudException;
import com.fit2cloud.common.provider.impl.aliyun.entity.credential.AliyunBaseCredential;
import darabonba.core.client.ClientOverrideConfiguration;

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

    /**
     * 获取 负载均衡 客户端
     *
     * @param regionId 区域
     * @return 负载均衡 客户端
     */
    public com.aliyun.slb20140515.Client getLoadBalancerClient(String regionId) {
        com.aliyun.teaopenapi.models.Config config = new com.aliyun.teaopenapi.models.Config()
                .setAccessKeyId(getAccessKeyId())
                .setAccessKeySecret(getAccessKeySecret())
                .setRegionId(regionId);
        // 访问的域名
        config.endpoint = "slb.aliyuncs.com";
        try {
            return new com.aliyun.slb20140515.Client(config);
        } catch (Exception e) {
            throw new Fit2cloudException(1000, "获取客户端失败");
        }
    }

    /**
     * 获取 vpc(弹性公网ip,VPC..)客户端
     *
     * @param regionId 区域id
     * @return vpc(弹性公网ip, VPC..)客户端
     */
    public com.aliyun.vpc20160428.Client getVpcClient(String regionId) {
        com.aliyun.teaopenapi.models.Config config = new com.aliyun.teaopenapi.models.Config()
                .setAccessKeyId(getAccessKeyId())
                .setAccessKeySecret(getAccessKeySecret())
                .setRegionId(regionId);
        // 访问的域名
        config.endpoint = "vpc.aliyuncs.com";
        try {
            return new com.aliyun.vpc20160428.Client(config);
        } catch (Exception e) {
            throw new Fit2cloudException(1000, "获取客户端失败");
        }
    }

    /**
     * 获取Ram客户端
     *
     * @return Ram客户端
     */
    public com.aliyun.ram20150501.Client getRamClient() {
        com.aliyun.teaopenapi.models.Config config = new com.aliyun.teaopenapi.models.Config()
                .setAccessKeyId(getAccessKeyId())
                .setAccessKeySecret(getAccessKeySecret());
        // 访问的域名
        config.endpoint = "ram.aliyuncs.com";
        try {
            return new com.aliyun.ram20150501.Client(config);
        } catch (Exception e) {
            throw new Fit2cloudException(1000, "获取客户端失败");
        }
    }

    /**
     * 获取对象存储客户端
     *
     * @param region 区域
     * @return 对象存储客户端
     */
    public AsyncClient getOssClient(String region) {
        StaticCredentialProvider provider = StaticCredentialProvider.create(Credential.builder()
                .accessKeyId(getAccessKeyId())
                .accessKeySecret(getAccessKeySecret())
                .build());
        return AsyncClient.builder()
                .region(region)
                .credentialsProvider(provider)
                .build();
    }

}
