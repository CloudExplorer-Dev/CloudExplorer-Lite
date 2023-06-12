package com.fit2cloud.controller.request.optimize;

import com.fit2cloud.common.validator.annnotaion.CustomValidated;
import com.fit2cloud.common.validator.group.ValidationGroup;
import com.fit2cloud.common.validator.handler.ExistHandler;
import com.fit2cloud.dao.mapper.OptimizationStrategyMapper;
import com.fit2cloud.dto.optimization.OptimizationRule;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

import java.util.List;


/**
 * 描述：创建或更新优化策略请求
 * @author jianneng
 */
@Data
@Validated
public class CreateOrUpdateOptimizationStrategyRequest {

    @ApiModelProperty(value = "主键ID")
    @CustomValidated(groups = {ValidationGroup.SAVE.class}, field = "id", mapper = OptimizationStrategyMapper.class, handler = ExistHandler.class, message = "策略ID重复", exist = true, ifNullPass = true)
    private String id;

    @ApiModelProperty(value = "策略名称", required = true)
    @CustomValidated(groups = {ValidationGroup.SAVE.class}, field = "name", mapper = OptimizationStrategyMapper.class, handler = ExistHandler.class, message = "策略名称重复", exist = true, ifNullPass = true)
    private String name;

    /**
     * MONITORING 监控，OTHER 其他
     */
    @ApiModelProperty(value = "策略类型", required = false)
    private String strategyType;

    @ApiModelProperty(value = "资源类型", required = true)
    private String resourceType;


    @ApiModelProperty(value = "分析数据范围,过去多少天")
    private Long days;


    @ApiModelProperty(value = "优化规则", required = true)
    private List<OptimizationRule> optimizationRules;


    @ApiModelProperty(value = "是否针对所有资源", required = true)
    private Boolean optimizationScope;

    @ApiModelProperty(value = "忽略资源id列表")
    private List<String> ignoreResourceIdList;

}
