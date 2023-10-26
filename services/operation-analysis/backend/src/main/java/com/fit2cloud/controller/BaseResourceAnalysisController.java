package com.fit2cloud.controller;

import com.alibaba.excel.EasyExcelFactory;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fit2cloud.base.entity.CloudAccount;
import com.fit2cloud.base.entity.VmCloudDatastore;
import com.fit2cloud.base.entity.VmCloudHost;
import com.fit2cloud.controller.handler.ResultHolder;
import com.fit2cloud.controller.request.base.resource.analysis.ResourceAnalysisRequest;
import com.fit2cloud.controller.request.base.resource.analysis.ResourceUsedTrendRequest;
import com.fit2cloud.controller.request.datastore.DatastoreRequest;
import com.fit2cloud.controller.request.datastore.PageDatastoreRequest;
import com.fit2cloud.controller.request.host.HostRequest;
import com.fit2cloud.controller.request.host.PageHostRequest;
import com.fit2cloud.controller.response.ChartData;
import com.fit2cloud.controller.response.ResourceAllocatedInfo;
import com.fit2cloud.dto.AnalysisDatastoreDTO;
import com.fit2cloud.dto.AnalysisHostDTO;
import com.fit2cloud.dto.KeyValue;
import com.fit2cloud.service.IBaseResourceAnalysisService;
import com.fit2cloud.utils.CustomCellWriteHeightConfig;
import com.fit2cloud.utils.CustomCellWriteWidthConfig;
import com.fit2cloud.utils.EasyExcelUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * 基础资源分析
 *
 * @author jianneng
 * @date 2022/12/11 18:42
 **/
@RestController
@RequestMapping("/api/base_resource_analysis")
@Validated
@Tag(name = "基础资源分析相关接口")
public class BaseResourceAnalysisController {

    @Resource
    private IBaseResourceAnalysisService iBaseResourceAnalysisService;

    @Operation(summary = "分页查询宿主机", description = "分页查询宿主机")
    @GetMapping("/host/page")
    @PreAuthorize("@cepc.hasAnyCePermission('BASE_RESOURCE_ANALYSIS:READ')")
    public ResultHolder<IPage<AnalysisHostDTO>> pageHostList(@Validated PageHostRequest pageHostRequest) {
        return ResultHolder.success(iBaseResourceAnalysisService.pageHost(pageHostRequest));
    }

    @Operation(summary = "宿主机明细下载", description = "宿主机明细下载")
    @GetMapping("/host/download")
    @PreAuthorize("@cepc.hasAnyCePermission('BASE_RESOURCE_ANALYSIS:READ')")
    public void hostListDownload(@Validated HostRequest request, HttpServletResponse response) {
        List<AnalysisHostDTO> list = iBaseResourceAnalysisService.listHost(request);
        try {
            EasyExcelFactory.write(response.getOutputStream(), AnalysisHostDTO.class)
                    .sheet("宿主机明细列表")
                    .registerWriteHandler(new CustomCellWriteWidthConfig())
                    .registerWriteHandler(new CustomCellWriteHeightConfig())
                    .registerWriteHandler(EasyExcelUtils.getStyleStrategy())
                    .doWrite(list);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Operation(summary = "查询宿主机数量", description = "查询宿主机数量")
    @GetMapping("/host/count")
    @PreAuthorize("@cepc.hasAnyCePermission('BASE_RESOURCE_ANALYSIS:READ','OVERVIEW:READ')")
    public ResultHolder<Long> countHost() {
        return ResultHolder.success(iBaseResourceAnalysisService.countHost());
    }

    @Operation(summary = "分页查询存储器", description = "分页查询存储器")
    @GetMapping("/datastore/page")
    @PreAuthorize("@cepc.hasAnyCePermission('BASE_RESOURCE_ANALYSIS:READ')")
    public ResultHolder<IPage<AnalysisDatastoreDTO>> pageDatastoreList(@Validated PageDatastoreRequest pageDatastoreRequest) {
        return ResultHolder.success(iBaseResourceAnalysisService.pageDatastore(pageDatastoreRequest));
    }

    @Operation(summary = "存储器明细下载", description = "存储器明细下载")
    @GetMapping("/datastore/download")
    @PreAuthorize("@cepc.hasAnyCePermission('BASE_RESOURCE_ANALYSIS:READ')")
    public void datastoreListDownload(@Validated DatastoreRequest request, HttpServletResponse response) {
        List<AnalysisDatastoreDTO> list = iBaseResourceAnalysisService.listDatastore(request);
        try {
            EasyExcelFactory.write(response.getOutputStream(), AnalysisDatastoreDTO.class)
                    .sheet("存储器明细列表")
                    .registerWriteHandler(new CustomCellWriteWidthConfig())
                    .registerWriteHandler(new CustomCellWriteHeightConfig())
                    .registerWriteHandler(EasyExcelUtils.getStyleStrategy())
                    .doWrite(list);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Operation(summary = "查询存储器数量", description = "查询存储器数量")
    @GetMapping("/datastore/count")
    @PreAuthorize("@cepc.hasAnyCePermission('BASE_RESOURCE_ANALYSIS:READ','OVERVIEW:READ')")
    public ResultHolder<Long> countDatastore() {
        return ResultHolder.success(iBaseResourceAnalysisService.countDatastore());
    }

    @Operation(summary = "查询私有云账号", description = "查询私有云账号")
    @GetMapping("/private_cloud_accounts")
    @PreAuthorize("@cepc.hasAnyCePermission('BASE_RESOURCE_ANALYSIS:READ')")
    public ResultHolder<List<CloudAccount>> listPrivateCloudAccount() {
        return ResultHolder.success(iBaseResourceAnalysisService.getAllPrivateCloudAccount());
    }

    @Operation(summary = "查询集群", description = "查询云账号下集群")
    @GetMapping("/clusters")
    @PreAuthorize("@cepc.hasAnyCePermission('BASE_RESOURCE_ANALYSIS:READ')")
    public ResultHolder<List<Map<String, String>>> getCluster(@Validated ResourceAnalysisRequest request) {
        return ResultHolder.success(iBaseResourceAnalysisService.getCluster(request));
    }

    @Operation(summary = "查询宿主机", description = "查询云账号下宿主机")
    @GetMapping("/hosts")
    @PreAuthorize("@cepc.hasAnyCePermission('BASE_RESOURCE_ANALYSIS:READ','OVERVIEW:READ')")
    public ResultHolder<List<VmCloudHost>> getVmHost(@Validated ResourceAnalysisRequest request) {
        return ResultHolder.success(iBaseResourceAnalysisService.getVmHost(request));
    }

    @Operation(summary = "查询存储器", description = "查询云账号下存储器")
    @GetMapping("/datastores")
    @PreAuthorize("@cepc.hasAnyCePermission('BASE_RESOURCE_ANALYSIS:READ','OVERVIEW:READ')")
    public ResultHolder<List<VmCloudDatastore>> getVmCloudDatastore(@Validated ResourceAnalysisRequest request) {
        return ResultHolder.success(iBaseResourceAnalysisService.getVmCloudDatastore(request));
    }

    @Operation(summary = "查询资源分配情况", description = "查询云账号下资源分配情况")
    @GetMapping("/allocated_info")
    @PreAuthorize("@cepc.hasAnyCePermission('BASE_RESOURCE_ANALYSIS:READ','OVERVIEW:READ')")
    public ResultHolder<Map<String, ResourceAllocatedInfo>> getResourceAllocatedInfo(@Validated ResourceAnalysisRequest request) {
        return ResultHolder.success(iBaseResourceAnalysisService.getResourceAllocatedInfo(request));
    }

    @Operation(summary = "查询资源分布情况", description = "查询云账号下资源分布情况")
    @GetMapping("/spread_info")
    @PreAuthorize("@cepc.hasAnyCePermission('BASE_RESOURCE_ANALYSIS:READ','OVERVIEW:READ')")
    public ResultHolder<Map<String, List<KeyValue>>> getResourceSpreadInfo(@Validated ResourceAnalysisRequest request) {
        return ResultHolder.success(iBaseResourceAnalysisService.getResourceSpreadInfo(request));
    }


    @Operation(summary = "查询资源使用趋势", description = "查询资源使用趋势")
    @GetMapping("/resource_trend")
    @PreAuthorize("@cepc.hasAnyCePermission('BASE_RESOURCE_ANALYSIS:READ')")
    public ResultHolder<List<ChartData>> getResourceUsedTrendData(
            @Validated ResourceUsedTrendRequest resourceUsedTrendRequest) {
        return ResultHolder.success(iBaseResourceAnalysisService.getResourceUsedTrendData(resourceUsedTrendRequest));
    }

    @Operation(summary = "根据云账号查询宿主机数量", description = "根据云账号查询宿主机数量")
    @GetMapping("/cloud_account/host/count")
    @PreAuthorize("@cepc.hasAnyCePermission('BASE_RESOURCE_ANALYSIS:READ','OVERVIEW:READ')")
    public ResultHolder<Long> countHostByCloudAccountId(@RequestParam("cloudAccountId") String cloudAccountId) {
        return ResultHolder.success(iBaseResourceAnalysisService.countHost(cloudAccountId));
    }

    @Operation(summary = "根据云账号查询存储器数量", description = "根据云账号查询存储器数量")
    @GetMapping("/cloud_account/datastore/count")
    @PreAuthorize("@cepc.hasAnyCePermission('BASE_RESOURCE_ANALYSIS:READ','OVERVIEW:READ')")
    public ResultHolder<Long> countDatastoreByCloudAccountId(@RequestParam("cloudAccountId") String cloudAccountId) {
        return ResultHolder.success(iBaseResourceAnalysisService.countDatastore(cloudAccountId));
    }

    @Operation(summary = "查询资源使用情况", description = "查询云账号下资使用情况")
    @GetMapping("/used_info")
    @PreAuthorize("@cepc.hasAnyCePermission('BASE_RESOURCE_ANALYSIS:READ','OVERVIEW:READ')")
    public ResultHolder<Map<String, ResourceAllocatedInfo>> getResourceUsedInfo(@Validated ResourceAnalysisRequest request) {
        return ResultHolder.success(iBaseResourceAnalysisService.getResourceUsedInfo(request));
    }


}
