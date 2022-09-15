package com.fit2cloud.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fit2cloud.base.entity.RolePermission;
import com.fit2cloud.common.constants.RoleConstants;
import com.fit2cloud.dto.permission.ModulePermission;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author fit2cloud
 * @since
 */
public interface IRolePermissionService extends IService<RolePermission> {

    /**
     * 获取保存在redis中的角色权限
     *
     * @param roleId
     * @return
     */
    List<String> getCachedRolePermissionsByRole(String roleId);

    Map<String, ModulePermission> getAllModulePermission(RoleConstants.ROLE role);

    void savePermissionsByRole(String roleId, List<String> permissionIds);

    void deletePermissionsByRole(String roleId);


}
