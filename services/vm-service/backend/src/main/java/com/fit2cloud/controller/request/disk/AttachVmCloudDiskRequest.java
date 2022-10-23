package com.fit2cloud.controller.request.disk;

import lombok.Data;

/**
 * Author: LiuDi
 * Date: 2022/10/19 2:26 PM
 */
@Data
public class AttachVmCloudDiskRequest {
    private String id;
    private String instanceUuid;
    private Boolean deleteWithInstance;
}
