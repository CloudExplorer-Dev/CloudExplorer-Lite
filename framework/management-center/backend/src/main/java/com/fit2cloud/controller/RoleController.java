package com.fit2cloud.controller;

import com.fit2cloud.base.entity.Role;
import com.fit2cloud.base.mapper.BaseRoleMapper;
import com.fit2cloud.common.constants.RoleConstants;
import com.fit2cloud.common.log.annotation.OperatedLog;
import com.fit2cloud.common.log.constants.OperatedTypeEnum;
import com.fit2cloud.common.log.constants.ResourceTypeEnum;
import com.fit2cloud.common.validator.annnotaion.CustomValidated;
import com.fit2cloud.common.validator.group.ValidationGroup;
import com.fit2cloud.common.validator.handler.ExistHandler;
import com.fit2cloud.controller.handler.ResultHolder;
import com.fit2cloud.controller.request.role.CreateRoleRequest;
import com.fit2cloud.controller.request.role.UpdateRoleRequest;
import com.fit2cloud.dto.permission.ModulePermission;
import com.fit2cloud.service.IRolePermissionService;
import com.fit2cloud.service.IRoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.annotation.Resource;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api/role")
@Tag(name = "角色相关接口")
public class RoleController {

    @Resource
    IRoleService roleService;
    @Resource
    IRolePermissionService rolePermissionService;

    @Operation(summary = "添加角色", description = "添加角色")
    @PreAuthorize("@cepc.hasAnyCePermission('ROLE:CREATE')")
    @PostMapping
    @OperatedLog(resourceType = ResourceTypeEnum.ROLE, operated = OperatedTypeEnum.ADD,
            content = "'添加了角色['+#request.name+']'",
            param = "#request")
    public ResultHolder<Role> save(@RequestBody @Validated(ValidationGroup.SAVE.class) CreateRoleRequest request) {
        Role role = new Role();
        BeanUtils.copyProperties(request, role);
        roleService.save(role);
        if (CollectionUtils.isNotEmpty(request.getPermissions())) {
            rolePermissionService.savePermissionsByRole(role.getId(), request.getPermissions());
        }
        return ResultHolder.success(roleService.getById(role.getId()));
    }

    @Operation(summary = "更新角色", description = "更新角色")
    @PreAuthorize("@cepc.hasAnyCePermission('ROLE:EDIT')")
    @PutMapping
    @OperatedLog(resourceType = ResourceTypeEnum.ROLE, operated = OperatedTypeEnum.MODIFY,
            resourceId = "#request.id",
            content = "'更新了角色['+#request.name+']'",
            param = "#request")
    public ResultHolder<Role> update(@RequestBody @Validated(ValidationGroup.UPDATE.class) UpdateRoleRequest request) {
        Role role = new Role();
        BeanUtils.copyProperties(request, role);
        role.setUpdateTime(null);
        roleService.updateRole(role);
        if (request.getPermissions() != null) {
            rolePermissionService.savePermissionsByRole(role.getId(), request.getPermissions());
        }
        return ResultHolder.success(roleService.getById(role.getId()));
    }

    @Operation(summary = "删除角色", description = "删除角色")
    @PreAuthorize("@cepc.hasAnyCePermission('ROLE:DELETE')")
    @DeleteMapping
    @OperatedLog(resourceType = ResourceTypeEnum.ROLE, operated = OperatedTypeEnum.DELETE,
            resourceId = "#id",
            content = "'删除角色'",
            param = "#id")
    public ResultHolder<Boolean> removeRole(
            @Parameter(description = "角色ID")
            @NotNull(message = "角色ID不能为空")
            @CustomValidated(mapper = BaseRoleMapper.class, handler = ExistHandler.class, message = "角色ID不存在", exist = false)
            @RequestParam("id")
            String id) {
        return ResultHolder.success(roleService.deleteRoleById(id));
    }

    @Operation(summary = "批量删除角色", description = "批量删除角色")
    @PreAuthorize("@cepc.hasAnyCePermission('ROLE:DELETE')")
    @DeleteMapping("batch")
    @OperatedLog(resourceType = ResourceTypeEnum.ROLE, operated = OperatedTypeEnum.BATCH_DELETE,
            resourceId = "#ids",
            content = "'批量删除了'+#ids.size+'个角色'",
            param = "#ids")
    public ResultHolder<Boolean> batchRemoveRole(
            @Parameter(description = "角色ID列表")
            @Size(min = 1, message = "至少需要一个角色ID")
            @RequestBody
            List<String> ids) {
        return ResultHolder.success(roleService.deleteRolesByIds(ids));
    }

    @Operation(summary = "查看模块权限", description = "查看模块权限")
    @PreAuthorize("@cepc.hasAnyCePermission('ROLE:READ')")
    @GetMapping("module-permission")
    public ResultHolder<Map<String, ModulePermission>> listModulePermissionByRole(
            @Parameter(description = "角色")
            @NotNull(message = "角色不能为空")
            @RequestParam("role")
            RoleConstants.ROLE role
    ) {
        return ResultHolder.success(rolePermissionService.getAllModulePermission(role));
    }

    @Operation(summary = "查看角色权限", description = "查看角色权限")
    @PreAuthorize("@cepc.hasAnyCePermission('ROLE:READ')")
    @GetMapping("permission")
    public ResultHolder<List<String>> listRolePermissionByRole(
            @Parameter(description = "角色ID")
            @NotNull(message = "角色ID不能为空")
            @RequestParam("id")
            String id
    ) {
        return ResultHolder.success(rolePermissionService.getCachedRolePermissionsByRole(id));
    }

    @Operation(summary = "更新角色权限", description = "更新角色权限")
    @PreAuthorize("@cepc.hasAnyCePermission('ROLE:EDIT')")
    @PostMapping("permission")
    @OperatedLog(resourceType = ResourceTypeEnum.ROLE, operated = OperatedTypeEnum.MODIFY,
            resourceId = "#id",
            content = "'更新角色权限为['+#permissionIds+']'",
            param = "#permissionIds")
    public ResultHolder<List<String>> updateRolePermissionByRole(
            @Parameter(description = "角色ID")
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
