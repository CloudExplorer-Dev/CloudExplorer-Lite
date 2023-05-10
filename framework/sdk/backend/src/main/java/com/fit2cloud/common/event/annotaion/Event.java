package com.fit2cloud.common.event.annotaion;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2023/5/8  15:13}
 * {@code @Version 1.0}
 * {@code @注释: 接收注解}
 */
@Documented
@Target({METHOD, TYPE})
@Retention(RUNTIME)
public @interface Event {
    /**
     * 注意,value的填写有一定的规范,UPDATE::CLOUD_ACCOUNT ::前面为执行操作 冒号后面为操作资源
     * 当然这只是一种建议
     *
     * @return 事件名称
     */
    String value();
}
