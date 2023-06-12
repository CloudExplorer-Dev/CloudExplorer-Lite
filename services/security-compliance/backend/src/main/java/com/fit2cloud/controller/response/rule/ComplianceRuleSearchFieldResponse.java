package com.fit2cloud.controller.response.rule;

import com.fit2cloud.provider.entity.InstanceFieldType;
import io.swagger.v3.oas.annotations.media.Schema;
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

    @Schema(title = "字段", description = "字段")
    private String field;

    @Schema(title = "提示", description = "提示")
    private String label;

    @Schema(title = "字段类型", description = "字段类型")
    private InstanceFieldType fieldType;

    @Schema(title = "字段可选比较器", description = "字段可选比较器")
    private List<DefaultKeyValue<String, String>> compares;

    @Schema(title = "字段枚举value", description = "字段枚举value")
    private List<DefaultKeyValue<String, Object>> options;

}
