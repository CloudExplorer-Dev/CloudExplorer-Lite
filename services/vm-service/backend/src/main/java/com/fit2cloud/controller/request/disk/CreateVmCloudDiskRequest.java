package com.fit2cloud.controller.request.disk;

import com.fit2cloud.base.entity.VmCloudDisk;

import java.util.HashMap;
import java.util.Objects;


/**
 * Author: LiuDi
 * Date: 2022/11/6 11:15 AM
 */

public class CreateVmCloudDiskRequest extends HashMap<String, Object> {

    public String getAccountId() {
        return (String) get("accountId");
    }

    public String getCredential() {
        return (String) get("credential");
    }


    public String getRegionId() {
        return (String) get("regionId");
    }


    public String getZone() {
        return (String) get("zone");
    }


    public String getDiskName() {
        return (String) get("diskName");
    }


    public String getDescription() {
        return (String) get("description");
    }


    public String getDiskType() {
        return (String) get("diskType");
    }

    public String getDiskMode() {
        return (String) get("diskMode");
    }

    public String getDatastore() {
        return (String) get("datastore");
    }


    public Long getSize() {
        if (get("size") instanceof Integer) {
            return ((Integer) get("size")).longValue();
        } else {
            return (Long) get("size");
        }
    }

    /**
     * 是否挂载到云主机
     */
    private Boolean isAttached;

    public void setIsAttached(boolean isAttached) {
        put("isAttached", isAttached);
    }

    public Boolean getIsAttached() {
        return (Boolean) get("isAttached");
    }

    /**
     * 云主机 ID
     */
    public String getInstanceUuid() {
        return (String) get("instanceUuid");
    }

    /**
     * 磁盘是否随云主机删除
     */
    public String getDeleteWithInstance() {
        return (String) get("deleteWithInstance");
    }

    public VmCloudDisk toVmCloudDisk(String id, String status) {
        VmCloudDisk vmCloudDisk = new VmCloudDisk();
        vmCloudDisk.setId(id);
        vmCloudDisk.setDiskName(this.getDiskName());
        vmCloudDisk.setDiskType(this.getDiskType());
        vmCloudDisk.setAccountId(this.getAccountId());
        vmCloudDisk.setRegion(this.getRegionId());
        vmCloudDisk.setZone(this.getZone());
        vmCloudDisk.setDescription(this.getDescription());
        vmCloudDisk.setSize(this.getSize());
        vmCloudDisk.setBootable(false);
        vmCloudDisk.setStatus(status);
        if (this.getInstanceUuid() != null) {
            vmCloudDisk.setInstanceUuid(this.getInstanceUuid());
            vmCloudDisk.setDeleteWithInstance(this.getDeleteWithInstance());
        }
        return vmCloudDisk;
    }
}
