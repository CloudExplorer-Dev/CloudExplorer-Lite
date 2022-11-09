package com.fit2cloud.common.annotaion;

import com.fit2cloud.common.conver.Convert;
import com.fit2cloud.common.conver.impl.DefaultConvert;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2022/11/4  1:59 PM}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
@Target(value = ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface BillField {
    /**
     * 字段label
     *
     * @return label
     */
    String label();

    /**
     * @return 是否是分组字段
     */
    boolean group() default false;

    /**
     * @return 是否是过滤字段
     */
    boolean filter() default false;

    /**
     * @return 是否是授权字段
     */
    boolean authorize() default false;

    /**
     * 资源转换器  用于转换id 到 名称 如 工作空间id转换为工作空间名称
     *
     * @return 资源转换器
     */
    Class<? extends Convert> conver() default DefaultConvert.class;
}
