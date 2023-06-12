package com.fit2cloud.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fit2cloud.common.log.annotation.OperatedLog;
import com.fit2cloud.common.log.constants.OperatedTypeEnum;
import com.fit2cloud.common.log.constants.ResourceTypeEnum;
import com.fit2cloud.common.validator.annnotaion.CustomValidated;
import com.fit2cloud.common.validator.handler.ExistHandler;
import com.fit2cloud.controller.handler.ResultHolder;
import com.fit2cloud.controller.request.optimize.CreateOrUpdateOptimizationStrategyRequest;
import com.fit2cloud.controller.request.optimize.OptimizationStrategyIgnoreResourceRequest;
import com.fit2cloud.controller.request.optimize.PageOptimizationStrategyRequest;
import com.fit2cloud.controller.request.server.PageServerRequest;
import com.fit2cloud.controller.request.server.ServerRequest;
import com.fit2cloud.dao.entity.OptimizationStrategy;
import com.fit2cloud.dao.mapper.OptimizationStrategyMapper;
import com.fit2cloud.dto.optimization.OptimizationStrategyDTO;
import com.fit2cloud.dto.optimization.ResourceTypeDTO;
import com.fit2cloud.dto.optimization.VmCloudServerDTO;
import com.fit2cloud.service.IOptimizationStrategyIgnoreResourceService;
import com.fit2cloud.service.IOptimizationStrategyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.constraints.NotNull;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author jianneng
 **/
@RestController
@RequestMapping("/api/optimization_strategy")
@Validated
@Tag(name = "优化策略相关接口")
public class OptimizationStrategyController {

    @Resource
    private IOptimizationStrategyService optimizationStrategyService;
    @Resource
    private IOptimizationStrategyIgnoreResourceService optimizationStrategyIgnoreResourceService;

    @Operation(summary = "分页查询优化策略")
    @GetMapping("/page")
    @PreAuthorize("hasAnyCePermission('OPTIMIZATION_STRATEGY:READ')")
    public ResultHolder<IPage<OptimizationStrategyDTO>> pageList(@Validated PageOptimizationStrategyRequest request) {
        return ResultHolder.success(optimizationStrategyService.pageList(request));
    }

    @Operation(summary = "查询优化策略列表")
    @GetMapping("/{resourceType}/list")
    @PreAuthorize("hasAnyCePermission('OPTIMIZATION_STRATEGY:READ')")
    public ResultHolder<List<OptimizationStrategy>> list(@PathVariable("resourceType") String resourceType) {
        return ResultHolder.success(optimizationStrategyService.getOptimizationStrategyList(resourceType));
    }

    @Operation(summary = "查询优化策略")
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyCePermission('OPTIMIZATION_STRATEGY:READ')")
    public ResultHolder<OptimizationStrategyDTO> getOneOptimizationStrategy(@Parameter(description = "主键 ID")
                                                                            @NotNull(message = "优化策略ID不能为空")
                                                                            @CustomValidated(mapper = OptimizationStrategyMapper.class, handler = ExistHandler.class, message = "优化策略不存在", exist = false)
                                                                            @PathVariable("id") String id) {
        return ResultHolder.success(optimizationStrategyService.getOneOptimizationStrategy(id));
    }

    @PostMapping("/add")
    @Operation(summary = "添加优化策略")
    @PreAuthorize("hasAnyCePermission('OPTIMIZATION_STRATEGY:CREATE')")
    @OperatedLog(resourceType = ResourceTypeEnum.OPTIMIZATION_STRATEGY, operated = OperatedTypeEnum.ADD,
            content = "'创建['+#optimizationStrategyRequest.name+']'",
            param = "#optimizationStrategyRequest")
    public ResultHolder<Boolean> save(@RequestBody CreateOrUpdateOptimizationStrategyRequest optimizationStrategyRequest) {
        return ResultHolder.success(optimizationStrategyService.saveOrUpdateStrategy(optimizationStrategyRequest));
    }

    @PostMapping("/update")
    @Operation(summary = "更新优化策略")
    @PreAuthorize("hasAnyCePermission('OPTIMIZATION_STRATEGY:EDIT')")
    @OperatedLog(resourceType = ResourceTypeEnum.OPTIMIZATION_STRATEGY, operated = OperatedTypeEnum.MODIFY,
            content = "'更新了ID为['+#optimizationStrategyRequest.id+']的优化策略'",
            param = "#optimizationStrategyRequest")
    public ResultHolder<Boolean> update(@RequestBody CreateOrUpdateOptimizationStrategyRequest optimizationStrategyRequest) {
        return ResultHolder.success(optimizationStrategyService.saveOrUpdateStrategy(optimizationStrategyRequest));
    }

    @Operation(summary = "删除一个优化策略")
    @PreAuthorize("hasAnyCePermission('OPTIMIZATION_STRATEGY:DELETE')")
    @DeleteMapping("/{optimizationStrategyId}")
    @OperatedLog(resourceType = ResourceTypeEnum.OPTIMIZATION_STRATEGY, operated = OperatedTypeEnum.DELETE,
            resourceId = "#optimizationStrategyId",
            content = "'删除优化策略'",
            param = "#optimizationStrategyId")
    public ResultHolder<Boolean> delete(@Parameter(description = "主键 ID")
                                        @NotNull(message = "优化策略ID不能为空")
                                        @CustomValidated(mapper = OptimizationStrategyMapper.class, handler = ExistHandler.class, message = "优化策略不存在", exist = false)
                                        @PathVariable("optimizationStrategyId") String optimizationStrategyId) {
        return ResultHolder.success(optimizationStrategyService.deleteOneOptimizationStrategy(optimizationStrategyId));
    }

    @PostMapping("/cancel_ignore")
    @Operation(summary = "优化策略忽略资源取消忽略")
    @PreAuthorize("hasAnyCePermission('OPTIMIZATION_STRATEGY_IGNORE_RESOURCE:CANCEL')")
    @OperatedLog(resourceType = ResourceTypeEnum.OPTIMIZATION_STRATEGY_IGNORE_RESOURCE, operated = OperatedTypeEnum.DELETE,
            content = "'取消ID为['+#request.optimizationStrategyId+']的优化策略的已忽略资源'")
    public ResultHolder<Boolean> cancelIgnore(@RequestBody OptimizationStrategyIgnoreResourceRequest request) {
        return ResultHolder.success(optimizationStrategyIgnoreResourceService.batchRemoveOptimizationStrategyIgnoreResource(request.getOptimizationStrategyId(), request.getResourceIdList()));
    }

    @PostMapping("/add_ignore")
    @Operation(summary = "优化策略添加忽略资源")
    @PreAuthorize("hasAnyCePermission('OPTIMIZATION_STRATEGY_IGNORE_RESOURCE:ADD')")
    @OperatedLog(resourceType = ResourceTypeEnum.OPTIMIZATION_STRATEGY_IGNORE_RESOURCE, operated = OperatedTypeEnum.ADD,
            content = "'添加ID为['+#request.optimizationStrategyId+']的优化策略的忽略资源'")
    public ResultHolder<Boolean> addIgnore(@RequestBody OptimizationStrategyIgnoreResourceRequest request) {
        return ResultHolder.success(optimizationStrategyIgnoreResourceService.batchInsertIgnoreResourceByOptimizationStrategyId(request.getOptimizationStrategyId(), request.getResourceIdList()));
    }

    @Operation(summary = "查询资源类型列表")
    @GetMapping("/resource_type/list")
    @PreAuthorize("hasAnyCePermission('OPTIMIZATION_STRATEGY:CREATE')")
    public ResultHolder<List<ResourceTypeDTO>> getResourceTypeList() {
        return ResultHolder.success(optimizationStrategyService.getResourceTypeList());
    }

    @Operation(summary = "启停策略")
    @PreAuthorize("hasAnyCePermission('OPTIMIZATION_STRATEGY:EDIT')")
    @PostMapping(value = "/changeStatus")
    @OperatedLog(resourceType = ResourceTypeEnum.OPTIMIZATION_STRATEGY, operated = OperatedTypeEnum.MODIFY,
            resourceId = "#optimizationStrategyDTO.id",
            content = "#optimizationStrategyDTO.enabled?'启用['+#optimizationStrategyDTO.name+']':'停用['+#optimizationStrategyDTO.name+']'",
            param = "#optimizationStrategyDTO")
    public ResultHolder<Boolean> changeStatus(@RequestBody OptimizationStrategyDTO optimizationStrategyDTO) {
        return ResultHolder.success(optimizationStrategyService.changeStatus(optimizationStrategyDTO));
    }

    @Operation(summary = "分页查询优化策略云主机列表")
    @GetMapping("/server/page")
    @PreAuthorize("hasAnyCePermission('OPTIMIZATION_STRATEGY:READ')")
    public ResultHolder<IPage<VmCloudServerDTO>> pageServer(@Validated PageServerRequest request) {
        return ResultHolder.success(optimizationStrategyIgnoreResourceService.pageVmCloudServerList(request));
    }

    @Operation(summary = "查询优化策略云主机列表")
    @GetMapping("/server/list")
    @PreAuthorize("hasAnyCePermission('OPTIMIZATION_STRATEGY:READ')")
    public ResultHolder<List<VmCloudServerDTO>> serverList(@Validated ServerRequest request) {
        return ResultHolder.success(optimizationStrategyIgnoreResourceService.vmCloudServerList(request));
    }
}
