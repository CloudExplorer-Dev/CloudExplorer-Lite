package com.fit2cloud.controller.response.rule_group;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
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

    @Schema(title = "主键id", description = "主键id")
    private String id;

    @Schema(title = "规则组名称", description = "规则组名称")
    private String name;

    @Schema(title = "描述", description = "描述")
    private String description;

    @Schema(title = "是否内置", description = "是否内置")
    private Boolean builtIn;

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

}
