package com.fit2cloud.controller;


import com.fit2cloud.base.entity.User;
import com.fit2cloud.base.service.IBaseUserService;
import com.fit2cloud.controller.handler.ResultHolder;
import com.fit2cloud.dto.UserDto;
import com.fit2cloud.request.user.EditUserRequest;
import com.fit2cloud.request.user.ResetPwdRequest;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class BaseUserController {
    @Resource
    private IBaseUserService userService;

    @GetMapping("user/current")
    public ResultHolder<UserDto> currentUser() {
        UserDto userDto = (UserDto) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.getById(userDto.getId());
        BeanUtils.copyProperties(user, userDto);
        userDto.setPassword(null);
        return ResultHolder.success(userDto);
    }

    @PutMapping("user/current")
    public ResultHolder<Boolean> updateCurrentUser(@RequestBody @Validated EditUserRequest request) {
        return ResultHolder.success(userService.updateUserBasicInfo(request));
    }

    @PutMapping("user/current/pwd")
    public ResultHolder<Boolean> resetPwd(@RequestBody @Validated ResetPwdRequest request) {
        return ResultHolder.success(userService.resetPwd(request));
    }
}
