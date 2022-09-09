package com.fit2cloud.controller.request.user;

import com.fit2cloud.request.pub.OrderRequest;
import com.fit2cloud.request.pub.PageRequest;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class PageUserRequest extends PageRequest {
    @ApiModelProperty("用户ID，模糊匹配")
    private String username;

    @ApiModelProperty("姓名，模糊匹配")
    private String name;

    @ApiModelProperty("邮箱，模糊匹配")
    private String email;

    @ApiModelProperty("角色ID，精确匹配")
    private String roleId;

    @ApiModelProperty(value = "排序", example = " {\"column\":\"createTime\",\"asc\":false}")
    private OrderRequest order;
}
