package com.fit2cloud.controller.response.compliance_scan_result;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fit2cloud.constants.ResourceTypeConstants;
import com.fit2cloud.dao.constants.ComplianceStatus;
import com.fit2cloud.dao.constants.RiskLevel;
import io.swagger.v3.oas.annotations.media.Schema;
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

    @Schema(title = "规则id", description = "规则id")
    private String id;

    @Schema(title = "规则名称", description = "规则名称")
    private String name;

    @Schema(title = "供应商", description = "供应商")
    private String platform;

    @Schema(title = "风险等级", description = "风险等级")
    private RiskLevel riskLevel;

    @Schema(title = "规则描述", description = "规则描述")
    private String description;

    @Schema(title = "资源类型", description = "资源类型")
    private ResourceTypeConstants resourceType;

    @Schema(title = "检查状态", description = "检查状态")
    private ComplianceStatus scanStatus;

    @Schema(title = "合规数量", description = "合规数量")
    private Long complianceCount;

    @Schema(title = "不合规数量", description = "不合规数量")
    private Long notComplianceCount;

    @Schema(title = "最后同步时间", description = "最后同步时间")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime updateTime;
}
