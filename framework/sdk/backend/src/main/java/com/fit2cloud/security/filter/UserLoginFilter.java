package com.fit2cloud.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fit2cloud.common.utils.JwtTokenUtils;
import com.fit2cloud.common.utils.SpringUtil;
import com.fit2cloud.controller.handler.ResultHolder;
import com.fit2cloud.dto.UserDto;
import com.fit2cloud.dto.security.SecurityUser;
import com.fit2cloud.service.TokenPoolService;
import org.apache.commons.collections4.KeyValue;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class UserLoginFilter extends UsernamePasswordAuthenticationFilter {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public UserLoginFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    /**
     * 登录验证，走/login （POST）
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        //return super.attemptAuthentication(request, response);
        UserDto userDto = null;
        try {
            userDto = new ObjectMapper().readValue(request.getInputStream(), UserDto.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        //无session模式，登录接口没必要返回权限，交给jwt token认证即可
        return getAuthenticationManager().authenticate(new UsernamePasswordAuthenticationToken(userDto.getUsername(), userDto.getPassword(), new ArrayList<>()));
    }

    /**
     * 验证成功后操作
     */
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        SecurityUser securityUser = (SecurityUser) authResult.getPrincipal();

        KeyValue<String, String> jwt = JwtTokenUtils.createJwtToken(securityUser.getCurrentUserInfoDto());

        SpringUtil.getBean(TokenPoolService.class).saveJwt(securityUser.getCurrentUserInfoDto().getId(), jwt.getKey());

        //将token放入header
        response.setHeader(JwtTokenUtils.TOKEN_NAME, jwt.getValue());

        //返回登录成功json
        response.setStatus(HttpStatus.OK.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        PrintWriter writer = response.getWriter();
        objectMapper.writeValue(writer, ResultHolder.success(null).message("登录成功"));

    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        //super.unsuccessfulAuthentication(request, response, failed);
        //todo 内置的message自定义？

        response.sendError(HttpStatus.UNAUTHORIZED.value(), failed.getMessage());

    }
}
