package com.fit2cloud.controller.request.disk;

import com.fit2cloud.request.pub.OrderRequest;
import com.fit2cloud.request.pub.PageRequest;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Size;
import java.util.List;

/**
 * @author jianneng
 * @date 2022/9/27 14:45
 **/
@Data
public class PageVmCloudDiskRequest extends PageRequest {
    @ApiModelProperty("名称")
    private String diskName;
    @ApiModelProperty("组织ID")
    private String organizationId;
    @ApiModelProperty("组织IDs")
    private List<String> organizationIds;
    @ApiModelProperty("组织名称")
    private String organizationName;
    @ApiModelProperty("工作空间ID")
    private String workspaceId;
    @ApiModelProperty("工作空间名称")
    private String workspaceName;
    @ApiModelProperty("云账号名称")
    private String accountName;
    @ApiModelProperty("所属云主机")
    private String vmInstanceName;
    @Size(min = 2, max = 2, message = "{i18n.request.date.message}")
    @ApiModelProperty(value = "创建时间", example = "createTime[]=2121&createTime[]=21212")
    private List<Long> createTime;
    @Size(min = 2, max = 2, message = "{i18n.request.date.message}")
    @ApiModelProperty(value = "到期时间", example = "updateTime[]=2121&updateTime[]=21212")
    private List<Long> expireTime;
    @ApiModelProperty(value = "排序", example = " {\"column\":\"createTime\",\"asc\":false}")
    private OrderRequest order;
}
