package com.fit2cloud.service;

import com.fit2cloud.base.entity.Role;
import com.fit2cloud.base.service.IBaseRolePermissionService;
import com.fit2cloud.base.service.IBaseRoleService;
import com.fit2cloud.base.service.IBaseUserRoleService;
import com.fit2cloud.common.constants.RoleConstants;
import com.fit2cloud.common.utils.JsonUtil;
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
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class BasePermissionService {

    @Resource
    private RedissonClient redissonClient;

    @Resource
    private IBaseRoleService roleService;

    @Resource
    private IBaseUserRoleService userRoleService;

    @Resource
    private IBaseRolePermissionService rolePermissionService;

    private static final String PERMISSION_MAP = "PERMISSION_MAP:";
    public static final String MODULE_PERMISSION = "MODULE_PERMISSION:";

    /**
     * 启动后推送到redis, role -- module -- permissions
     *
     * @param module
     * @param modulePermission
     */
    public void init(String module, ModulePermission modulePermission) {

        Map<String, List<CeGrantedAuthority>> rolePermissions = new HashMap<>();

        List<Role> roles = roleService.list();
        List<String> baseRoles = Arrays.stream(RoleConstants.ROLE.values()).map(Enum::name).toList();

        //从数据库获取当前模块自定义角色权限
        Map<String, List<CeGrantedAuthority>> customsRolePermissionMap = rolePermissionService.getCurrentModulePermissionMap(roles.stream().map(Role::getId).collect(Collectors.toList()));

        //系统内置权限
        for (RoleConstants.ROLE baseRole : RoleConstants.ROLE.values()) {
            putModulePermission(module, baseRole, modulePermission);
        }

        //系统内置角色权限
        for (PermissionGroup group : modulePermission.getGroups()) {
            for (Permission permission : group.getPermissions()) {
                for (RoleConstants.ROLE role : permission.getRoles()) {
                    rolePermissions.computeIfAbsent(role.name(), k -> new ArrayList<>());
                    rolePermissions.get(role.name()).add(new CeGrantedAuthority(permission.getId(), permission.getSimpleId()));
                }
            }
        }
        //系统内置角色权限ID列表
        Map<String, List<String>> originRolePermissionIdsMap = new HashMap<>();
        for (String baseRole : baseRoles) {
            originRolePermissionIdsMap.put(baseRole, rolePermissions.getOrDefault(baseRole, new ArrayList<>()).stream().map(CeGrantedAuthority::getAuthority).collect(Collectors.toList()));
        }

        for (Role role : roles) {
            if (baseRoles.contains(role.getId())) { //系统默认的角色已经根据程序初始化了
                continue;
            }
            rolePermissions.computeIfAbsent(role.getId(), k -> new ArrayList<>());

            //根据系统默认角色权限过滤一下可能会有无效的权限
            List<CeGrantedAuthority> customPermissionList = customsRolePermissionMap.getOrDefault(role.getId(), new ArrayList<>());
            customPermissionList = customPermissionList.stream().filter(ceGrantedAuthority -> originRolePermissionIdsMap.get(role.getParentRoleId().name()).contains(ceGrantedAuthority.getAuthority())).collect(Collectors.toList());

            rolePermissions.put(role.getId(), customPermissionList);
        }

        for (Role role : roles) {
            putPermission(module, role.getId(), rolePermissions);
        }

    }

    /**
     * 为了方便权限列表编辑，把整个ModulePermission对象也存到redis中
     */
    private void putModulePermission(String module, RoleConstants.ROLE role, ModulePermission modulePermission) {
        ModulePermission allPermission = JsonUtil.parseObject(JsonUtil.toJSONString(modulePermission), ModulePermission.class);
        for (PermissionGroup group : allPermission.getGroups()) {
            group.setPermissions(group.getPermissions().stream().filter(permission -> permission.getRoles().contains(role)).collect(Collectors.toList()));
        }
        allPermission.setGroups(allPermission.getGroups().stream().filter(group -> CollectionUtils.isNotEmpty(group.getPermissions())).collect(Collectors.toList()));

        String key = MODULE_PERMISSION + role;

        RMap<String, ModulePermission> map = redissonClient.getMap(key);
        RReadWriteLock lock = map.getReadWriteLock(key);

        try {
            lock.writeLock().lock(10, TimeUnit.SECONDS);
            map.put(module, allPermission);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.writeLock().unlock();
        }
    }

    private void putPermission(String module, String role, Map<String, List<CeGrantedAuthority>> rolePermissions) {
        RMap<String, List<CeGrantedAuthority>> map = redissonClient.getMap(PERMISSION_MAP + role);
        RReadWriteLock lock = map.getReadWriteLock(PERMISSION_MAP + role);

        try {
            lock.writeLock().lock(10, TimeUnit.SECONDS);
            map.put(module, rolePermissions.getOrDefault(role, new ArrayList<>()));
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
