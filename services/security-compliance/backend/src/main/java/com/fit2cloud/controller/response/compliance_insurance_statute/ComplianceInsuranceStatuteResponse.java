package com.fit2cloud.controller.response.compliance_insurance_statute;

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
 * {@code @Date: 2022/12/30  9:56}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
@Data
public class ComplianceInsuranceStatuteResponse {

    @ApiModelProperty(value = "序号", notes = "序号")
    private Integer id;

    @ApiModelProperty(value = "等级保护基本要求条款", notes = "等级保护基本要求条款")
    private String baseClause;

    @ApiModelProperty(value = "安全层面", notes = "安全层面")
    private String securityLevel;

    @ApiModelProperty(value = "控制点", notes = "控制点")
    private String controlPoint;

    @ApiModelProperty(value = "改进建议", notes = "改进建议")
    private String improvementProposal;

    @ApiModelProperty(value = "创建时间", notes = "创建时间")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "修改时间", notes = "修改时间")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime updateTime;
}
