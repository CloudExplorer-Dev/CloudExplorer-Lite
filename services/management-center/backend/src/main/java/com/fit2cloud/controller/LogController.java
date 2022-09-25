package com.fit2cloud.controller;

import com.fit2cloud.common.log.annotation.OperatedLog;
import com.fit2cloud.common.log.constants.OperatedTypeEnum;
import com.fit2cloud.common.log.constants.ResourceTypeEnum;
import com.fit2cloud.controller.handler.ResultHolder;
import com.fit2cloud.controller.request.es.PageOperatedLogRequest;
import com.fit2cloud.controller.request.es.PageSystemLogRequest;
import com.fit2cloud.service.ILogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author jianneng
 * @date 2022/9/23 16:06
 **/
@RestController
@RequestMapping("/api/log")
@Api("ES操作相关接口")
public class LogController {

    @Resource
    private ILogService logService;

    @GetMapping("/system/list")
    @ApiOperation(value = "系统日志搜索", notes = "系统日志搜索")
    @OperatedLog(resourceType = ResourceTypeEnum.LOG,operated = OperatedTypeEnum.SEARCH,param = "#pageSystemLogRequest")
    public ResultHolder<Object> systemLogs(@Validated PageSystemLogRequest pageSystemLogRequest) {
        return ResultHolder.success(logService.systemLogs(pageSystemLogRequest));
    }

    @GetMapping("/operated/list")
    @ApiOperation(value = "操作日志搜索", notes = "操作日志搜索")
    @OperatedLog(resourceType = ResourceTypeEnum.LOG,operated = OperatedTypeEnum.SEARCH,param = "#pageOperatedLogRequest")
    public ResultHolder<Object> apiLogs(@Validated PageOperatedLogRequest pageOperatedLogRequest) {
        return ResultHolder.success(logService.operatedLogs(pageOperatedLogRequest));
    }
}
