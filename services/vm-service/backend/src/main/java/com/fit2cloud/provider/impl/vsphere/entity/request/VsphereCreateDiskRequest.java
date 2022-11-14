package com.fit2cloud.provider.impl.vsphere.entity.request;

import com.fit2cloud.provider.constants.DeleteWithInstance;
import com.fit2cloud.provider.entity.F2CDisk;
import lombok.Data;

import java.util.Arrays;

/**
 * Author: LiuDi
 * Date: 2022/10/13 8:52 PM
 */
@Data
public class VsphereCreateDiskRequest extends VsphereVmBaseRequest {
    /**
     * 磁盘名称
     */
    private String diskName;
    /**
     * 磁盘类型
     */
    private String diskType;
    /**
     * 磁盘模型
     */
    private String diskMode;
    /**
     * 磁盘大小
     */
    private long size;
    /**
     * 磁盘数据存储唯一id
     */
    private String datastore;
    /**
     * 云主机 ID
     */
    private String instanceUuid;

    public VsphereCreateDisksRequest toVsphereCreateDisksRequest() {
        VsphereCreateDisksRequest req = new VsphereCreateDisksRequest();
        req.setCredential(this.getCredential());
        req.setRegionId(this.getRegionId());
        req.setInstanceUuid(this.instanceUuid);

        F2CDisk disk = new F2CDisk();
        disk.setRegion(this.getRegionId());
        disk.setDiskType(this.diskType);
        disk.setDiskMode(this.diskMode);
        disk.setSize(this.size);
        disk.setDatastoreName(this.datastore == null ? "only-a-flag":this.datastore);
        disk.setDeleteWithInstance(DeleteWithInstance.YES.name());
        req.setDisks(Arrays.asList(disk));

        return req;
    }
}
