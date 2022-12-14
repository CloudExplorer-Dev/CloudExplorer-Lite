package com.fit2cloud.provider.impl.tencent.entity.credential;

import com.fit2cloud.common.provider.impl.tencent.entity.credential.TencentBaseCredential;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.cvm.v20170312.CvmClient;

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
}
