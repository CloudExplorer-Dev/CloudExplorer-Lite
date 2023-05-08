package com.fit2cloud.controller.request.optimize.strategy;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * 按资源使用率进行优化建议
 *
 * @author jianneng
 * @date 2023/4/6 15:20
 **/
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Validated
public class ByResourceUsedRateRequest {

    @ApiModelProperty("比较条件，OR，AND")
    @NotNull(message = "比较条件不能为空")
    private String comparisonCondition;
    @ApiModelProperty("时间，天数")
    @NotNull(message = "天数不能为空")
    private Long days;
    @ApiModelProperty("CPU比较值")
    @Min(value = 1, message = "CPU比较值最小值为1")
    @Max(value = 100, message = "CPU比较值最大值为100")
    @NotNull(message = "CPU比较值不能为空")
    private Long cpuValue;
    @ApiModelProperty("CPU比较取值，AVG,MAX,MIN")
    @NotNull(message = "CPU比较取值不能为空")
    private String cpuComparisonValue;
    @ApiModelProperty("内存比较值")
    @Min(value = 1, message = "内存比较值最小值为1")
    @Max(value = 100, message = "内存比较值最大值为100")
    @NotNull(message = "内存比较值不能为空")
    private Long memoryValue;
    @ApiModelProperty("内存比较取值，AVG,MAX,MIN")
    @NotNull(message = "内存比较取值不能为空")
    private String memoryComparisonValue;

    /**
     * ES上CPU内存最新的同步数据时间点
     */
    private Long esLastTime;
    /**
     * 升配
     */
    private boolean upgrade;


}
