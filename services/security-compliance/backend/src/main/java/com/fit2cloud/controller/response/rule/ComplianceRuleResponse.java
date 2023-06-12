package com.fit2cloud.controller.response.rule;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fit2cloud.dao.constants.RiskLevel;
import com.fit2cloud.dao.entity.ComplianceRule;
import com.fit2cloud.dao.jentity.Rules;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

import java.time.LocalDateTime;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2022/12/9  9:54}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
@Data
@NoArgsConstructor
public class ComplianceRuleResponse {

    @Schema(title = "主键id", description = "主键id")
    private String id;

    @Schema(title = "规则名称", description = "规则名称")
    private String name;

    @Schema(title = "规则组id", description = "规则组id")
    private String ruleGroupId;

    @Schema(title = "规则组名称", description = "规则组名称")
    private String ruleGroupName;

    @Schema(title = "云平台", description = "云平台")
    private String platform;

    @Schema(title = "资源类型", description = "资源类型")
    private String resourceType;

    @Schema(title = "规则条件", description = "规则条件")
    private Rules rules;

    @Schema(title = "风险等级", description = "风险等级")
    private RiskLevel riskLevel;

    @Schema(title = "描述", description = "描述")
    private String description;

    @Schema(title = "是否启用", description = "是否启用")
    private Boolean enable;

    @Schema(title = "创建时间", description = "创建时间")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;

    @Schema(title = "修改时间", description = "修改时间")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime updateTime;

    public ComplianceRuleResponse(ComplianceRule complianceRule) {
        BeanUtils.copyProperties(complianceRule, this);
    }
}
