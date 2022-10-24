package com.fit2cloud.common.form.annotaion;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;


@Documented
@Target({TYPE})
@Retention(RUNTIME)
public @interface FormStepInfos {

    FormStepInfo[] value();

}
