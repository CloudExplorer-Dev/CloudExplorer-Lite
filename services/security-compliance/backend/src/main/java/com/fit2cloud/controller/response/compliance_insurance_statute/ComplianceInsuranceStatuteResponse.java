package com.fit2cloud.controller.response.compliance_insurance_statute;

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
 * {@code @Date: 2022/12/30  9:56}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
@Data
public class ComplianceInsuranceStatuteResponse {

    @Schema(title = "序号", description = "序号")
    private Integer id;

    @Schema(title = "等级保护基本要求条款", description = "等级保护基本要求条款")
    private String baseClause;

    @Schema(title = "安全层面", description = "安全层面")
    private String securityLevel;

    @Schema(title = "控制点", description = "控制点")
    private String controlPoint;

    @Schema(title = "改进建议", description = "改进建议")
    private String improvementProposal;

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
