package com.fit2cloud.common.constants;

import com.fit2cloud.common.platform.bill.Bill;
import com.fit2cloud.common.platform.bill.impl.*;
import com.fit2cloud.common.platform.credential.Credential;
import com.fit2cloud.common.platform.credential.impl.*;

/**
 * @Author:张少虎
 * @Date: 2022/9/6  2:35 PM
 * @Version 1.0
 * @注释:
 */
public enum PlatformConstants {
    /**
     * 阿里云平台
     */
    fit2cloud_ali_platform("阿里云", true, AliCredential.class, AliBill.class),
    /**
     * 华为云平台
     */
    fit2cloud_huawei_platform("华为云", true, HuaweiCredential.class, HuaweiBill.class),
    /**
     * 腾讯云平台
     */
    fit2cloud_tencent_platform("腾讯云", true, TencentCredential.class, TencentBill.class),

    /**
     * VMWARE 平台
     */
    fit2cloud_vsphere_platform("VMware", false, VsphereCredential.class, VsphereBill.class),

    /**
     * OpenStack 平台
     */
    fit2cloud_openstack_platform("OpenStack", false, OpenStackCredential.class, OpenStackBill.class);

    private final String message;

    private final boolean publicCloud;

    private final Class<? extends Credential> credentialClass;

    private final Class<? extends Bill> billClass;

    PlatformConstants(String message, boolean publicCloud, Class<? extends Credential> credentialClass, Class<? extends Bill> billClass) {
        this.message = message;
        this.publicCloud = publicCloud;
        this.credentialClass = credentialClass;
        this.billClass = billClass;
    }

    public Class<? extends Credential> getCredentialClass() {
        return credentialClass;
    }

    public Class<? extends Bill> getBillClass() {
        return billClass;
    }

    public String getMessage() {
        return message;
    }

    public boolean getPublicCloud() {
        return publicCloud;
    }
}
