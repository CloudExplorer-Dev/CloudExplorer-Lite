package com.fit2cloud.controller;

import com.fit2cloud.controller.handler.ResultHolder;
import com.fit2cloud.controller.request.view.ComplianceCountRequest;
import com.fit2cloud.controller.request.view.ComplianceGroupRequest;
import com.fit2cloud.controller.response.view.ComplianceViewCountResponse;
import com.fit2cloud.controller.response.view.ComplianceViewGroupResponse;
import com.fit2cloud.service.IComplianceViewService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.auth.In;
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

    @GetMapping("/resource/count")
    @ApiOperation("获取资源统计数据")
    public ResultHolder<ComplianceViewCountResponse> resourceCount(ComplianceCountRequest request) {
        ComplianceViewCountResponse complianceViewCountResponse = complianceViewService.resourceCount(request);
        return ResultHolder.success(complianceViewCountResponse);
    }

    @GetMapping("/rule/count")
    @ApiOperation("获取规则统计数据")
    public ResultHolder<ComplianceViewCountResponse> ruleCount(ComplianceCountRequest request) {
        ComplianceViewCountResponse complianceViewCountResponse = complianceViewService.ruleCount(request);
        return ResultHolder.success(complianceViewCountResponse);
    }

    @GetMapping("/resource/group")
    @ApiOperation(value = "获取资源分组统计数据", notes = "获取资源分组统计数据")
    public ResultHolder<List<ComplianceViewGroupResponse>> group(ComplianceGroupRequest request) {
        List<ComplianceViewGroupResponse> res = complianceViewService.resourceGroup(request);
        return ResultHolder.success(res);
    }
}
