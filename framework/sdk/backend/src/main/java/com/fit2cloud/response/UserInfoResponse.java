package com.fit2cloud.response;

import com.fit2cloud.base.entity.Role;
import com.fit2cloud.dto.permission.Permission;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @Author:张少虎
 * @Date: 2022/8/25  2:18 PM
 * @Version 1.0
 * @注释:
 */
@Data
public class UserInfoResponse {

    @ApiModelProperty(value = "用户拥有的所有角色",notes = "用户拥有的所有角色")
    private List<Role> roles;

    @ApiModelProperty(value = "当前角色",notes = "当前角色")
    private Role currentRole;

    @ApiModelProperty(value = "当前角色权限",notes = "当前角色权限")
    private Permission permission;
}
