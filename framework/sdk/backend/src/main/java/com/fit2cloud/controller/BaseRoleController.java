package com.fit2cloud.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fit2cloud.base.entity.Role;
import com.fit2cloud.base.service.IBaseRoleService;
import com.fit2cloud.controller.editor.OrderEditor;
import com.fit2cloud.controller.handler.ResultHolder;
import com.fit2cloud.request.pub.OrderRequest;
import com.fit2cloud.request.role.RolePageRequest;
import com.fit2cloud.request.role.RoleRequest;
import io.swagger.annotations.ApiParam;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/api")
public class BaseRoleController {

    @Resource
    private IBaseRoleService roleService;

    @GetMapping("role")
    public ResultHolder<Role> role(
            @ApiParam("角色ID")
            @RequestParam("id")
            String id
    ) {
        return ResultHolder.success(roleService.getById(id));
    }

    @GetMapping("roles")
    public ResultHolder<List<Role>> roles(@Validated RoleRequest roleRequest) {
        return ResultHolder.success(roleService.roles(roleRequest));
    }

    @GetMapping("role/pages")
    public ResultHolder<IPage<Role>> pages(@Validated RolePageRequest roleRequest) {
        return ResultHolder.success(roleService.pages(roleRequest));
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(OrderRequest.class, new OrderEditor());
    }
}
