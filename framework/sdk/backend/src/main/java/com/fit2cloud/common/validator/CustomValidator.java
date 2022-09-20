package com.fit2cloud.common.validator;

import com.fit2cloud.common.validator.annnotaion.CustomValidated;
import com.fit2cloud.common.validator.function.ValidatorFunction;
import lombok.SneakyThrows;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @Author:张少虎
 * @Date: 2022/8/24  9:25 PM
 * @Version 1.0
 * @注释:
 */
public class CustomValidator implements ConstraintValidator<CustomValidated, Object> {

    private CustomValidated customValidated;

    @SneakyThrows
    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        ValidatorFunction<Object, ConstraintValidatorContext, CustomValidated, Boolean> validatorFunction = customValidated.handler().getDeclaredConstructor().newInstance();
        return validatorFunction.apply(value, context, customValidated);
    }

    @Override
    public void initialize(CustomValidated constraintAnnotation) {
        this.customValidated = constraintAnnotation;
    }
}
