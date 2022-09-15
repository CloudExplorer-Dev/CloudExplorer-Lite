package com.fit2cloud.security;

import com.fit2cloud.base.service.IBaseUserRoleService;
import com.fit2cloud.security.filter.JwtTokenAuthFilter;
import com.fit2cloud.security.filter.UserLoginFilter;
import com.fit2cloud.service.BasePermissionService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;

public class SecurityDSL extends AbstractHttpConfigurer<SecurityDSL, HttpSecurity> {

    private static BasePermissionService basePermissionService;

    private static IBaseUserRoleService userRoleService;

    @Override
    public void configure(HttpSecurity http) throws Exception {
        //只能在这获取AuthenticationManager
        AuthenticationManager authenticationManager = http.getSharedObject(AuthenticationManager.class);
        http.addFilter(new UserLoginFilter(authenticationManager));
        http.addFilter(new JwtTokenAuthFilter(authenticationManager, basePermissionService, userRoleService));
    }

    public static SecurityDSL securityDSL(BasePermissionService basePermissionService, IBaseUserRoleService userRoleService) {
        SecurityDSL dsl = new SecurityDSL();
        SecurityDSL.basePermissionService = basePermissionService;
        SecurityDSL.userRoleService = userRoleService;
        return dsl;
    }

}
