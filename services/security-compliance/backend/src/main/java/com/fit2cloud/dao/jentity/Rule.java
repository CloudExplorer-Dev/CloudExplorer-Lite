package com.fit2cloud.dao.jentity;

import lombok.Data;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2022/12/8  16:51}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
@Data
public class Rule {
    /**
     * 字段
     */
    private String field;
    /**
     * 比较条件
     */
    private String compare;
    /**
     * 值
     */
    private Object value;
}
