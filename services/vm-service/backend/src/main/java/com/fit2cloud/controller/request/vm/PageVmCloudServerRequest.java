package com.fit2cloud.controller.request.vm;

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
public class PageVmCloudServerRequest extends PageRequest {
    @ApiModelProperty(value = "云主机状态", allowableValues = "Running,Stopped,Deleted")
    private List<String> instanceStatus;
    @ApiModelProperty("云主机名称")
    private String instanceName;
    @ApiModelProperty("云账号IDs")
    private List<String>accountIds;
    @ApiModelProperty("云主机ID")
    private String cloudServerId;
    @ApiModelProperty("云账号名称")
    private String accountName;
    @ApiModelProperty("数据中心")
    private String region;
    @ApiModelProperty("集群")
    private String zone;
    @ApiModelProperty("公网IP")
    private String remoteIp;
    @ApiModelProperty("产品名称")
    private String productName;
    @ApiModelProperty("IP地址")
    private String ipArray;
    @ApiModelProperty("工作空间ID")
    private String workspaceId;
    @ApiModelProperty("工作空间IDs")
    private List<String> workspaceIds;
    @ApiModelProperty("组织ID")
    private String organizationId;
    @ApiModelProperty("组织IDs")
    private List<String> organizationIds;
    @ApiModelProperty("组织名称")
    private String organizationName;
    @ApiModelProperty("操作系统")
    private String os;
    @ApiModelProperty("操作系统版本")
    private List<String> osVersion;
    @ApiModelProperty("云主机ID")
    private List<String> ids;
    @ApiModelProperty(hidden = true)
    private String sort;
    @ApiModelProperty("projectId")
    private String projectId;
    @ApiModelProperty("操作系统信息")
    private String osInfo;
    @ApiModelProperty("主机名")
    private String hostname;
    @ApiModelProperty("申请人")
    private String applyUser;
    @Size(min = 2, max = 2, message = "{i18n.request.date.message}")
    @ApiModelProperty(value = "创建时间", example = "createTime[]=2121&createTime[]=21212")
    private List<Long> createTime;
    @Size(min = 2, max = 2, message = "{i18n.request.date.message}")
    @ApiModelProperty(value = "到期时间", example = "updateTime[]=2121&updateTime[]=21212")
    private List<Long> expireTime;
    @ApiModelProperty(value = "排序", example = " {\"column\":\"createTime\",\"asc\":false}")
    private OrderRequest order;
}
