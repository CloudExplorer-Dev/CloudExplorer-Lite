package com.fit2cloud.controller;

import com.fit2cloud.controller.handler.ResultHolder;
import com.fit2cloud.controller.request.view.ComplianceCountRequest;
import com.fit2cloud.controller.request.view.ComplianceGroupRequest;
import com.fit2cloud.controller.response.view.ComplianceViewCountResponse;
import com.fit2cloud.controller.response.view.ComplianceViewGroupResponse;
import com.fit2cloud.service.IComplianceViewService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2023/2/10  9:11}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
@RestController
@RequestMapping("/api/compliance_view")
@Validated
@Api(value = "合规总览相关接口", tags = "合规总览相关接口")
public class ComplianceViewController {
    @Resource
    private IComplianceViewService complianceViewService;

    @GetMapping("/count")
    @ApiOperation("获取资源统计数据")
    public ResultHolder<ComplianceViewCountResponse> count(ComplianceCountRequest request) {
        ComplianceViewCountResponse complianceViewCountResponse = complianceViewService.getComplianceViewCountResponse(request);
        return ResultHolder.success(complianceViewCountResponse);
    }

    @GetMapping("/group")
    @ApiOperation(value = "获取分组统计数据", notes = "获取分组统计数据")
    public ResultHolder<List<ComplianceViewGroupResponse>> group(ComplianceGroupRequest request) {
        List<ComplianceViewGroupResponse> res = complianceViewService.group(request);
        return ResultHolder.success(res);
    }
}
