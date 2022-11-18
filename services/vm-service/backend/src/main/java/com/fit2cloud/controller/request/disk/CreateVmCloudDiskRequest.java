package com.fit2cloud.controller.request.disk;

import com.fit2cloud.base.entity.VmCloudDisk;
import lombok.Data;


/**
 * Author: LiuDi
 * Date: 2022/11/6 11:15 AM
 */
@Data
public class CreateVmCloudDiskRequest {
    private String accountId;

    private String credential;

    private String regionId;

    private String zone;

    private String diskName;

    private String description;

    private String diskType;

    private String diskMode;

    private String datastore;

    private Long size;

    /**
     * 是否挂载到云主机
     */
    private Boolean isAttached;

    /**
     * 云主机 ID
     */
    private String instanceUuid;

    /**
     * 磁盘是否随云主机删除
     */
    private String deleteWithInstance;

    public VmCloudDisk toVmCloudDisk(String id, String status) {
        VmCloudDisk vmCloudDisk = new VmCloudDisk();
        vmCloudDisk.setId(id);
        vmCloudDisk.setDiskName(this.diskName);
        vmCloudDisk.setDiskType(this.diskType);
        vmCloudDisk.setAccountId(this.accountId);
        vmCloudDisk.setRegion(this.regionId);
        vmCloudDisk.setZone(this.zone);
        vmCloudDisk.setDescription(this.description);
        vmCloudDisk.setSize(this.size);
        vmCloudDisk.setBootable(false);
        vmCloudDisk.setStatus(status);
        if (this.instanceUuid != null) {
            vmCloudDisk.setInstanceUuid(this.instanceUuid);
            vmCloudDisk.setDeleteWithInstance(this.deleteWithInstance);
        }
        return vmCloudDisk;
    }
}
