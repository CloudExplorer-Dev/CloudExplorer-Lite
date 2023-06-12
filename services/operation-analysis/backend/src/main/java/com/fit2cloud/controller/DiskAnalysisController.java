package com.fit2cloud.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fit2cloud.base.entity.CloudAccount;
import com.fit2cloud.controller.handler.ResultHolder;
import com.fit2cloud.controller.request.disk.PageDiskRequest;
import com.fit2cloud.controller.request.disk.ResourceAnalysisRequest;
import com.fit2cloud.controller.response.ChartData;
import com.fit2cloud.controller.response.TreeNode;
import com.fit2cloud.dto.AnalysisDiskDTO;
import com.fit2cloud.dto.KeyValue;
import com.fit2cloud.service.IDiskAnalysisService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author jianneng
 * @date 2022/12/24 11:23
 **/
@RestController
@RequestMapping("/api/disk_analysis")
@Validated
@Tag(name = "磁盘分析相关接口")
public class DiskAnalysisController {
    @Resource
    private IDiskAnalysisService iDiskAnalysisService;

    @Operation(summary = "分页查询磁盘", description = "分页查询磁盘")
    @GetMapping("/disk/page")
    @PreAuthorize("@cepc.hasAnyCePermission('DISK_ANALYSIS:READ')")
    public ResultHolder<IPage<AnalysisDiskDTO>> pageDiskList(@Validated PageDiskRequest request) {
        return ResultHolder.success(iDiskAnalysisService.pageDisk(request));
    }

    @Operation(summary = "查询云账号", description = "查询云账号")
    @GetMapping("/cloud_accounts")
    @PreAuthorize("@cepc.hasAnyCePermission('DISK_ANALYSIS:READ','OVERVIEW:READ')")
    public ResultHolder<List<CloudAccount>> listCloudAccount() {
        return ResultHolder.success(iDiskAnalysisService.getAllCloudAccount());
    }

    @Operation(summary = "根据分布类型查询磁盘分布情况", description = "根据分布类型查询磁盘分布情况")
    @GetMapping("/spread")
    @PreAuthorize("@cepc.hasAnyCePermission('DISK_ANALYSIS:READ','OVERVIEW:READ')")
    public ResultHolder<Map<String, List<KeyValue>>> getDiskSpreadInfo(@Validated ResourceAnalysisRequest request) {
        return ResultHolder.success(iDiskAnalysisService.spread(request));
    }

    @Operation(summary = "查询磁盘趋势", description = "查询磁盘趋势")
    @GetMapping("/increase_trend")
    @PreAuthorize("@cepc.hasAnyCePermission('DISK_ANALYSIS:READ')")
    public ResultHolder<List<ChartData>> getDiskIncreaseTrendData(
            @Validated ResourceAnalysisRequest request) {
        return ResultHolder.success(iDiskAnalysisService.diskIncreaseTrend(request));
    }

    @Operation(summary = "组织或工作空间磁盘分布", description = "组织或工作空间磁盘分布")
    @GetMapping("/org_workspace_disk_count_bar")
    @PreAuthorize("@cepc.hasAnyCePermission('DISK_ANALYSIS:READ')")
    public ResultHolder<Map<String, List<TreeNode>>> analysisCloudDiskByOrgWorkspace(
            @Validated ResourceAnalysisRequest request) {
        return ResultHolder.success(iDiskAnalysisService.analysisCloudDiskByOrgWorkspace(request));
    }

    @Operation(summary = "根据云账号查询云盘数量", description = "根据云账号查询云盘数量")
    @GetMapping("/cloud_account/disk/count")
    @PreAuthorize("@cepc.hasAnyCePermission('DISK_ANALYSIS:READ')")
    public ResultHolder<Long> countDiskByCloudAccountId(@RequestParam("cloudAccountId") String cloudAccountId) {
        return ResultHolder.success(iDiskAnalysisService.countDiskByCloudAccount(cloudAccountId));
    }

}
