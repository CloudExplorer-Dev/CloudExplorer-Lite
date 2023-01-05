package com.fit2cloud.provider.impl.tencent.entity.credential;

import com.fit2cloud.common.provider.impl.tencent.entity.credential.TencentBaseCredential;
import com.tencentcloudapi.cdb.v20170320.CdbClient;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.cvm.v20170312.CvmClient;
import com.tencentcloudapi.es.v20180416.EsClient;
import com.tencentcloudapi.mariadb.v20170312.MariadbClient;
import com.tencentcloudapi.postgres.v20170312.PostgresClient;
import com.tencentcloudapi.redis.v20180412.RedisClient;
import com.tencentcloudapi.mongodb.v20190725.MongodbClient;
import com.tencentcloudapi.sqlserver.v20180328.SqlserverClient;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2022/12/6  13:49}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
public class TencentSecurityComplianceCredential extends TencentBaseCredential {
    /**
     * 获取腾讯云Cvm客户端
     *
     * @param region 区域
     * @return 腾讯云cvm客户端对象
     */
    public CvmClient getCvmClient(String region) {
        Credential cred = new Credential(getSecretId(), getSecretKey());
        HttpProfile httpProfile = new HttpProfile();
        httpProfile.setEndpoint("cvm.tencentcloudapi.com");
        ClientProfile clientProfile = new ClientProfile();
        clientProfile.setHttpProfile(httpProfile);
        return new CvmClient(cred, region, clientProfile);
    }

    /**
     * 获取redis客户端
     *
     * @param region 区域
     * @return redis客户端
     */
    public RedisClient getRedisClient(String region) {
        Credential cred = new Credential(getSecretId(), getSecretKey());
        HttpProfile httpProfile = new HttpProfile();
        httpProfile.setEndpoint("redis.tencentcloudapi.com");
        ClientProfile clientProfile = new ClientProfile();
        clientProfile.setHttpProfile(httpProfile);
        return new RedisClient(cred, region, clientProfile);
    }

    /**
     * 获取 mongodb客户端
     *
     * @param region 区域
     * @return mongodb客户端
     */
    public MongodbClient getMongodbClient(String region) {
        Credential cred = new Credential(getSecretId(), getSecretKey());
        HttpProfile httpProfile = new HttpProfile();
        httpProfile.setEndpoint("mongodb.tencentcloudapi.com");
        ClientProfile clientProfile = new ClientProfile();
        clientProfile.setHttpProfile(httpProfile);
        return new MongodbClient(cred, region, clientProfile);
    }

    /**
     * 获取 cdb Mysql客户端
     *
     * @param region 区域
     * @return cdb(mysql) 客户端
     */
    public CdbClient getCdbClient(String region) {
        Credential cred = new Credential(getSecretId(), getSecretKey());
        HttpProfile httpProfile = new HttpProfile();
        httpProfile.setEndpoint("cdb.tencentcloudapi.com");
        ClientProfile clientProfile = new ClientProfile();
        clientProfile.setHttpProfile(httpProfile);
        return new CdbClient(cred, region, clientProfile);
    }

    /**
     * 获取 sqlServer客户端
     *
     * @param region 区域
     * @return SqlServer客户端
     */
    public SqlserverClient getSqlServerClient(String region) {
        Credential cred = new Credential(getSecretId(), getSecretKey());
        HttpProfile httpProfile = new HttpProfile();
        httpProfile.setEndpoint("sqlserver.tencentcloudapi.com");
        ClientProfile clientProfile = new ClientProfile();
        clientProfile.setHttpProfile(httpProfile);
        return new SqlserverClient(cred, region, clientProfile);
    }

    /**
     * 获取 PostGreSql 客户端
     *
     * @param region 区域
     * @return PostGreSql客户端
     */
    public PostgresClient getPostgresClient(String region) {
        Credential cred = new Credential(getSecretId(), getSecretKey());
        HttpProfile httpProfile = new HttpProfile();
        httpProfile.setEndpoint("postgres.tencentcloudapi.com");
        ClientProfile clientProfile = new ClientProfile();
        clientProfile.setHttpProfile(httpProfile);
        return new PostgresClient(cred, region, clientProfile);
    }

    /**
     * 获取 MariaDB客户端
     *
     * @param region 区域
     * @return MariaDB客户端
     */
    public MariadbClient getMariadbClient(String region) {
        Credential cred = new Credential(getSecretId(), getSecretKey());
        HttpProfile httpProfile = new HttpProfile();
        httpProfile.setEndpoint("mariadb.tencentcloudapi.com");
        ClientProfile clientProfile = new ClientProfile();
        clientProfile.setHttpProfile(httpProfile);
        return new MariadbClient(cred, region, clientProfile);
    }

    /**
     * 获取 ElasticSearch 客户端
     *
     * @param region 区域
     * @return ElasticSearch 客户端
     */
    public EsClient getEsClient(String region) {
        Credential cred = new Credential(getSecretId(), getSecretKey());
        HttpProfile httpProfile = new HttpProfile();
        httpProfile.setEndpoint("es.tencentcloudapi.com");
        ClientProfile clientProfile = new ClientProfile();
        clientProfile.setHttpProfile(httpProfile);
        return new EsClient(cred, region, clientProfile);
    }
}
