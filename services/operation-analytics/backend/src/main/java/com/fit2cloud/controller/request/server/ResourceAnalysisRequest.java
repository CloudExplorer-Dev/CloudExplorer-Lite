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
    @ApiModelProperty("月数")
    private Long monthNumber;
    @ApiModelProperty("监控指标")
    private String metricName;
    @ApiModelProperty("开始时间")
    private long startTime;
    @ApiModelProperty("结束时间")
    private long endTime;

    private CalendarInterval intervalPosition;



}
