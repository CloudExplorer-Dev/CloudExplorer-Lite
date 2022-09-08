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
import com.fit2cloud.common.utils.ColumnNameUtil;
import com.fit2cloud.common.utils.PageUtil;
import com.fit2cloud.request.role.RolePageRequest;
import com.fit2cloud.request.role.RoleRequest;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

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
    public IPage<Role> pages(RolePageRequest roleRequest) {
        Page<Role> page = PageUtil.of(roleRequest, Role.class);
        return this.page(page, getQueryWrapper(roleRequest, CollectionUtils.isEmpty(((PageDTO<Role>) page).getOrders())));
    }

    private Wrapper<Role> getQueryWrapper(RoleRequest roleRequest, boolean defaultSort) {
        LambdaQueryWrapper<Role> wrapper = new LambdaQueryWrapper<Role>()
                .eq(StringUtils.isNotBlank(roleRequest.getId()), Role::getId, roleRequest.getId())
                .like(StringUtils.isNotBlank(roleRequest.getName()), Role::getName, roleRequest.getName())
                .eq(roleRequest.getType() != null, Role::getType, roleRequest.getType())
                .eq(roleRequest.getParentRoleId() != null, Role::getParentRoleId, roleRequest.getParentRoleId());
        if (defaultSort) {
            wrapper.orderBy(true, true, Role::getType, Role::getSort, Role::getParentRoleId);
        }
        return wrapper;
    }

}
