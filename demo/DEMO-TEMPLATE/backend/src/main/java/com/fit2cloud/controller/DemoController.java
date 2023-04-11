package com.fit2cloud.controller;

import com.fit2cloud.common.utils.CurrentUserUtils;
import com.fit2cloud.common.validator.group.ValidationGroup;
import com.fit2cloud.controller.handler.ResultHolder;
import com.fit2cloud.controller.request.DemoRequest;
import com.fit2cloud.dto.DemoObject;
import com.fit2cloud.dto.UserDto;
import com.fit2cloud.service.IDemoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/api/demo")
@Validated
@Api("Demo接口")
public class DemoController {

    @Resource
    private IDemoService demoService;

    @GetMapping("/currentUser")
    @ApiOperation(value = "获取当前用户信息", notes = "获取当前用户信息")
    @PreAuthorize("hasAnyCePermission('DEMO:READ')") //这里结合PermissionConstants设置接口权限
    public ResultHolder<UserDto> currentUser() {
        return ResultHolder.success(CurrentUserUtils.getUser());
    }

    @GetMapping("/demo")
    @ApiOperation(value = "demo", notes = "demo")
    @PreAuthorize("hasAnyCePermission('DEMO:READ')") //这里结合PermissionConstants设置接口权限
    public ResultHolder<DemoObject> demo() {
        return ResultHolder.success(demoService.getDemoObject());
    }


    @PostMapping("/demo")
    @ApiOperation(value = "demo", notes = "demo")
    @PreAuthorize("hasAnyCePermission('DEMO:READ')") //这里结合PermissionConstants设置接口权限
    public ResultHolder<DemoObject> setDemoObjectValue(@RequestBody
                                                       @Validated(ValidationGroup.UPDATE.class)
                                                       DemoRequest request) {
        return ResultHolder.success(demoService.setDemoObjectValue(request.getValue()));
    }


}
