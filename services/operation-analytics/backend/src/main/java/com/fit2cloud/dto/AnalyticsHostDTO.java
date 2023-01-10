package com.fit2cloud.dto;

import com.fit2cloud.base.entity.VmCloudHost;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author jianneng
 * @date 2022/12/11 18:55
 **/
@Data
public class AnalyticsHostDTO extends VmCloudHost {
    @ApiModelProperty("组织名称")
    private String organizationName;
    @ApiModelProperty("工作空间名称")
    private String workspaceName;
    @ApiModelProperty("云账号名称")
    private String accountName;
    @ApiModelProperty("企业项目")
    private String cloudProjectName;
    @ApiModelProperty("云平台")
    private String platform;
}
