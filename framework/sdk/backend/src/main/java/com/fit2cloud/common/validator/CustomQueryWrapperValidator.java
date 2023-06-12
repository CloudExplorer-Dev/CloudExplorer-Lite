package com.fit2cloud.common.validator;

import com.fit2cloud.common.validator.annnotaion.CustomQueryWrapperValidated;
import com.fit2cloud.common.validator.function.ValidatorFunction;
import lombok.SneakyThrows;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2023/3/6  15:49}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
public class CustomQueryWrapperValidator implements ConstraintValidator<CustomQueryWrapperValidated, Object> {

    private CustomQueryWrapperValidated customQueryWrapperValidated;

    @SneakyThrows
    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        ValidatorFunction<Object, ConstraintValidatorContext, CustomQueryWrapperValidated, Boolean> validatorFunction = customQueryWrapperValidated.handler().getDeclaredConstructor().newInstance();
        return validatorFunction.apply(value, context, customQueryWrapperValidated);
    }

    @Override
    public void initialize(CustomQueryWrapperValidated customQueryWrapperValidated) {
        this.customQueryWrapperValidated = customQueryWrapperValidated;
    }
}
