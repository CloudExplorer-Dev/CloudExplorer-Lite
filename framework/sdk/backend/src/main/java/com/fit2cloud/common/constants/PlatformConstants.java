package com.fit2cloud.common.constants;

import com.fit2cloud.common.platform.bill.Bill;
import com.fit2cloud.common.platform.bill.impl.AliBill;
import com.fit2cloud.common.platform.bill.impl.HuaweiBill;
import com.fit2cloud.common.platform.bill.impl.TencentBill;
import com.fit2cloud.common.platform.credential.impl.AliCredential;
import com.fit2cloud.common.platform.credential.Credential;
import com.fit2cloud.common.platform.credential.impl.HuaweiCredential;
import com.fit2cloud.common.platform.credential.impl.TencentCredential;
import com.fit2cloud.common.platform.credential.impl.VsphereCredential;

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
    fit2cloud_ali_platform("阿里云", AliCredential.class, AliBill.class),
    /**
     * 华为云平台
     */
    fit2cloud_huawei_platform("华为云", HuaweiCredential.class, HuaweiBill.class),
    /**
     * 腾讯云平台
     */
    fit2cloud_tencent_platform("腾讯云", TencentCredential.class, TencentBill.class),

    /**
     * VMWARE 平台
     */
    fit2cloud_vsphere_platform("VMWare vSphere", VsphereCredential.class, null);

    private String message;

    private Class<? extends Credential> credentialClass;

    private Class<? extends Bill> billClass;

    PlatformConstants(String message, Class<? extends Credential> credentialClass, Class<? extends Bill> billclass) {
        this.message = message;
        this.credentialClass = credentialClass;
        this.billClass = billclass;
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
}
