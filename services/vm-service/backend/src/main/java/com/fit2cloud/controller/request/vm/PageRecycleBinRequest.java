package com.fit2cloud.controller.request.vm;

import com.fit2cloud.request.pub.OrderRequest;
import com.fit2cloud.request.pub.PageOrderRequestInterface;
import com.fit2cloud.request.pub.PageRequest;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author jianneng
 * @date 2022/9/27 14:45
 **/
@Data
public class PageRecycleBinRequest extends PageRequest implements PageOrderRequestInterface {
    @ApiModelProperty("资源名称")
    private String resourceName;

    @ApiModelProperty("IP地址")
    private String ipArray;

    @ApiModelProperty("关联资源名称")
    private String relateResource;

    @ApiModelProperty("操作人名称")
    private String userName;

    @ApiModelProperty("资源类型")
    private String resourceType;

    @ApiModelProperty("资源状态")
    private String resourceStatus;

    @ApiModelProperty("是否随实例删除")
    private String deleteWithInstance;

    @ApiModelProperty("云账号IDs")
    private List<String> accountIds;

    @ApiModelProperty("组织或者工作空间 ID 集合")
    private List<String> sourceIds;

    @ApiModelProperty("组织 ID 集合")
    private List<String> organizationIds;

    @ApiModelProperty("工作空间 ID 集合")
    private List<String> workspaceIds;

    @ApiModelProperty(value = "排序", example = " {\"column\":\"createTime\",\"asc\":false}")
    private OrderRequest order;
}
