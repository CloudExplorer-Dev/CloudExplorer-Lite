package com.fit2cloud.controller.response.compliance_scan;

import com.fit2cloud.dao.constants.ComplianceStatus;
import io.swagger.v3.oas.annotations.media.Schema;
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

    @Schema(title = "主键id")
    private String id;

    @Schema(title = "供应商")
    private String platform;

    @Schema(title = "云账号id")
    private String cloudAccountId;

    @Schema(title = "云账号名称")
    private String cloudAccountName;

    @Schema(title = "资源类型")
    private String resourceType;

    @Schema(title = "资源名称")
    private String resourceName;

    @Schema(title = "资源id")
    private String resourceId;

    @Schema(title = "扫描状态")
    private ComplianceStatus complianceStatus;

    @Schema(title = "实例对象")
    private Map<String, Object> instance;
}
