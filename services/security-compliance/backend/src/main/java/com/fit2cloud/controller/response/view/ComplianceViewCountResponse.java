package com.fit2cloud.controller.response.view;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2023/2/10  15:36}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ComplianceViewCountResponse {
    @ApiModelProperty(value = "合规数量", notes = "合规数量")
    private int complianceCount;
    @ApiModelProperty(value = "总数", notes = "总数")
    private int total;
    @ApiModelProperty(value = "不合规数量", notes = "不合规数量")
    private int notComplianceCount;
    @ApiModelProperty(value = "不合规资源占比", notes = "不合规资源占比")
    private double notCompliancePercentage;
}
