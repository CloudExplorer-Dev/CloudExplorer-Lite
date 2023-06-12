package com.fit2cloud.controller.request.base.resource.analysis;

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

    @Schema(title = "集群名称")
    private List<String> clusterIds;

    @Schema(title = "宿主机ID")
    private List<String> hostIds;

    @Schema(title = "存储器ID")
    private List<String> datastoreIds;

    @Schema(title = "云主机状态")
    private String vmStatus;

    @Schema(title = "组织或者工作空间 ID 集合")
    private List<String> sourceIds;

}
