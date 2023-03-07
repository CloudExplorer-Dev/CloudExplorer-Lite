package com.fit2cloud.common.validator.annnotaion;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fit2cloud.common.validator.CustomQueryWrapperValidator;
import com.fit2cloud.common.validator.function.ValidatorFunction;

import javax.validation.Constraint;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2023/3/6  10:19}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
@Documented
@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
@Retention(RUNTIME)
@Constraint(validatedBy = {CustomQueryWrapperValidator.class})
public @interface CustomQueryWrapperValidated {
    /**
     * 校验错误提示
     *
     * @return 校验错误提示
     */
    String message();

    /**
     * 自定义处理器
     *
     * @return 自定义处理器
     */
    Class<? extends ValidatorFunction<Object, ConstraintValidatorContext, CustomQueryWrapperValidated, Boolean>> handler();

    /**
     * 分组
     *
     * @return 分组
     */
    Class<?>[] groups() default {};

    /**
     * queryWrapper的实例对象
     * getQueryWrapper为内置函数
     * 例如: #getQueryWrapper().eq("id",#this.id);
     *
     * @return 返回一个Query查询对象
     */
    String el();

    /**
     * mapper 处理查询的Mapper
     *
     * @return 处理Mapper
     */
    Class<? extends BaseMapper<?>> mapper();

    /**
     * 判断报错的条件，true：存在就报错，false：不存在就报错
     *
     * @return 是否存在
     */
    boolean exist();

    Class<? extends Payload>[] payload() default {};
}
