package com.fit2cloud.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fit2cloud.base.entity.Role;
import com.fit2cloud.controller.handler.ResultHolder;
import com.fit2cloud.dto.UserDto;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class BaseRoleController {


    @GetMapping("roles")
    public ResultHolder<List<Role>> roles() {
        return ResultHolder.success(null);
    }

    @GetMapping("role/pages")
    public ResultHolder<IPage<Role>> pages() {
        return ResultHolder.success(null);
    }

}
