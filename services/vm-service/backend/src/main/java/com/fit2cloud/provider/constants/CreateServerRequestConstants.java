package com.fit2cloud.provider.constants;

import com.fit2cloud.provider.ICreateServerRequest;
import com.fit2cloud.provider.impl.aliyun.entity.request.AliyunVmCreateRequest;
import com.fit2cloud.provider.impl.huawei.entity.request.HuaweiVmCreateRequest;
import com.fit2cloud.provider.impl.openstack.entity.request.OpenStackServerCreateRequest;
import com.fit2cloud.provider.impl.tencent.entity.request.TencentVmCreateRequest;
import com.fit2cloud.provider.impl.vsphere.entity.request.VsphereVmCreateRequest;

public enum CreateServerRequestConstants {
    /**
     * 阿里云平台
     */
    fit2cloud_ali_platform("阿里云", AliyunVmCreateRequest.class),
    /**
     * 华为云平台
     */
    fit2cloud_huawei_platform("华为云", HuaweiVmCreateRequest.class),
    /**
     * 腾讯云平台
     */
    fit2cloud_tencent_platform("腾讯云", TencentVmCreateRequest.class),
    /**
     * VMWARE 平台
     */
    fit2cloud_vsphere_platform("VMWare vSphere", VsphereVmCreateRequest.class),
    /**
     * OpenStack 平台
     */
    fit2cloud_openstack_platform("VMWare vSphere", OpenStackServerCreateRequest.class);

    private String message;

    private Class<? extends ICreateServerRequest> createRequest;

    CreateServerRequestConstants(String message, Class<? extends ICreateServerRequest> createRequest) {
        this.message = message;
        this.createRequest = createRequest;
    }

    public Class<? extends ICreateServerRequest> getCreateRequest() {
        return createRequest;
    }

    public String getMessage() {
        return message;
    }
}
