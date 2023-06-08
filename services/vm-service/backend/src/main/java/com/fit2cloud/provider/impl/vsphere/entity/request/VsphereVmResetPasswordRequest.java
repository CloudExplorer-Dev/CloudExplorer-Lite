package com.fit2cloud.provider.impl.vsphere.entity.request;


import lombok.Data;

@Data
public class VsphereVmResetPasswordRequest extends VsphereVmBaseRequest {

    private String serverId;

    private VsphereVmCreateRequest.PasswordObject passwordSetting;


}
