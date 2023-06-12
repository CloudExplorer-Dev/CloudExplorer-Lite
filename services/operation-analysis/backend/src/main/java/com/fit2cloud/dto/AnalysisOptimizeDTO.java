package com.fit2cloud.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author jianneng
 * @date 2023/1/16 10:23
 **/
@Data
public class AnalysisOptimizeDTO extends AnalysisServerDTO {

    @Schema(title = "优化建议")
    private String optimizeSuggest;

    @Schema(title = "建议原因")
    private String content;

}
