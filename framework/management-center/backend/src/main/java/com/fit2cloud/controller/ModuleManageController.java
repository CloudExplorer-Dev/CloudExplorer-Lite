package com.fit2cloud.controller;

import com.fit2cloud.controller.handler.ResultHolder;
import com.fit2cloud.dao.entity.ExtraModule;
import com.fit2cloud.dao.entity.ExtraModules;
import com.fit2cloud.service.IModuleManageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.annotation.Resource;

@RestController
@RequestMapping("/api/module_manage")
@Tag(name = "模块管理")
public class ModuleManageController {

    @Resource
    private IModuleManageService iModuleManageService;

    @GetMapping("list")
    @Operation(summary = "查询模块", description = "查询模块")
    @PreAuthorize("@cepc.hasAnyCePermission('MODULE_MANAGE:READ')")
    public ResultHolder<ExtraModules> list() {
        return ResultHolder.success(iModuleManageService.list());
    }


    @PostMapping("install")
    @Operation(summary = "安装/升级模块", description = "安装/升级模块")
    @PreAuthorize("@cepc.hasAnyCePermission('MODULE_MANAGE:EDIT')")
    public ResultHolder<Object> install(@RequestBody ExtraModule module) {
        iModuleManageService.install(module.getName(), module.getVersion());
        return ResultHolder.success(null);
    }

    @GetMapping("uninstall/{module}")
    @Operation(summary = "卸载模块", description = "卸载模块")
    @PreAuthorize("@cepc.hasAnyCePermission('MODULE_MANAGE:EDIT')")
    public ResultHolder<Object> uninstall(@PathVariable String module) {
        iModuleManageService.uninstall(module);
        return ResultHolder.success(null);
    }

    @GetMapping("start/{module}")
    @Operation(summary = "启动模块", description = "启动模块")
    @PreAuthorize("@cepc.hasAnyCePermission('MODULE_MANAGE:EDIT')")
    public ResultHolder<Object> start(@PathVariable String module) {
        iModuleManageService.start(module);
        return ResultHolder.success(null);
    }

    @GetMapping("stop/{module}")
    @Operation(summary = "停止模块", description = "停止模块")
    @PreAuthorize("@cepc.hasAnyCePermission('MODULE_MANAGE:EDIT')")
    public ResultHolder<Object> stop(@PathVariable String module) {
        iModuleManageService.stop(module);
        return ResultHolder.success(null);
    }

    @ResponseBody
    @RequestMapping("upload")
    @PreAuthorize("@cepc.hasAnyCePermission('MODULE_MANAGE:EDIT')")
    public ResultHolder<Boolean> uploadFile(@RequestParam("file") MultipartFile file) {
        iModuleManageService.upload(file);
        return ResultHolder.success(true);
    }


}
