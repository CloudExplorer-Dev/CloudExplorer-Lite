package com.fit2cloud.controller.response;

import com.fit2cloud.dto.optimization.OptimizationRuleFieldType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.collections4.keyvalue.DefaultKeyValue;

import java.util.List;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2022/12/9  14:36}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
@Data
public class OptimizationRuleFieldResponse {
    @ApiModelProperty(value = "字段", notes = "字段")
    private String field;

    @ApiModelProperty(value = "提示", notes = "提示")
    private String label;

    @ApiModelProperty(value = "字段类型", notes = "字段类型")
    private OptimizationRuleFieldType fieldType;

    @ApiModelProperty(value = "ES字段", notes = "ES字段")
    private boolean esField;
    @ApiModelProperty(value = "单位", notes = "单位")
    private String unit;

    @ApiModelProperty(value = "字段可选比较器", notes = "字段可选比较器")
    private List<DefaultKeyValue<String, String>> compares;

    @ApiModelProperty(value = "字段枚举value", notes = "字段枚举value")
    private List<DefaultKeyValue<String, Object>> options;
}
