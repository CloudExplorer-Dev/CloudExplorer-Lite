package com.fit2cloud.controller.response.view;

import io.swagger.v3.oas.annotations.media.Schema;
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

    @Schema(title = "合规数量", description = "合规数量")
    private int complianceCount;

    @Schema(title = "总数", description = "总数")
    private int total;

    @Schema(title = "不合规数量", description = "不合规数量")
    private int notComplianceCount;

    @Schema(title = "不合规资源占比", description = "不合规资源占比")
    private double notCompliancePercentage;
}
