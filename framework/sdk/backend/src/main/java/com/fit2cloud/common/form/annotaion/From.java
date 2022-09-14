package com.fit2cloud.common.form.annotaion;

import com.fit2cloud.common.form.constants.InputType;
import com.fit2cloud.common.validator.CustomValidator;

import javax.validation.Constraint;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.ElementType.TYPE_USE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @Author:张少虎
 * @Date: 2022/9/7  10:04 AM
 * @Version 1.0
 * @注释:
 */
@Documented
@Target({FIELD})
@Retention(RUNTIME)
public @interface From {

    /**
     * 表单类型
     *
     * @return 表单类型
     */
    InputType inputType();


    /**
     * 表单提示
     *
     * @return 表单提示
     */
    String label();

    /**
     * 是否必填
     *
     * @return 是否必填
     */
    boolean required() default true;

    /**
     * 默认值
     *
     * @return 默认值
     */
    String defaultValue() default "";

    /**
     * 描述
     *
     * @return 描述字符串
     */
    String description() default "";

    /**
     * 单选多选框 option选项对应的值字段
     *
     * @return option对象值的属性名
     */
    String valueField() default "";

    /**
     * 单选多选框 option选项对应的文本字段
     *
     * @return option对象文本提示的属性名
     */
    String textField() default "";

    /**
     * 执行函数,用于获取单选,多选框的options
     *
     * @return 执行函数名称
     */
    String method() default "";
}
