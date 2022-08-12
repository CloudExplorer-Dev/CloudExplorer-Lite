package com.fit2cloud.controller;

import com.fit2cloud.service.LoginService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping
public class LoginController {

    @Resource
    private LoginService loginService;

    /*@PostMapping("login")
    public ResultHolder<Object> login(@RequestBody LoginRequest loginRequest, HttpServletResponse httpResponse) {
        String token = loginService.login(loginRequest);

        //认证成功在header中添加token返回
        httpResponse.setHeader(JwtTokenUtils.TOKEN_NAME, token);

        return ResultHolder.success(null).message("登录成功");

    }*/


}
