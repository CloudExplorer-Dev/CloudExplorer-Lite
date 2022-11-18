package com.fit2cloud.dao.jentity;

import lombok.Data;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2022/10/28  5:52 PM}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
@Data
public class Group {
    /**
     * 分组名称
     */
    private String name;
    /**
     * 字段名称
     */
    private String field;
    /**
     * 分组未命中的名称
     */
    private String missName;
}
