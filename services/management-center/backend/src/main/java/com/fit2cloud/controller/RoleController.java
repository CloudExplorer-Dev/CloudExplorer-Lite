package com.fit2cloud.controller;

import com.fit2cloud.base.entity.Role;
import com.fit2cloud.controller.handler.ResultHolder;
import com.fit2cloud.service.IRoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * Author: LiuDi
 * Date: 2022/9/1 10:25 AM
 */
@RestController
@RequestMapping("/api/role")
@Api("角色相关接口")
public class RoleController {

    @Resource
    IRoleService roleService;

    @ApiOperation(value = "查询所有角色")
    @GetMapping("/current")
    public ResultHolder<List<Role>> currentUserRoles() {
        return ResultHolder.success(roleService.currentUserRoles());
    }


}
