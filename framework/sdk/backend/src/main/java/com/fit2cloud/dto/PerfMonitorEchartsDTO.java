package com.fit2cloud.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * @author jianneng
 * @date 2022/10/30 20:31
 **/
@Data
public class PerfMonitorEchartsDTO {

    @Schema(title = "资源 ID")
    private String resourceId;

    @Schema(title = "监控值")
    private List<Object> values;

    @Schema(title = "时间戳")
    private List<Long> timestamps;

    @Schema(title = "监控指标")
    private String metricName;

    @Schema(title = "单位")
    private String unit;
}
