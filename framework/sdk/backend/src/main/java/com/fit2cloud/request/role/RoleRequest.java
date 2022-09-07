package com.fit2cloud.request.role;

import io.swagger.annotations.ApiModelProperty;

public class RoleRequest {

    @ApiModelProperty("角色")
    private String id;

    @ApiModelProperty("角色名称")
    private String name;

    @ApiModelProperty("继承角色")
    private String parentRoleId;

}
