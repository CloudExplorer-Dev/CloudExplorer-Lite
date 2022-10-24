package com.fit2cloud.controller.request.disk;

import lombok.Data;

/**
 * Author: LiuDi
 * Date: 2022/10/21 10:45 AM
 */
@Data
public class EnlargeVmCloudDiskRequest {
    /**
     * vm_cloud_disk表主键
     */
    private String id;
    private String instanceUuid;
    private long newDiskSize;
}
