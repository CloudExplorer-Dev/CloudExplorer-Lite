package com.fit2cloud.controller.request.base.resource.analysis;

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
    @ApiModelProperty("集群名称")
    private List<String> clusterIds;
    @ApiModelProperty("宿主机ID")
    private List<String> hostIds;
    @ApiModelProperty("存储器ID")
    private List<String> datastoreIds;
    @ApiModelProperty("云主机状态")
    private String vmStatus;

}
