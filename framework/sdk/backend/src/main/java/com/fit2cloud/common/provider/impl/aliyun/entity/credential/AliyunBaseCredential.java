package com.fit2cloud.common.provider.impl.aliyun.entity.credential;

import com.aliyun.ecs20140526.Client;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.teaopenapi.models.Config;
import com.fit2cloud.common.exception.Fit2cloudException;
import com.fit2cloud.common.platform.credential.Credential;
import com.fit2cloud.common.platform.credential.impl.AliCredential;
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


    public OSS getOssClient() {
        return new OSSClientBuilder().build("oss-cn-shanghai.aliyuncs.com", getAccessKeyId(), getAccessKeySecret());
    }

}
