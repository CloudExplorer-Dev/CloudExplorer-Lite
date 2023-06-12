package com.fit2cloud.dto;

import com.fit2cloud.base.entity.VmCloudServer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * @author jianneng
 * @date 2022/12/24 11:25
 **/
@EqualsAndHashCode(callSuper = true)
@Data
public class AnalysisServerDTO extends VmCloudServer {

    @Schema(title = "组织名称")
    private String organizationName;

    @Schema(title = "工作空间名称")
    private String workspaceName;

    @Schema(title = "云账号名称")
    private String accountName;

    @Schema(title = "企业项目")
    private String cloudProjectName;

    @Schema(title = "云平台")
    private String platform;

    private String chargeType;

    private String createMonth;

    private String deleteMonth;

    private BigDecimal cpuMinimum;

    private BigDecimal cpuMaximum;

    private BigDecimal cpuAverage;

    private BigDecimal memoryMinimum;

    private BigDecimal memoryMaximum;

    private BigDecimal memoryAverage;

    private BigDecimal diskAverage;

    @Schema(title = "优化建议")
    private String optimizeSuggest;

    @Schema(title = "优化建议类型")
    private String optimizeSuggestCode;

    @Schema(title = "建议原因")
    private String content;

}
