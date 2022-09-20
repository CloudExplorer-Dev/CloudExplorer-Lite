package com.fit2cloud.common.validator.handler;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fit2cloud.common.utils.SpringUtil;
import com.fit2cloud.common.validator.annnotaion.CustomValidated;
import com.fit2cloud.common.validator.function.ValidatorFunction;
import org.hibernate.validator.internal.engine.constraintvalidation.ConstraintValidatorContextImpl;
import org.hibernate.validator.internal.engine.constraintvalidation.ConstraintViolationCreationContext;
import org.hibernate.validator.internal.engine.path.NodeImpl;

import javax.validation.ConstraintValidatorContext;
import javax.validation.ElementKind;

/**
 * @Author:张少虎
 * @Date: 2022/8/24  11:53 PM
 * @Version 1.0
 * @注释:
 */
public class ExistHandler implements ValidatorFunction<Object, ConstraintValidatorContext, CustomValidated, Boolean> {
    @Override
    public Boolean apply(Object value, ConstraintValidatorContext constraintValidatorContext, CustomValidated customValidated) {
        if (value == null && customValidated.ifNullPass()) {
            return true;
        }
        BaseMapper bean = SpringUtil.getBean(customValidated.mapper());
        if (constraintValidatorContext instanceof ConstraintValidatorContextImpl) {
            ConstraintValidatorContextImpl context = (ConstraintValidatorContextImpl) constraintValidatorContext;
            if (context.getConstraintViolationCreationContexts().size() == 1) {
                ConstraintViolationCreationContext next = context.getConstraintViolationCreationContexts().listIterator().next();
                NodeImpl leafNode = next.getPath().getLeafNode();
                String field = customValidated.field().length() == 0 ? leafNode.asString() : customValidated.field();
                if (leafNode.getKind().equals(ElementKind.PROPERTY) || leafNode.getKind().equals(ElementKind.PARAMETER)) {
                    QueryWrapper queryWrapper = new QueryWrapper();
                    queryWrapper.eq(field, value);
                    if (customValidated.exist()) {
                        return !bean.exists(queryWrapper);
                    }
                    return bean.exists(queryWrapper);
                }
            }
        }
        return true;
    }
}
