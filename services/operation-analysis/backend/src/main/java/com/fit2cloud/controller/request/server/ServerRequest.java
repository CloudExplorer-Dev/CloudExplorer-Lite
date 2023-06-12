package com.fit2cloud.controller.request.server;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * @author jianneng
 * @date 2022/12/11 19:03
 **/
@Accessors(chain = true)
@Data
public class ServerRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 8484185034593083260L;
    @ApiModelProperty("名称")
    private String instanceName;
    @ApiModelProperty("云账号IDs")
    private List<String> accountIds;
    @ApiModelProperty("组织或者工作空间 ID 集合")
    private List<String> sourceIds;
    @ApiModelProperty("IP地址")
    private String ipArray;
    @ApiModelProperty("优化策略ID")
    private String optimizationStrategyId;
    @ApiModelProperty("忽略的")
    private boolean ignore;

}
