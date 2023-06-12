package com.fit2cloud.controller;

import com.fit2cloud.controller.handler.ResultHolder;
import com.fit2cloud.controller.request.view.ComplianceCountRequest;
import com.fit2cloud.controller.request.view.ComplianceGroupRequest;
import com.fit2cloud.controller.response.view.ComplianceViewCountResponse;
import com.fit2cloud.controller.response.view.ComplianceViewGroupResponse;
import com.fit2cloud.controller.response.view.ComplianceViewRuleCountResponse;
import com.fit2cloud.dao.constants.RiskLevel;
import com.fit2cloud.service.IComplianceViewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2023/2/10  9:11}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
@RestController
@RequestMapping("/api/compliance_view")
@Validated
@Tag(name = "合规总览相关接口", description = "合规总览相关接口")
public class ComplianceViewController {
    @Resource
    private IComplianceViewService complianceViewService;

    @GetMapping("/resource/count")
    @Operation(summary = "获取资源统计数据")
    @PreAuthorize("@cepc.hasAnyCePermission('OVERVIEW:READ')")
    public ResultHolder<ComplianceViewCountResponse> resourceCount(ComplianceCountRequest request) {
        ComplianceViewCountResponse complianceViewCountResponse = complianceViewService.resourceCount(request);
        return ResultHolder.success(complianceViewCountResponse);
    }

    @GetMapping("/rule/count")
    @Operation(summary = "获取规则统计数据")
    @PreAuthorize("@cepc.hasAnyCePermission('OVERVIEW:READ')")
    public ResultHolder<Map<RiskLevel, ComplianceViewRuleCountResponse>> ruleCount(ComplianceCountRequest request) {
        Map<RiskLevel, ComplianceViewRuleCountResponse> riskLevelComplianceViewRuleCountResponseMap = complianceViewService.ruleCount(request);
        return ResultHolder.success(riskLevelComplianceViewRuleCountResponseMap);
    }

    @GetMapping("/resource/group")
    @Operation(summary = "获取资源分组统计数据", description = "获取资源分组统计数据")
    @PreAuthorize("@cepc.hasAnyCePermission('OVERVIEW:READ')")
    public ResultHolder<List<ComplianceViewGroupResponse>> group(ComplianceGroupRequest request) {
        List<ComplianceViewGroupResponse> res = complianceViewService.resourceGroup(request);
        return ResultHolder.success(res);
    }

}
