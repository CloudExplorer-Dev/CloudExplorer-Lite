package com.fit2cloud.response;

import com.fit2cloud.base.entity.Role;
import com.fit2cloud.dto.permission.Permission;
import io.swagger.v3.oas.annotations.media.Schema;
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

    @Schema(title = "用户拥有的所有角色", description = "用户拥有的所有角色")
    private List<Role> roles;

    @Schema(title = "当前角色", description = "当前角色")
    private Role currentRole;

    @Schema(title = "当前角色权限", description = "当前角色权限")
    private Permission permission;
}
