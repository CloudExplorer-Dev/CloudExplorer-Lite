package com.fit2cloud.controller.request.disk;

import lombok.Data;

/**
 * Author: LiuDi
 * Date: 2022/10/27 10:35 AM
 */
@Data
public class BatchAttachVmCloudDiskRequest{
    /**
     * vm_cloud_disk表主键数组
     */
    private String[] ids;
    private String instanceUuid;
    private Boolean deleteWithInstance;

}
