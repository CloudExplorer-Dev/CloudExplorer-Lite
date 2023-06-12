package com.fit2cloud.controller;


import com.fit2cloud.controller.handler.ResultHolder;
import com.fit2cloud.dto.module.Menu;
import com.fit2cloud.service.MenuService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.annotation.Resource;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class MenuController {

    @Resource
    private MenuService menuService;

    @GetMapping("menus")
    public ResultHolder<Map<String, List<Menu>>> modules() {
        return ResultHolder.success(menuService.getAvailableMenus());
    }

}
