package com.fit2cloud.provider.impl.tencent.entity.credential;

import com.fit2cloud.common.platform.credential.Credential;
import com.fit2cloud.common.platform.credential.impl.TencentCredential;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.tencentcloudapi.billing.v20180709.BillingClient;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2022/10/14  10:52 AM}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
public class TencentBillCredential extends TencentCredential implements Credential {
    public BillingClient getBillingClient() {
        // 实例化一个http选项，可选的，没有特殊需求可以跳过
        HttpProfile httpProfile = new HttpProfile();
        httpProfile.setEndpoint("billing.tencentcloudapi.com");
        // 实例化一个client选项，可选的，没有特殊需求可以跳过
        ClientProfile clientProfile = new ClientProfile();
        clientProfile.setHttpProfile(httpProfile);
        // 实例化要请求产品的client对象,clientProfile是可选的
        return new BillingClient(new com.tencentcloudapi.common.Credential(getSecretId(), getSecretKey()), "", clientProfile);
    }

    public COSClient getCOSClient(String region) {
        COSCredentials cred = new BasicCOSCredentials(getSecretId(), getSecretKey());
        return new COSClient(cred, new ClientConfig(new com.qcloud.cos.region.Region(region)));
    }
}
