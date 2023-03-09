package com.fit2cloud.common.advice.annnotaion;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2023/3/9  15:42}
 * {@code @Version 1.0}
 * {@code @注释:  不支持token续期 controller标注了当前主键就不返回ResponseHeader就不返回新token}
 */
@Documented
@Target({METHOD})
@Retention(RUNTIME)
public @interface TokenRenewal {
    /**
     * 是否支持token续期
     *
     * @return true支持, false不支持
     */
    boolean support() default false;
}
