package com.fit2cloud.controller.request.compliance_scan;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2023/1/30  13:56}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
@Data
public class ComplianceSyncRequest {

    @ApiModelProperty(value = "需要同步的云账号以及资源", notes = "需要同步的云账号以及资源")
    private List<CloudAccountResource> cloudAccountResources;

    @Data
    public static class CloudAccountResource {
        @ApiModelProperty(value = "云账号id", notes = "云账号id")
        private String cloudAccountId;
        @ApiModelProperty(value = "资源类型", notes = "资源类型")
        private List<String> resourceType;
    }
}
