package com.fit2cloud.service;

import com.fit2cloud.base.entity.Role;
import com.fit2cloud.base.service.IBaseRoleService;
import com.fit2cloud.base.service.IBaseUserRoleService;
import com.fit2cloud.common.constants.RoleConstants;
import com.fit2cloud.dto.UserRoleDto;
import com.fit2cloud.dto.permission.ModulePermission;
import com.fit2cloud.dto.permission.Permission;
import com.fit2cloud.dto.permission.PermissionGroup;
import com.fit2cloud.security.CeGrantedAuthority;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RMap;
import org.redisson.api.RReadWriteLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
public class PermissionService {

    @Resource
    private RedissonClient redissonClient;

    @Resource
    private IBaseRoleService roleService;

    @Resource
    private IBaseUserRoleService userRoleService;

    public static String module;

    @Value("${spring.application.name}")
    public void setModule(String module) {
        PermissionService.module = module;
    }

    private static final String PERMISSION_MAP = "PERMISSION_MAP:";

    //启动后推送到redis, role -- module -- permissions
    public void init(String module, ModulePermission modulePermission) {

        Map<String, List<CeGrantedAuthority>> rolePermissions = new HashMap<>();

        List<String> roles = roleService.list().stream().map(Role::getId).toList();

        for (String role : roles) {
            rolePermissions.computeIfAbsent(role, k -> new ArrayList<>());
        }

        //todo 自定义role permissions, 只处理本模块，其他模块在redis中为空就直接放进redis?


        for (PermissionGroup group : modulePermission.getGroups()) {
            for (Permission permission : group.getPermissions()) {
                for (RoleConstants.ROLE role : permission.getRoles()) {
                    rolePermissions.get(role.name()).add(new CeGrantedAuthority(permission.getId(), permission.getSimpleId()));
                }
            }
        }


        for (String role : roles) {
            putPermission(module, role, rolePermissions);
        }

    }

    private void putPermission(String module, String role, Map<String, List<CeGrantedAuthority>> rolePermissions) {
        RMap<String, List<CeGrantedAuthority>> map = redissonClient.getMap(PERMISSION_MAP + role);
        RReadWriteLock lock = map.getReadWriteLock(PERMISSION_MAP + role);

        try {
            lock.writeLock().lock(10, TimeUnit.SECONDS);
            map.put(module, rolePermissions.get(role));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.writeLock().unlock();
        }
    }

    public List<CeGrantedAuthority> readPermissionFromRedis(Set<String> roles) {

        Set<CeGrantedAuthority> authorities = new HashSet<>();

        for (String role : roles) {
            RMap<String, List<CeGrantedAuthority>> map = redissonClient.getMap(PERMISSION_MAP + role);
            RReadWriteLock lock = map.getReadWriteLock(PERMISSION_MAP + role);
            Map<String, List<CeGrantedAuthority>> results = new HashMap<>();
            try {
                lock.readLock().lock(10, TimeUnit.SECONDS);
                results = map.readAllMap();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                lock.readLock().unlock();
            }
            for (List<CeGrantedAuthority> value : results.values()) {
                if (value != null) {
                    authorities.addAll(value);
                }
            }
        }

        return new ArrayList<>(authorities);
    }

    public Set<String> getPlainPermissions(String userId, RoleConstants.ROLE role, String source) {
        Map<RoleConstants.ROLE, List<UserRoleDto>> map = userRoleService.getCachedUserRoleMap(userId);
        List<CeGrantedAuthority> authority = readPermissionFromRedis(rolesForSearchAuthority(map, role, source));
        Set<String> plainPermissions = new HashSet<>();
        for (CeGrantedAuthority ceGrantedAuthority : authority) {
            //将完整权限id和simpleId都放进去
            plainPermissions.add(ceGrantedAuthority.getAuthority());
            plainPermissions.add(ceGrantedAuthority.getCurrentModulePermission());
        }
        return plainPermissions;
    }

    public Set<String> rolesForSearchAuthority(Map<RoleConstants.ROLE, List<UserRoleDto>> map, RoleConstants.ROLE role, String source) {
        Set<String> rolesForSearchAuthority = new HashSet<>();

        List<UserRoleDto> userRoleDtos = map.getOrDefault(role, new ArrayList<>());

        if (RoleConstants.ROLE.ADMIN.equals(role)) {
            if (CollectionUtils.isNotEmpty(userRoleDtos)) {
                rolesForSearchAuthority.addAll(userRoleDtos.get(0).getRoles().stream().map(Role::getId).toList());
            }
        } else if (source != null) {
            for (UserRoleDto userRoleDto : userRoleDtos) {
                if (StringUtils.equals(source, userRoleDto.getSource())) {
                    if (role.equals(userRoleDto.getParentRole())) {
                        rolesForSearchAuthority.addAll(userRoleDto.getRoles().stream().map(Role::getId).toList());
                    }
                    break;
                }
            }
        }

        rolesForSearchAuthority.add(RoleConstants.ROLE.ANONYMOUS.name()); //默认有匿名用户权限

        return rolesForSearchAuthority;
    }


}
