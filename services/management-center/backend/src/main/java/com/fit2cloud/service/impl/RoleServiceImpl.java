package com.fit2cloud.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fit2cloud.base.entity.Role;
import com.fit2cloud.common.constants.RoleConstants;
import com.fit2cloud.common.exception.Fit2cloudException;
import com.fit2cloud.constants.ErrorCodeConstants;
import com.fit2cloud.dao.mapper.RoleMapper;
import com.fit2cloud.service.IRolePermissionService;
import com.fit2cloud.service.IRoleService;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author fit2cloud
 * @since
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements IRoleService {

    @Resource
    private IRolePermissionService rolePermissionService;

    public List<Role> getRolesByResourceIds(Map<String, Object> param) {
        return baseMapper.getRolesByResourceIds(param);
    }

    public boolean deleteRoleById(String id) {
        if (Arrays.stream(RoleConstants.ROLE.values()).map(Enum::name).toList().contains(id)) {
            throw new Fit2cloudException(ErrorCodeConstants.BASE_ROLE_CANNOT_DELETE.getCode(), ErrorCodeConstants.BASE_ROLE_CANNOT_DELETE.getMessage());
        }
        boolean success = removeById(id);
        rolePermissionService.deletePermissionsByRole(id);
        return success;
    }

    public boolean deleteRolesByIds(List<String> ids) {
        if (Arrays.stream(RoleConstants.ROLE.values()).map(Enum::name).anyMatch(ids::contains)) {
            throw new Fit2cloudException(ErrorCodeConstants.BASE_ROLE_CANNOT_DELETE.getCode(), ErrorCodeConstants.BASE_ROLE_CANNOT_DELETE.getMessage());
        }
        boolean success = removeByIds(ids);
        ids.forEach(id -> rolePermissionService.deletePermissionsByRole(id));
        return success;
    }

    @Override
    public boolean updateRole(Role role) {
        if (this.count(new LambdaQueryWrapper<Role>().eq(Role::getName, role.getName()).ne(Role::getId, role.getId())) > 0) {
            throw new Fit2cloudException(ErrorCodeConstants.ROLE_NAME_CANNOT_DUPLICATED.getCode(), ErrorCodeConstants.ROLE_NAME_CANNOT_DUPLICATED.getMessage());
        }
        return this.updateById(role);
    }


}
