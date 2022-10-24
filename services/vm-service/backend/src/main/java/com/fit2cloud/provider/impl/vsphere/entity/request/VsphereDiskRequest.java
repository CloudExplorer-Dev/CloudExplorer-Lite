package com.fit2cloud.provider.impl.vsphere.entity.request;

import lombok.Data;

/**
 * Author: LiuDi
 * Date: 2022/10/16 8:18 PM
 */
@Data
public class VsphereDiskRequest extends VsphereVmBaseRequest{
    private String instanceUuid;

    private String diskId;
}
