package com.fit2cloud.common.validator.annnotaion;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fit2cloud.common.validator.CustomValidator;
import com.fit2cloud.common.validator.function.ValidatorFunction;

import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * { @Author:张少虎}
 * { @Date: 2022/8/24  9:22 PM}
 * { @Version 1.0}
 * { @注释: 自定义校验器 }
 */
@Documented
@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
@Retention(RUNTIME)
@Constraint(validatedBy = {CustomValidator.class})
public @interface CustomValidated {
    /**
     * 校验错误提示
     *
     * @return 校验错误提示
     */
    String message();

    /**
     * 判断报错的条件，true：存在就报错，false：不存在就报错
     *
     * @return 是否存在
     */
    boolean exist();

    /**
     * mapper
     *
     * @return 处理Mapper
     */
    Class<? extends BaseMapper<?>> mapper();

    /**
     * 自定义处理器
     *
     * @return 自定义处理器
     */
    Class<? extends ValidatorFunction<Object, ConstraintValidatorContext, CustomValidated, Boolean>> handler();

    /**
     * 分组
     *
     * @return 分组
     */
    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    /**
     * the method's name ,which used to validate the enum's value
     *
     * @return method's name
     */
    String method() default "ordinal";

    String field() default "";

    /**
     * 值为空时，是否跳过判断
     * true 跳过判断
     * false 进入判断（默认）
     *
     * @return 是否空值跳过判断
     */
    boolean ifNullPass() default false;
}
