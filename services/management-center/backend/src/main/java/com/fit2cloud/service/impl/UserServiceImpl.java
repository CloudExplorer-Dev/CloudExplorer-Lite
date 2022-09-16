package com.fit2cloud.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fit2cloud.base.entity.Role;
import com.fit2cloud.base.entity.User;
import com.fit2cloud.base.entity.UserRole;
import com.fit2cloud.common.constants.RoleConstants;
import com.fit2cloud.common.constants.SystemUserConstants;
import com.fit2cloud.common.exception.Fit2cloudException;
import com.fit2cloud.common.utils.CurrentUserUtils;
import com.fit2cloud.common.utils.MD5Util;
import com.fit2cloud.constants.ErrorCodeConstants;
import com.fit2cloud.controller.request.user.CreateUserRequest;
import com.fit2cloud.controller.request.user.PageUserRequest;
import com.fit2cloud.controller.request.user.UpdateUserRequest;
import com.fit2cloud.controller.request.user.UserBatchAddRoleRequest;
import com.fit2cloud.dao.entity.UserNotificationSetting;
import com.fit2cloud.dao.mapper.UserMapper;
import com.fit2cloud.dto.*;
import com.fit2cloud.service.IUserService;
import com.fit2cloud.service.OrganizationCommonService;
import com.fit2cloud.service.WorkspaceCommonService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;

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
    RoleServiceImpl roleServiceImpl;

    @Resource
    BaseMapper<UserRole> userRoleMapper;

    @Resource
    BaseMapper<Role> roleMapper;

    @Resource
    BaseMapper<UserNotificationSetting> userNotificationSettingMapper;

    @Resource
    OrganizationCommonService organizationCommonService;

    @Resource
    WorkspaceCommonService workspaceCommonService;

    @Override
    public IPage<UserDto> pageUser(PageUserRequest pageUserRequest) {
        Page<User> page = new Page<>(pageUserRequest.getCurrentPage(), pageUserRequest.getPageSize());
        page.setOrders(new ArrayList<>() {{
            add(pageUserRequest.getOrder());
        }});

        QueryWrapper<User> wrapper = new QueryWrapper<>();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        if (StringUtils.isNotEmpty(pageUserRequest.getUsername())) {
            wrapper.like(true, "username", pageUserRequest.getUsername());
        }
        if (StringUtils.isNotEmpty(pageUserRequest.getName())) {
            wrapper.like(true, "user._name", pageUserRequest.getName());
        }
        if (StringUtils.isNotEmpty(pageUserRequest.getEmail())) {
            wrapper.like(true, "email", pageUserRequest.getEmail());
        }
        if (StringUtils.isNotEmpty(pageUserRequest.getRoleId())) {
            wrapper.eq(true, "role_id", pageUserRequest.getRoleId());
        }
        if (StringUtils.isNotEmpty(pageUserRequest.getRoleName())) {
            wrapper.like(true, "role._name", pageUserRequest.getRoleName());
        }
        if (org.apache.commons.collections4.CollectionUtils.isNotEmpty(pageUserRequest.getUpdateTime())) {
            wrapper.between("user.update_time", simpleDateFormat.format(pageUserRequest.getUpdateTime().get(0)), simpleDateFormat.format(pageUserRequest.getUpdateTime().get(1)));
        }
        if (org.apache.commons.collections4.CollectionUtils.isNotEmpty(pageUserRequest.getCreateTime())) {
            wrapper.between("user.create_time", simpleDateFormat.format(pageUserRequest.getCreateTime().get(0)), simpleDateFormat.format(pageUserRequest.getCreateTime().get(1)));
        }

        IPage<User> userIPage = baseMapper.pageUser(page, wrapper);

        Map<String, Object> param = new HashMap<>();
        return userIPage.convert(user -> {
            param.put("userId", user.getId());
            UserDto userDto = new UserDto();
            userDto.setRoles(roleServiceImpl.getRolesByResourceIds(param));
            BeanUtils.copyProperties(user, userDto);
            return userDto;
        });
    }

    @Override
    public boolean deleteUser(String userId) {
        String currentLoginUserId = CurrentUserUtils.getUser().getId();

        User user = baseMapper.selectById(userId);
        if (user == null) {
            throw new Fit2cloudException(ErrorCodeConstants.USER_NOT_EXIST.getCode(), ErrorCodeConstants.USER_CAN_NOT_DELETE.getMessage());
        }

        if (StringUtils.equalsIgnoreCase(user.getUsername(), currentLoginUserId)) {
            throw new Fit2cloudException(ErrorCodeConstants.USER_CAN_NOT_DELETE.getCode(), ErrorCodeConstants.USER_CAN_NOT_DELETE.getMessage());
        }

        // TODO 增加删除用户的操作日志
        System.out.println(user.getUsername() + "is deleted by" + currentLoginUserId);

        // 当前登录用户以组织管理员的角色删除用户
        if (CurrentUserUtils.isOrgAdmin()) {
            // 当为组织管理员时，删除当前组织及其子组织下的工作空间（解绑关系user_role）
            List<String> sourceList = new ArrayList<>();

            // 获取当前组织及其所有子组织ID
            List<String> subOrgIds = organizationCommonService.getOrgIdsByPid(CurrentUserUtils.getOrganizationId());
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
            userRoleWrapper.lambda().eq(true, UserRole::getId, userId).in(true, UserRole::getSource, sourceList);
            userRoleMapper.delete(userRoleWrapper);

            // 查询该用户具有的所有角色，如果为空，则删除用户
            List<RoleInfo> userRoleList = roleInfo(currentLoginUserId, false);
            if (CollectionUtils.isEmpty(userRoleList)) {
                baseMapper.deleteById(userId);
            }
        }

        // 当前登录用户以系统管理员的角色删除用户
        if (CurrentUserUtils.isAdmin()) {
            // 删除用户
            baseMapper.deleteById(userId);

            // 删除用户的角色关系
            QueryWrapper<UserRole> userRoleWrapper = Wrappers.query();
            userRoleWrapper.eq(true, "user_id", userId);
            userRoleMapper.delete(userRoleWrapper);
        }
        return true;
    }

    public boolean changeUserStatus(UserDto userDto) {
        User userUpdate = new User();
        userUpdate.setId(userDto.getId());
        userUpdate.setEnabled(userDto.getEnabled());
        if (userDto.getRoles().stream().map(Role::getId).anyMatch(s -> s.equals(RoleConstants.ROLE.ADMIN.name())) && !userDto.getEnabled()) {
            Long countAdmin = baseMapper.countActiveUsers(RoleConstants.ROLE.ADMIN.name());
            if (countAdmin > 1) {
                baseMapper.updateById(userUpdate);
            } else {
                throw new Fit2cloudException(ErrorCodeConstants.USER_KEEP_ONE_ADMIN.getCode(), ErrorCodeConstants.USER_KEEP_ONE_ADMIN.getMessage());
            }
        } else {
            baseMapper.updateById(userUpdate);
        }
        return true;
    }

    @Transactional
    public boolean createUser(CreateUserRequest request) {
        validateUserID(request.getUsername());
        validateUserDataExist("username",request.getUsername(),"用户ID","create", request.getId());
        validateUserDataExist("email",request.getEmail(),"用户邮箱","create", request.getId());

        UserOperateDto user = new UserOperateDto();
        BeanUtils.copyProperties(request, user);

        if (StringUtils.isBlank(user.getId())) {
            user.setId(UUID.randomUUID().toString());
        }
        if (StringUtils.isBlank(user.getSource())) {
            user.setSource("unknown");
        }
        user.setPassword(MD5Util.md5(user.getPassword()));
        baseMapper.insert(user);

        if (CollectionUtils.isNotEmpty(user.getRoleInfoList())) {
            insertUserRoleInfo(user);
        }
        return true;
    }

    @Transactional
    public boolean updateUser(UpdateUserRequest request) {
        // 校验用户邮箱是否已存在
        validateUserDataExist("email", request.getEmail(), "邮箱", "update", request.getId());

        UserOperateDto user = new UserOperateDto();
        BeanUtils.copyProperties(request, user);
        baseMapper.updateById(user);
        QueryWrapper<UserRole> wrapper = Wrappers.query();
        wrapper.lambda().eq(true, UserRole::getUserId, request.getId());

        if (CollectionUtils.isNotEmpty(user.getRoleInfoList())) {
            if (CurrentUserUtils.isAdmin()) {
                // 校验系统是否还有系统管理员的角色，如果没有不允许本次修改
                checkSystemAdmin(user);
                // 删除要编辑的用户在 user_role 的信息，然后 reinsert
                userRoleMapper.delete(wrapper);
            }

            if (CurrentUserUtils.isOrgAdmin()) {
                // 删除要编辑的用户在当前组织下的 user_role 的信息，然后 reinsert
                List<String> list = new ArrayList<>();
                List<String> orgIds = organizationCommonService.getOrgIdsByPid(CurrentUserUtils.getOrganizationId());
                list.addAll(orgIds);
                List<String> workspaceIds = workspaceCommonService.getWorkspaceIdsByOrgIds(list);
                if (CollectionUtils.isNotEmpty(workspaceIds)) {
                    list.addAll(workspaceIds);
                }
                wrapper.lambda().in(true, UserRole::getSource, list);
                userRoleMapper.delete(wrapper);
            }

            insertUserRoleInfo(user);
        }
        return true;
    }

    public boolean updatePwd(User user) {
        // 非本地创建用户不允许修改密码
        if (!"local".equalsIgnoreCase(CurrentUserUtils.getUser().getSource())) {
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
        List roleInfos = roleInfo(userId, true);

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
            List<String> orgIds = organizationCommonService.getOrgIdsByPid(CurrentUserUtils.getOrganizationId());
            List<String> resourceIds = workspaceCommonService.getWorkspaceIdsByOrgIds(orgIds);
            resourceIds.addAll(orgIds);
            param.put("resourceIds", resourceIds);
        }
        return baseMapper.roleInfo(param);
    }

    private void insertUserRoleInfo(UserRole userRole, String sourceId) {
        userRole.setId(UUID.randomUUID().toString());
        userRole.setSource(sourceId);
        userRoleMapper.insert(userRole);
    }

    public RoleConstants.ROLE getParentRoleId(String roleId) {
        Role role = roleMapper.selectById(roleId);
        return role.getParentRoleId();
    }

    /**
     * 校验用户ID
     * @param userID
     */
    private void validateUserID(String userID) {
        if (SystemUserConstants.getUserName().equalsIgnoreCase(userID)) {
            throw new Fit2cloudException(ErrorCodeConstants.USER_ID_CANNOT_BE_SYSTEM.getCode(), ErrorCodeConstants.USER_ID_CANNOT_BE_SYSTEM.getMessage());
        }
    }

    private void validateUserDataExist(String colName, String colValue, String colDisplayName, String optType, String id) {
        QueryWrapper<User> wrapper = Wrappers.query();
        wrapper.eq(true, colName, colValue);
        if ("create".equals(optType) && baseMapper.selectCount(wrapper) > 0) {
            throw new RuntimeException(colDisplayName + "已存在");
        }
        if ("update".equals(optType)) {
            wrapper.ne(true, "id", id);
            if (baseMapper.selectCount(wrapper) > 0) {
                throw new RuntimeException(colDisplayName + "已存在");
            }
        }
    }

    public boolean updateUserNotification(UserNotifySettingDTO userNotificationSetting) {
        // 更新用户表的邮箱和手机号
        User user = baseMapper.selectById(userNotificationSetting.getId());
        user.setEmail(userNotificationSetting.getEmail());
        user.setPhone(userNotificationSetting.getPhone());
        baseMapper.updateById(user);

        // 删除企业微信账号记录
        userNotificationSettingMapper.deleteById(userNotificationSetting.getId());

        // 重新插入企业微信账号记录
        UserNotificationSetting notificationSetting = new UserNotificationSetting();
        notificationSetting.setUserId(userNotificationSetting.getId());
        notificationSetting.setWechatAccount(userNotificationSetting.getWechatAccount());
        userNotificationSettingMapper.insert(notificationSetting);
        return true;
    }

    public UserNotifySettingDTO findUserNotification(String userId) {
        UserNotifySettingDTO notificationDTO = new UserNotifySettingDTO();
        User user = baseMapper.selectById(userId);
        UserNotificationSetting userNotificationSetting = userNotificationSettingMapper.selectById(userId);
        notificationDTO.setId(userId);
        notificationDTO.setEmail(user.getEmail());
        notificationDTO.setPhone(user.getPhone());
        if (userNotificationSetting != null) {
            notificationDTO.setWechatAccount(userNotificationSetting.getWechatAccount());
        }
        return notificationDTO;
    }

    @Transactional
    public Boolean addUserRole(UserBatchAddRoleRequest userBatchAddRoleRequest) {
        userBatchAddRoleRequest.getUserIdList().forEach(userId -> {
            userBatchAddRoleRequest.getRoleInfoList().forEach(roleInfo -> {
                UserRole userRole = new UserRole();
                userRole.setRoleId(roleInfo.getRoleId());
                userRole.setUserId(userId);
                RoleConstants.ROLE parentRoleId = getParentRoleId(roleInfo.getRoleId());
                if (RoleConstants.ROLE.USER.equals(parentRoleId)) {
                    roleInfo.getWorkspaceIds().forEach(workspaceId -> {
                                if (!hasUserRole(userId, roleInfo.getRoleId(), workspaceId)) {
                                    insertUserRoleInfo(userRole, workspaceId);
                                }
                            }
                    );
                }
                if (RoleConstants.ROLE.ORGADMIN.equals(parentRoleId)) {
                    roleInfo.getOrganizationIds().forEach(organizationId -> {
                                if (!hasUserRole(userId, roleInfo.getRoleId(), organizationId)) {
                                    insertUserRoleInfo(userRole, organizationId);
                                }
                            }
                    );
                }
                if (RoleConstants.ROLE.ADMIN.equals(parentRoleId)) {
                    if (!hasUserRole(userId, roleInfo.getRoleId(), null)) {
                        insertUserRoleInfo(userRole, null);
                    }
                }
            });
        });
        return true;
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
        List<UserRole> userRoleList = userRoleMapper.selectList(userRoleQueryWrapper);

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
        long countAdmin = userRoleMapper.selectCount(userRoleQueryWrapper);
        if (countAdmin < 2) {
            List<UserRole> userRoles = userRoleMapper.selectList(userRoleQueryWrapper);
            boolean anyMatch = userRoles.stream().anyMatch(userRole -> userRole.getUserId().equals(userOperate.getId()));
            boolean isContainAdmin = userOperate.getRoleInfoList().stream().anyMatch(roleInfo -> StringUtils.equals(roleInfo.getRoleId(), RoleConstants.ROLE.ADMIN.name()));
            if (anyMatch && !isContainAdmin) {
                throw new Fit2cloudException(ErrorCodeConstants.SYSTEM_NOT_HAVE_ADMIN.getCode(), ErrorCodeConstants.SYSTEM_NOT_HAVE_ADMIN.getMessage());
            }
        }
    }
}
