package com.fit2cloud.request.pub;

import io.swagger.annotations.ApiModelProperty;

/**
 * @Author:张少虎
 * @Date: 2022/8/24  2:10 PM
 * @Version 1.0
 * @注释:
 */
public class OrderRequest {
    /**
     * 排序字段
     */
    @ApiModelProperty("排序字段")
    private String field;
    /**
     * asc
     * desc
     */
    @ApiModelProperty("排序类型 asc desc")
    private String sort;
}
