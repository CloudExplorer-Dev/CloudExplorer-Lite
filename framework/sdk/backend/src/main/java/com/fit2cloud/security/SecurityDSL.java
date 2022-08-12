package com.fit2cloud.security;

import com.fit2cloud.security.filter.TwtTokenAuthFilter;
import com.fit2cloud.security.filter.UserLoginFilter;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;

public class SecurityDSL extends AbstractHttpConfigurer<SecurityDSL, HttpSecurity> {

    @Override
    public void configure(HttpSecurity http) throws Exception {
        //只能在这获取AuthenticationManager
        AuthenticationManager authenticationManager = http.getSharedObject(AuthenticationManager.class);
        http.addFilter(new UserLoginFilter(authenticationManager));
        http.addFilter(new TwtTokenAuthFilter(authenticationManager));
    }

    public static SecurityDSL securityDSL(){
        return new SecurityDSL();
    }

}
