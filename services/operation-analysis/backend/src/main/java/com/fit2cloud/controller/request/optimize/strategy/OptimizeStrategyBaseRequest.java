package com.fit2cloud.controller.request.optimize.strategy;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * @author jianneng
 * @date 2023/4/10 16:31
 **/
@Getter
@Setter
@Validated
public class OptimizeStrategyBaseRequest {
    /**
     * 目前支持4中优化建议
     * 建议降配
     * 建议升配
     * 建议变更付费方式
     * 建议回收资源
     */
    @ApiModelProperty("优化建议")
    @NotNull(message = "优化建议策略code不能为空")
    private String optimizeSuggestCode;
    @ApiModelProperty("资源使用率策略升降配参数")
    @Valid
    private ByResourceUsedRateRequest usedRateRequest;
    @ApiModelProperty("资源状态策略参数")
    private ByResourceStatusRequest statusRequest;
}
