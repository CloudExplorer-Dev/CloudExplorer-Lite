package com.fit2cloud.dto.optimization;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 描述：监测数据值dto
 *
 * @author jianneng
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MonitoringDataValueDTO {

    @Schema(title = "最大值")
    private BigDecimal maxValue;

    @Schema(title = "最小值")
    private BigDecimal minValue;

    @Schema(title = "平均值")
    private BigDecimal avgValue;

}
