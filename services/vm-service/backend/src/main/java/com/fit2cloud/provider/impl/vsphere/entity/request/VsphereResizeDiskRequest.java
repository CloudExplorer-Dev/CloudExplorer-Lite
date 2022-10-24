package com.fit2cloud.provider.impl.vsphere.entity.request;

import lombok.Data;

/**
 * Author: LiuDi
 * Date: 2022/10/13
 */
@Data
public class VsphereResizeDiskRequest extends VsphereVmBaseRequest {
   private String diskId;
   private String instanceUuid;
   private Integer newDiskSize;
}
