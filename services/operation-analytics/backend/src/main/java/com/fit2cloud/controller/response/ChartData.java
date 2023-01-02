package com.fit2cloud.controller.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author jianneng
 * @date 2022/12/20 12:14
 **/
@Data
public class ChartData {

    @ApiModelProperty("X轴")
    private String xAxis;

    @ApiModelProperty("Y轴")
    private BigDecimal yAxis = BigDecimal.ZERO;

    @ApiModelProperty("Y轴-右侧")
    private BigDecimal yAxis2 = BigDecimal.ZERO;

    @ApiModelProperty("series名称")
    private String groupName;

    private BigDecimal markPoint = BigDecimal.ZERO;

    @ApiModelProperty("描述")
    private String description;

    @ApiModelProperty("自定义数据")
    private String customId;
}
