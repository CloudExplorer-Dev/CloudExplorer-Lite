package com.fit2cloud.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fit2cloud.controller.handler.ResultHolder;
import com.fit2cloud.controller.request.optimize.PageOptimizeBaseRequest;
import com.fit2cloud.controller.request.optimize.strategy.OptimizeStrategyBaseRequest;
import com.fit2cloud.dto.AnalysisServerDTO;
import com.fit2cloud.dto.OptimizeSuggestDTO;
import com.fit2cloud.service.IOptimizeService;
import com.fit2cloud.service.IOptimizeSuggestStrategyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import java.util.List;

/**
 * 优化建议
 *
 * @author jianneng
 * @date 2022/12/24 11:23
 **/
@RestController
@RequestMapping("/api/optimize")
@Validated
@Tag(name = "资源优化分析相关接口")
public class OptimizeController {
    @Resource
    private IOptimizeService iOptimizeService;

    @Resource
    private IOptimizeSuggestStrategyService iOptimizeSuggestStrategyService;

    @Operation(summary = "查询优化建议", description = "查询优化建议")
    @GetMapping("/server/optimize_suggest")
    @PreAuthorize("@cepc.hasAnyCePermission('SERVER_OPTIMIZE:READ','OVERVIEW:READ')")
    public ResultHolder<List<OptimizeSuggestDTO>> getOptimizeSuggestStrategy() {
        return ResultHolder.success(iOptimizeService.optimizeSuggests());
    }

    @Operation(summary = "资源优化分析", description = "资源优化分析")
    @GetMapping("/server/page")
    @PreAuthorize("@cepc.hasAnyCePermission('SERVER_OPTIMIZE:READ','OVERVIEW:READ')")
    public ResultHolder<IPage<AnalysisServerDTO>> pageServerList(@Validated PageOptimizeBaseRequest request) {
        return ResultHolder.success(iOptimizeService.pageServer(request));
    }

    @Operation(summary = "查询优化策略", description = "查询优化策略")
    @GetMapping("/server/strategy/{optimizeSuggestCode}")
    @PreAuthorize("@cepc.hasAnyCePermission('SERVER_OPTIMIZE:READ','OVERVIEW:READ')")
    public ResultHolder<Object> getOptimizeSuggestStrategy(@PathVariable("optimizeSuggestCode") String optimizeSuggestCode) {
        return ResultHolder.success(iOptimizeSuggestStrategyService.optimizeSuggestStrategy(optimizeSuggestCode));
    }

    @Operation(summary = "修改优化建议策略", description = "修改优化建议策略")
    @PostMapping("/server/strategy/modify")
    @PreAuthorize("@cepc.hasAnyCePermission('SERVER_OPTIMIZE:READ','OVERVIEW:READ')")
    public ResultHolder<Boolean> modifyOptimizeSuggestStrategy(@RequestBody @Valid OptimizeStrategyBaseRequest optimizeStrategyBaseRequest) {
        return ResultHolder.success(iOptimizeSuggestStrategyService.modify(optimizeStrategyBaseRequest));
    }


}
