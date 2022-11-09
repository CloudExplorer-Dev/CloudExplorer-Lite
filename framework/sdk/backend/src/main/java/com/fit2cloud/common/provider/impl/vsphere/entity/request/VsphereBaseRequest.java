package com.fit2cloud.common.provider.impl.vsphere.entity.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fit2cloud.common.platform.credential.impl.VsphereCredential;
import com.fit2cloud.common.utils.JsonUtil;

public class VsphereBaseRequest extends BaseRequest {
    private VsphereCredential vsphereCredential;

    @JsonIgnore
    public VsphereCredential getVsphereCredential() {
        if (vsphereCredential == null) {
            vsphereCredential = JsonUtil.parseObject(getCredential(), VsphereCredential.class);
        }
        return vsphereCredential;
    }
}
