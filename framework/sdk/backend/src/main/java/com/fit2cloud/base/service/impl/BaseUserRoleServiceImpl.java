package com.fit2cloud.base.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fit2cloud.base.entity.Role;
import com.fit2cloud.base.entity.UserRole;
import com.fit2cloud.base.mapper.BaseUserRoleMapper;
import com.fit2cloud.base.service.IBaseRoleService;
import com.fit2cloud.base.service.IBaseUserRoleService;
import com.fit2cloud.common.constants.RoleConstants;
import com.fit2cloud.common.utils.JwtTokenUtils;
import com.fit2cloud.dto.UserRoleDto;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RBucket;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
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
public class BaseUserRoleServiceImpl extends ServiceImpl<BaseUserRoleMapper, UserRole> implements IBaseUserRoleService {

    @Resource
    private RedissonClient redissonClient;
    @Resource
    private IBaseRoleService roleService;

    private static final String USER_ROLE_KEY = "USER_ROLE_MAP:";
    private static final String LOCK_ROLE_KEY = "LOCK_USER_ROLE_MAP:";

    @Override
    public Map<RoleConstants.ROLE, List<UserRoleDto>> getCachedUserRoleMap(String userId) {
        RBucket<Map<RoleConstants.ROLE, List<UserRoleDto>>> bucket = redissonClient.getBucket(USER_ROLE_KEY + userId);
        Map<RoleConstants.ROLE, List<UserRoleDto>> map = null;
        RLock lock = redissonClient.getLock(LOCK_ROLE_KEY + userId);
        try {
            lock.lock(5, TimeUnit.SECONDS);
            map = bucket.get();
        } catch (Exception ignore) {
        } finally {
            lock.unlock();
        }
        if (map == null || map.isEmpty()) {
            return saveCachedUserRoleMap(userId);
        } else {
            return map;
        }
    }

    @Override
    public Map<RoleConstants.ROLE, List<UserRoleDto>> saveCachedUserRoleMap(String userId) {
        Map<RoleConstants.ROLE, List<UserRoleDto>> map = getUserRoleMap(userId);
        RBucket<Map<RoleConstants.ROLE, List<UserRoleDto>>> bucket = redissonClient.getBucket(USER_ROLE_KEY + userId);
        RLock lock = redissonClient.getLock(LOCK_ROLE_KEY + userId);
        try {
            lock.lock(5, TimeUnit.SECONDS);
            bucket.set(map, JwtTokenUtils.JWT_EXPIRE_MINUTES, TimeUnit.MINUTES); //与jwt token失效时间保持一致
        } catch (Exception ignore) {
        } finally {
            lock.unlock();
        }
        return map;
    }

    @Override
    public Map<RoleConstants.ROLE, List<UserRoleDto>> getUserRoleMap(String userId) {

        Map<RoleConstants.ROLE, List<UserRoleDto>> result = new HashMap<>();

        Map<String, Role> roleMap = roleService.list().stream().collect(Collectors.toMap(Role::getId, role -> role));

        List<UserRole> userRoles = this.list(new LambdaQueryWrapper<UserRole>()
                        .eq(UserRole::getUserId, userId)
                        .in(UserRole::getRoleId, roleMap.keySet())
                //todo 判断source
        );

        Map<String, List<Role>> sourceRolesMap = new HashMap<>();
        List<Role> adminRoles = null;
        for (UserRole userRole : userRoles) {
            Role role = roleMap.get(userRole.getRoleId());
            if (StringUtils.isNotBlank(userRole.getSource())) { //组织，工作空间的角色
                sourceRolesMap.computeIfAbsent(userRole.getSource(), k -> new ArrayList<>());
                sourceRolesMap.get(userRole.getSource()).add(role);
                continue;
            }
            if (role.getParentRoleId().equals(RoleConstants.ROLE.ADMIN)) { //系统管理员
                if (adminRoles == null) {
                    adminRoles = new ArrayList<>();
                }
                adminRoles.add(role);
            }
        }

        //系统管理员
        if (CollectionUtils.isNotEmpty(adminRoles)) {
            UserRoleDto dto = new UserRoleDto()
                    .setRoles(adminRoles)
                    .setParentRole(RoleConstants.ROLE.ADMIN)
                    .sortRoles();
            result.put(RoleConstants.ROLE.ADMIN, Collections.singletonList(dto));
        }

        if (!sourceRolesMap.isEmpty()) {
            sourceRolesMap.forEach((source, roleList) -> {
                UserRoleDto dto = new UserRoleDto()
                        .setSource(source)
                        .setParentSource(null) //todo
                        .setRoles(roleList)
                        .sortRoles();
                if (RoleConstants.Type.origin.equals(roleList.get(0).getType())) {
                    dto.setParentRole(RoleConstants.ROLE.valueOf(roleList.get(0).getId()));
                } else {
                    dto.setParentRole(roleList.get(0).getParentRoleId());
                }
                result.computeIfAbsent(dto.getParentRole(), k -> new ArrayList<>());
                result.get(dto.getParentRole()).add(dto);
            });

        }

        return result;


    }

    @Override
    public boolean deleteUserRoleByRoleId(String roleId) {
        QueryWrapper<UserRole> userRoleQueryWrapper = Wrappers.query();
        userRoleQueryWrapper.lambda().eq(UserRole::getRoleId, roleId);
        return remove(userRoleQueryWrapper);
    }

    @Override
    public boolean removeUserRoleByUserIdAndRoleId(String userId, String roleId, String sourceId) {

        Role role = roleService.getById(roleId);
        if (!RoleConstants.ROLE.ADMIN.equals(role.getParentRoleId())) {
            if (StringUtils.isEmpty(sourceId)) {
                throw new RuntimeException("组织/工作空间ID不能为空");
            }
        }

        return remove(new LambdaQueryWrapper<UserRole>()
                .eq(UserRole::getUserId, userId)
                .eq(UserRole::getRoleId, roleId)
                .eq(StringUtils.isNotEmpty(sourceId), UserRole::getSource, sourceId)
        );
    }

    @Override
    public List<UserRole> searchByUsersAndRole(String roleId, List<String> userIds) {
        return this.list(new LambdaQueryWrapper<UserRole>().eq(UserRole::getRoleId, roleId).in(UserRole::getUserId, userIds));
    }

    @Override
    public boolean deleteUserRoleByOrgId(String orgId) {
        return deleteUserRoleBySource(orgId);
    }

    @Override
    public boolean deleteUserRoleByWorkspaceId(String workspaceId) {
        return deleteUserRoleBySource(workspaceId);
    }

    private Boolean deleteUserRoleBySource(String source) {
        QueryWrapper<UserRole> userRoleQueryWrapper = Wrappers.query();
        userRoleQueryWrapper.lambda().eq(UserRole::getSource, source);
        return remove(userRoleQueryWrapper);
    }
}
