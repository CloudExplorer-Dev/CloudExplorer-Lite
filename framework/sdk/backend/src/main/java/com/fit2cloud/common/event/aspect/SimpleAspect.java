package com.fit2cloud.common.event.aspect;

import com.fit2cloud.common.event.annotaion.Emit;
import com.fit2cloud.common.event.impl.EmitTemplate;
import com.fit2cloud.common.event.impl.SimpleElArray;
import com.fit2cloud.common.log.utils.LogUtil;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

import jakarta.annotation.Resource;
import java.lang.reflect.Method;
import java.util.Collection;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2023/5/8  16:16}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
@Aspect
@Component
public class SimpleAspect {
    @Resource
    private EmitTemplate emitTemplate;

    @Around("@annotation(com.fit2cloud.common.event.annotaion.Emit)")
    public Object action(ProceedingJoinPoint pjd) throws Throwable {
        // 执行函数
        Object proceed = pjd.proceed();
        try {
            emit(pjd, proceed);
        } catch (Exception e) {
            LogUtil.error("发送事件失败", e);
        }
        return proceed;
    }

    /**
     * 解析el表达式
     *
     * @param paramsNameList 参数名称
     * @param args           参数
     * @param expression     el表达式
     * @return 解析后的数据
     */
    @SneakyThrows
    public Object analysisEl(String[] paramsNameList, Object[] args, Object result, String expression) {
        ExpressionParser parser = new SpelExpressionParser();
        StandardEvaluationContext context = new StandardEvaluationContext();
        context.registerFunction("arrayOf", SimpleElArray.class.getMethod("of", Collection.class));
        for (int i = 0; i < paramsNameList.length && i < args.length; i++) {
            context.setVariable(paramsNameList[i], args[i]);
        }
        context.setVariable("result", result);
        Expression exp = parser.parseExpression(expression);
        return exp.getValue(context);
    }

    /**
     * 发送
     *
     * @param pjd Point对象
     */
    public void emit(ProceedingJoinPoint pjd, Object result) {
        MethodSignature methodSignature = (MethodSignature) pjd.getSignature();
        Method method = methodSignature.getMethod();
        // 参数名称
        String[] parameterNames = ((MethodSignature) pjd.getSignature()).getParameterNames();
        // 函数全量参数
        Object[] args = pjd.getArgs();
        // 获取注解
        Emit emit = method.getAnnotation(Emit.class);
        if (StringUtils.isNotEmpty(emit.el())) {
            args = new Object[]{analysisEl(parameterNames, args, result, emit.el())};

        }
        emitTemplate.emit(emit.value(), emit.excludeService(), args);
    }

}
