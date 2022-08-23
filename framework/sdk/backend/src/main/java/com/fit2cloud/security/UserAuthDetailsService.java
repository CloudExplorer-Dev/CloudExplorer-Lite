package com.fit2cloud.security;

import com.fit2cloud.base.service.IUserService;
import com.fit2cloud.dto.UserDto;
import com.fit2cloud.dto.security.SecurityUser;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import javax.annotation.Resource;
import java.util.ArrayList;


public class UserAuthDetailsService implements UserDetailsService {

    @Resource
    private IUserService loginService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        UserDto userDto = loginService.getUserByIdOrEmail(username);

        if (userDto == null) {
            throw new RuntimeException("用户不存在");
        }

        SecurityUser securityUser = new SecurityUser();
        securityUser.setCurrentUserInfoDto(userDto);

        //无session模式，登录接口没必要返回权限，交给jwt token认证即可
        securityUser.setPermissionValueList(new ArrayList<>());

        return securityUser;
    }
}
