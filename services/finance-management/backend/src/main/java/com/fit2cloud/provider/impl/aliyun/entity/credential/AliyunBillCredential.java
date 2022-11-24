package com.fit2cloud.provider.impl.aliyun.entity.credential;

import com.aliyun.bssopenapi20171214.Client;
import com.aliyun.teaopenapi.models.Config;
import com.fit2cloud.common.platform.credential.Credential;
import com.fit2cloud.common.platform.credential.impl.AliCredential;

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
}
