package com.fit2cloud.common.form.annotaion;

import java.lang.annotation.Documented;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;


@Documented
@Target({TYPE})
@Retention(RUNTIME)
@Repeatable(FormStepInfos.class)
public @interface FormStepInfo {

    int step() default 1;

    String name();

    String description() default "";


}
