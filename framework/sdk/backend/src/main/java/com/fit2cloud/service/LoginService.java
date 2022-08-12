package com.fit2cloud.service;

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

        User user = getUserByIdOrEmail(loginRequest.getUsername());
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }

        if (!checkPassword(user, loginRequest.getPassword())) {
            throw new RuntimeException("密码错误");
        }

        return JwtTokenUtils.createJwtToken(user);
    }

    public User getUserByIdOrEmail(String username) {
        User user = getUserById(username);
        if (user == null) {
            user = getUserByEmail(username);
        }

        return user;
    }


    //todo
    public User getUserById(String userId) {

        if(!userId.equals("admin")){
            return null;
        }
        User user = new User();
        user.setId(userId);
        user.setUsername("admin");
        user.setEmail("admin@email.com");
        //user.setPassword("password");
        user.setPassword("5f4dcc3b5aa765d61d8327deb882cf99");
        user.setPhone("11111111111");
        user.setEnabled(true);
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
