package com.fit2cloud.provider.constants;



import com.fit2cloud.provider.ICloudProvider;
import com.fit2cloud.provider.impl.aliyun.AliyunCloudProvider;
import com.fit2cloud.provider.impl.huawei.HuaweiCloudProvider;
import com.fit2cloud.provider.impl.tencent.TencentCloudProvider;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2022/10/19  10:24 AM}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
public  enum ProviderConstants {
    /**
     * 阿里云平台
     */
    fit2cloud_ali_platform("阿里云", AliyunCloudProvider.class),
    /**
     * 华为云平台
     */
    fit2cloud_huawei_platform("华为云", HuaweiCloudProvider.class),
    /**
     * 腾讯云平台
     */
    fit2cloud_tencent_platform("腾讯云", TencentCloudProvider.class);
    private String message;
    private Class<? extends ICloudProvider> cloudProvider;

    ProviderConstants(String message, Class<? extends ICloudProvider> cloudProvider) {
        this.message = message;
        this.cloudProvider = cloudProvider;
    }

    public Class<? extends ICloudProvider> getCloudProvider() {
        return cloudProvider;
    }

    public String getMessage() {
        return message;
    }
}
