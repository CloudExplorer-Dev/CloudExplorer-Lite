package com.fit2cloud.common.log.annotation;

import com.fit2cloud.common.log.constants.OperatedTypeEnum;
import com.fit2cloud.common.log.constants.ResourceTypeEnum;

import java.lang.annotation.*;

/**
 * 日志注解
 * @author jianneng
 * @date 2022/9/15 10:25
 **/
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@Documented
public @interface OperatedLog {

    /**
     * 操作类型
     */
    OperatedTypeEnum operated();


    /**
     * 资源ID
     * @return
     */
    String resourceId() default "";

    /**
     * 资源类型
     * @return
     */
    ResourceTypeEnum resourceType();


    /**
     * 关联资源ID,磁盘挂载到虚拟机，虚拟机卸载磁盘
     * @return
     */
    String joinResourceId() default "";

    /**
     * 接口描述
     */
    String description() default "";

    /**
     * 操作内容
     * @return
     */
    String content() default "";

    /**
     * 访问参数
     */
    String param() default "";
}
