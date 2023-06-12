package com.fit2cloud.controller.request.server;

import co.elastic.clients.elasticsearch._types.aggregations.CalendarInterval;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * @author jianneng
 * @date 2022/12/14 16:02
 **/
@Data
public class ResourceAnalysisRequest {

    @Schema(title = "云账号ID")
    private List<String> accountIds;

    @Schema(title = "宿主机ID")
    private List<String> hostIds;

    @Schema(title = "分布类型")
    private String spreadType;

    @Schema(title = "天数")
    private Long dayNumber;

    @Schema(title = "监控指标")
    private String metricName;

    @Schema(title = "开始时间")
    private long startTime;

    @Schema(title = "结束时间")
    private long endTime;

    @Schema(title = "工作空间统计")
    private boolean analysisWorkspace;

    private CalendarInterval intervalPosition;

    @Schema(title = "组织或者工作空间 ID 集合")
    private List<String> sourceIds;


}
