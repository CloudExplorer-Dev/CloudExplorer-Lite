package com.fit2cloud.controller.response.rule;

import com.fit2cloud.provider.entity.InstanceFieldType;
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
public class ComplianceRuleSearchFieldResponse {
    @ApiModelProperty(value = "字段", notes = "字段")
    private String field;

    @ApiModelProperty(value = "提示", notes = "提示")
    private String label;

    @ApiModelProperty(value = "字段类型", notes = "字段类型")
    private InstanceFieldType fieldType;

    @ApiModelProperty(value = "字段可选比较器", notes = "字段可选比较器")
    private List<DefaultKeyValue<String, String>> compares;

    @ApiModelProperty(value = "字段枚举value", notes = "字段枚举value")
    private List<DefaultKeyValue<String, Object>> options;
}
