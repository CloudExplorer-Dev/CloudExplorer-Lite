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
@Repeatable(FormConfirmInfos.class)
public @interface FormConfirmInfo {

    int group() default 0;

    String name();

    String description() default "";

    int items() default 3;

}
