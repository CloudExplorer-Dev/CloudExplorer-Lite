package com.fit2cloud.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fit2cloud.controller.handler.ResultHolder;
import com.fit2cloud.controller.request.compliance_scan.ComplianceScanRequest;
import com.fit2cloud.controller.request.view.ListRuleGroupRiskDataRequest;
import com.fit2cloud.controller.response.compliance_scan_result.ComplianceScanResultResponse;
import com.fit2cloud.controller.response.compliance_scan_result.ComplianceScanRuleGroupResultResponse;
import com.fit2cloud.controller.response.view.ComplianceRuleGroupCountResponse;
import com.fit2cloud.service.IComplianceScanResultService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.annotation.Resource;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.util.List;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2023/2/8  15:56}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
@RestController
@RequestMapping("/api/compliance_scan_result")
@Validated
@Tag(name = "安全合规扫描结果相关接口", description = "安全合规扫描结果相关接口")
public class ComplianceScanResultController {
    @Resource
    private IComplianceScanResultService complianceScanResultService;

    @GetMapping
    @Operation(summary = "获取当前合规信息")
    @PreAuthorize("@cepc.hasAnyCePermission('SCAN:READ')")
    public ResultHolder<List<ComplianceScanResultResponse>> list(ComplianceScanRequest request) {
        List<ComplianceScanResultResponse> list = complianceScanResultService.list(request);
        return ResultHolder.success(list);
    }

    @GetMapping("/{currentPage}/{limit}")
    @Operation(summary = "分页查看规则的合规信息")
    @PreAuthorize("@cepc.hasAnyCePermission('SCAN:READ')")
    public ResultHolder<IPage<ComplianceScanResultResponse>> page(@NotNull(message = "当前页不能为空")
                                                                  @Min(message = "当前页不能小于0", value = 1)
                                                                  @PathVariable("currentPage")
                                                                  Integer currentPage,
                                                                  @NotNull(message = "每页大小不能为空")
                                                                  @Min(message = "每页大小不能小于1", value = 1)
                                                                  @PathVariable("limit")
                                                                  Integer limit, ComplianceScanRequest request) {
        IPage<ComplianceScanResultResponse> page = complianceScanResultService.page(currentPage, limit, request);
        return ResultHolder.success(page);
    }


    @GetMapping("/rule_group/{complianceRuleGroupId}")
    @Operation(summary = "获取规则组扫描情况")
    @PreAuthorize("@cepc.hasAnyCePermission('SCAN:READ')")
    public ResultHolder<ComplianceScanRuleGroupResultResponse> getRuleGroupCompliance(@PathVariable("complianceRuleGroupId")
                                                                                      String complianceRuleGroupId) {
        ComplianceScanRuleGroupResultResponse ruleGroupCompliance = complianceScanResultService.getComplianceRuleGroupByGroupId(complianceRuleGroupId);
        return ResultHolder.success(ruleGroupCompliance);
    }

    @GetMapping("/rule_group")
    @PreAuthorize("@cepc.hasAnyCePermission('SCAN:READ')")
    @Operation(summary = "获取规则组的扫描详情", description = "获取规则组的扫描详情")
    public ResultHolder<List<ComplianceRuleGroupCountResponse>> listRuleGroupRiskData(ListRuleGroupRiskDataRequest request) {
        List<ComplianceRuleGroupCountResponse> res = complianceScanResultService.listRuleGroupRiskData(request);
        return ResultHolder.success(res);
    }
}
