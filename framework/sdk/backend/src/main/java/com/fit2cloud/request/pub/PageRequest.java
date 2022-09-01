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
    @Min(value = 0,message = "{i18n.page.current_page.min}")
    @NotNull(message = "{i18n.page.current_page.is.not.empty}")
    private Integer currentPage;

    @ApiModelProperty(value = "每页大小",required = true)
    @Min(value =0,message = "{i18n.page.page.size.min}")
    @NotNull(message = "{i18n.page.page.size.is.not.empty}")
    private Integer pageSize;
}
