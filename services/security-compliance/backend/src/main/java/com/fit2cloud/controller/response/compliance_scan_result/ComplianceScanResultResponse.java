package com.fit2cloud.controller.response.compliance_scan_result;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fit2cloud.constants.ResourceTypeConstants;
import com.fit2cloud.dao.constants.ComplianceStatus;
import com.fit2cloud.dao.constants.ResourceType;
import com.fit2cloud.dao.constants.RiskLevel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2022/12/15  16:56}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
@Data
public class ComplianceScanResultResponse {

    @ApiModelProperty(value = "规则id", notes = "规则id")
    private String id;

    @ApiModelProperty(value = "规则名称", notes = "规则名称")
    private String name;

    @ApiModelProperty(value = "供应商", notes = "供应商")
    private String platform;

    @ApiModelProperty(value = "风险等级", notes = "风险等级")
    private RiskLevel riskLevel;

    @ApiModelProperty(value = "规则描述", notes = "规则描述")
    private String description;

    @ApiModelProperty(value = "资源类型", notes = "资源类型")
    private ResourceTypeConstants resourceType;

    @ApiModelProperty(value = "检查状态", notes = "检查状态")
    private ComplianceStatus scanStatus;

    @ApiModelProperty(value = "合规数量", notes = "合规数量")
    private Long complianceCount;

    @ApiModelProperty(value = "不合规数量", notes = "不合规数量")
    private Long notComplianceCount;

    @ApiModelProperty(value = "最后同步时间", notes = "最后同步时间")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime updateTime;
}
