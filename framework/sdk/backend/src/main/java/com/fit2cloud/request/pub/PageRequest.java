package com.fit2cloud.request.pub;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @Author:张少虎
 * @Date: 2022/8/24  1:48 PM
 * @Version 1.0
 * @注释:
 */
@Data
public class PageRequest {

    @ApiModelProperty(value = "当前页",required = true)
    @Min(value = 0,message = "当前页不能小于0")
    @NotNull(message = "当前页不能为null")
    private Integer currentPage;

    @ApiModelProperty(value = "每页大小",required = true)
    @Min(value =0,message = "每页大小不能小于0")
    @NotNull(message = "每页大小不能为null")
    private Integer pageSize;
}
