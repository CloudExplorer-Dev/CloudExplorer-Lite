package com.fit2cloud.common.provider.impl.aliyun.entity.credential;

import com.aliyun.auth.credentials.provider.StaticCredentialProvider;
import com.aliyun.ecs20140526.Client;
import com.aliyun.sdk.service.oss20190517.AsyncClient;
import com.aliyun.teaopenapi.models.Config;
import com.fit2cloud.common.exception.Fit2cloudException;
import com.fit2cloud.common.platform.credential.Credential;
import com.fit2cloud.common.platform.credential.impl.AliCredential;
import darabonba.core.client.ClientOverrideConfiguration;
import lombok.Data;

@Data
public class AliyunBaseCredential extends AliCredential implements Credential {
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


    public com.aliyun.bssopenapi20171214.Client getBssClient() {
        Config config = new Config()
                .setAccessKeyId(getAccessKeyId())
                .setAccessKeySecret(getAccessKeySecret())
                .setEndpoint("business.aliyuncs.com");
        try {
            return new com.aliyun.bssopenapi20171214.Client(config);
        } catch (Exception e) {
            throw new Fit2cloudException(1000, "获取客户端失败");
        }
    }


    public AsyncClient getOssClient() {
        StaticCredentialProvider provider = StaticCredentialProvider.create(com.aliyun.auth.credentials.Credential.builder()
                .accessKeyId(getAccessKeyId())
                .accessKeySecret(getAccessKeySecret())
                .build());
        return AsyncClient.builder()
                //.httpClient(httpClient) // Use the configured HttpClient, otherwise use the default HttpClient (Apache HttpClient)
                .credentialsProvider(provider)
                //.serviceConfiguration(Configuration.create()) // Service-level configuration
                // Client-level configuration rewrite, can set Endpoint, Http request parameters, etc.
                .overrideConfiguration(
                        ClientOverrideConfiguration.create()
                                .setEndpointOverride("oss-cn-qingdao.aliyuncs.com")
                        //.setConnectTimeout(Duration.ofSeconds(30))
                )
                .build();

    }


}
