package com.fit2cloud.provider.impl.tencent.entity.request;

import com.fit2cloud.provider.impl.tencent.util.TencentMappingUtil;
import com.tencentcloudapi.cbs.v20170312.models.AutoMountConfiguration;
import com.tencentcloudapi.cbs.v20170312.models.CreateDisksRequest;
import com.tencentcloudapi.cbs.v20170312.models.Placement;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

/**
 * Author: LiuDi
 * Date: 2022/10/13 8:42 PM
 */
@Data
public class TencentCreateDiskRequest extends TencentBaseRequest {
    private String zone;
    private String diskName;
    private String diskType;
    private String diskChargeType;
    private String projectId;
    private long size;
    /**
     * 是否自动挂载至云主机，并初始化文件系统
     */
    private Boolean isAttached;
    /**
     * 云主机 ID
     */
    private String instanceUuid;
    /**
     * 文件系统:EXT4 和 XFS
     */
    private String fileSystemType;
    /**
     * 挂载路径
     */
    private String mountPoint;
    /**
     * 云盘是否随云主机删除
     */
    private String deleteWithInstance;

    public CreateDisksRequest toCreateDisksRequest() {
        CreateDisksRequest createDisksRequest = new CreateDisksRequest();
        createDisksRequest.setDiskType(this.diskType);
        createDisksRequest.setDiskName(this.diskName);
        createDisksRequest.setDiskSize(this.size);
        createDisksRequest.setDiskCount(1l);
        createDisksRequest.setDiskChargeType(TencentMappingUtil.toTencentChargeType(this.diskChargeType));

        // 放置位置
        Placement placement = new Placement();
        placement.setZone(this.zone);
        if (StringUtils.isNotEmpty(this.projectId)) {
            placement.setProjectId(Long.valueOf(this.projectId));
        }
        createDisksRequest.setPlacement(placement);

        // 挂载，选择云服务器实例挂载并快速初始化至指定文件系统和指定挂载路径（当前仅支持Linux操作系统）
        if (this.isAttached && this.fileSystemType != null && this.mountPoint != null) {
            AutoMountConfiguration autoMountConfiguration = new AutoMountConfiguration();
            autoMountConfiguration.setInstanceId(new String[]{this.instanceUuid});
            autoMountConfiguration.setMountPoint(new String[]{this.mountPoint});
            autoMountConfiguration.setFileSystemType(this.fileSystemType);
            createDisksRequest.setAutoMountConfiguration(autoMountConfiguration);
        }
        return createDisksRequest;
    }

}
