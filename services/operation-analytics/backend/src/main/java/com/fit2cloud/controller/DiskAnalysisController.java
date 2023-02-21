package com.fit2cloud.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fit2cloud.base.entity.CloudAccount;
import com.fit2cloud.controller.handler.ResultHolder;
import com.fit2cloud.controller.request.disk.PageDiskRequest;
import com.fit2cloud.controller.request.disk.ResourceAnalysisRequest;
import com.fit2cloud.controller.response.BarTreeChartData;
import com.fit2cloud.controller.response.ChartData;
import com.fit2cloud.dto.KeyValue;
import com.fit2cloud.dto.AnalyticsDiskDTO;
import com.fit2cloud.service.IDiskAnalysisService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author jianneng
 * @date 2022/12/24 11:23
 **/
@RestController
@RequestMapping("/api/disk_analysis")
@Validated
@Api("磁盘分析相关接口")
public class DiskAnalysisController {
    @Resource
    private IDiskAnalysisService iDiskAnalysisService;

    @ApiOperation(value = "分页查询磁盘", notes = "分页查询磁盘")
    @GetMapping("/disk/page")
    @PreAuthorize("hasAnyCePermission('DISK_ANALYSIS:READ')")
    public ResultHolder<IPage<AnalyticsDiskDTO>> pageDiskList(@Validated PageDiskRequest request) {
        return ResultHolder.success(iDiskAnalysisService.pageDisk(request));
    }

    @ApiOperation(value = "查询云账号", notes = "查询云账号")
    @GetMapping("/cloud_accounts")
    @PreAuthorize("hasAnyCePermission('DISK_ANALYSIS:READ','OVERVIEW:READ')")
    public ResultHolder<List<CloudAccount>> listCloudAccount() {
        return ResultHolder.success(iDiskAnalysisService.getAllCloudAccount());
    }

    @ApiOperation(value = "根据分布类型查询磁盘分布情况", notes = "根据分布类型查询磁盘分布情况")
    @GetMapping("/spread")
    @PreAuthorize("hasAnyCePermission('DISK_ANALYSIS:READ','OVERVIEW:READ')")
    public ResultHolder<Map<String,List<KeyValue>>> getDiskSpreadInfo(@Validated ResourceAnalysisRequest request) {
        return ResultHolder.success(iDiskAnalysisService.spread(request));
    }

    @ApiOperation(value="查询磁盘趋势",notes = "查询磁盘趋势")
    @GetMapping("/increase_trend")
    @PreAuthorize("hasAnyCePermission('DISK_ANALYSIS:READ')")
    public ResultHolder<List<ChartData>> getDiskIncreaseTrendData(
            @Validated ResourceAnalysisRequest request) {
        return ResultHolder.success(iDiskAnalysisService.diskIncreaseTrend(request));
    }

    @ApiOperation(value="组织或工作空间磁盘分布",notes = "组织或工作空间磁盘分布")
    @GetMapping("/org_workspace_disk_count_bar")
    @PreAuthorize("hasAnyCePermission('DISK_ANALYSIS:READ')")
    public ResultHolder<Map<String,List<BarTreeChartData>>> analyticsCloudDiskByOrgWorkspace(
            @Validated ResourceAnalysisRequest request) {
        return ResultHolder.success(iDiskAnalysisService.analyticsCloudDiskByOrgWorkspace(request));
    }

}
