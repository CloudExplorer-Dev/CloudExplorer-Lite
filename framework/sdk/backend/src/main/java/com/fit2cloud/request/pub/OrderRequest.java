package com.fit2cloud.request.pub;

import com.baomidou.mybatisplus.core.metadata.OrderItem;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;

/**
 * @Author:张少虎
 * @Date: 2022/8/24  2:10 PM
 * @Version 1.0
 * @注释:
 */
@Data
public class OrderRequest extends OrderItem {

    @Serial
    private static final long serialVersionUID = -8075296429060718545L;

    /**
     * 排序字段
     */
    @Schema(title = "排序字段")
    private String column;

    /**
     * asc
     * desc
     */
    @Schema(title = "asc 是否顺序")
    private boolean asc;

    public OrderRequest resetColumn(String column) {
        this.column = column;
        return this;
    }

}
