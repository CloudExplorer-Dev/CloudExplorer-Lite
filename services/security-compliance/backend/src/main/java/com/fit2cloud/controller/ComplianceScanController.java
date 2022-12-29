package com.fit2cloud.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fit2cloud.controller.handler.ResultHolder;
import com.fit2cloud.controller.request.compliance_scan.ComplianceResourceRequest;
import com.fit2cloud.controller.request.compliance_scan.ComplianceScanRequest;
import com.fit2cloud.controller.response.compliance_scan.ComplianceResourceResponse;
import com.fit2cloud.controller.response.compliance_scan.ComplianceScanResponse;
import com.fit2cloud.controller.response.compliance_scan.ComplianceScanRuleGroupResponse;
import com.fit2cloud.service.IComplianceScanService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2022/12/15  16:37}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
@RestController
@RequestMapping("/api/compliance_scan")
@Validated
@Api("安全合规扫描相关接口")
public class ComplianceScanController {
    @Resource
    private IComplianceScanService complianceScanService;

    @GetMapping
    @ApiOperation("获取当前合规信息")
    public ResultHolder<List<ComplianceScanResponse>> list(ComplianceScanRequest request) {
        List<ComplianceScanResponse> list = complianceScanService.list(request);
        return ResultHolder.success(list);
    }

    @GetMapping("/{currentPage}/{limit}")
    @ApiOperation("分页查看规则的合规信息")
    public ResultHolder<Page<ComplianceScanResponse>> page(@NotNull(message = "当前页不能为空")
                                                           @Min(message = "当前页不能小于0", value = 1)
                                                           @PathVariable("currentPage")
                                                           Integer currentPage,
                                                           @NotNull(message = "每页大小不能为空")
                                                           @Min(message = "每页大小不能小于1", value = 1)
                                                           @PathVariable("limit")
                                                           Integer limit, ComplianceScanRequest request) {
        Page<ComplianceScanResponse> page = complianceScanService.page(currentPage, limit, request);
        return ResultHolder.success(page);
    }

    @GetMapping("/rule_group/{complianceRuleGroupId}")
    @ApiOperation("获取规则组扫描情况")
    public ResultHolder<ComplianceScanRuleGroupResponse> getRuleGroupCompliance(@PathVariable("complianceRuleGroupId")
                                                                                String complianceRuleGroupId) {
        ComplianceScanRuleGroupResponse ruleGroupCompliance = complianceScanService.getRuleGroupCompliance(complianceRuleGroupId);
        return ResultHolder.success(ruleGroupCompliance);
    }

    @GetMapping("/resource/{complianceRuleId}/{currentPage}/{limit}")
    @ApiOperation("分页查询资源")
    public ResultHolder<Page<ComplianceResourceResponse>> pageResource(@NotNull(message = "合规规则id不能为空")
                                                                       @PathVariable("complianceRuleId")
                                                                       String complianceRuleId,
                                                                       @NotNull(message = "当前页不能为空")
                                                                       @Min(message = "当前页不能小于0", value = 1)
                                                                       @PathVariable("currentPage")
                                                                       Integer currentPage,
                                                                       @NotNull(message = "每页大小不能为空")
                                                                       @Min(message = "每页大小不能小于1", value = 1)
                                                                       @PathVariable("limit")
                                                                       Integer limit, ComplianceResourceRequest complianceResourceRequest) {
        Page<ComplianceResourceResponse> res = complianceScanService.pageResource(complianceRuleId, currentPage, limit, complianceResourceRequest);
        return ResultHolder.success(res);
    }

}
