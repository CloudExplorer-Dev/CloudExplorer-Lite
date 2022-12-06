package com.fit2cloud.provider.impl.tencent.entity.credential;

import com.fit2cloud.common.platform.credential.impl.TencentCredential;
import com.tencentcloudapi.cbs.v20170312.CbsClient;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.common.profile.Language;
import com.tencentcloudapi.cvm.v20170312.CvmClient;
import com.tencentcloudapi.monitor.v20180724.MonitorClient;
import com.tencentcloudapi.vpc.v20170312.VpcClient;

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
        ClientProfile clientProfile = getClientProfile();
        clientProfile.setHttpProfile(httpProfile);
        return new CvmClient(cred, region, clientProfile);
    }

    public CbsClient getCbsClient(String region) {
        Credential cred = new Credential(getSecretId(), getSecretKey());
        HttpProfile httpProfile = new HttpProfile();
        httpProfile.setEndpoint("cbs.tencentcloudapi.com");
        ClientProfile clientProfile = getClientProfile();
        clientProfile.setHttpProfile(httpProfile);
        return new CbsClient(cred, region, clientProfile);
    }

    public VpcClient getVpcClient(String region) {
        Credential cred = new Credential(getSecretId(), getSecretKey());
        HttpProfile httpProfile = new HttpProfile();
        httpProfile.setEndpoint("vpc.tencentcloudapi.com");
        ClientProfile clientProfile = getClientProfile();
        clientProfile.setHttpProfile(httpProfile);
        return new VpcClient(cred, region, clientProfile);
    }

    public MonitorClient getMonitorClient(String region) {
        Credential cred = new Credential(getSecretId(), getSecretKey());
        HttpProfile httpProfile = new HttpProfile();
        httpProfile.setEndpoint("monitor.tencentcloudapi.com");
        ClientProfile clientProfile = new ClientProfile();
        clientProfile.setHttpProfile(httpProfile);
        return new MonitorClient(cred, region, clientProfile);
    }

    private ClientProfile getClientProfile() {
        ClientProfile clientProfile = new ClientProfile();
        clientProfile.setLanguage(Language.ZH_CN);
        return clientProfile;
    }
}
