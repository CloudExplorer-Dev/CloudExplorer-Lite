package com.fit2cloud.gateway.service;

import com.fit2cloud.common.utils.JwtTokenUtils;
import com.fit2cloud.dto.User;
import com.fit2cloud.request.LoginRequest;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

@Service
public class LoginService {

    public String login(LoginRequest loginRequest) {

        if (StringUtils.isBlank(loginRequest.getUsername())) {
            throw new RuntimeException("用户名为空");
        }

        if (StringUtils.isBlank(loginRequest.getPassword())) {
            throw new RuntimeException("密码为空");
        }

        User user = getUserById(loginRequest.getUsername());
        if (user == null) {
            user = getUserByEmail(loginRequest.getUsername());
        }
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }

        if (!checkPassword(user, loginRequest.getPassword())) {
            throw new RuntimeException("密码错误");
        }

        return JwtTokenUtils.createJwtToken(user);
    }


    //todo
    public User getUserById(String userId) {
        User user = new User();
        user.setId("admin");
        user.setUsername("管理员");
        user.setEmail("admin@email.com");
        //user.setPassword("password");
        user.setPhone("11111111111");
        return user;
    }

    //todo
    public User getUserByEmail(String email) {
        return null;
    }

    //todo
    public boolean checkPassword(User user, String password) {

        return true;
    }

}
