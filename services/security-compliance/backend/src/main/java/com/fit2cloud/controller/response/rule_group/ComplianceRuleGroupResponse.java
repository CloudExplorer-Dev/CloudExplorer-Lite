package com.fit2cloud.controller.response.rule_group;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2022/12/9  9:55}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
@Data
public class ComplianceRuleGroupResponse {
    @ApiModelProperty(name = "主键id", notes = "主键id")
    private String id;
    @ApiModelProperty(name = "规则组名称", notes = "规则组名称")
    private String name;
    @ApiModelProperty(name = "描述", notes = "描述")
    private String description;
    @ApiModelProperty(name = "是否内置", notes = "是否内置")
    private Boolean builtIn;

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
