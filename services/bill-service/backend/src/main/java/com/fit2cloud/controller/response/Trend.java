package com.fit2cloud.controller.response;

import lombok.Data;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2022/10/28  3:07 PM}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
@Data
public class Trend {
    /**
     * 提示
     */
    private String label;
    /**
     * 值
     */
    private Double value;

}
