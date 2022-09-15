package com.fit2cloud.base.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fit2cloud.autoconfigure.ServerInfo;
import com.fit2cloud.base.entity.RolePermission;
import com.fit2cloud.base.mapper.BaseRolePermissionMapper;
import com.fit2cloud.base.service.IBaseRolePermissionService;
import com.fit2cloud.security.CeGrantedAuthority;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author fit2cloud
 * @since
 */
@Service
public class BaseRolePermissionServiceImpl extends ServiceImpl<BaseRolePermissionMapper, RolePermission> implements IBaseRolePermissionService {


    @Override
    public Map<String, List<CeGrantedAuthority>> getCurrentModulePermissionMap(List<String> roleIds) {
        String module = ServerInfo.getModule();
        if (StringUtils.isBlank(module) || StringUtils.equals("gateway", module)) {
            return new HashMap<>();
        }
        List<RolePermission> list = listByIds(roleIds);
        if (CollectionUtils.isEmpty(list)) {
            return new HashMap<>();
        }
        return list.stream().collect(Collectors.toMap(RolePermission::getRoleId, rolePermission -> rolePermission.getPermissions().stream().filter(ceGrantedAuthority -> StringUtils.startsWith(ceGrantedAuthority.getAuthority(), "[" + module + "]")).collect(Collectors.toList())));

    }
}
