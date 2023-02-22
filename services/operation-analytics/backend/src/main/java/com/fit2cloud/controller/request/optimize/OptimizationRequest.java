package com.fit2cloud.controller.request.optimize;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author jianneng
 * @date 2023/1/16 11:20
 **/
@Data
public class OptimizationRequest {

    @ApiModelProperty("最近天数")
    private Long days;
    @ApiModelProperty("CPU使用率")
    private Float cpuRate;
    @ApiModelProperty("CPU最大使用率")
    private boolean cpuMaxRate;
    @ApiModelProperty("内存使用率")
    private Float memoryRate;
    @ApiModelProperty("内存最大使用率")
    private boolean memoryMaxRate;
    @ApiModelProperty("云账号ID")
    private String accountId;
    @ApiModelProperty("云账号IDS")
    private List<String> accountIds;
    @ApiModelProperty("云主机名称")
    private String instanceName;
    @ApiModelProperty("优化建议")
    private String optimizeSuggest;
    @ApiModelProperty("包周期资源持续开机")
    private boolean cycleContinuedRunning;
    @ApiModelProperty("包周期资源持续天数")
    private Long cycleContinuedDays;
    @ApiModelProperty("按需按量资源持续开机")
    private boolean volumeContinuedRunning;
    @ApiModelProperty("按需按量资源持续天数")
    private Long volumeContinuedDays;
    @ApiModelProperty("资源持续开机")
    private boolean continuedRunning;
    @ApiModelProperty("资源持续天数")
    private Long continuedDays;

    @ApiModelProperty("条件，OR、AND")
    private String conditionOr;

    private List<String> instanceIds;
    private List<String> instanceUuids;

    @ApiModelProperty("组织或者工作空间 ID 集合")
    private List<String> sourceIds;

    @ApiModelProperty("ES上CPU内存最新的同步数据时间点")
    private Long esLastTime;

}
