package com.fit2cloud.dto.optimization;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 描述：监测数据值dto
 * @author jianneng
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MonitoringDataValueDTO {

    @ApiModelProperty("最大值")
    private BigDecimal maxValue;
    @ApiModelProperty("最小值")
    private BigDecimal minValue;
    @ApiModelProperty("平均值")
    private BigDecimal avgValue;

}
