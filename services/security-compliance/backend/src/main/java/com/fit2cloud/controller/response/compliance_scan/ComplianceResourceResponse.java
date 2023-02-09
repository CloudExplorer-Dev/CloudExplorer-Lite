package com.fit2cloud.controller.response.compliance_scan;

import com.fit2cloud.dao.constants.ComplianceStatus;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Map;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2022/12/26  9:20}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
@Data
public class ComplianceResourceResponse {
    @ApiModelProperty("主键id")
    private String id;
    @ApiModelProperty("供应商")
    private String platform;
    @ApiModelProperty("云账号id")
    private String cloudAccountId;
    @ApiModelProperty("云账号名称")
    private String cloudAccountName;
    @ApiModelProperty("资源类型")
    private String resourceType;
    @ApiModelProperty("资源名称")
    private String resourceName;
    @ApiModelProperty("资源id")
    private String resourceId;
    @ApiModelProperty("扫描状态")
    private ComplianceStatus complianceStatus;
    @ApiModelProperty("实例对象")
    private Map<String, Object> instance;
}
