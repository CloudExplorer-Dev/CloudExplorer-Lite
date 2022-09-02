package com.fit2cloud.controller;


import com.fit2cloud.controller.handler.ResultHolder;
import com.fit2cloud.dto.UserDto;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
public class BaseUserController {

    @GetMapping("current")
    public ResultHolder<UserDto> currentUser() {
        return ResultHolder.success((UserDto) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
    }

}
