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
public interface IUserRoleService extends IService<UserRole> {

    Map<RoleConstants.ROLE, List<UserRoleDto>> getUserRoleMap(String userId);

    Map<RoleConstants.ROLE, List<UserRoleDto>> getCachedUserRoleMap(String userId);

    Map<RoleConstants.ROLE, List<UserRoleDto>> saveCachedUserRoleMap(String userId);
}
