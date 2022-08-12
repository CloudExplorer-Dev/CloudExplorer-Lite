package com.fit2cloud.security;

import com.fit2cloud.dto.User;
import com.fit2cloud.dto.security.SecurityUser;
import com.fit2cloud.service.LoginService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import javax.annotation.Resource;
import java.util.ArrayList;


public class UserAuthDetailsService implements UserDetailsService {

    @Resource
    private LoginService loginService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = loginService.getUserByIdOrEmail(username);

        if (user == null) {
            throw new RuntimeException("用户不存在");
        }

        SecurityUser securityUser = new SecurityUser();
        securityUser.setCurrentUserInfo(user);
        //todo 设置权限
        securityUser.setPermissionValueList(new ArrayList<>());

        return securityUser;
    }
}
