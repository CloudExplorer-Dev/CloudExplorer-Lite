package com.fit2cloud.controller.request.base.resource.analysis;

import co.elastic.clients.elasticsearch._types.aggregations.CalendarInterval;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * @author jianneng
 * @date 2022/10/30 20:49
 **/
@Data
public class ResourceUsedTrendRequest {

    @Schema(title = "云账号ID")
    private List<String> accountIds;

    @Schema(title = "资源类型")
    private String entityType;

    @Schema(title = "集群名称")
    private List<String> clusterIds;

    @Schema(title = "资源ID")
    private List<String> resourceIds;

    @Schema(title = "监控指标")
    private String metricName;

    @Schema(title = "开始时间")
    private long startTime;

    @Schema(title = "结束时间")
    private long endTime;

    @Schema(title = "组织或者工作空间 ID 集合")
    private List<String> sourceIds;

    private CalendarInterval intervalPosition;
}
