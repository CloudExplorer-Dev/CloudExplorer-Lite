package com.fit2cloud.controller.request.compliance_scan;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2023/1/30  13:56}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
@Data
public class ComplianceSyncRequest {

    @Schema(title = "需要扫描的云账号id", description = "需要扫描的云账号id")
    @Size(min = 1, message = "云账号最少传入一个")
    @NotNull(message = "云账号不能为空")
    private List<String> cloudAccountIds;

    @Schema(title = "需要扫描的规则组id", description = "需要扫描的规则组id")
    @Size(min = 1, message = "规则组最小传入一个")
    @NotNull(message = "规则组不能为空")
    private List<String> ruleGroupIds;
}
