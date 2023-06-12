package com.fit2cloud.controller.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author jianneng
 * @date 2022/12/20 12:14
 **/
@Data
public class ChartData {

    @Schema(title = "X轴")
    private String xAxis;

    @Schema(title = "Y轴")
    private BigDecimal yAxis = BigDecimal.ZERO;

    @Schema(title = "Y轴-右侧")
    private BigDecimal yAxis2 = BigDecimal.ZERO;

    @Schema(title = "series名称")
    private String groupName;

    private BigDecimal markPoint = BigDecimal.ZERO;

    @Schema(title = "描述")
    private String description;

    @Schema(title = "自定义数据")
    private String customId;
}
