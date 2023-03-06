package com.fit2cloud.controller.request.user;

import com.fit2cloud.request.pub.OrderRequest;
import com.fit2cloud.request.pub.PageOrderRequestInterface;
import com.fit2cloud.request.pub.PageRequest;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Size;
import java.io.Serial;
import java.util.List;

@Data
public class PageUserRequest extends PageRequest implements PageOrderRequestInterface {

    @Serial
    private static final long serialVersionUID = -5792220537769141979L;

    @ApiModelProperty("用户ID，模糊匹配")
    private String username;

    @ApiModelProperty("姓名，模糊匹配")
    private String name;

    @ApiModelProperty("邮箱，模糊匹配")
    private String email;

    @ApiModelProperty("角色ID，精确匹配")
    private String roleId;

    @ApiModelProperty("角色名称，模糊匹配")
    private String roleName;

    @ApiModelProperty("工作空间ID，精确匹配")
    private String workspaceId;

    @Size(min = 2, max = 2, message = "{i18n.request.date.message}")
    @ApiModelProperty(value = "创建时间", example = "createTime[]=2121&createTime[]=21212")
    private List<Long> createTime;

    @Size(min = 2, max = 2, message = "{i18n.request.date.message}")
    @ApiModelProperty(value = "修改时间", example = "updateTime[]=2121&updateTime[]=21212")
    private List<Long> updateTime;

    @ApiModelProperty(value = "排序", example = " {\"column\":\"createTime\",\"asc\":false}")
    private OrderRequest order;
}
