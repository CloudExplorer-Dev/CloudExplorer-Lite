package com.fit2cloud.common.form.annotaion;

import com.fit2cloud.common.form.constants.InputType;
import com.fit2cloud.common.provider.IBaseCloudProvider;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;


@Documented
@Target({FIELD})
@Retention(RUNTIME)
public @interface Form {

    /**
     * 表单类型
     *
     * @return 表单类型
     */
    InputType inputType();


    String attrs() default "{}";


    /**
     * 表单提示
     *
     * @return 表单提示
     */
    String label() default "";

    /**
     * 单位
     *
     * @return
     */
    String unit() default "";

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
     * 默认值是否需要json解析给前端用
     *
     * @return
     */
    boolean defaultJsonValue() default false;

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

    boolean formatTextField() default false;

    /**
     * 执行函数所属类
     *
     * @return 必填
     */
    Class<?> clazz() default IBaseCloudProvider.class;

    /**
     * 执行函数,用于获取单选,多选框的options
     *
     * @return 执行函数名称
     */
    String method() default "";

    /**
     * 调用的类是否为spring的bean
     * @return
     */
    boolean serviceMethod() default false;


    /**
     * 那些按钮发生变化的时候,调用接口获取数据
     *
     * @return 需要插入的树属性
     */
    String[] relationTrigger() default {};

    /**
     * 那些数据有值的时候,显示当前节点
     *
     * @return 那些数据为true, 或者有值的时候, 展示当前节点
     */
    String[] relationShows() default {};

    /**
     * container分组
     *
     * @return
     */
    int group() default 0;

    /**
     * 分页步骤
     *
     * @return
     */
    int step() default 0;


}
