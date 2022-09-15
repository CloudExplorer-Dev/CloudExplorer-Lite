package com.fit2cloud.controller;


import com.fit2cloud.controller.handler.ResultHolder;
import com.fit2cloud.dto.UserDto;
import com.fit2cloud.service.BasePermissionService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Set;

@RestController
@RequestMapping("/api")
public class PermissionController {

    @Resource
    private BasePermissionService basePermissionService;

    @GetMapping("permission/current")
    public ResultHolder<Set<String>> getCurrentUserPermissionSet() {
        UserDto user = (UserDto) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ResultHolder.success(basePermissionService.getPlainPermissions(user.getId(), user.getCurrentRole(), user.getCurrentSource()));
    }

}
