package com.fit2cloud.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author jianneng
 * @date 2022/10/30 20:31
 **/
@Data
public class PerfMonitorEchartsDTO {
    @ApiModelProperty("资源 ID")
    private String resourceId;
    @ApiModelProperty("监控值")
    private List<Object> values;
    @ApiModelProperty("时间戳")
    private List<Long> timestamps;
    @ApiModelProperty("监控指标")
    private String metricName;
    @ApiModelProperty("单位")
    private String unit;
}
