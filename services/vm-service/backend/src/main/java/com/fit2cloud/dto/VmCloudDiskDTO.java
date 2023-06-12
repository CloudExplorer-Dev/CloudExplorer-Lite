package com.fit2cloud.dto;

import com.fit2cloud.base.entity.VmCloudDisk;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author jianneng
 * @date 2022/9/27 14:39
 **/
@Data
public class VmCloudDiskDTO extends VmCloudDisk {

    @Schema(title = "组织名称")
    private String organizationName;

    @Schema(title = "工作空间名称")
    private String workspaceName;

    @Schema(title = "组织ID")
    private String organizationId;

    @Schema(title = "工作空间ID")
    private String workspaceId;

    @Schema(title = "云账号名称")
    private String accountName;

    @Schema(title = "所属云主机")
    private String vmInstanceName;

    @Schema(title = "所属云主机 ID")
    private String serverId;

    @Schema(title = "所属云平台")
    private String platform;

    @Schema(title = "磁盘属性")
    private String bootableText;
}
