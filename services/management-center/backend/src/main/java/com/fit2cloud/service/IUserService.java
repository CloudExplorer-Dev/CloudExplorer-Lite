package com.fit2cloud.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fit2cloud.base.entity.User;
import com.fit2cloud.dto.UserDto;
import com.fit2cloud.dto.UserOperateDto;
import com.fit2cloud.request.CreateUserRequest;
import com.fit2cloud.request.PageUserRequest;

public interface IUserService extends IService<User> {
    IPage<UserDto> pageUser(PageUserRequest pageUserRequest);

    boolean createUser(CreateUserRequest request);

    boolean updateUser(CreateUserRequest request);

    boolean updatePwd(User user);

    boolean deleteUser(String id);

    UserOperateDto userRoleInfo(String userId);

    }
