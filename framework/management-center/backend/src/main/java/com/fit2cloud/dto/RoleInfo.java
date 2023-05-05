package com.fit2cloud.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * Author: LiuDi
 * Date: 2022/8/30 4:39 PM
 */
@Data
public class RoleInfo implements Serializable {

    @Serial
    private static final long serialVersionUID = -6403922661617397034L;

    @ApiModelProperty("组织 ID 集合")
    private List<String> organizationIds;

    @ApiModelProperty("工作空间 ID 集合")
    private List<String> workspaceIds;

    @ApiModelProperty("角色ID")
    private String roleId;
}
