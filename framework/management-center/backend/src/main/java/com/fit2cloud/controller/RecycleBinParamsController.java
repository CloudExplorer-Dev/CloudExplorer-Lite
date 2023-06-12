package com.fit2cloud.controller;

import com.fit2cloud.base.entity.SystemParameter;
import com.fit2cloud.base.service.IBaseSystemParameterService;
import com.fit2cloud.common.log.annotation.OperatedLog;
import com.fit2cloud.common.log.constants.OperatedTypeEnum;
import com.fit2cloud.common.log.constants.ResourceTypeEnum;
import com.fit2cloud.controller.handler.ResultHolder;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.annotation.Resource;

/**
 * @author : LiuDi
 * @date : 2023/1/15 14:28
 */
@RestController
@RequestMapping("/api/system-setting/recycle-setting")
@Tag(name = "回收站参数设置相关接口")
@Validated
public class RecycleBinParamsController {
    @Resource
    private IBaseSystemParameterService baseSystemParameterService;

    @Operation(summary = "查看回收站开启状态", description = "查看回收站参数")
    @GetMapping("getRecycleBinStatus")
    @PreAuthorize("@cepc.hasAnyCePermission('PARAMS_SETTING:READ')")
    public ResultHolder<Boolean> getRecycleBinStatus() {
        String result = baseSystemParameterService.getValue("recycle_bin.enable");
        return ResultHolder.success("true".equalsIgnoreCase(result));
    }

    @Operation(summary = "变更回收站状态", description = "变更回收站状态")
    @PostMapping("changeRecycleBinStatus")
    @PreAuthorize("@cepc.hasAnyCePermission('PARAMS_SETTING:EDIT')")
    @OperatedLog(resourceType = ResourceTypeEnum.SYSTEM, operated = OperatedTypeEnum.MODIFY,
            content = "#parameter.paramValue=='true'?'变更回收站状态为[开启]':'变更回收站状态为[关闭]'",
            param = "#parameter")
    public ResultHolder<Boolean> changeRecycleBinStatus(@RequestBody SystemParameter parameter) {
        if ("recycle_bin.enable".equals(parameter.getParamKey()) && ("true".equals(parameter.getParamValue()) || "false".equals(parameter.getParamValue()))) {
            baseSystemParameterService.saveValue(parameter);
            return ResultHolder.success(true);
        } else {
            throw new RuntimeException("参数错误");
        }
    }


}
