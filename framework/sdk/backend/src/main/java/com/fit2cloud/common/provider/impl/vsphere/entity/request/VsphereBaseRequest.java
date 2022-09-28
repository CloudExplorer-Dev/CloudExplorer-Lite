package com.fit2cloud.common.provider.impl.vsphere.entity.request;

import com.fit2cloud.common.provider.impl.vsphere.entity.credential.VsphereCredential;
import com.google.gson.Gson;

public class VsphereBaseRequest extends BaseRequest {
    private VsphereCredential vsphereCredential;

    public VsphereCredential getVsphereCredential() {
        if (vsphereCredential == null) {
            vsphereCredential = new Gson().fromJson(getCredential(), VsphereCredential.class);
        }
        return vsphereCredential;
    }
}
