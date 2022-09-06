package com.fit2cloud.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fit2cloud.base.entity.User;
import com.fit2cloud.dto.RoleInfo;
import com.fit2cloud.dto.UserDto;
import com.fit2cloud.request.CreateUserRequest;
import com.fit2cloud.request.PageUserRequest;

import java.util.List;

public interface IUserService extends IService<User> {
    IPage<UserDto> pageUser(PageUserRequest pageUserRequest);

    boolean createUser(CreateUserRequest request);

    boolean deleteUser(String id);

    public List<RoleInfo> roleInfo(String userId);

    }
