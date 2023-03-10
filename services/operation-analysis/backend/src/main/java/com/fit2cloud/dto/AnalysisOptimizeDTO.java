package com.fit2cloud.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author jianneng
 * @date 2023/1/16 10:23
 **/
@Data
public class AnalysisOptimizeDTO extends AnalysisServerDTO {

    @ApiModelProperty("优化建议")
    private String optimizeSuggest;
    @ApiModelProperty("建议原因")
    private String content;

}
