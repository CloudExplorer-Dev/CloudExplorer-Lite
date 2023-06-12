package com.fit2cloud.common.validator.handler;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fit2cloud.common.utils.SpringUtil;
import com.fit2cloud.common.validator.annnotaion.CustomQueryWrapperValidated;
import com.fit2cloud.common.validator.function.ValidatorFunction;
import lombok.SneakyThrows;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import jakarta.validation.ConstraintValidatorContext;
import java.lang.reflect.Method;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2023/3/6  10:20}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
public class ExistQueryWrapperValidatedHandler implements ValidatorFunction<Object, ConstraintValidatorContext, CustomQueryWrapperValidated, Boolean> {
    @SneakyThrows
    @Override
    public Boolean apply(Object o, ConstraintValidatorContext constraintValidatorContext, CustomQueryWrapperValidated customQueryWrapperValidated) {
        // 获取查询Mapper
        BaseMapper mapper = SpringUtil.getBean(customQueryWrapperValidated.mapper());
        // 获取查询条件el表达式
        String queryWrapperEl = customQueryWrapperValidated.el();
        // 获取el解析器
        ExpressionParser parser = new SpelExpressionParser();
        StandardEvaluationContext context = new StandardEvaluationContext(o);
        Method getQueryWrapper = ExistQueryWrapperValidatedHandler.class.getDeclaredMethod("getQueryWrapper");
        context.registerFunction("getQueryWrapper", getQueryWrapper);
        QueryWrapper<?> queryWrapper = parser.parseExpression(queryWrapperEl).getValue(context, QueryWrapper.class);
        boolean exists = mapper.exists(queryWrapper);
        if (customQueryWrapperValidated.exist()) {
            // 存在则报错
            return !exists;
        } else {
            // 不存在报错
            return exists;
        }

    }


    /**
     * 获取QueryWrapper
     *
     * @return mybatis查询对象
     */
    public static QueryWrapper<?> getQueryWrapper() {
        return new QueryWrapper<>();
    }
}
