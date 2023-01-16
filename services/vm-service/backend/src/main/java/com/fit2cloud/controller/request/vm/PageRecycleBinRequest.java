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

    @ApiModelProperty("组织或者工作空间 ID 集合")
    private List<String> sourceIds;

    @ApiModelProperty(value = "排序", example = " {\"column\":\"createTime\",\"asc\":false}")
    private OrderRequest order;
}
