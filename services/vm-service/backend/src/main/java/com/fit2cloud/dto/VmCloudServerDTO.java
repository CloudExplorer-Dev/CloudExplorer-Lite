package com.fit2cloud.dto;

import com.fit2cloud.base.entity.VmCloudServer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serial;

/**
 * @author jianneng
 * @date 2022/9/27 14:39
 **/
@Data
public class VmCloudServerDTO extends VmCloudServer {

    @Serial
    private static final long serialVersionUID = 2780061897725836746L;

    @ApiModelProperty("组织名称")
    private String organizationName;
    @ApiModelProperty("工作空间名称")
    private String workspaceName;
    @ApiModelProperty("组织ID")
    private String organizationId;
    @ApiModelProperty("工作空间ID")
    private String workspaceId;
    @ApiModelProperty("云账号名称")
    private String accountName;
    @ApiModelProperty("企业项目")
    private String cloudProjectName;
    @ApiModelProperty("云平台")
    private String platform;
}
