package com.fit2cloud.controller.request.server;

import co.elastic.clients.elasticsearch._types.aggregations.CalendarInterval;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author jianneng
 * @date 2022/12/14 16:02
 **/
@Data
public class ResourceAnalysisRequest {
    @ApiModelProperty("云账号ID")
    private List<String> accountIds;
    @ApiModelProperty("宿主机ID")
    private List<String> hostIds;
    @ApiModelProperty("分布类型")
    private String spreadType;
    @ApiModelProperty("天数")
    private Long dayNumber;
    @ApiModelProperty("监控指标")
    private String metricName;
    @ApiModelProperty("开始时间")
    private long startTime;
    @ApiModelProperty("结束时间")
    private long endTime;
    @ApiModelProperty("工作空间统计")
    private boolean analysisWorkspace;
    private CalendarInterval intervalPosition;
    @ApiModelProperty("组织或者工作空间 ID 集合")
    private List<String> sourceIds;


}
