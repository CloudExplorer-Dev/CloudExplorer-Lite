package com.fit2cloud.common.constants;

import com.fit2cloud.common.provider.IBaseCloudProvider;
import com.fit2cloud.common.provider.impl.aliyun.AliyunBaseCloudProvider;
import com.fit2cloud.common.provider.impl.huawei.HuaweiBaseCloudProvider;
import com.fit2cloud.common.provider.impl.tencent.TencentBaseCloudProvider;

public enum ProviderConstants {
    /**
     * 阿里云平台
     */
    fit2cloud_ali_platform("阿里云", AliyunBaseCloudProvider.class),
    /**
     * 华为云平台
     */
    fit2cloud_huawei_platform("华为云", HuaweiBaseCloudProvider.class),
    /**
     * 腾讯云平台
     */
    fit2cloud_tencent_platform("腾讯云", TencentBaseCloudProvider.class);

    private String message;

    private Class<? extends IBaseCloudProvider> cloudProvider;

    ProviderConstants(String message, Class<? extends IBaseCloudProvider> cloudProvider) {
        this.message = message;
        this.cloudProvider = cloudProvider;
    }

    public Class<? extends IBaseCloudProvider> getCloudProvider() {
        return cloudProvider;
    }

    public String getMessage() {
        return message;
    }
}
