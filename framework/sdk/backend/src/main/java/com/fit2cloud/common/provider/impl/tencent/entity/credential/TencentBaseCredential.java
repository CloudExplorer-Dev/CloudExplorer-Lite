package com.fit2cloud.common.provider.impl.tencent.entity.credential;

import com.fit2cloud.common.platform.credential.Credential;
import com.fit2cloud.common.platform.credential.impl.TencentCredential;
import com.tencentcloudapi.billing.v20180709.BillingClient;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;

public class TencentBaseCredential extends TencentCredential implements Credential {

    public BillingClient getBillingClient() {
        com.tencentcloudapi.common.Credential cred = new com.tencentcloudapi.common.Credential(getSecretId(), getSecretKey());
        HttpProfile httpProfile = new HttpProfile();
        httpProfile.setEndpoint("billing.tencentcloudapi.com");
        ClientProfile clientProfile = new ClientProfile();
        clientProfile.setHttpProfile(httpProfile);
        return new BillingClient(cred, "", clientProfile);
    }
}
