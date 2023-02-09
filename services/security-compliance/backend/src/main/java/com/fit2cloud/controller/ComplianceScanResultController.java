package com.fit2cloud.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fit2cloud.controller.handler.ResultHolder;
import com.fit2cloud.controller.request.compliance_scan.ComplianceScanRequest;
import com.fit2cloud.controller.response.compliance_scan_result.ComplianceScanResultResponse;
import com.fit2cloud.controller.response.compliance_scan_result.ComplianceScanRuleGroupResultResponse;
import com.fit2cloud.service.IComplianceScanResultService;
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
 * {@code @Date: 2023/2/8  15:56}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
@RestController
@RequestMapping("/api/compliance_scan_result")
@Validated
@Api(value = "安全合规扫描结果相关接口", tags = "安全合规扫描结果相关接口")
public class ComplianceScanResultController {
    @Resource
    private IComplianceScanResultService complianceScanResultService;

    @GetMapping
    @ApiOperation("获取当前合规信息")
    public ResultHolder<List<ComplianceScanResultResponse>> list(ComplianceScanRequest request) {
        List<ComplianceScanResultResponse> list = complianceScanResultService.list(request);
        return ResultHolder.success(list);
    }

    @GetMapping("/{currentPage}/{limit}")
    @ApiOperation("分页查看规则的合规信息")
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
    @ApiOperation("获取规则组扫描情况")
    public ResultHolder<ComplianceScanRuleGroupResultResponse> getRuleGroupCompliance(@PathVariable("complianceRuleGroupId")
                                                                                String complianceRuleGroupId) {
        ComplianceScanRuleGroupResultResponse ruleGroupCompliance = complianceScanResultService.getComplianceRuleGroupByGroupId(complianceRuleGroupId);
        return ResultHolder.success(ruleGroupCompliance);
    }

}
