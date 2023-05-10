package com.fit2cloud.common.event.annotaion;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2023/5/8  15:15}
 * {@code @Version 1.0}
 * {@code @注释: 触发注解}
 */
@Documented
@Target({METHOD})
@Retention(RUNTIME)
public @interface Emit {
    // 默认发送给除开GateWay剩下的所有服务,我们可以排除部分服务发送
    String[] excludeService() default {"gateway"};

    /**
     * 注意,value的填写有一定的规范,UPDATE::CLOUD_ACCOUNT ::前面为执行操作 冒号后面为操作资源
     * 当然这只是一种建议
     *
     * @return 事件名称
     */
    String value();

    /**
     * 如果el不传,
     * 默认将方法全部的参数发送
     * 如果写了el 则只发送el拿到的参数
     *
     * @return el表达式
     */
    String el() default "";
}
