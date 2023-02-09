package com.fit2cloud.controller.response.rule;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fit2cloud.dao.constants.RiskLevel;
import com.fit2cloud.dao.jentity.Rules;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2022/12/9  9:54}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
@Data
public class ComplianceRuleResponse {

    @ApiModelProperty(value = "主键id", notes = "主键id")
    private String id;

    @ApiModelProperty(value = "规则名称", notes = "规则名称")
    private String name;

    @ApiModelProperty(value = "规则组id", notes = "规则组id")
    private String ruleGroupId;

    @ApiModelProperty(value = "规则组名称", notes = "规则组名称")
    private String ruleGroupName;

    @ApiModelProperty(value = "云平台", notes = "云平台")
    private String platform;

    @ApiModelProperty(value = "资源类型", notes = "资源类型")
    private String resourceType;

    @ApiModelProperty(value = "规则条件", notes = "规则条件")
    private Rules rules;

    @ApiModelProperty(value = "风险等级", notes = "风险等级")
    private RiskLevel riskLevel;

    @ApiModelProperty(value = "描述", notes = "描述")
    private String description;

    @ApiModelProperty(value = "是否启用", notes = "是否启用")
    private Boolean enable;

    @ApiModelProperty(name = "创建时间", notes = "创建时间")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;

    @ApiModelProperty(name = "修改时间", notes = "修改时间")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime updateTime;
}
