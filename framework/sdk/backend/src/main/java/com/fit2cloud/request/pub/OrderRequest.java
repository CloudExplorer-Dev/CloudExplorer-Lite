package com.fit2cloud.request.pub;

import com.baomidou.mybatisplus.core.metadata.OrderItem;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author:张少虎
 * @Date: 2022/8/24  2:10 PM
 * @Version 1.0
 * @注释:
 */
@Data
public class OrderRequest extends OrderItem {
    /**
     * 排序字段
     */
    @ApiModelProperty("排序字段")
    private String column;
    /**
     * asc
     * desc
     */
    @ApiModelProperty("asc 是否顺序")
    private boolean asc;

}
