package com.fit2cloud.controller.request;

import com.fit2cloud.request.pub.OrderRequest;
import com.fit2cloud.request.pub.PageRequest;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Size;
import java.util.List;

/**
 * @Author:张少虎
 * @Date: 2022/8/24  1:54 PM
 * @Version 1.0
 * @注释:
 */
@Data
public class PageOrganizationRequest extends PageRequest {
    @ApiModelProperty("组织名称")
    private String name;

    @Size(min = 2, max = 2, message = "{i18n.request.date.message}")
    @ApiModelProperty(value = "创建时间", example = "createTime[]=2121&createTime[]=21212")
    private List<Long> createTime;

    @Size(min = 2, max = 2, message = "{i18n.request.date.message}")
    @ApiModelProperty(value = "修改时间", example = "updateTime[]=2121&updateTime[]=21212")
    private List<Long> updateTime;

    @ApiModelProperty(value = "排序", example = " {\"column\":\"createTime\",\"asc\":false}")
    private OrderRequest order;
}
