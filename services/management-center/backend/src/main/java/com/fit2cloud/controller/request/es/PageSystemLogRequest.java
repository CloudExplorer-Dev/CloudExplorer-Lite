package com.fit2cloud.controller.request.es;

import com.fit2cloud.request.pub.OrderRequest;
import com.fit2cloud.request.pub.PageRequest;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Size;
import java.util.List;

@Data
public class PageSystemLogRequest extends PageRequest {
    /**
     * 详细信息
     */
    @ApiModelProperty("详细信息")
    private String message;

    /**
     * 模块
     */
    @ApiModelProperty("模块")
    private String module;

    /**
     * 等级
     */
    @ApiModelProperty("等级")
    private String level;

    @Size(min = 2,max = 2,message = "{i18n.request.date.message}")
    @ApiModelProperty(value="创建时间",example ="createTime[]=2121&createTime[]=21212")
    private List<Long> createTime;

    @ApiModelProperty(value = "排序",example = " {\"column\":\"createTime\",\"asc\":false}")
    private OrderRequest order;

}
