package com.fit2cloud.common.query.annotaion;


import com.fit2cloud.common.utils.QueryUtil;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2022/11/10  4:14 PM}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
@Target(value = ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Query {
    /**
     * 比较
     *
     * @return 条件
     */
    QueryUtil.CompareType compareType();

    /**
     * es 查询字段
     *
     * @return es字段
     */
    String field();
}
