package com.fit2cloud.base.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fit2cloud.base.entity.RolePermission;
import com.fit2cloud.security.CeGrantedAuthority;

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
public interface IBaseRolePermissionService extends IService<RolePermission> {

    /**
     * 从数据库获取当前模块自定义角色权限
     *
     * @param roleIds
     * @return
     */
    Map<String, List<CeGrantedAuthority>> getCurrentModulePermissionMap(String module, List<String> roleIds);

}
