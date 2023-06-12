package com.fit2cloud.controller.request.disk;

import co.elastic.clients.elasticsearch._types.aggregations.CalendarInterval;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * @author jianneng
 * @date 2022/12/14 16:02
 **/
@Data
public class ResourceAnalysisRequest {

    @Schema(title = "云账号ID")
    private List<String> accountIds;

    @Schema(title = "统计块")
    private boolean statisticalBlock;

    @Schema(title = "天数")
    private Long dayNumber;

    @Schema(title = "分布类型")
    private String spreadType;

    @Schema(title = "TOP类型")
    private String topType;

    @Schema(title = "工作空间统计")
    private boolean analysisWorkspace;

    private CalendarInterval intervalPosition;

    @Schema(title = "组织或者工作空间 ID 集合")
    private List<String> sourceIds;


}
