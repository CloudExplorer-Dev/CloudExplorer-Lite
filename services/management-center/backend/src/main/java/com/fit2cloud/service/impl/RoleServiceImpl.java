package com.fit2cloud.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fit2cloud.base.entity.Role;
import com.fit2cloud.common.constants.RoleConstants;
import com.fit2cloud.common.utils.CurrentUserUtils;
import com.fit2cloud.dao.mapper.RoleMapper;
import com.fit2cloud.service.IRoleService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author fit2cloud
 * @since
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements IRoleService {

    public List<Role> getRolesByResourceIds(Map<String, Object> param) {
        return baseMapper.getRolesByResourceIds(param);
    }

    public List<Role> currentUserRoles() {
        QueryWrapper<Role> roleQueryWrapper = Wrappers.query();
        if (CurrentUserUtils.isOrgAdmin()) {
            roleQueryWrapper.in("parent_role_id",new ArrayList<>(Arrays.asList(RoleConstants.ROLE.USER.name(), RoleConstants.ROLE.ORGADMIN.name())));
        }
        roleQueryWrapper.orderByAsc("_type").orderByAsc("parent_role_id");
        return baseMapper.selectList(roleQueryWrapper);
    }
}
