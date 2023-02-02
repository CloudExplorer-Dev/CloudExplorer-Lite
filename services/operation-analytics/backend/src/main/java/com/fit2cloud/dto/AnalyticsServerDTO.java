package com.fit2cloud.dto;

import com.fit2cloud.base.entity.VmCloudServer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * @author jianneng
 * @date 2022/12/24 11:25
 **/
@EqualsAndHashCode(callSuper = true)
@Data
public class AnalyticsServerDTO extends VmCloudServer {

    @ApiModelProperty("组织名称")
    private String organizationName;
    @ApiModelProperty("工作空间名称")
    private String workspaceName;
    @ApiModelProperty("云账号名称")
    private String accountName;
    @ApiModelProperty("企业项目")
    private String cloudProjectName;
    @ApiModelProperty("云平台")
    private String platform;

    private String chargeType;

    private String createMonth;

    private BigDecimal cpuMinimum;
    private BigDecimal cpuMaximum;
    private BigDecimal cpuAverage;

    private BigDecimal memoryMinimum;
    private BigDecimal memoryMaximum;
    private BigDecimal memoryAverage;

    private BigDecimal diskAverage;

    @ApiModelProperty("优化建议")
    private String optimizeSuggest;

    @ApiModelProperty("优化建议类型")
    private String optimizeSuggestCode;
    @ApiModelProperty("建议原因")
    private String content;

}
