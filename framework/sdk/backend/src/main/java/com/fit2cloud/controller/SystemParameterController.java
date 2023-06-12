package com.fit2cloud.controller;

import com.fit2cloud.base.service.IBaseSystemParameterService;
import com.fit2cloud.controller.handler.ResultHolder;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.annotation.Resource;

@RestController
@RequestMapping("/api/system_parameter")
@Tag(name = "基础参数接口")
public class SystemParameterController {
    @Resource
    private IBaseSystemParameterService baseSystemParameterService;

    @GetMapping("/findHelpLink")
    @Operation(summary = "帮助链接", description = "帮助链接")
    public ResultHolder<Object> findHelpLink() {
        return ResultHolder.success(baseSystemParameterService.getValue("help.link"));
    }
}
