package com.fit2cloud.dto;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.fit2cloud.base.entity.VmCloudDisk;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author jianneng
 * @date 2022/9/27 14:39
 **/
@EqualsAndHashCode(callSuper = true)
@Data
public class AnalysisDiskDTO extends VmCloudDisk {

    @Schema(title = "组织名称")
    @ExcelIgnore
    private String organizationName;

    @Schema(title = "工作空间名称")
    @ExcelIgnore
    private String workspaceName;

    @Schema(title = "云账号名称")
    @ExcelProperty(value = "云账号名称")
    private String accountName;

    @Schema(title = "所属云主机")
    @ExcelProperty(value = "所属云主机")
    private String vmInstanceName;

    @Schema(title = "所属云平台")
    @ExcelIgnore
    private String platform;

    @Schema(title = "磁盘属性")
    @ExcelIgnore
    private String bootableText;

    @ExcelIgnore
    private String createMonth;

    @ExcelIgnore
    private String deleteMonth;

    /**
     * 统计个数或者size
     */
    @ExcelIgnore
    private Integer value;

}
