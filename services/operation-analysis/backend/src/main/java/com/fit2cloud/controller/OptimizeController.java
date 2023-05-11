package com.fit2cloud.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fit2cloud.controller.handler.ResultHolder;
import com.fit2cloud.controller.request.optimize.PageOptimizeBaseRequest;
import com.fit2cloud.controller.request.optimize.strategy.OptimizeStrategyBaseRequest;
import com.fit2cloud.dto.AnalysisServerDTO;
import com.fit2cloud.dto.OptimizeSuggestDTO;
import com.fit2cloud.service.IOptimizeService;
import com.fit2cloud.service.IOptimizeSuggestStrategyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
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
@Api("资源优化分析相关接口")
public class OptimizeController {
    @Resource
    private IOptimizeService iOptimizeService;

    @Resource
    private IOptimizeSuggestStrategyService iOptimizeSuggestStrategyService;

    @ApiOperation(value = "查询优化建议", notes = "查询优化建议")
    @GetMapping("/server/optimize_suggest")
    @PreAuthorize("hasAnyCePermission('SERVER_OPTIMIZE:READ','OVERVIEW:READ')")
    public ResultHolder<List<OptimizeSuggestDTO>> getOptimizeSuggestStrategy() {
        return ResultHolder.success(iOptimizeService.optimizeSuggests());
    }

    @ApiOperation(value = "资源优化分析", notes = "资源优化分析")
    @GetMapping("/server/page")
    @PreAuthorize("hasAnyCePermission('SERVER_OPTIMIZE:READ','OVERVIEW:READ')")
    public ResultHolder<IPage<AnalysisServerDTO>> pageServerList(@Validated PageOptimizeBaseRequest request) {
        return ResultHolder.success(iOptimizeService.pageServer(request));
    }

    @ApiOperation(value = "查询优化策略", notes = "查询优化策略")
    @GetMapping("/server/strategy/{optimizeSuggestCode}")
    @PreAuthorize("hasAnyCePermission('SERVER_OPTIMIZE:READ','OVERVIEW:READ')")
    public ResultHolder<Object> getOptimizeSuggestStrategy(@PathVariable("optimizeSuggestCode") String optimizeSuggestCode) {
        return ResultHolder.success(iOptimizeSuggestStrategyService.optimizeSuggestStrategy(optimizeSuggestCode));
    }

    @ApiOperation(value = "修改优化建议策略", notes = "修改优化建议策略")
    @PostMapping("/server/strategy/modify")
    @PreAuthorize("hasAnyCePermission('SERVER_OPTIMIZE:READ','OVERVIEW:READ')")
    public ResultHolder<Boolean> modifyOptimizeSuggestStrategy(@RequestBody @Valid OptimizeStrategyBaseRequest optimizeStrategyBaseRequest) {
        return ResultHolder.success(iOptimizeSuggestStrategyService.modify(optimizeStrategyBaseRequest));
    }


}
