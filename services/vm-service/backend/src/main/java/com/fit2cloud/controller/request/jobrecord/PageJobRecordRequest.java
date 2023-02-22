package com.fit2cloud.controller.request.jobrecord;

import com.fit2cloud.common.constants.JobStatusConstants;
import com.fit2cloud.common.constants.JobTypeConstants;
import com.fit2cloud.request.pub.OrderRequest;
import com.fit2cloud.request.pub.PageOrderRequestInterface;
import com.fit2cloud.request.pub.PageRequest;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Size;
import java.io.Serial;
import java.util.List;


@Data
public class PageJobRecordRequest extends PageRequest implements PageOrderRequestInterface {

    @Serial
    private static final long serialVersionUID = 1739312687653996345L;

    @ApiModelProperty("描述")
    private String description;
    @ApiModelProperty("ID")
    private String id;

    private List<JobTypeConstants> type;
    private List<JobStatusConstants> status;

    @Size(min = 2, max = 2, message = "{i18n.request.date.message}")
    @ApiModelProperty(value = "创建时间", example = "createTime[]=2121&createTime[]=21212")
    private List<Long> createTime;
    @Size(min = 2, max = 2, message = "{i18n.request.date.message}")
    @ApiModelProperty(value = "结束时间", example = "finishTime[]=2121&updateTime[]=21212")
    private List<Long> finishTime;
    @ApiModelProperty(value = "排序", example = " {\"column\":\"createTime\",\"asc\":false}")
    private OrderRequest order;

    @ApiModelProperty("当前登录角色有权限查询的组织或者工作空间 ID 集合")
    private List<String> sourceIds;
    @ApiModelProperty("工作空间 ID 集合")
    private List<String> workspaceIds;
    @ApiModelProperty("组织 ID 集合")
    private List<String> organizationIds;
    @ApiModelProperty("关联资源")
    private String resourceName;
    @ApiModelProperty("操作人")
    private String operateUserName;
}
