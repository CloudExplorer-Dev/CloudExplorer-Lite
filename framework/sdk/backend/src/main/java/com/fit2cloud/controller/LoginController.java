package com.fit2cloud.controller;

import com.fit2cloud.common.utils.JwtTokenUtils;
import com.fit2cloud.controller.handler.ResultHolder;
import com.fit2cloud.request.LoginRequest;
import com.fit2cloud.service.LoginService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping
public class LoginController {

    @Resource
    private LoginService loginService;

    @PostMapping("login")
    public ResultHolder<Object> login(@RequestBody LoginRequest loginRequest, HttpServletResponse httpResponse) {
        String token = loginService.login(loginRequest);

        //认证成功在header中添加token返回
        httpResponse.setHeader(JwtTokenUtils.TOKEN_NAME, token);

        return ResultHolder.success(null).message("登录成功");

    }


}
