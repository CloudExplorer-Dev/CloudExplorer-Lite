package com.fit2cloud.common.log.annotation;

import com.fit2cloud.common.log.conver.ResourceConvert;
import com.fit2cloud.common.log.conver.impl.DefaultResourceConvert;

import java.lang.annotation.*;

/**
 * 日志字段转换器
 * {@code @Author:张少虎}
 * {@code @Date: 2022/11/4  1:59 PM}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
@Target(value = ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface OperatedLogFieldConver {


    /**
     * 资源转换器  用于转换id 到 名称 如 工作空间id转换为工作空间名称
     *
     * @return 资源转换器
     */
    Class<? extends ResourceConvert> conver() default DefaultResourceConvert.class;

}
