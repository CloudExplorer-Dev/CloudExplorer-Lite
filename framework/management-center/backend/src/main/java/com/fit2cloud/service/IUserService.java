package com.fit2cloud.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fit2cloud.base.entity.User;
import com.fit2cloud.controller.request.user.CreateUserRequest;
import com.fit2cloud.controller.request.user.PageUserRequest;
import com.fit2cloud.controller.request.user.UpdateUserRequest;
import com.fit2cloud.controller.request.user.UserBatchAddRoleRequest;
import com.fit2cloud.dto.UserDto;
import com.fit2cloud.dto.UserNotifySettingDTO;
import com.fit2cloud.dto.UserOperateDto;

public interface IUserService extends IService<User> {
    IPage<UserDto> pageUser(PageUserRequest pageUserRequest);

    boolean createUser(CreateUserRequest request);

    boolean updateUser(UpdateUserRequest request);

    boolean updatePwd(User user);

    boolean deleteUser(String id);

    boolean changeUserStatus(UserDto userDto);

    UserOperateDto userRoleInfo(String userId);

    boolean updateUserNotification(UserNotifySettingDTO userNotificationSetting);

    UserNotifySettingDTO findUserNotification(String userId);

    Boolean addUserRole(UserBatchAddRoleRequest userBatchAddRoleRequest);

    long countUser();
}
