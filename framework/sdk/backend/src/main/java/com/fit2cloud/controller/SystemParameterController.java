package com.fit2cloud.controller;

import com.fit2cloud.base.service.IBaseSystemParameterService;
import com.fit2cloud.controller.handler.ResultHolder;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/api/system_parameter")
@Api("基础参数接口")
public class SystemParameterController {
    @Resource
    private IBaseSystemParameterService baseSystemParameterService;

    @GetMapping("/findHelpLink")
    @ApiOperation(value = "帮助链接", notes = "帮助链接")
    public ResultHolder<Object> findHelpLink() {
        return ResultHolder.success(baseSystemParameterService.getValue("help.link"));
    }
}
