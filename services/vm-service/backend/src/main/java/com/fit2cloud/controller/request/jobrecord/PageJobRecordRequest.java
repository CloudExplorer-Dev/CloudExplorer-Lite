package com.fit2cloud.controller.request.jobrecord;

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

    @ApiModelProperty("任务")
    private String description;


    @Size(min = 2, max = 2, message = "{i18n.request.date.message}")
    @ApiModelProperty(value = "创建时间", example = "createTime[]=2121&createTime[]=21212")
    private List<Long> createTime;
    @Size(min = 2, max = 2, message = "{i18n.request.date.message}")
    @ApiModelProperty(value = "结束时间", example = "finishTime[]=2121&updateTime[]=21212")
    private List<Long> finishTime;
    @ApiModelProperty(value = "排序", example = " {\"column\":\"createTime\",\"asc\":false}")
    private OrderRequest order;
}