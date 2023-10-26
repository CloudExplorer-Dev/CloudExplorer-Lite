package com.fit2cloud.controller;

import com.alibaba.excel.EasyExcelFactory;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fit2cloud.base.entity.CloudAccount;
import com.fit2cloud.base.entity.VmCloudHost;
import com.fit2cloud.controller.handler.ResultHolder;
import com.fit2cloud.controller.request.server.PageServerRequest;
import com.fit2cloud.controller.request.server.ResourceAnalysisRequest;
import com.fit2cloud.controller.request.server.ServerRequest;
import com.fit2cloud.controller.response.ChartData;
import com.fit2cloud.controller.response.TreeNode;
import com.fit2cloud.dto.AnalysisServerDTO;
import com.fit2cloud.dto.KeyValue;
import com.fit2cloud.service.IServerAnalysisService;
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
 * @author jianneng
 * @date 2022/12/24 11:23
 **/
@RestController
@RequestMapping("/api/server_analysis")
@Validated
@Tag(name = "云主机分析相关接口")
public class ServerAnalysisController {
    @Resource
    private IServerAnalysisService iServerAnalysisService;

    @Operation(summary = "分页查询云主机", description = "分页查询云主机")
    @GetMapping("/server/page")
    @PreAuthorize("@cepc.hasAnyCePermission('SERVER_ANALYSIS:READ')")
    public ResultHolder<IPage<AnalysisServerDTO>> pageServerList(@Validated PageServerRequest request) {
        return ResultHolder.success(iServerAnalysisService.pageServer(request));
    }

    @Operation(summary = "云主机明细下载", description = "云主机明细下载")
    @GetMapping("/server/download")
    @PreAuthorize("@cepc.hasAnyCePermission('SERVER_ANALYSIS:READ')")
    public void datastoreListDownload(@Validated ServerRequest request, HttpServletResponse response) {
        List<AnalysisServerDTO> list = iServerAnalysisService.listServer(request);
        try {
            EasyExcelFactory.write(response.getOutputStream(), AnalysisServerDTO.class)
                    .sheet("云主机明细列表")
                    .registerWriteHandler(new CustomCellWriteWidthConfig())
                    .registerWriteHandler(new CustomCellWriteHeightConfig())
                    .registerWriteHandler(EasyExcelUtils.getStyleStrategy())
                    .doWrite(list);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Operation(summary = "查询云账号", description = "查询云账号")
    @GetMapping("/cloud_accounts")
    @PreAuthorize("@cepc.hasAnyCePermission('SERVER_ANALYSIS:READ','OVERVIEW:READ')")
    public ResultHolder<List<CloudAccount>> listCloudAccount() {
        return ResultHolder.success(iServerAnalysisService.getAllCloudAccount());
    }

    @Operation(summary = "查询宿主机", description = "查询云账号下宿主机")
    @GetMapping("/hosts")
    @PreAuthorize("@cepc.hasAnyCePermission('SERVER_ANALYSIS:READ')")
    public ResultHolder<List<VmCloudHost>> getVmHost(@Validated ResourceAnalysisRequest request) {
        return ResultHolder.success(iServerAnalysisService.getVmHost(request));
    }

    @Operation(summary = "根据分布类型查询云主机分布情况", description = "根据分布类型查询云主机分布情况")
    @GetMapping("/spread")
    @PreAuthorize("@cepc.hasAnyCePermission('SERVER_ANALYSIS:READ','OVERVIEW:READ')")
    public ResultHolder<Map<String, List<KeyValue>>> getVmSpreadInfo(@Validated ResourceAnalysisRequest request) {
        return ResultHolder.success(iServerAnalysisService.spread(request));
    }

    @Operation(summary = "查询云主机趋势", description = "查询云主机趋势")
    @GetMapping("/increase_trend")
    @PreAuthorize("@cepc.hasAnyCePermission('SERVER_ANALYSIS:READ','OVERVIEW:READ')")
    public ResultHolder<List<ChartData>> getVmIncreaseTrendData(
            @Validated ResourceAnalysisRequest request) {
        return ResultHolder.success(iServerAnalysisService.vmIncreaseTrend(request));
    }

    @Operation(summary = "查询云主机资源使用趋势", description = "查询云主机资源使用趋势")
    @GetMapping("/resource_used_trend")
    @PreAuthorize("@cepc.hasAnyCePermission('SERVER_ANALYSIS:READ')")
    public ResultHolder<List<ChartData>> getResourceUsedTrendData(
            @Validated ResourceAnalysisRequest request) {
        return ResultHolder.success(iServerAnalysisService.getResourceTrendData(request));
    }

    @Operation(summary = "组织或工作空间云主机分布", description = "组织或工作空间云主机分布")
    @GetMapping("/org_workspace_vm_count_bar")
    @PreAuthorize("@cepc.hasAnyCePermission('SERVER_ANALYSIS:READ')")
    public ResultHolder<Map<String, List<TreeNode>>> analysisVmCloudServerByOrgWorkspace(
            @Validated ResourceAnalysisRequest request) {
        return ResultHolder.success(iServerAnalysisService.analysisVmCloudServerByOrgWorkspace(request));
    }

    @Operation(summary = "根据云账号查询云主机数量", description = "根据云账号查询云主机数量")
    @GetMapping("/cloud_account/cloud_server/count")
    @PreAuthorize("@cepc.hasAnyCePermission('SERVER_ANALYSIS:READ')")
    public ResultHolder<Long> countCloudServerByCloudAccountId(@RequestParam("cloudAccountId") String cloudAccountId) {
        return ResultHolder.success(iServerAnalysisService.countCloudServerByCloudAccount(cloudAccountId));
    }


}
