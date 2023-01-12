package com.fit2cloud.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fit2cloud.base.entity.CloudAccount;
import com.fit2cloud.base.entity.VmCloudDatastore;
import com.fit2cloud.base.entity.VmCloudHost;
import com.fit2cloud.controller.handler.ResultHolder;
import com.fit2cloud.controller.request.base.resource.analysis.ResourceAnalysisRequest;
import com.fit2cloud.controller.request.datastore.PageDatastoreRequest;
import com.fit2cloud.controller.request.host.PageHostRequest;
import com.fit2cloud.controller.response.ResourceAllocatedInfo;
import com.fit2cloud.dto.KeyValue;
import com.fit2cloud.dto.AnalyticsDatastoreDTO;
import com.fit2cloud.dto.AnalyticsHostDTO;
import com.fit2cloud.controller.response.ChartData;
import com.fit2cloud.controller.request.base.resource.analysis.ResourceUsedTrendRequest;
import com.fit2cloud.service.IBaseResourceAnalysisService;
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
 * 基础资源分析
 * @author jianneng
 * @date 2022/12/11 18:42
 **/
@RestController
@RequestMapping("/api/base_resource_analysis")
@Validated
@Api("基础资源分析相关接口")
public class BaseResourceAnalysisController {



    @Resource
    private IBaseResourceAnalysisService iBaseResourceAnalysisService;

    @ApiOperation(value = "分页查询宿主机", notes = "分页查询宿主机")
    @GetMapping("/host/page")
    @PreAuthorize("hasAnyCePermission('RESOURCE_ANALYSIS:READ')")
    public ResultHolder<IPage<AnalyticsHostDTO>> pageHostList(@Validated PageHostRequest pageHostRequest) {
        return ResultHolder.success(iBaseResourceAnalysisService.pageHost(pageHostRequest));
    }

    @ApiOperation(value = "查询宿主机数量", notes = "查询宿主机数量")
    @GetMapping("/host/count")
    @PreAuthorize("hasAnyCePermission('RESOURCE_ANALYSIS:READ')")
    public ResultHolder<Long> countHost() {
        return ResultHolder.success(iBaseResourceAnalysisService.countHost());
    }

    @ApiOperation(value = "分页查询存储器", notes = "分页查询存储器")
    @GetMapping("/datastore/page")
    @PreAuthorize("hasAnyCePermission('RESOURCE_ANALYSIS:READ')")
    public ResultHolder<IPage<AnalyticsDatastoreDTO>> pageDatastoreList(@Validated PageDatastoreRequest pageDatastoreRequest) {
        return ResultHolder.success(iBaseResourceAnalysisService.pageDatastore(pageDatastoreRequest));
    }

    @ApiOperation(value = "查询存储器数量", notes = "查询存储器数量")
    @GetMapping("/datastore/count")
    @PreAuthorize("hasAnyCePermission('RESOURCE_ANALYSIS:READ')")
    public ResultHolder<Long> countDatastore() {
        return ResultHolder.success(iBaseResourceAnalysisService.countDatastore());
    }

    @ApiOperation(value = "查询私有云账号", notes = "查询私有云账号")
    @GetMapping("/private_cloud_accounts")
    @PreAuthorize("hasAnyCePermission('RESOURCE_ANALYSIS:READ')")
    public ResultHolder<List<CloudAccount>> listPrivateCloudAccount() {
        return ResultHolder.success(iBaseResourceAnalysisService.getAllPrivateCloudAccount());
    }

    @ApiOperation(value = "查询集群", notes = "查询云账号下集群")
    @GetMapping("/clusters")
    @PreAuthorize("hasAnyCePermission('RESOURCE_ANALYSIS:READ')")
    public ResultHolder<List<Map<String, String>>> getCluster(@Validated ResourceAnalysisRequest request) {
        return ResultHolder.success(iBaseResourceAnalysisService.getCluster(request));
    }

    @ApiOperation(value = "查询宿主机", notes = "查询云账号下宿主机")
    @GetMapping("/hosts")
    @PreAuthorize("hasAnyCePermission('RESOURCE_ANALYSIS:READ')")
    public ResultHolder<List<VmCloudHost>> getVmHost(@Validated ResourceAnalysisRequest request) {
        return ResultHolder.success(iBaseResourceAnalysisService.getVmHost(request));
    }

    @ApiOperation(value = "查询存储器", notes = "查询云账号下存储器")
    @GetMapping("/datastores")
    @PreAuthorize("hasAnyCePermission('RESOURCE_ANALYSIS:READ')")
    public ResultHolder<List<VmCloudDatastore>> getVmCloudDatastore(@Validated ResourceAnalysisRequest request) {
        return ResultHolder.success(iBaseResourceAnalysisService.getVmCloudDatastore(request));
    }

    @ApiOperation(value = "查询资源分配情况", notes = "查询云账号下资源分配情况")
    @GetMapping("/allocated_info")
    @PreAuthorize("hasAnyCePermission('RESOURCE_ANALYSIS:READ')")
    public ResultHolder<Map<String, ResourceAllocatedInfo>> getResourceAllocatedInfo(@Validated ResourceAnalysisRequest request) {
        return ResultHolder.success(iBaseResourceAnalysisService.getResourceAllocatedInfo(request));
    }

    @ApiOperation(value = "查询资源分布情况", notes = "查询云账号下资源分布情况")
    @GetMapping("/spread_info")
    @PreAuthorize("hasAnyCePermission('RESOURCE_ANALYSIS:READ')")
    public ResultHolder<Map<String,List<KeyValue>>> getResourceSpreadInfo(@Validated ResourceAnalysisRequest request) {
        return ResultHolder.success(iBaseResourceAnalysisService.getResourceSpreadInfo(request));
    }


    @ApiOperation(value="查询资源使用趋势",notes = "查询资源使用趋势")
    @GetMapping("/resource_trend")
    @PreAuthorize("hasAnyCePermission('RESOURCE_ANALYSIS:READ')")
    public ResultHolder<List<ChartData>> getResourceUsedTrendData(
            @Validated ResourceUsedTrendRequest resourceUsedTrendRequest) {
        return ResultHolder.success(iBaseResourceAnalysisService.getResourceUsedTrendData(resourceUsedTrendRequest));
    }
}
