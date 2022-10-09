package com.fit2cloud.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fit2cloud.autoconfigure.ServerInfo;
import com.fit2cloud.base.entity.Role;
import com.fit2cloud.base.entity.RolePermission;
import com.fit2cloud.common.constants.RoleConstants;
import com.fit2cloud.dao.mapper.RoleMapper;
import com.fit2cloud.dao.mapper.RolePermissionMapper;
import com.fit2cloud.dto.permission.ModulePermission;
import com.fit2cloud.security.CeGrantedAuthority;
import com.fit2cloud.service.BasePermissionService;
import com.fit2cloud.service.IRolePermissionService;
import org.apache.commons.collections4.CollectionUtils;
import org.redisson.api.RMap;
import org.redisson.api.RReadWriteLock;
import org.redisson.api.RedissonClient;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.TimeUnit;
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
public class RolePermissionServiceImpl extends ServiceImpl<RolePermissionMapper, RolePermission> implements IRolePermissionService {

    @Resource
    private BasePermissionService basePermissionService;

    @Resource
    private RoleMapper roleMapper;

    @Resource
    private RedissonClient redissonClient;

    @Resource
    private DiscoveryClient discoveryClient;

    @Override
    public List<String> getCachedRolePermissionsByRole(String roleId) {
        return basePermissionService.readPermissionFromRedis(Collections.singleton(roleId)).stream().map(CeGrantedAuthority::getAuthority).collect(Collectors.toList());
    }

    @Override
    public void savePermissionsByRole(String roleId, List<String> permissionIds) {
        Role role = roleMapper.selectById(roleId);

        Map<String, Set<CeGrantedAuthority>> authorities = new HashMap<>();

        //找到基础权限
        if (CollectionUtils.isNotEmpty(permissionIds)) {
            Map<String, ModulePermission> modulePermissionMap = getAllModulePermission(role.getParentRoleId());
            modulePermissionMap.forEach((module, modulePermission) -> {
                authorities.putIfAbsent(module, new HashSet<>());
                modulePermission.getGroups().forEach(permissionGroup -> {
                    permissionGroup.getPermissions().forEach(permission -> {
                        if (permissionIds.contains(permission.getId())) {
                            authorities.get(module).add(new CeGrantedAuthority(permission.getId(), permission.getSimpleId()));
                        }
                    });
                });
            });
        }

        List<CeGrantedAuthority> permissions = authorities.values().stream().flatMap(Collection::stream).collect(Collectors.toList());

        //保存数据库
        RolePermission rolePermission = new RolePermission()
                .setRoleId(roleId)
                .setPermissions(permissions);
        this.saveOrUpdate(rolePermission);

        if (CollectionUtils.isNotEmpty(permissions)) {
            //缓存到redis
            authorities.forEach((module, list) -> {
                basePermissionService.putPermission(module, roleId, new ArrayList<>(list));
            });
        } else {
            basePermissionService.removePermission(roleId);
        }
    }

    @Override
    public void deletePermissionsByRole(String roleId) {
        //数据库删除
        this.removeById(roleId);

        //清理redis
        basePermissionService.removePermission(roleId);
    }

    @Override
    public Map<String, ModulePermission> getAllModulePermission(RoleConstants.ROLE role) {
        RMap<String, ModulePermission> map = redissonClient.getMap(BasePermissionService.MODULE_PERMISSION + role);
        RReadWriteLock lock = map.getReadWriteLock(BasePermissionService.MODULE_PERMISSION + role);
        Map<String, ModulePermission> results = new HashMap<>();
        try {
            lock.readLock().lock(10, TimeUnit.SECONDS);
            results = map.readAllMap();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.readLock().unlock();
        }
        Set<String> modules = new HashSet<>(discoveryClient.getServices());
        modules.add(ServerInfo.module);

        results.keySet().removeIf(key -> !modules.contains(key));

        return results;
    }
}
