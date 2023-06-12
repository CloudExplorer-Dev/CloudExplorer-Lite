package com.fit2cloud.controller.request.optimize;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * @author jianneng
 * @date 2023/1/16 11:20
 **/
@Data
public class OptimizationRequest {

    @Schema(title = "最近天数")
    private Long days;

    @Schema(title = "CPU使用率")
    private Float cpuRate;

    @Schema(title = "CPU最大使用率")
    private boolean cpuMaxRate;

    @Schema(title = "内存使用率")
    private Float memoryRate;

    @Schema(title = "内存最大使用率")
    private boolean memoryMaxRate;

    @Schema(title = "云账号ID")
    private String accountId;

    @Schema(title = "云账号ID列表")
    private List<String> accountIds;

    @Schema(title = "云主机名称")
    private String instanceName;

    @Schema(title = "优化建议")
    private String optimizeSuggest;

    @Schema(title = "包周期资源持续开机")
    private boolean cycleContinuedRunning;

    @Schema(title = "包周期资源持续天数")
    private Long cycleContinuedDays;

    @Schema(title = "按需按量资源持续开机")
    private boolean volumeContinuedRunning;

    @Schema(title = "按需按量资源持续天数")
    private Long volumeContinuedDays;

    @Schema(title = "资源持续开机")
    private boolean continuedRunning;

    @Schema(title = "资源持续天数")
    private Long continuedDays;

    @Schema(title = "条件，OR、AND")
    private String conditionOr;

    private List<String> instanceIds;

    private List<String> instanceUuids;

    @Schema(title = "组织或者工作空间 ID 集合")
    private List<String> sourceIds;

    @Schema(title = "ES上CPU内存最新的同步数据时间点")
    private Long esLastTime;

}
