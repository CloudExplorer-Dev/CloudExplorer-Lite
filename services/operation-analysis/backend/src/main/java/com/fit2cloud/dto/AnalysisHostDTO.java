package com.fit2cloud.dto;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.fit2cloud.base.entity.VmCloudHost;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author jianneng
 * @date 2022/12/11 18:55
 **/
@EqualsAndHashCode(callSuper = true)
@Data
public class AnalysisHostDTO extends VmCloudHost {

    @Schema(title = "组织名称")
    @ExcelIgnore
    private String organizationName;

    @Schema(title = "工作空间名称")
    @ExcelIgnore
    private String workspaceName;

    @Schema(title = "云账号名称")
    @ExcelProperty(value = "云账号名称")
    private String accountName;

    @Schema(title = "企业项目")
    @ExcelIgnore
    private String cloudProjectName;

    @Schema(title = "云平台")
    @ExcelIgnore
    private String platform;
}
