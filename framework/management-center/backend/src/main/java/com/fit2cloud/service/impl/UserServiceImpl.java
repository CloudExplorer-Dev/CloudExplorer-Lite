package com.fit2cloud.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fit2cloud.base.entity.Organization;
import com.fit2cloud.base.entity.Role;
import com.fit2cloud.base.entity.User;
import com.fit2cloud.base.entity.UserRole;
import com.fit2cloud.base.service.IBaseOrganizationService;
import com.fit2cloud.base.service.IBaseRoleService;
import com.fit2cloud.base.service.IBaseUserRoleService;
import com.fit2cloud.common.constants.RoleConstants;
import com.fit2cloud.common.constants.SystemUserConstants;
import com.fit2cloud.common.exception.Fit2cloudException;
import com.fit2cloud.common.utils.ColumnNameUtil;
import com.fit2cloud.common.utils.CurrentUserUtils;
import com.fit2cloud.common.utils.MD5Util;
import com.fit2cloud.common.utils.PageUtil;
import com.fit2cloud.constants.ErrorCodeConstants;
import com.fit2cloud.constants.UserConstants;
import com.fit2cloud.controller.request.user.*;
import com.fit2cloud.dao.entity.UserNotificationSetting;
import com.fit2cloud.dao.entity.Workspace;
import com.fit2cloud.dao.mapper.UserMapper;
import com.fit2cloud.dto.*;
import com.fit2cloud.request.role.RoleRequest;
import com.fit2cloud.service.*;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 用户 服务实现类
 * </p>
 *
 * @author fit2cloud
 * @since
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Resource
    IRoleService roleService;

    @Resource
    IBaseRoleService baseRoleService;

    @Resource
    IBaseUserRoleService userRoleService;

    @Resource
    BaseMapper<Role> roleMapper;

    @Resource
    IBaseOrganizationService baseOrganizationService;

    @Resource
    IWorkspaceService workspaceService;

    @Resource
    BaseMapper<UserNotificationSetting> userNotificationSettingMapper;

    @Resource
    OrganizationCommonService organizationCommonService;

    @Resource
    WorkspaceCommonService workspaceCommonService;

    @Override
    public IPage<UserDto> pageUser(PageUserRequest pageUserRequest) {
        Page<User> page = PageUtil.of(pageUserRequest, User.class, true);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        QueryWrapper<User> wrapper = new QueryWrapper<User>()
                .like(StringUtils.isNotBlank(pageUserRequest.getUsername()), ColumnNameUtil.getColumnName(User::getUsername, true), pageUserRequest.getUsername())
                .like(StringUtils.isNotBlank(pageUserRequest.getName()), ColumnNameUtil.getColumnName(User::getName, true), pageUserRequest.getName())
                .like(StringUtils.isNotBlank(pageUserRequest.getEmail()), ColumnNameUtil.getColumnName(User::getEmail, true), pageUserRequest.getEmail())
                .eq(StringUtils.isNotBlank(pageUserRequest.getRoleId()), ColumnNameUtil.getColumnName(UserRole::getRoleId, true), pageUserRequest.getRoleId())
                .like(StringUtils.isNotBlank(pageUserRequest.getRoleName()), ColumnNameUtil.getColumnName(Role::getName, true), pageUserRequest.getRoleName())
                .eq(StringUtils.isNotBlank(pageUserRequest.getWorkspaceId()), ColumnNameUtil.getColumnName(UserRole::getSource, true), pageUserRequest.getWorkspaceId());

        if (pageUserRequest.getParentRole() != null) {
            wrapper.eq(ColumnNameUtil.getColumnName(Role::getParentRoleId, true), pageUserRequest.getParentRole().name());
        }

        if (StringUtils.isNotBlank(pageUserRequest.getOrganizationId())) {
            wrapper.eq(ColumnNameUtil.getColumnName(UserRole::getSource, true), pageUserRequest.getOrganizationId());
        }

        if (CollectionUtils.isNotEmpty(pageUserRequest.getUpdateTime())) {
            wrapper.between(ColumnNameUtil.getColumnName(User::getUpdateTime, true), simpleDateFormat.format(pageUserRequest.getUpdateTime().get(0)), simpleDateFormat.format(pageUserRequest.getUpdateTime().get(1)));
        }
        if (CollectionUtils.isNotEmpty(pageUserRequest.getCreateTime())) {
            wrapper.between(ColumnNameUtil.getColumnName(User::getCreateTime, true), simpleDateFormat.format(pageUserRequest.getCreateTime().get(0)), simpleDateFormat.format(pageUserRequest.getCreateTime().get(1)));
        }

        // 根据当前所在角色过滤
        List<String> resourceIds = new ArrayList<>();
        if (CurrentUserUtils.isOrgAdmin()) {
            List<String> orgIds = organizationCommonService.getOrgIdsByParentId(CurrentUserUtils.getOrganizationId());
            resourceIds = workspaceCommonService.getWorkspaceIdsByOrgIds(orgIds);
            resourceIds.addAll(orgIds);
            wrapper.in(CollectionUtils.isNotEmpty(resourceIds), ColumnNameUtil.getColumnName(UserRole::getSource, true), resourceIds);
        }

        IPage<User> userIPage = baseMapper.pageUser(page, wrapper);

        Map<String, Object> param = new HashMap<>();
        if (CollectionUtils.isNotEmpty(resourceIds)) {
            param.put("resourceIds", resourceIds);
        }

        List<String> finalResourceIds = resourceIds;
        return userIPage.convert(user -> {
            param.put("userId", user.getId());
            UserDto userDto = new UserDto();
            userDto.setRoles(roleService.getRolesByResourceIds(param));
            BeanUtils.copyProperties(user, userDto);

            Map<RoleConstants.ROLE, List<UserRoleDto>> userRoleMap = userRoleService.getUserRoleMap(user.getId());

            if (CollectionUtils.isNotEmpty(finalResourceIds)) {
                userRoleMap.entrySet().forEach(map -> {
                    map.setValue(map.getValue().stream().filter(userRoleDto -> finalResourceIds.contains(userRoleDto.getSource())).toList());
                });
                userRoleMap = userRoleMap.entrySet().stream().filter(map -> map.getValue().size() > 0).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
            }

            userDto.setRoleMap(userRoleMap);

            return userDto;
        });
    }

    public List<User> getManageUserSimpleList(List<String> userIds) {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        // 根据当前所在角色过滤
        if (CurrentUserUtils.isOrgAdmin()) {
            List<String> orgIds = organizationCommonService.getOrgIdsByParentId(CurrentUserUtils.getOrganizationId());
            List<String> resourceIds = workspaceCommonService.getWorkspaceIdsByOrgIds(orgIds);
            resourceIds.addAll(orgIds);
            wrapper.in(CollectionUtils.isNotEmpty(resourceIds), ColumnNameUtil.getColumnName(UserRole::getSource, true), resourceIds);
        }
        if (userIds != null) {
            if (CollectionUtils.isEmpty(userIds)) {
                return new ArrayList<>();
            } else {
                wrapper.in(ColumnNameUtil.getColumnName(User::getId, true), userIds);
            }
        }
        return baseMapper.listUser(wrapper);
    }

    @Override
    public UserDto getUser(String userId) {
        if (CurrentUserUtils.isOrgAdmin()) {
            List<String> filteredUserIds = this.getManageUserSimpleList(List.of(userId)).stream().map(User::getId).toList();
            if (CollectionUtils.isEmpty(filteredUserIds)) {
                throw new RuntimeException("没有权限查看该用户");
            }
        } else if (CurrentUserUtils.isUser() && !StringUtils.equals(CurrentUserUtils.getUser().getId(), userId)) {
            throw new RuntimeException("没有权限查看该用户");
        }
        User user = this.getById(userId);
        // 根据当前所在角色过滤
        List<String> resourceIds = new ArrayList<>();
        if (CurrentUserUtils.isOrgAdmin()) {
            List<String> orgIds = organizationCommonService.getOrgIdsByParentId(CurrentUserUtils.getOrganizationId());
            resourceIds = workspaceCommonService.getWorkspaceIdsByOrgIds(orgIds);
            resourceIds.addAll(orgIds);
        }
        Map<String, Object> param = new HashMap<>();
        if (CollectionUtils.isNotEmpty(resourceIds)) {
            param.put("resourceIds", resourceIds);
        }

        param.put("userId", user.getId());
        UserDto userDto = new UserDto();
        userDto.setRoles(roleService.getRolesByResourceIds(param));
        BeanUtils.copyProperties(user, userDto);

        Map<RoleConstants.ROLE, List<UserRoleDto>> userRoleMap = userRoleService.getUserRoleMap(user.getId());

        if (CollectionUtils.isNotEmpty(resourceIds)) {
            List<String> finalResourceIds = resourceIds;
            userRoleMap.entrySet().forEach(map -> {
                map.setValue(map.getValue().stream().filter(userRoleDto -> finalResourceIds.contains(userRoleDto.getSource())).toList());
            });
            userRoleMap = userRoleMap.entrySet().stream().filter(map -> map.getValue().size() > 0).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        }

        userDto.setRoleMap(userRoleMap);

        return userDto;
    }

    @Override
    public boolean deleteUser(String userId) {
        String currentLoginUserId = CurrentUserUtils.getUser().getId();
        User user = baseMapper.selectById(userId);

        // 不能删除自己
        if (StringUtils.equalsIgnoreCase(user.getId(), currentLoginUserId)) {
            throw new Fit2cloudException(ErrorCodeConstants.USER_CAN_NOT_DELETE.getCode(), ErrorCodeConstants.USER_CAN_NOT_DELETE.getMessage());
        }

        // TODO 增加删除用户的操作日志
        System.out.println(user.getUsername() + "is deleted by" + currentLoginUserId);

        // 当前登录用户以组织管理员的角色删除用户
        if (CurrentUserUtils.isOrgAdmin()) {
            // 当为组织管理员时，删除当前组织及其子组织下的工作空间（解绑关系user_role）
            List<String> sourceList = new ArrayList<>();

            // 获取当前组织及其所有子组织ID
            List<String> subOrgIds = organizationCommonService.getOrgIdsByParentId(CurrentUserUtils.getOrganizationId());
            if (CollectionUtils.isNotEmpty(subOrgIds)) {
                sourceList.addAll(subOrgIds);
            }

            // 查询当前组织及其子组织下的工作空间
            List<String> workspaceIds = workspaceCommonService.getWorkspaceIdsByOrgIds(subOrgIds);
            if (CollectionUtils.isNotEmpty(workspaceIds)) {
                sourceList.addAll(workspaceIds);
            }

            // 根据sourceList，删除用户角色记录
            QueryWrapper<UserRole> userRoleWrapper = Wrappers.query();
            userRoleWrapper.lambda()
                    .eq(true, UserRole::getId, userId)
                    .in(true, UserRole::getSource, sourceList);
            userRoleService.remove(userRoleWrapper);

            // 查询该用户具有的所有角色，如果为空，则删除用户
            List<RoleInfo> userRoleList = roleInfo(currentLoginUserId, false);
            if (CollectionUtils.isEmpty(userRoleList)) {
                removeById(userId);
            }
        }

        // 当前登录用户以系统管理员的角色删除用户
        if (CurrentUserUtils.isAdmin()) {
            // 删除用户
            removeById(userId);

            // 删除用户的角色关系
            QueryWrapper<UserRole> userRoleWrapper = Wrappers.query();
            userRoleWrapper.lambda()
                    .eq(true, UserRole::getUserId, userId);
            userRoleService.remove(userRoleWrapper);
        }

        //更新redis
        userRoleService.saveCachedUserRoleMap(user.getId());

        return true;
    }

    public boolean changeUserStatus(UserDto userDto) {
        if (CurrentUserUtils.isOrgAdmin()) {
            List<String> filteredUserIds = this.getManageUserSimpleList(List.of(userDto.getId())).stream().map(User::getId).toList();
            if (CollectionUtils.isEmpty(filteredUserIds)) {
                throw new RuntimeException("没有权限修改该用户");
            }
        }

        User userUpdate = new User();
        userUpdate.setId(userDto.getId());
        userUpdate.setEnabled(userDto.getEnabled());
        if (userDto.getRoles().stream().map(Role::getId).anyMatch(s -> s.equals(RoleConstants.ROLE.ADMIN.name())) && !userDto.getEnabled()) {
            Long countAdmin = baseMapper.countActiveUsers(RoleConstants.ROLE.ADMIN.name());
            if (countAdmin > 1) {
                this.updateById(userUpdate);
            } else {
                throw new Fit2cloudException(ErrorCodeConstants.USER_KEEP_ONE_ADMIN.getCode(), ErrorCodeConstants.USER_KEEP_ONE_ADMIN.getMessage());
            }
        } else {
            this.updateById(userUpdate);
        }
        return true;
    }

    @Transactional
    public boolean createUser(CreateUserRequest request) {
        validateUserID(request.getUsername());

        UserOperateDto user = new UserOperateDto();
        BeanUtils.copyProperties(request, user);

        if (!UserConstants.Source.LOCAL.getValue().equalsIgnoreCase(user.getSource()) && !UserConstants.Source.EXTRA.getValue().equalsIgnoreCase(user.getSource())) {
            user.setSource(UserConstants.Source.LOCAL.getValue());
        }

        user.setPassword(MD5Util.md5(user.getPassword()));

        if (CollectionUtils.isNotEmpty(user.getRoleInfoList())) {
            //获取可以添加的角色列表
            List<String> roles = baseRoleService.roles(new RoleRequest()).stream().map(Role::getId).toList();

            List<String> orgIds = null, workspaceIds = null;
            if (CurrentUserUtils.isOrgAdmin()) {
                // 删除要编辑的用户在当前组织下的 user_role 的信息，然后 reinsert
                orgIds = baseOrganizationService.getOrgAdminOrgIds();
                workspaceIds = workspaceCommonService.getWorkspaceIdsByOrgIds(orgIds);
            }
            for (RoleInfo roleInfo : user.getRoleInfoList()) {
                if (!roles.contains(roleInfo.getRoleId())) {
                    throw new RuntimeException("没有权限添加ID为[" + roleInfo.getRoleId() + "]的角色");
                }
                if (orgIds != null && workspaceIds != null) {
                    //组织管理员
                    RoleConstants.ROLE parentRoleId = getParentRoleId(roleInfo.getRoleId());
                    if (RoleConstants.ROLE.USER.equals(parentRoleId)) {
                        for (String workspaceId : roleInfo.getWorkspaceIds()) {
                            if (!workspaceIds.contains(workspaceId)) {
                                throw new RuntimeException("不能添加到ID为[" + workspaceId + "]的工作空间中");
                            }
                        }
                    } else if (RoleConstants.ROLE.ORGADMIN.equals(parentRoleId)) {
                        for (String organizationId : roleInfo.getOrganizationIds()) {
                            if (!orgIds.contains(organizationId)) {
                                throw new RuntimeException("不能添加到ID为[" + organizationId + "]的组织中");
                            }
                        }
                    }
                }
            }
        }

        baseMapper.insert(user);

        if (CollectionUtils.isNotEmpty(user.getRoleInfoList())) {
            insertUserRoleInfo(user);
        }
        return true;
    }

    @Transactional
    public boolean updateUser(UpdateUserRequest request) {
        //校验修改的用户邮箱是否已存在
        if (StringUtils.isNotEmpty(request.getEmail())) {
            if (this.count(new LambdaQueryWrapper<User>().ne(User::getId, request.getId()).eq(User::getEmail, request.getEmail())) > 0) {
                throw new Fit2cloudException(ErrorCodeConstants.USER_EMAIL_WARN_DUPLICATED.getCode(), ErrorCodeConstants.USER_EMAIL_WARN_DUPLICATED.getMessage());
            }
        }

        UserOperateDto user = new UserOperateDto();
        BeanUtils.copyProperties(request, user);
        baseMapper.updateById(user);

        if (user.getRoleInfoList() != null) {

            QueryWrapper<UserRole> wrapper = Wrappers.query();
            wrapper.lambda().eq(UserRole::getUserId, request.getId());

            //获取可以添加的角色列表
            List<String> roles = baseRoleService.roles(new RoleRequest()).stream().map(Role::getId).toList();
            if (CollectionUtils.isEmpty(roles) && user.getRoleInfoList().size() > 0) {
                throw new RuntimeException("没有权限添加角色");
            }
            wrapper.lambda().in(UserRole::getRoleId, roles);

            List<String> orgIds = null, workspaceIds = null;
            if (CurrentUserUtils.isOrgAdmin()) {
                // 删除要编辑的用户在当前组织下的 user_role 的信息，然后 reinsert
                orgIds = baseOrganizationService.getOrgAdminOrgIds();
                workspaceIds = workspaceCommonService.getWorkspaceIdsByOrgIds(orgIds);
                // 删除要编辑的用户在当前组织下的 user_role 的信息，然后 reinsert
                wrapper.lambda().in(true, UserRole::getSource, CollectionUtils.union(orgIds, workspaceIds));
            }
            for (RoleInfo roleInfo : user.getRoleInfoList()) {
                if (!roles.contains(roleInfo.getRoleId())) {
                    throw new RuntimeException("没有权限添加ID为[" + roleInfo.getRoleId() + "]的角色");
                }
                if (orgIds != null && workspaceIds != null) {
                    //组织管理员
                    RoleConstants.ROLE parentRoleId = getParentRoleId(roleInfo.getRoleId());
                    if (RoleConstants.ROLE.USER.equals(parentRoleId)) {
                        for (String workspaceId : roleInfo.getWorkspaceIds()) {
                            if (!workspaceIds.contains(workspaceId)) {
                                throw new RuntimeException("不能添加到ID为[" + workspaceId + "]的工作空间中");
                            }
                        }
                    } else if (RoleConstants.ROLE.ORGADMIN.equals(parentRoleId)) {
                        for (String organizationId : roleInfo.getOrganizationIds()) {
                            if (!orgIds.contains(organizationId)) {
                                throw new RuntimeException("不能添加到ID为[" + organizationId + "]的组织中");
                            }
                        }
                    }
                }
            }

            if (CurrentUserUtils.isAdmin() && roles.contains(RoleConstants.ROLE.ADMIN.name())) {
                // 校验系统是否还有系统管理员的角色，如果没有不允许本次修改
                checkSystemAdmin(user);
            }

            // 删除要编辑的用户在 user_role 的信息，然后 reinsert
            userRoleService.remove(wrapper);

            insertUserRoleInfo(user);
        }

        //更新redis中该用户的角色
        userRoleService.saveCachedUserRoleMap(user.getId());

        return true;
    }

    public boolean updatePwd(User user) {
        if (CurrentUserUtils.isOrgAdmin()) {
            List<String> filteredUserIds = this.getManageUserSimpleList(List.of(user.getId())).stream().map(User::getId).toList();
            if (CollectionUtils.isEmpty(filteredUserIds)) {
                throw new RuntimeException("没有权限查看该用户");
            }
        } else if (CurrentUserUtils.isUser() && !StringUtils.equals(CurrentUserUtils.getUser().getId(), user.getId())) {
            throw new RuntimeException("没有权限查看该用户");
        }
        // 非本地创建用户不允许修改密码
        if (!"local".equalsIgnoreCase(this.getById(user.getId()).getSource())) {
            throw new Fit2cloudException(ErrorCodeConstants.USER_NOT_LOCAL_CAN_NOT_EDIT_PASSWORD.getCode(), ErrorCodeConstants.USER_NOT_LOCAL_CAN_NOT_EDIT_PASSWORD.getMessage());
        }
        User updateUser = new User();
        updateUser.setId(user.getId());
        updateUser.setPassword(MD5Util.md5(user.getPassword()));
        baseMapper.updateById(updateUser);
        return true;
    }

    public void insertUserRoleInfo(UserOperateDto user) {
        for (RoleInfo roleInfo : user.getRoleInfoList()) {
            UserRole userRole = new UserRole();
            userRole.setRoleId(roleInfo.getRoleId());
            userRole.setUserId(user.getId());

            RoleConstants.ROLE parentRoleId = getParentRoleId(roleInfo.getRoleId());
            if (RoleConstants.ROLE.USER.equals(parentRoleId)) {
                roleInfo.getWorkspaceIds().forEach(workspaceId -> insertUserRoleInfo(userRole, workspaceId));
            }
            if (RoleConstants.ROLE.ORGADMIN.equals(parentRoleId)) {
                roleInfo.getOrganizationIds().forEach(organizationId -> insertUserRoleInfo(userRole, organizationId));
            }
            if (RoleConstants.ROLE.ADMIN.equals(parentRoleId)) {
                insertUserRoleInfo(userRole, null);
            }
        }
    }

    public UserOperateDto userRoleInfo(String userId) {
        // 查询角色信息
        List<RoleInfo> roleInfos = roleInfo(userId, true);

        // 查询用户信息
        User user = baseMapper.selectById(userId);
        UserOperateDto userOperateDto = new UserOperateDto();

        // 组合数据
        BeanUtils.copyProperties(user, userOperateDto);
        userOperateDto.setPassword(null);
        userOperateDto.setRoleInfoList(roleInfos);

        return userOperateDto;
    }

    /**
     * 根据用户ID及过滤条件查询用户角色
     *
     * @param userId
     * @param addCondition
     * @return
     */
    public List<RoleInfo> roleInfo(String userId, Boolean addCondition) {
        Map<String, Object> param = new HashMap<>();
        param.put("userId", userId);
        if (CurrentUserUtils.isOrgAdmin() && addCondition) {
            List<String> orgIds = organizationCommonService.getOrgIdsByParentId(CurrentUserUtils.getOrganizationId());
            List<String> resourceIds = workspaceCommonService.getWorkspaceIdsByOrgIds(orgIds);
            resourceIds.addAll(orgIds);
            param.put("resourceIds", resourceIds);
        }
        return baseMapper.roleInfo(param);
    }

    private void insertUserRoleInfo(UserRole userRole, String sourceId) {
        userRole.setId(null);
        userRole.setSource(sourceId);
        userRoleService.saveOrUpdate(userRole, new LambdaQueryWrapper<UserRole>()
                .eq(UserRole::getRoleId, userRole.getRoleId())
                .eq(UserRole::getUserId, userRole.getUserId())
                .eq(UserRole::getSource, userRole.getSource())
        );
    }

    public RoleConstants.ROLE getParentRoleId(String roleId) {
        Role role = roleMapper.selectById(roleId);
        return role.getParentRoleId();
    }

    /**
     * 校验用户
     *
     * @param username
     */
    private void validateUserID(String username) {
        if (SystemUserConstants.isSystemUser(username)) {
            throw new Fit2cloudException(ErrorCodeConstants.USER_ID_CANNOT_BE_SYSTEM.getCode(), ErrorCodeConstants.USER_ID_CANNOT_BE_SYSTEM.getMessage());
        }
    }


    public boolean updateUserNotification(UserNotifySettingDTO userNotificationSetting) {
        // 更新用户表的邮箱和手机号
        User user = this.getById(userNotificationSetting.getId())
                .setEmail(userNotificationSetting.getEmail())
                .setPhone(userNotificationSetting.getPhone());
        this.updateById(user);

        // 删除企业微信账号记录
        userNotificationSettingMapper.deleteById(userNotificationSetting.getId());

        // 重新插入企业微信账号记录
        UserNotificationSetting notificationSetting = new UserNotificationSetting()
                .setUserId(userNotificationSetting.getId())
                .setWechatAccount(userNotificationSetting.getWechatAccount());
        userNotificationSettingMapper.insert(notificationSetting);
        return true;
    }

    public UserNotifySettingDTO findUserNotification(String userId) {
        User user = this.getById(userId);
        UserNotificationSetting userNotificationSetting = userNotificationSettingMapper.selectById(userId);

        UserNotifySettingDTO notificationDTO = new UserNotifySettingDTO();
        notificationDTO.setId(userId)
                .setEmail(user.getEmail())
                .setPhone(user.getPhone());
        if (userNotificationSetting != null) {
            notificationDTO.setWechatAccount(userNotificationSetting.getWechatAccount());
        }
        return notificationDTO;
    }

    @Transactional
    @Override
    public int addUserRole(UserBatchAddRoleRequest userBatchAddRoleRequest) {
        int count = 0;

        //获取可以添加的角色列表
        List<String> roles = baseRoleService.roles(new RoleRequest()).stream().map(Role::getId).toList();
        if (CollectionUtils.isEmpty(roles)) {
            throw new RuntimeException("没有权限添加角色");
        }
        List<String> orgIds = null, workspaceIds = null;
        if (CurrentUserUtils.isOrgAdmin()) {
            // 删除要编辑的用户在当前组织下的 user_role 的信息，然后 reinsert
            orgIds = baseOrganizationService.getOrgAdminOrgIds();
            workspaceIds = workspaceCommonService.getWorkspaceIdsByOrgIds(orgIds);
        }
        for (RoleInfo roleInfo : userBatchAddRoleRequest.getRoleInfoList()) {
            if (!roles.contains(roleInfo.getRoleId())) {
                throw new RuntimeException("没有权限添加ID为[" + roleInfo.getRoleId() + "]的角色");
            }
            if (orgIds != null && workspaceIds != null) {
                //组织管理员
                RoleConstants.ROLE parentRoleId = getParentRoleId(roleInfo.getRoleId());
                if (RoleConstants.ROLE.USER.equals(parentRoleId)) {
                    for (String workspaceId : roleInfo.getWorkspaceIds()) {
                        if (!workspaceIds.contains(workspaceId)) {
                            throw new RuntimeException("不能添加到ID为[" + workspaceId + "]的工作空间中");
                        }
                    }
                } else if (RoleConstants.ROLE.ORGADMIN.equals(parentRoleId)) {
                    for (String organizationId : roleInfo.getOrganizationIds()) {
                        if (!orgIds.contains(organizationId)) {
                            throw new RuntimeException("不能添加到ID为[" + organizationId + "]的组织中");
                        }
                    }
                }
            }
        }

        List<String> filteredUserIds = this.getManageUserSimpleList(userBatchAddRoleRequest.getUserIdList()).stream().map(User::getId).toList();
        if (CollectionUtils.isEmpty(filteredUserIds)) {
            return count;
        }

        for (String userId : filteredUserIds) {
            for (RoleInfo roleInfo : userBatchAddRoleRequest.getRoleInfoList()) {
                UserRole userRole = new UserRole();
                userRole.setRoleId(roleInfo.getRoleId());
                userRole.setUserId(userId);
                RoleConstants.ROLE parentRoleId = getParentRoleId(roleInfo.getRoleId());
                if (RoleConstants.ROLE.USER.equals(parentRoleId)) {
                    for (String workspaceId : roleInfo.getWorkspaceIds()) {
                        if (!hasUserRole(userId, roleInfo.getRoleId(), workspaceId)) {
                            insertUserRoleInfo(userRole, workspaceId);
                            count++;
                        }
                    }
                } else if (RoleConstants.ROLE.ORGADMIN.equals(parentRoleId)) {
                    for (String organizationId : roleInfo.getOrganizationIds()) {
                        if (!hasUserRole(userId, roleInfo.getRoleId(), organizationId)) {
                            insertUserRoleInfo(userRole, organizationId);
                            count++;
                        }
                    }
                } else if (RoleConstants.ROLE.ADMIN.equals(parentRoleId)) {
                    if (!hasUserRole(userId, roleInfo.getRoleId(), null)) {
                        insertUserRoleInfo(userRole, null);
                        count++;
                    }
                }
            }
        }

        //更新完数据库后再更新redis
        userBatchAddRoleRequest.getUserIdList().forEach(userId -> {
            userRoleService.saveCachedUserRoleMap(userId);
        });

        return count;
    }

    @Transactional
    @Override
    public int addUserRoleV2(UserBatchAddRoleRequestV2 userBatchAddRoleRequest) {
        int count = 0;

        //校验组织管理员或继承的管理员是否能添加该角色
        List<Role> roles = baseRoleService.roles(new RoleRequest().setId(userBatchAddRoleRequest.getRoleId()));
        if (CollectionUtils.isEmpty(roles)) {
            throw new RuntimeException("没有权限添加该角色");
        }

        Role role = roles.get(0);

        Set<String> allUsers = new HashSet<>();

        for (UserBatchAddRoleObjectV2 map : userBatchAddRoleRequest.getUserSourceMappings()) {
            if (RoleConstants.ROLE.ORGADMIN.equals(role.getParentRoleId()) || RoleConstants.ROLE.USER.equals(role.getParentRoleId())) {
                if (CollectionUtils.isEmpty(map.getSourceIds())) {
                    throw new RuntimeException("组织/工作空间ID列表不能为空");
                }
            }
            //根据当前用户查询
            List<String> filteredUserIds = this.getManageUserSimpleList(map.getUserIds()).stream().map(User::getId).toList();
            if (CollectionUtils.isEmpty(filteredUserIds)) {
                continue;
            }
            List<String> sourceIds = null;
            List<String> organizationIdsForSearch = baseOrganizationService.getOrgAdminOrgIds();

            if (RoleConstants.ROLE.ORGADMIN.equals(role.getParentRoleId())) {
                //根据当前用户查询
                if (CurrentUserUtils.isAdmin()) {
                    sourceIds = baseOrganizationService.listByIds(map.getSourceIds()).stream().map(Organization::getId).toList();
                } else if (CurrentUserUtils.isOrgAdmin()) {
                    sourceIds = organizationIdsForSearch.stream().filter(map.getSourceIds()::contains).toList();
                }
            } else if (RoleConstants.ROLE.USER.equals(role.getParentRoleId())) {
                //根据当前用户查询
                if (CurrentUserUtils.isAdmin()) {
                    sourceIds = workspaceService.listByIds(map.getSourceIds()).stream().map(Workspace::getId).toList();
                } else if (CurrentUserUtils.isOrgAdmin()) {
                    sourceIds = workspaceCommonService.getWorkspaceIdsByOrgIds(organizationIdsForSearch).stream().filter(map.getSourceIds()::contains).toList();
                }
            }
            if (!RoleConstants.ROLE.ADMIN.equals(role.getParentRoleId()) && CollectionUtils.isEmpty(sourceIds)) {
                continue;
            }
            allUsers.addAll(filteredUserIds);

            for (String userId : filteredUserIds) {
                if (sourceIds == null) {
                    insertUserRoleInfo(new UserRole().setRoleId(role.getId()).setUserId(userId), null);
                    count++;
                } else {
                    for (String sourceId : sourceIds) {
                        insertUserRoleInfo(new UserRole().setRoleId(role.getId()).setUserId(userId), sourceId);
                        count++;
                    }
                }
            }
        }

        //更新完数据库后再更新redis
        allUsers.forEach(userId -> {
            userRoleService.saveCachedUserRoleMap(userId);
        });

        return count;
    }

    @Transactional
    @Override
    public int addUserRoleV3(UserBatchAddRoleRequestV3 userBatchAddRoleRequest) {
        int count = 0;

        RoleConstants.ROLE parentRole;
        String sourceId = null;

        List<String> organizationIdsForSearch = baseOrganizationService.getOrgAdminOrgIds();

        if (StringUtils.equals(userBatchAddRoleRequest.getType(), "WORKSPACE")) {
            if (StringUtils.isBlank(userBatchAddRoleRequest.getSourceId())) {
                throw new RuntimeException("工作空间ID不能为空");
            }
            parentRole = RoleConstants.ROLE.USER;
            sourceId = userBatchAddRoleRequest.getSourceId();

            if (CurrentUserUtils.isOrgAdmin()) {
                if (!workspaceCommonService.getWorkspaceIdsByOrgIds(organizationIdsForSearch).contains(sourceId)) {
                    throw new RuntimeException("没有权限添加到该工作空间下");
                }
            }

        } else if (StringUtils.equals(userBatchAddRoleRequest.getType(), "ORGANIZATION")) {
            if (StringUtils.isBlank(userBatchAddRoleRequest.getSourceId())) {
                throw new RuntimeException("组织ID不能为空");
            }
            parentRole = RoleConstants.ROLE.ORGADMIN;
            sourceId = userBatchAddRoleRequest.getSourceId();

            if (CurrentUserUtils.isOrgAdmin()) {
                if (!organizationIdsForSearch.contains(sourceId)) {
                    throw new RuntimeException("没有权限添加到该组织下");
                }
            }
        } else {
            //系统管理员
            parentRole = RoleConstants.ROLE.ADMIN;
        }

        Set<String> allUsers = new HashSet<>();

        //获取可以添加的角色列表
        List<String> roles = baseRoleService.roles(new RoleRequest().setParentRoleId(List.of(parentRole))).stream().map(Role::getId).toList();
        if (CollectionUtils.isEmpty(roles)) {
            throw new RuntimeException("没有可以添加的角色");
        }

        for (UserBatchAddRoleObjectV3 map : userBatchAddRoleRequest.getUserRoleMappings()) {
            //获取可以添加的用户列表
            //根据当前用户查询
            List<String> filteredUserIds = this.getManageUserSimpleList(map.getUserIds()).stream().map(User::getId).toList();
            if (CollectionUtils.isEmpty(filteredUserIds)) {
                continue;
            }
            List<String> filteredRoleIds = map.getRoleIds().stream().filter(roles::contains).toList();
            if (CollectionUtils.isEmpty(filteredRoleIds)) {
                continue;
            }
            allUsers.addAll(filteredUserIds);

            for (String filteredUserId : filteredUserIds) {
                for (String filteredRoleId : filteredRoleIds) {
                    insertUserRoleInfo(new UserRole().setRoleId(filteredRoleId).setUserId(filteredUserId), sourceId);
                    count++;
                }
            }
        }

        //更新完数据库后再更新redis
        allUsers.forEach(userId -> {
            userRoleService.saveCachedUserRoleMap(userId);
        });

        return count;
    }

    @Override
    public boolean removeUserRole(String userId, String roleId, String sourceId) {

        if (StringUtils.equals(RoleConstants.ROLE.ADMIN.name(), roleId)) {
            //需要查询是否还有admin权限的角色，如果是最后一个，不允许删除
            List<String> allUsers = this.list().stream().map(User::getId).toList();
            if (userRoleService.searchByUsersAndRole(RoleConstants.ROLE.ADMIN.name(), allUsers).size() == 1) {
                throw new RuntimeException("不能移除最后一个系统管理员");
            }
        }

        //判断没有权限去移除
        List<String> roles = baseRoleService.roles(new RoleRequest()).stream().map(Role::getId).toList();
        if (!roles.contains(roleId)) {
            throw new RuntimeException("不能移除该用户角色");
        }

        if (CurrentUserUtils.isOrgAdmin()) {
            List<String> organizationIdsForSearch = baseOrganizationService.getOrgAdminOrgIds();
            RoleConstants.ROLE parentRoleId = getParentRoleId(roleId);
            if (RoleConstants.ROLE.USER.equals(parentRoleId)) {
                if (!workspaceCommonService.getWorkspaceIdsByOrgIds(organizationIdsForSearch).contains(sourceId)) {
                    throw new RuntimeException("不能在该工作空间下移除用户角色");
                }
            } else if (RoleConstants.ROLE.ORGADMIN.equals(parentRoleId)) {
                if (!organizationIdsForSearch.contains(sourceId)) {
                    throw new RuntimeException("不能在该组织下移除用户角色");
                }
            }
        }

        userRoleService.removeUserRoleByUserIdAndRoleId(userId, roleId, sourceId);
        //更新redis中该用户的角色
        userRoleService.saveCachedUserRoleMap(userId);

        return true;
    }

    @Override
    public long countUser() {
        PageUserRequest request = new PageUserRequest();
        request.setCurrentPage(1);
        request.setPageSize(1);
        return pageUser(request).getTotal();
    }

    /**
     * 判断用户是否已有某个角色
     *
     * @param userId
     * @param roleId
     * @param sourceId
     * @return
     */
    private boolean hasUserRole(String userId, String roleId, String sourceId) {
        QueryWrapper<UserRole> userRoleQueryWrapper = Wrappers.query();
        userRoleQueryWrapper.lambda().eq(UserRole::getUserId, userId);
        userRoleQueryWrapper.lambda().eq(UserRole::getRoleId, roleId);
        if (!StringUtils.isBlank(sourceId)) {
            userRoleQueryWrapper.lambda().eq(UserRole::getSource, sourceId);
        }
        List<UserRole> userRoleList = userRoleService.list(userRoleQueryWrapper);

        return !CollectionUtils.isEmpty(userRoleList);
    }

    /**
     * 判断是否还有用户具有系统管理员角色
     *
     * @param userOperate
     */
    private void checkSystemAdmin(UserOperateDto userOperate) {
        // 查看默认系统管理员的个数，即 roleId 为 ADMIN
        QueryWrapper<UserRole> userRoleQueryWrapper = Wrappers.query();
        userRoleQueryWrapper.lambda().eq(UserRole::getRoleId, RoleConstants.ROLE.ADMIN.name());
        long countAdmin = userRoleService.count(userRoleQueryWrapper);
        if (countAdmin < 2) {
            List<UserRole> userRoles = userRoleService.list(userRoleQueryWrapper);
            boolean anyMatch = userRoles.stream().anyMatch(userRole -> userRole.getUserId().equals(userOperate.getId()));
            boolean isContainAdmin = userOperate.getRoleInfoList().stream().anyMatch(roleInfo -> StringUtils.equals(roleInfo.getRoleId(), RoleConstants.ROLE.ADMIN.name()));
            if (anyMatch && !isContainAdmin) {
                throw new Fit2cloudException(ErrorCodeConstants.SYSTEM_NOT_HAVE_ADMIN.getCode(), ErrorCodeConstants.SYSTEM_NOT_HAVE_ADMIN.getMessage());
            }
        }
    }
}
