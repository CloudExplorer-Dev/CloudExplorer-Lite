package com.fit2cloud.gateway.controller;

import com.fit2cloud.common.utils.JwtTokenUtils;
import com.fit2cloud.controller.handler.ResultHolder;
import com.fit2cloud.request.LoginRequest;
import com.fit2cloud.service.LoginService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;

@RestController
@RequestMapping
public class LoginController {

    @Resource
    private LoginService loginService;

    @PostMapping("login")
    public Mono<ResponseEntity<ResultHolder<Object>>> login(@RequestBody LoginRequest loginRequest) {
        String token = null;
        try {
            token = loginService.login(loginRequest);
        } catch (Exception e) {
            return Mono.just(
                    ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                            .contentType(MediaType.APPLICATION_JSON)
                            .body(ResultHolder.error(null).code(HttpStatus.UNAUTHORIZED.value()).message(e.getMessage()))
            );
        }
        if (token != null) {
            return Mono.just(
                    ResponseEntity.ok()
                            .header(JwtTokenUtils.TOKEN_NAME, token)
                            .contentType(MediaType.APPLICATION_JSON)
                            .body(ResultHolder.success(null).message("登录成功"))
            );
        }
        return Mono.empty();
    }

    @GetMapping("logout")
    public Mono<String> logout() {
        return Mono.just("test");
    }

    @GetMapping("api/currentUser")
    public Mono<String> currentUser() {
        //

        return Mono.just("test");
    }


    @GetMapping("api/menus")
    public Mono<String> menus() throws Exception {
        //
        throw new RuntimeException("adsfasfaf");
        //return Mono.just("test");
    }


}
