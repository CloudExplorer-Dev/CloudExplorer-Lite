package com.fit2cloud.controller;

import com.fit2cloud.base.entity.Role;
import com.fit2cloud.base.mapper.BaseRoleMapper;
import com.fit2cloud.common.constants.RoleConstants;
import com.fit2cloud.common.validator.annnotaion.CustomValidated;
import com.fit2cloud.common.validator.group.ValidationGroup;
import com.fit2cloud.common.validator.handler.ExistHandler;
import com.fit2cloud.controller.handler.ResultHolder;
import com.fit2cloud.controller.request.role.CreateRoleRequest;
import com.fit2cloud.controller.request.role.UpdateRoleRequest;
import com.fit2cloud.dto.permission.ModulePermission;
import com.fit2cloud.service.IRolePermissionService;
import com.fit2cloud.service.IRoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api/role")
@Api("角色相关接口")
public class RoleController {

    @Resource
    IRoleService roleService;
    @Resource
    IRolePermissionService rolePermissionService;

    @ApiOperation(value = "添加角色", notes = "添加角色")
    @PreAuthorize("hasAnyCePermission('ROLE:CREATE')")
    @PostMapping
    public ResultHolder<Role> save(@RequestBody @Validated(ValidationGroup.SAVE.class) CreateRoleRequest request) {
        Role role = new Role();
        BeanUtils.copyProperties(request, role);
        roleService.save(role);
        if (CollectionUtils.isNotEmpty(request.getPermissions())) {
            rolePermissionService.savePermissionsByRole(role.getId(), request.getPermissions());
        }
        return ResultHolder.success(roleService.getById(role.getId()));
    }

    @ApiOperation(value = "更新角色", notes = "更新角色")
    @PreAuthorize("hasAnyCePermission('ROLE:EDIT')")
    @PutMapping
    public ResultHolder<Role> update(@RequestBody @Validated(ValidationGroup.UPDATE.class) UpdateRoleRequest request) {
        Role role = new Role();
        BeanUtils.copyProperties(request, role);
        roleService.updateRole(role);
        if(request.getPermissions() !=null) {
            rolePermissionService.savePermissionsByRole(role.getId(), request.getPermissions());
        }
        return ResultHolder.success(roleService.getById(role.getId()));
    }

    @ApiOperation(value = "删除角色", notes = "删除角色")
    @PreAuthorize("hasAnyCePermission('ROLE:DELETE')")
    @DeleteMapping
    public ResultHolder<Boolean> removeRole(
            @ApiParam("角色ID")
            @NotNull(message = "角色ID不能为空")
            @CustomValidated(mapper = BaseRoleMapper.class, handler = ExistHandler.class, message = "角色ID不存在", exist = false)
            @RequestParam("id")
            String id) {
        return ResultHolder.success(roleService.deleteRoleById(id));
    }

    @ApiOperation(value = "批量删除角色", notes = "批量删除角色")
    @PreAuthorize("hasAnyCePermission('ROLE:DELETE')")
    @DeleteMapping("batch")
    public ResultHolder<Boolean> batchRemoveRole(
            @ApiParam("角色ID列表")
            @Size(min = 1, message = "至少需要一个角色ID")
            @RequestBody
            List<String> ids) {
        return ResultHolder.success(roleService.deleteRolesByIds(ids));
    }

    @ApiOperation(value = "查看模块权限", notes = "查看模块权限")
    @PreAuthorize("hasAnyCePermission('ROLE:READ')")
    @GetMapping("module-permission")
    public ResultHolder<Map<String, ModulePermission>> listModulePermissionByRole(
            @ApiParam("角色")
            @NotNull(message = "角色不能为空")
            @RequestParam("role")
            RoleConstants.ROLE role
    ) {
        return ResultHolder.success(rolePermissionService.getAllModulePermission(role));
    }

    @ApiOperation(value = "查看角色权限", notes = "查看角色权限")
    @PreAuthorize("hasAnyCePermission('ROLE:READ')")
    @GetMapping("permission")
    public ResultHolder<List<String>> listRolePermissionByRole(
            @ApiParam("角色ID")
            @NotNull(message = "角色ID不能为空")
            @RequestParam("id")
            String id
    ) {
        return ResultHolder.success(rolePermissionService.getCachedRolePermissionsByRole(id));
    }

    @ApiOperation(value = "更新角色权限", notes = "更新角色权限")
    @PreAuthorize("hasAnyCePermission('ROLE:EDIT')")
    @PostMapping("permission")
    public ResultHolder<List<String>> updateRolePermissionByRole(
            @ApiParam("角色ID")
            @NotNull(message = "角色ID不能为空")
            @CustomValidated(mapper = BaseRoleMapper.class, handler = ExistHandler.class, message = "角色ID不存在", exist = false)
            @RequestParam("id")
            String id,
            @RequestBody
            List<String> permissionIds
    ) {
        rolePermissionService.savePermissionsByRole(id, permissionIds);
        return ResultHolder.success(rolePermissionService.getCachedRolePermissionsByRole(id));
    }


}
