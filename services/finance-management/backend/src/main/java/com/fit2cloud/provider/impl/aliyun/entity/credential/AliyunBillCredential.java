package com.fit2cloud.provider.impl.aliyun.entity.credential;

import com.aliyun.auth.credentials.provider.StaticCredentialProvider;
import com.aliyun.bssopenapi20171214.Client;
import com.aliyun.sdk.service.oss20190517.AsyncClient;
import com.aliyun.teaopenapi.models.Config;
import com.fit2cloud.common.platform.credential.Credential;
import com.fit2cloud.common.platform.credential.impl.AliCredential;
import darabonba.core.client.ClientOverrideConfiguration;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2022/10/13  4:47 PM}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
public class AliyunBillCredential extends AliCredential implements Credential {

    public Client getClient() {
        Config config = new com.aliyun.teaopenapi.models.Config()
                // 您的 AccessKey ID
                .setAccessKeyId(getAccessKeyId())
                // 您的 AccessKey Secret
                .setAccessKeySecret(getAccessKeySecret());
        // 访问的域名
        config.endpoint = "business.aliyuncs.com";
        Client client = null;
        try {
            client = new Client(config);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return client;
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
