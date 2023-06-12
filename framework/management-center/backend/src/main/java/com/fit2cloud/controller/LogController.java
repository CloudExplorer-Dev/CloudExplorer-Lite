package com.fit2cloud.controller;

import com.fit2cloud.base.entity.SystemParameter;
import com.fit2cloud.base.service.IBaseSystemParameterService;
import com.fit2cloud.common.log.annotation.OperatedLog;
import com.fit2cloud.common.log.constants.OperatedTypeEnum;
import com.fit2cloud.common.log.constants.ResourceTypeEnum;
import com.fit2cloud.controller.handler.ResultHolder;
import com.fit2cloud.controller.request.es.PageOperatedLogRequest;
import com.fit2cloud.controller.request.es.PageSystemLogRequest;
import com.fit2cloud.service.ILogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.annotation.Resource;
import java.util.List;

/**
 * @author jianneng
 * @date 2022/9/23 16:06
 **/
@RestController
@RequestMapping("/api/log")
@Tag(name = "ES操作相关接口")
public class LogController {

    @Resource
    private ILogService logService;
    @Resource
    private IBaseSystemParameterService baseSystemParameterService;

    @GetMapping("/system/list")
    @Operation(summary = "系统日志搜索", description = "系统日志搜索")
    @PreAuthorize("@cepc.hasAnyCePermission('SYS_LOG:READ')")
    public ResultHolder<Object> systemLogs(@Validated PageSystemLogRequest pageSystemLogRequest) {
        return ResultHolder.success(logService.systemLogs(pageSystemLogRequest));
    }

    @GetMapping("/operated/list")
    @Operation(summary = "操作日志搜索", description = "操作日志搜索")
    @PreAuthorize("@cepc.hasAnyCePermission('OPERATED_LOG:READ')")
    public ResultHolder<Object> apiLogs(@Validated PageOperatedLogRequest pageOperatedLogRequest) {
        return ResultHolder.success(logService.operatedLogs(pageOperatedLogRequest));
    }

    @GetMapping("keep/months")
    @PreAuthorize("@cepc.hasAnyCePermission('PARAMS_SETTING:EDIT')")
    public ResultHolder<String> getKeepMonths(SystemParameter systemParameter) {
        String value = baseSystemParameterService.getValue(systemParameter.getParamKey());
        if (StringUtils.isNotBlank(value)) {
            return ResultHolder.success(value);
        }
        // 如果数据库里没有这个值，默认3
        return ResultHolder.success("3");
    }

    @PostMapping("keep/months")
    @PreAuthorize("@cepc.hasAnyCePermission('PARAMS_SETTING:EDIT')")
    @OperatedLog(resourceType = ResourceTypeEnum.LOG, operated = OperatedTypeEnum.MODIFY,
            content = "'更新日志['+#systemParameter.paramKey+']清理策略为['+#systemParameter.paramValue+']天'",
            param = "#systemParameter")
    public ResultHolder<Boolean> saveKeepMonths(@RequestBody SystemParameter systemParameter) {
        systemParameter.setParamKey(systemParameter.getParamKey());
        baseSystemParameterService.saveValue(systemParameter);
        return ResultHolder.success(true);
    }

    @PostMapping("keep/months/batch")
    @PreAuthorize("@cepc.hasAnyCePermission('PARAMS_SETTING:EDIT')")
    @OperatedLog(resourceType = ResourceTypeEnum.LOG, operated = OperatedTypeEnum.MODIFY,
            content = "'更新日志清理策略'",
            param = "#systemParameters")
    public ResultHolder<Boolean> saveKeepMonths(@RequestBody List<SystemParameter> systemParameters) {
        for (SystemParameter systemParameter : systemParameters) {
            systemParameter.setParamKey(systemParameter.getParamKey());
            baseSystemParameterService.saveValue(systemParameter);
        }
        return ResultHolder.success(true);
    }
}
