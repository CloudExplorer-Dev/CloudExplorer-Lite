package com.fit2cloud.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fit2cloud.common.advice.annnotaion.TokenRenewal;
import com.fit2cloud.common.log.annotation.OperatedLog;
import com.fit2cloud.common.log.constants.OperatedTypeEnum;
import com.fit2cloud.common.log.constants.ResourceTypeEnum;
import com.fit2cloud.controller.handler.ResultHolder;
import com.fit2cloud.controller.request.compliance_scan.ComplianceResourceRequest;
import com.fit2cloud.controller.request.compliance_scan.ComplianceSyncRequest;
import com.fit2cloud.controller.request.view.ListRuleGroupRiskDataRequest;
import com.fit2cloud.controller.response.compliance_scan.ComplianceResourceResponse;
import com.fit2cloud.controller.response.compliance_scan.SupportCloudAccountResourceResponse;
import com.fit2cloud.controller.response.compliance_scan.SupportPlatformResourceResponse;
import com.fit2cloud.controller.response.view.ComplianceRuleGroupCountResponse;
import com.fit2cloud.response.JobRecordResourceResponse;
import com.fit2cloud.service.IComplianceScanService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections4.keyvalue.DefaultKeyValue;
import org.springframework.security.access.prepost.PreAuthorize;
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


    @GetMapping("/resource/{complianceRuleId}/{currentPage}/{limit}")
    @ApiOperation("分页查询资源")
    @PreAuthorize("hasAnyCePermission('SCAN:READ')")
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
    @ApiOperation("发送扫描任务")
    @PreAuthorize("hasAnyCePermission('SCAN:SEND_JOB')")
    @OperatedLog(resourceType = ResourceTypeEnum.COMPLIANCE_SCAN, operated = OperatedTypeEnum.SCAN,
            resourceId = "#request.cloudAccountResources.![cloudAccountId]",
            content = "'发送扫描任务'",
            param = "#request")
    public ResultHolder<Boolean> scan(@RequestBody ComplianceSyncRequest request) {
        complianceScanService.scan(request);
        return ResultHolder.success(true);
    }

    @ApiOperation("获取支持的云账号以及云账号可扫描资源")
    @GetMapping("/support_cloud_account")
    @PreAuthorize("hasAnyCePermission('SCAN:READ')")
    public ResultHolder<List<SupportCloudAccountResourceResponse>> listSupportCloudAccountResource() {
        List<SupportCloudAccountResourceResponse> list = complianceScanService.listSupportCloudAccountResource();
        return ResultHolder.success(list);
    }

    @ApiOperation("获取资源类型同步情况")
    @GetMapping("job_record")
    @PreAuthorize("hasAnyCePermission('SCAN:READ')")
    @TokenRenewal
    public ResultHolder<List<JobRecordResourceResponse>> listJobRecord() {
        List<JobRecordResourceResponse> res = complianceScanService.listJobRecord();
        return ResultHolder.success(res);
    }
}
