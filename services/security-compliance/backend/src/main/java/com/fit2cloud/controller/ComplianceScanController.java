package com.fit2cloud.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fit2cloud.autoconfigure.PluginsContextHolder;
import com.fit2cloud.base.entity.CloudAccount;
import com.fit2cloud.base.service.IBaseCloudAccountService;
import com.fit2cloud.common.advice.annnotaion.TokenRenewal;
import com.fit2cloud.common.log.annotation.OperatedLog;
import com.fit2cloud.common.log.constants.OperatedTypeEnum;
import com.fit2cloud.common.log.constants.ResourceTypeEnum;
import com.fit2cloud.common.provider.IBaseCloudProvider;
import com.fit2cloud.controller.handler.ResultHolder;
import com.fit2cloud.controller.request.compliance_scan.ComplianceResourceRequest;
import com.fit2cloud.controller.request.compliance_scan.ComplianceSyncRequest;
import com.fit2cloud.controller.response.compliance_scan.ComplianceResourceResponse;
import com.fit2cloud.controller.response.compliance_scan.SupportCloudAccountResourceResponse;
import com.fit2cloud.provider.ICloudProvider;
import com.fit2cloud.response.JobRecordResourceResponse;
import com.fit2cloud.service.IComplianceScanService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.annotation.Resource;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

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
@Tag(name = "安全合规扫描相关接口", description = "安全合规扫描相关接口")
public class ComplianceScanController {
    @Resource
    private IComplianceScanService complianceScanService;
    @Resource
    private IBaseCloudAccountService cloudAccountService;

    @GetMapping("/cloud_account")
    public ResultHolder<List<CloudAccount>> listCloudAccount() {
        List<ICloudProvider> iCloudProviderList = PluginsContextHolder.getExtensions(ICloudProvider.class);
        List<CloudAccount> cloudAccounts = cloudAccountService.list()
                .stream().filter(cloudAccount -> iCloudProviderList.stream().anyMatch(p -> StringUtils.equals(p.getCloudAccountMeta().platform, cloudAccount.getPlatform())))
                .toList();
        return ResultHolder.success(cloudAccounts);
    }

    @GetMapping("/resource/{complianceRuleId}/{currentPage}/{limit}")
    @Operation(summary = "分页查询资源")
    @PreAuthorize("@cepc.hasAnyCePermission('SCAN:READ')")
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
    @Operation(summary = "发送扫描任务")
    @PreAuthorize("@cepc.hasAnyCePermission('SCAN:SEND_JOB')")
    @OperatedLog(resourceType = ResourceTypeEnum.COMPLIANCE_SCAN, operated = OperatedTypeEnum.SCAN,
            resourceId = "#request.cloudAccountResources.![cloudAccountId]",
            content = "'发送扫描任务'",
            param = "#request")
    public ResultHolder<Boolean> scan(@RequestBody ComplianceSyncRequest request) {
        complianceScanService.scan(request);
        return ResultHolder.success(true);
    }

    @Operation(summary = "获取支持的云账号以及云账号可扫描资源")
    @GetMapping("/support_cloud_account")
    @PreAuthorize("@cepc.hasAnyCePermission('SCAN:READ')")
    public ResultHolder<List<SupportCloudAccountResourceResponse>> listSupportCloudAccountResource() {
        List<SupportCloudAccountResourceResponse> list = complianceScanService.listSupportCloudAccountResource();
        return ResultHolder.success(list);
    }

    @Operation(summary = "获取资源类型同步情况")
    @GetMapping("job_record")
    @PreAuthorize("@cepc.hasAnyCePermission('SCAN:READ')")
    @TokenRenewal
    public ResultHolder<List<JobRecordResourceResponse>> listJobRecord() {
        List<JobRecordResourceResponse> res = complianceScanService.listJobRecord();
        return ResultHolder.success(res);
    }
}
