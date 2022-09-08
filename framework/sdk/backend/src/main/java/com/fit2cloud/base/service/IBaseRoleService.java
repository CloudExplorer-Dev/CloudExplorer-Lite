package com.fit2cloud.base.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fit2cloud.base.entity.Role;
import com.fit2cloud.request.role.RolePageRequest;
import com.fit2cloud.request.role.RoleRequest;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author fit2cloud
 * @since
 */
public interface IBaseRoleService extends IService<Role> {

    List<Role> roles(RoleRequest roleRequest);

    IPage<Role> pages(RolePageRequest roleRequest);

}
