package com.fit2cloud.dto;

import com.fit2cloud.base.entity.RecycleBin;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author : LiuDi
 * @date : 2023/1/12 14:45
 */
@Data
public class RecycleBinDTO extends RecycleBin {

    @Schema(title = "用户名称")
    private String userName;

    @Schema(title = "资源名称")
    private String resourceName;

    @Schema(title = "资源状态")
    private String resourceStatus;

    @Schema(title = "资源配置")
    private String resourceConfig;

    @Schema(title = "ip")
    private String ipArray;

    @Schema(title = "公网 IP")
    private String remoteIp;

    @Schema(title = "关联资源")
    private String relateResource;

    @Schema(title = "资源创建时间")
    private String resourceCreateTime;

    @Schema(title = "是否随实例删除")
    private String deleteWithInstance;

    @Schema(title = "账号ID")
    private String accountId;

    @Schema(title = "账号名称")
    private String accountName;

    @Schema(title = "云平台")
    private String platform;

    @Schema(title = "工作空间名称")
    private String workspaceName;

    @Schema(title = "工作空间ID")
    private String workspaceId;

    @Schema(title = "组织名称")
    private String organizationName;

    @Schema(title = "组织ID")
    private String organizationId;
}
