package com.fit2cloud.controller;

import com.fit2cloud.base.entity.SystemParameter;
import com.fit2cloud.base.service.IBaseSystemParameterService;
import com.fit2cloud.common.log.annotation.OperatedLog;
import com.fit2cloud.common.log.constants.OperatedTypeEnum;
import com.fit2cloud.common.log.constants.ResourceTypeEnum;
import com.fit2cloud.controller.handler.ResultHolder;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author : LiuDi
 * @date : 2023/1/15 14:28
 */
@RestController
@RequestMapping("/api/system-setting/recycle-setting")
@Api("回收站参数设置相关接口")
@Validated
public class RecycleBinParamsController {
    @Resource
    private IBaseSystemParameterService baseSystemParameterService;

    @ApiOperation(value = "查看回收站开启状态", notes = "查看回收站参数")
    @GetMapping("getRecycleBinStatus")
    @PreAuthorize("hasAnyCePermission('PARAMS_SETTING:READ')")
    public ResultHolder<Boolean> getRecycleBinStatus() {
        String result = baseSystemParameterService.getValue("recycle_bin.enable");
        return ResultHolder.success("true".equalsIgnoreCase(result));
    }

    @ApiOperation(value = "变更回收站状态", notes = "变更回收站状态")
    @PostMapping("changeRecycleBinStatus")
    @PreAuthorize("hasAnyCePermission('PARAMS_SETTING:EDIT')")
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
