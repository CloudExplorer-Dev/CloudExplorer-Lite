package com.fit2cloud.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fit2cloud.controller.handler.ResultHolder;
import com.fit2cloud.controller.request.server.PageServerRequest;
import com.fit2cloud.dto.optimization.VmCloudServerDTO;
import com.fit2cloud.service.ICloudServerOptimizationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

/**
 * 查询优化策略资源
 *
 * @author jianneng
 **/
@RestController
@RequestMapping("/api/optimization_strategy_resource")
@Validated
@Tag(name = "优化策略资源相关接口")
public class OptimizationStrategyResourceController {

    @Resource
    private ICloudServerOptimizationService iCloudServerOptimizationService;

    @Operation(summary = "云主机优化")
    @GetMapping("/server/page")
    @PreAuthorize("hasAnyCePermission('SERVER_OPTIMIZATION:READ','OVERVIEW:READ')")
    public ResultHolder<IPage<VmCloudServerDTO>> pageServerList(@Validated PageServerRequest request) {
        return ResultHolder.success(iCloudServerOptimizationService.pageList(request));
    }

    @Operation(summary = "导出云主机优化列表")
    @GetMapping("/server/download/{version}")
    public void download(@Validated PageServerRequest request, @PathVariable(value = "version") String version, HttpServletResponse response) {
        try {
            iCloudServerOptimizationService.downloadExcel(request, version, response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
