package com.fit2cloud.dto;

import com.fit2cloud.base.entity.RecycleBin;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author : LiuDi
 * @date : 2023/1/12 14:45
 */
@Data
public class RecycleBinDTO extends RecycleBin {
    @ApiModelProperty("用户名称")
    private String userName;
    @ApiModelProperty("资源名称")
    private String resourceName;
    @ApiModelProperty("资源状态")
    private String resourceStatus;
    @ApiModelProperty("资源配置")
    private String resourceConfig;
    @ApiModelProperty("ip")
    private String ipArray;
    @ApiModelProperty("关联资源")
    private String relateResource;
    @ApiModelProperty("资源创建时间")
    private String resourceCreateTime;
    @ApiModelProperty("是否随实例删除")
    private String deleteWithInstance;
    @ApiModelProperty("账号ID")
    private String accountId;
    @ApiModelProperty("账号名称")
    private String accountName;
    @ApiModelProperty("工作空间名称")
    private String workspaceName;
    @ApiModelProperty("工作空间ID")
    private String workspaceId;
    @ApiModelProperty("组织名称")
    private String organizationName;
    @ApiModelProperty("组织ID")
    private String organizationId;
}
