package com.fit2cloud.common.query.convert;

import lombok.Builder;
import lombok.Data;

import java.util.function.Function;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2022/11/10  4:46 PM}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
@Builder
@Data
public class QueryFieldValueConvert {
    /**
     * 转换处理器
     */
    Function<Object, Object> convert;
    /**
     * 字段名
     */
    private String field;
}
