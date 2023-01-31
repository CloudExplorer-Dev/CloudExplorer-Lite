package com.fit2cloud.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fit2cloud.common.provider.util.CommonUtil;
import com.fit2cloud.controller.handler.ResultHolder;
import com.fit2cloud.controller.request.compliance_scan.ComplianceResourceRequest;
import com.fit2cloud.controller.request.compliance_scan.ComplianceScanRequest;
import com.fit2cloud.controller.request.compliance_scan.ComplianceSyncRequest;
import com.fit2cloud.controller.response.compliance_scan.SupportCloudAccountResourceResponse;
import com.fit2cloud.controller.response.compliance_scan.ComplianceResourceResponse;
import com.fit2cloud.controller.response.compliance_scan.ComplianceScanResponse;
import com.fit2cloud.controller.response.compliance_scan.ComplianceScanRuleGroupResponse;
import com.fit2cloud.response.JobRecordResourceResponse;
import com.fit2cloud.service.IComplianceScanService;
import com.fit2cloud.service.ISyncService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
@Api(value = "安全合规扫描相关接口", tags = "安全合规扫描相关接口")
public class ComplianceScanController {
    @Resource
    private IComplianceScanService complianceScanService;
    @Resource
    private ISyncService syncService;

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
        ComplianceScanRuleGroupResponse ruleGroupCompliance = complianceScanService.scanComplianceRuleGroupByGroupId(complianceRuleGroupId);
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

    @PostMapping("/sync_scan")
    @ApiOperation("扫描")
    public ResultHolder<Boolean> scan(@RequestBody ComplianceSyncRequest request) {
        for (ComplianceSyncRequest.CloudAccountResource cloudAccountResource : request.getCloudAccountResources()) {
            syncService.syncInstance(cloudAccountResource.getCloudAccountId(), cloudAccountResource.getResourceType());
        }
        return ResultHolder.success(true);
    }

    @ApiOperation("获取支持的云账号以及云账号可扫描资源")
    @GetMapping("/support_cloud_account")
    public ResultHolder<List<SupportCloudAccountResourceResponse>> listSupportCloudAccountResource() {
        List<SupportCloudAccountResourceResponse> list = complianceScanService.listSupportCloudAccountResource();
        return ResultHolder.success(list);
    }

    @ApiOperation("获取资源类型同步情况")
    @GetMapping("job_record")
    public ResultHolder<List<JobRecordResourceResponse>> listJobRecord() {
        List<JobRecordResourceResponse> res = complianceScanService.listJobRecord();
        return ResultHolder.success(res);
    }
}
