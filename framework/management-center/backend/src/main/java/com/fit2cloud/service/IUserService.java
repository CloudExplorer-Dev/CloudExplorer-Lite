package com.fit2cloud.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fit2cloud.base.entity.User;
import com.fit2cloud.controller.request.user.*;
import com.fit2cloud.dto.UserDto;
import com.fit2cloud.dto.UserNotifySettingDTO;
import com.fit2cloud.dto.UserOperateDto;

import java.util.List;

public interface IUserService extends IService<User> {
    IPage<UserDto> pageUser(PageUserRequest pageUserRequest);

    /**
     * 管理员/组织管理员获取可管理的用户列表
     * @return
     */
    List<User> getManageUserSimpleList(List<String> userIds);

    UserDto getUser(String userId);

    boolean createUser(CreateUserRequest request);

    boolean updateUser(UpdateUserRequest request);

    boolean updatePwd(User user);

    boolean deleteUser(String id);

    boolean changeUserStatus(UserDto userDto);

    UserOperateDto userRoleInfo(String userId);

    boolean updateUserNotification(UserNotifySettingDTO userNotificationSetting);

    UserNotifySettingDTO findUserNotification(String userId);

    /**
     * 为指定用户添加角色关联关系
     * @param userBatchAddRoleRequest
     * @return
     */
    int addUserRole(UserBatchAddRoleRequest userBatchAddRoleRequest);

    /**
     * 为指定角色添加用户关联关系
     * @param userBatchAddRoleRequest
     * @return
     */
    int addUserRoleV2(UserBatchAddRoleRequestV2 userBatchAddRoleRequest);

    /**
     * 为指定source添加用户角色关联关系
     * @param userBatchAddRoleRequest
     * @return
     */
    int addUserRoleV3(UserBatchAddRoleRequestV3 userBatchAddRoleRequest);

    boolean removeUserRole(String userId, String roleId, String sourceId);

    long countUser();

}
