package com.fit2cloud.dto;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
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
    @ExcelIgnore
    private String organizationName;

    @Schema(title = "工作空间名称")
    @ExcelIgnore
    private String workspaceName;

    @Schema(title = "云账号名称")
    @ExcelProperty("云账号")
    private String accountName;

    @Schema(title = "企业项目")
    @ExcelIgnore
    private String cloudProjectName;

    @Schema(title = "云平台")
    @ExcelIgnore
    private String platform;
    @ExcelIgnore
    private String chargeType;
    @ExcelIgnore
    private String createMonth;
    @ExcelIgnore
    private String deleteMonth;
    @ExcelIgnore
    private BigDecimal cpuMinimum;
    @ExcelProperty("CPU最大使用率")
    private BigDecimal cpuMaximum;
    @ExcelProperty("CPU平均使用率")
    private BigDecimal cpuAverage;
    @ExcelIgnore
    private BigDecimal memoryMinimum;
    @ExcelProperty("内存最大使用率")
    private BigDecimal memoryMaximum;
    @ExcelProperty("内存平均使用率")
    private BigDecimal memoryAverage;
    @ExcelIgnore
    private BigDecimal diskAverage;

    @Schema(title = "优化建议")
    @ExcelIgnore
    private String optimizeSuggest;

    @Schema(title = "优化建议类型")
    @ExcelIgnore
    private String optimizeSuggestCode;

    @Schema(title = "建议原因")
    @ExcelIgnore
    private String content;

}
