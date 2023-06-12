package com.fit2cloud.controller.request.recycle_bin;

import com.fit2cloud.request.pub.OrderRequest;
import com.fit2cloud.request.pub.PageOrderRequestInterface;
import com.fit2cloud.request.pub.PageRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
public class PageRecycleBinRequest extends PageRequest implements PageOrderRequestInterface {

    @Schema(title = "资源名称")
    private String resourceName;

    @Schema(title = "IP地址")
    private String ipArray;

    @Schema(title = "关联资源名称")
    private String relateResource;

    @Schema(title = "操作人名称")
    private String userName;

    @Schema(title = "资源类型")
    private String resourceType;

    @Schema(title = "资源状态")
    private String resourceStatus;

    @Schema(title = "是否随实例删除")
    private String deleteWithInstance;

    @Schema(title = "云账号IDs")
    private List<String> accountIds;

    @Schema(title = "组织或者工作空间 ID 集合")
    private List<String> sourceIds;

    @Schema(title = "组织 ID 集合")
    private List<String> organizationIds;

    @Schema(title = "工作空间 ID 集合")
    private List<String> workspaceIds;

    @Schema(title = "排序", example = " {\"column\":\"createTime\",\"asc\":false}")
    private OrderRequest order;

}
