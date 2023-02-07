package com.fit2cloud.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fit2cloud.base.entity.CloudAccount;
import com.fit2cloud.base.entity.VmCloudHost;
import com.fit2cloud.controller.handler.ResultHolder;
import com.fit2cloud.controller.request.server.PageServerRequest;
import com.fit2cloud.controller.request.server.ResourceAnalysisRequest;
import com.fit2cloud.controller.response.BarTreeChartData;
import com.fit2cloud.controller.response.ChartData;
import com.fit2cloud.dto.AnalyticsServerDTO;
import com.fit2cloud.dto.KeyValue;
import com.fit2cloud.service.IServerAnalysisService;
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
@RequestMapping("/api/server_analysis")
@Validated
@Api("云主机分析相关接口")
public class ServerAnalysisController {
    @Resource
    private IServerAnalysisService iServerAnalysisService;

    @ApiOperation(value = "分页查询云主机", notes = "分页查询云主机")
    @GetMapping("/server/page")
    @PreAuthorize("hasAnyCePermission('SERVER_ANALYSIS:READ')")
    public ResultHolder<IPage<AnalyticsServerDTO>> pageServerList(@Validated PageServerRequest request) {
        return ResultHolder.success(iServerAnalysisService.pageServer(request));
    }

    @ApiOperation(value = "查询云账号", notes = "查询云账号")
    @GetMapping("/cloud_accounts")
    @PreAuthorize("hasAnyCePermission('SERVER_ANALYSIS:READ','OVERVIEW:READ')")
    public ResultHolder<List<CloudAccount>> listCloudAccount() {
        return ResultHolder.success(iServerAnalysisService.getAllCloudAccount());
    }

    @ApiOperation(value = "查询宿主机", notes = "查询云账号下宿主机")
    @GetMapping("/hosts")
    @PreAuthorize("hasAnyCePermission('SERVER_ANALYSIS:READ')")
    public ResultHolder<List<VmCloudHost>> getVmHost(@Validated ResourceAnalysisRequest request) {
        return ResultHolder.success(iServerAnalysisService.getVmHost(request));
    }

    @ApiOperation(value = "根据分布类型查询云主机分布情况", notes = "根据分布类型查询云主机分布情况")
    @GetMapping("/spread")
    @PreAuthorize("hasAnyCePermission('SERVER_ANALYSIS:READ','OVERVIEW:READ')")
    public ResultHolder<Map<String,List<KeyValue>>> getVmSpreadInfo(@Validated ResourceAnalysisRequest request) {
        return ResultHolder.success(iServerAnalysisService.spread(request));
    }

    @ApiOperation(value="查询云主机增长趋势",notes = "查询云主机增长趋势")
    @GetMapping("/increase_trend")
    @PreAuthorize("hasAnyCePermission('SERVER_ANALYSIS:READ','OVERVIEW:READ')")
    public ResultHolder<List<ChartData>> getVmIncreaseTrendData(
            @Validated ResourceAnalysisRequest request) {
        return ResultHolder.success(iServerAnalysisService.vmIncreaseTrend(request));
    }

    @ApiOperation(value="查询云主机资源使用趋势",notes = "查询云主机资源使用趋势")
    @GetMapping("/resource_used_trend")
    @PreAuthorize("hasAnyCePermission('SERVER_ANALYSIS:READ')")
    public ResultHolder<List<ChartData>> getResourceUsedTrendData(
            @Validated ResourceAnalysisRequest request) {
        return ResultHolder.success(iServerAnalysisService.getResourceTrendData(request));
    }

    @ApiOperation(value="组织或工作空间云主机分布",notes = "组织或工作空间云主机分布")
    @GetMapping("/org_workspace_vm_count_bar")
    @PreAuthorize("hasAnyCePermission('SERVER_ANALYSIS:READ')")
    public ResultHolder<Map<String,List<BarTreeChartData>>> analyticsVmCloudServerByOrgWorkspace(
            @Validated ResourceAnalysisRequest request) {
        return ResultHolder.success(iServerAnalysisService.analyticsVmCloudServerByOrgWorkspace(request));
    }


}
