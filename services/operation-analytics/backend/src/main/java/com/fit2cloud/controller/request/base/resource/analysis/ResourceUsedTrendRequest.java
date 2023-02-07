package com.fit2cloud.controller.request.base.resource.analysis;

import co.elastic.clients.elasticsearch._types.aggregations.CalendarInterval;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author jianneng
 * @date 2022/10/30 20:49
 **/
@Data
public class ResourceUsedTrendRequest {
    @ApiModelProperty("云账号ID")
    private List<String> accountIds;
    @ApiModelProperty("资源类型")
    private String entityType;
    @ApiModelProperty("集群名称")
    private List<String> clusterIds;
    @ApiModelProperty("资源ID")
    private List<String> resourceIds;
    @ApiModelProperty("监控指标")
    private String metricName;
    @ApiModelProperty("开始时间")
    private long startTime;
    @ApiModelProperty("结束时间")
    private long endTime;
    @ApiModelProperty("组织或者工作空间 ID 集合")
    private List<String> sourceIds;

    private CalendarInterval intervalPosition;
}
