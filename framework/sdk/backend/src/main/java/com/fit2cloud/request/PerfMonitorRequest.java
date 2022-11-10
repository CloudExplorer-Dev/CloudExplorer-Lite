package com.fit2cloud.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author jianneng
 * @date 2022/10/30 20:49
 **/
@Data
public class PerfMonitorRequest {
    @ApiModelProperty("云账号ID")
    private String cloudAccountId;
    @ApiModelProperty("资源类型")
    private String entityType;
    @ApiModelProperty("资源ID")
    private String instanceId;
    @ApiModelProperty("监控指标")
    private String metricName;
    @ApiModelProperty("开始时间")
    private long startTime;
    @ApiModelProperty("结束时间")
    private long endTime;
}
