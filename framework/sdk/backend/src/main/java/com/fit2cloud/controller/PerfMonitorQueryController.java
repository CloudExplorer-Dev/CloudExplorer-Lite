package com.fit2cloud.controller;

import com.fit2cloud.controller.handler.ResultHolder;
import com.fit2cloud.dto.PerfMonitorEchartsDTO;
import com.fit2cloud.request.PerfMonitorRequest;
import com.fit2cloud.service.PerfMonitorService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 性能监控数据查询
 *
 * @author jianneng
 **/
@Validated
@RestController
@RequestMapping("/api/base/monitor")
@Api(value = "公共监控相关接口", tags = "公共监控相关接口")
public class PerfMonitorQueryController {

    @Resource
    private PerfMonitorService perfMonitorService;

    @ApiOperation(value = "查询监控数据", notes = "查询监控数据")
    @GetMapping("/list")
    public ResultHolder<Map<String, List<PerfMonitorEchartsDTO>>> list(
            @Validated PerfMonitorRequest perfMonitorRequest) {
        return ResultHolder.success(perfMonitorService.getPerfMonitorData(perfMonitorRequest));
    }
}
