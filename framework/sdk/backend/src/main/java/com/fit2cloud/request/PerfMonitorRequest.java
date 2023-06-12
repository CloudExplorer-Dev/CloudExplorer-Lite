package com.fit2cloud.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author jianneng
 * @date 2022/10/30 20:49
 **/
@Data
public class PerfMonitorRequest {

    @Schema(title = "云账号ID")
    private String cloudAccountId;

    @Schema(title = "资源类型")
    private String entityType;

    @Schema(title = "资源ID")
    private String instanceId;

    @Schema(title = "监控指标")
    private String metricName;

    @Schema(title = "开始时间")
    private long startTime;

    @Schema(title = "结束时间")
    private long endTime;
}
