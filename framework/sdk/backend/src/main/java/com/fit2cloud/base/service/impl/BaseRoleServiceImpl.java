package com.fit2cloud.base.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fit2cloud.base.entity.Role;
import com.fit2cloud.base.mapper.BaseRoleMapper;
import com.fit2cloud.base.service.IBaseRoleService;
import com.fit2cloud.common.constants.RoleConstants;
import com.fit2cloud.common.utils.CurrentUserUtils;
import com.fit2cloud.common.utils.PageUtil;
import com.fit2cloud.dto.UserRoleDto;
import com.fit2cloud.request.role.RolePageRequest;
import com.fit2cloud.request.role.RoleRequest;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author fit2cloud
 * @since
 */
@Service
public class BaseRoleServiceImpl extends ServiceImpl<BaseRoleMapper, Role> implements IBaseRoleService {

    @Override
    public List<Role> roles(RoleRequest roleRequest) {
        return list(getQueryWrapper(roleRequest, true));
    }

    @Override
    public List<Role> listOriginRoles() {
        return this.list(new LambdaQueryWrapper<Role>()
                .eq(Role::getType, RoleConstants.Type.origin)
                .orderBy(true, true, Role::getType, Role::getSort, Role::getParentRoleId)
        );
    }

    @Override
    public IPage<Role> pages(RolePageRequest roleRequest) {
        Page<Role> page = PageUtil.of(roleRequest, Role.class);
        return this.page(page, getQueryWrapper(roleRequest, CollectionUtils.isEmpty(((PageDTO<Role>) page).getOrders())));
    }

    private Wrapper<Role> getQueryWrapper(RoleRequest roleRequest, boolean defaultSort) {
        LambdaQueryWrapper<Role> wrapper = new LambdaQueryWrapper<Role>()
                .eq(StringUtils.isNotBlank(roleRequest.getId()), Role::getId, roleRequest.getId())
                .like(StringUtils.isNotBlank(roleRequest.getName()), Role::getName, roleRequest.getName())
                .in(CollectionUtils.isNotEmpty(roleRequest.getType()), Role::getType, roleRequest.getType())
                .in(CollectionUtils.isNotEmpty(roleRequest.getParentRoleId()), Role::getParentRoleId, roleRequest.getParentRoleId())
                .in(roleRequest.getBaseRole() != null, Role::getParentRoleId, getRoleList(roleRequest.getBaseRole()));
        if (CollectionUtils.isNotEmpty(roleRequest.getCreateTime())) {
            wrapper.between(Role::getCreateTime, roleRequest.getCreateTime().get(0), roleRequest.getCreateTime().get(1));
        }
        if (CollectionUtils.isNotEmpty(roleRequest.getUpdateTime())) {
            wrapper.between(Role::getUpdateTime, roleRequest.getUpdateTime().get(0), roleRequest.getUpdateTime().get(1));
        }

        List<UserRoleDto> roles = CurrentUserUtils.getUser().getRoleMap().get(CurrentUserUtils.getUser().getCurrentRole());
        if (CurrentUserUtils.isAdmin()) {
            boolean isSystemAdmin = false;
            Set<String> roleIds = new HashSet<>();
            for (UserRoleDto userRoleDto : roles) {
                for (Role role : userRoleDto.getRoles()) {
                    if (StringUtils.equals(role.getId(), RoleConstants.ROLE.ADMIN.name())) {
                        isSystemAdmin = true;
                    }
                    roleIds.add(role.getId());
                }
            }
            if (isSystemAdmin) {
                wrapper.in(Role::getParentRoleId, getRoleList(RoleConstants.ROLE.ADMIN));
            } else {
                wrapper.and(w -> w
                        .in(Role::getParentRoleId, getRoleList(RoleConstants.ROLE.ORGADMIN))
                        .or()
                        .in(Role::getId, roleIds)
                );
            }

        }
        if (CurrentUserUtils.isOrgAdmin()) {
            boolean isSystemOrgAdmin = false;
            Set<String> roleIds = new HashSet<>();
            for (UserRoleDto userRoleDto : roles) {
                for (Role role : userRoleDto.getRoles()) {
                    if (StringUtils.equals(role.getId(), RoleConstants.ROLE.ORGADMIN.name())) {
                        isSystemOrgAdmin = true;
                    }
                    roleIds.add(role.getId());
                }
            }
            if (isSystemOrgAdmin) {
                wrapper.in(Role::getParentRoleId, getRoleList(RoleConstants.ROLE.ORGADMIN));
            } else {
                wrapper.and(w -> w
                        .in(Role::getParentRoleId, getRoleList(RoleConstants.ROLE.USER))
                        .or()
                        .in(Role::getId, roleIds)
                );
            }

        }


        if (defaultSort) {
            wrapper.orderBy(true, true, Role::getType, Role::getSort, Role::getParentRoleId);
        }
        return wrapper;
    }

    private static List<RoleConstants.ROLE> getRoleList(RoleConstants.ROLE baseRole) {
        if (baseRole == null) {
            return new ArrayList<>();
        }
        switch (baseRole) {
            case ADMIN -> {
                return Arrays.asList(RoleConstants.ROLE.ADMIN, RoleConstants.ROLE.ORGADMIN, RoleConstants.ROLE.USER);
            }
            case ORGADMIN -> {
                return Arrays.asList(RoleConstants.ROLE.ORGADMIN, RoleConstants.ROLE.USER);
            }
            case USER -> {
                return List.of(RoleConstants.ROLE.USER);
            }
        }
        return new ArrayList<>();
    }

}
