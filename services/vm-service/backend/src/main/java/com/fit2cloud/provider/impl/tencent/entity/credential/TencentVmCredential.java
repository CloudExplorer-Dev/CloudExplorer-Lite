package com.fit2cloud.provider.impl.tencent.entity.credential;

import com.fit2cloud.common.platform.credential.impl.TencentCredential;
import com.tencentcloudapi.cbs.v20170312.CbsClient;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.cvm.v20170312.CvmClient;

/**
 * @Author:张少虎
 * @Date: 2022/9/23  3:47 PM
 * @Version 1.0
 * @注释:
 */
public class TencentVmCredential extends TencentCredential implements com.fit2cloud.common.platform.credential.Credential {
    public CvmClient getCvmClient(String region) {
        Credential cred = new Credential(getSecretId(), getSecretKey());
        HttpProfile httpProfile = new HttpProfile();
        httpProfile.setEndpoint("cvm.tencentcloudapi.com");
        ClientProfile clientProfile = new ClientProfile();
        clientProfile.setHttpProfile(httpProfile);
        return new CvmClient(cred, region, clientProfile);
    }

    public CbsClient getCbsClient(String region) {
        Credential cred = new Credential(getSecretId(), getSecretKey());
        HttpProfile httpProfile = new HttpProfile();
        httpProfile.setEndpoint("cbs.tencentcloudapi.com");
        ClientProfile clientProfile = new ClientProfile();
        clientProfile.setHttpProfile(httpProfile);
        return new CbsClient(cred, region, clientProfile);
    }
}
