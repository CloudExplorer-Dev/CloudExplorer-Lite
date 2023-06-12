package com.fit2cloud.dto.optimization;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author jianneng
 * @date 2022/9/27 14:39
 **/
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class VmCloudServerExcelDTO {

    private static final long serialVersionUID = 2780061897725836746L;

    @ExcelProperty(value = "组织名称")
    private String organizationName;
    @ExcelProperty("工作空间")
    private String workspaceName;
    @ExcelProperty(value = "云账号名称", index = 1)
    private String accountName;
    @ExcelProperty("企业项目")
    private String cloudProjectName;
    @ExcelProperty("申请用户名")
    private String applyUserName;
    @ExcelProperty(value = "CPU使用率最大值", index = 5)
    private Double cpuMaxValue;
    @ExcelProperty(value = "CPU使用率平均值", index = 6)
    private Double cpuAvgValue;
    @ExcelProperty(value = "内存使用率最大值", index = 7)
    private Double memoryMaxValue;
    @ExcelProperty(value = "内存使用率平均值", index = 8)
    private Double memoryAvgValue;
    @ExcelProperty(value = "优化建议", index = 4)
    private String content;
    @ExcelProperty(value = "实例名称", index = 0)
    private String instanceName;
    @ExcelProperty(value = "实例类型", index = 3)
    private String instanceTypeDescription;
    @ExcelProperty(value = "IP 地址", index = 2)
    private String ipArray;
    @ExcelProperty("管理 IP")
    private String managementIp;
    @ExcelProperty("计费方式")
    private String instanceChargeType;

}
