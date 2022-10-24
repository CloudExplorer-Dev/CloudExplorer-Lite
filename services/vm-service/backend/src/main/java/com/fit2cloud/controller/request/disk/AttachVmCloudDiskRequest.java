package com.fit2cloud.controller.request.disk;

import lombok.Data;

/**
 * Author: LiuDi
 * Date: 2022/10/19 2:26 PM
 */
@Data
public class AttachVmCloudDiskRequest {
    /**
     * vm_cloud_disk表主键
     */
    private String id;
    private String instanceUuid;
    private Boolean deleteWithInstance;
}
