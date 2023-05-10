package com.fit2cloud.base.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fit2cloud.base.entity.UserRole;
import com.fit2cloud.common.constants.RoleConstants;
import com.fit2cloud.dto.UserRoleDto;

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
public interface IBaseUserRoleService extends IService<UserRole> {

    Map<RoleConstants.ROLE, List<UserRoleDto>> getUserRoleMap(String userId);

    Map<RoleConstants.ROLE, List<UserRoleDto>> getCachedUserRoleMap(String userId);

    Map<RoleConstants.ROLE, List<UserRoleDto>> saveCachedUserRoleMap(String userId);

    boolean deleteUserRoleByOrgId(String orgId);

    boolean deleteUserRoleByWorkspaceId(String workspaceId);

    boolean deleteUserRoleByRoleId(String roleId);

    boolean removeUserRoleByUserIdAndRoleId(String userId, String roleId, String sourceId);

    List<UserRole> searchByUsersAndRole(String roleId, List<String> userIds);
}
