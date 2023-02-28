package com.fit2cloud.controller;

import com.fit2cloud.controller.handler.ResultHolder;
import com.fit2cloud.dao.entity.ExtraModule;
import com.fit2cloud.dao.entity.ExtraModules;
import com.fit2cloud.service.IModuleManageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

@RestController
@RequestMapping("/api/module_manage")
@Api("模块管理")
public class ModuleManageController {

    @Resource
    private IModuleManageService iModuleManageService;

    @GetMapping("list")
    @ApiOperation(value = "查询模块", notes = "查询模块")
    @PreAuthorize("hasAnyCePermission('MODULE_MANAGE:READ')")
    public ResultHolder<ExtraModules> list() {
        return ResultHolder.success(iModuleManageService.list());
    }


    @PostMapping("install")
    @ApiOperation(value = "安装/升级模块", notes = "安装/升级模块")
    @PreAuthorize("hasAnyCePermission('MODULE_MANAGE:EDIT')")
    public ResultHolder<Object> install(@RequestBody ExtraModule module) {
        iModuleManageService.install(module.getDownloadUrl());
        return ResultHolder.success(null);
    }

    @GetMapping("uninstall/{module}")
    @ApiOperation(value = "卸载模块", notes = "卸载模块")
    @PreAuthorize("hasAnyCePermission('MODULE_MANAGE:EDIT')")
    public ResultHolder<Object> uninstall(@PathVariable String module) {
        iModuleManageService.uninstall(module);
        return ResultHolder.success(null);
    }

    @GetMapping("start/{module}")
    @ApiOperation(value = "启动模块", notes = "启动模块")
    @PreAuthorize("hasAnyCePermission('MODULE_MANAGE:EDIT')")
    public ResultHolder<Object> start(@PathVariable String module) {
        iModuleManageService.start(module);
        return ResultHolder.success(null);
    }

    @GetMapping("stop/{module}")
    @ApiOperation(value = "停止模块", notes = "停止模块")
    @PreAuthorize("hasAnyCePermission('MODULE_MANAGE:EDIT')")
    public ResultHolder<Object> stop(@PathVariable String module) {
        iModuleManageService.stop(module);
        return ResultHolder.success(null);
    }

    @ResponseBody
    @RequestMapping("upload")
    public ResultHolder<Boolean> uploadFile(@RequestParam("file") MultipartFile file) {
        iModuleManageService.upload(file);
        return ResultHolder.success(true);
    }


}
