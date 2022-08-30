package com.fit2cloud.base.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fit2cloud.base.entity.User;
import com.fit2cloud.dto.UserDto;
import com.fit2cloud.request.LoginRequest;

/**
 * <p>
 * 用户 服务类
 * </p>
 *
 * @author fit2cloud
 * @since 
 */
public interface IBaseUserService extends IService<User> {

    String login(LoginRequest loginRequest);

    UserDto getUserByIdOrEmail(String username);

    boolean checkPassword(User user, String password);

}
