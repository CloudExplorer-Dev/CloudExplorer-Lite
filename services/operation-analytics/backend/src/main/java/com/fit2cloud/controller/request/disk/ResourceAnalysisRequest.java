package com.fit2cloud.controller.request.disk;

import co.elastic.clients.elasticsearch._types.aggregations.CalendarInterval;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author jianneng
 * @date 2022/12/14 16:02
 **/
@Data
public class ResourceAnalysisRequest {
    @ApiModelProperty("云账号ID")
    private List<String> accountIds;
    @ApiModelProperty("统计块")
    private boolean statisticalBlock;
    @ApiModelProperty("月数")
    private Long monthNumber;
    @ApiModelProperty("分布类型")
    private String spreadType;
    @ApiModelProperty("TOP类型")
    private String topType;
    private CalendarInterval intervalPosition;



}
