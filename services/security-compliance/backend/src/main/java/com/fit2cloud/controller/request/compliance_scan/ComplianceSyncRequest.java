package com.fit2cloud.controller.request.compliance_scan;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2023/1/30  13:56}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
@Data
public class ComplianceSyncRequest {
    @ApiModelProperty(value = "需要扫描的云账号id", notes = "需要扫描的云账号id")
    @Size(min = 1, message = "云账号最少传入一个")
    @NotNull(message = "云账号不能为空")
    private List<String> cloudAccountIds;
    @ApiModelProperty(value = "需要扫描的规则组id", notes = "需要扫描的规则组id")
    @Size(min = 1, message = "规则组最小传入一个")
    @NotNull(message = "规则组不能为空")
    private List<String> ruleGroupIds;
}
