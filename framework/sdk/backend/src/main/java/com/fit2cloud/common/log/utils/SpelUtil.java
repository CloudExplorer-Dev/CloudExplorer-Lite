package com.fit2cloud.common.log.utils;

import com.fit2cloud.common.utils.JsonUtil;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.lang.reflect.Method;

/**
 * SpEL解析工具
 * @author jianneng
 * @date 2022/9/15 10:46
 **/
public class SpelUtil {

    private static SpelExpressionParser parser = new SpelExpressionParser();
    private LocalVariableTableParameterNameDiscoverer discoverer = new LocalVariableTableParameterNameDiscoverer();

    public static String getElValueByParam(String spELString, Object[] param, String[] parameterNames) {
        try {
            if (StringUtils.isBlank(spELString)) {
                return spELString;
            }
            Expression expression = parser.parseExpression(spELString);
            EvaluationContext context = new StandardEvaluationContext();
            if (param == null || param.length == 0) {
                return expression.getValue(context, String.class);
            }
            for (int i = 0; i < param.length; i++) {
                context.setVariable(parameterNames[i],JsonUtil.parseObject(JsonUtil.toJSONString(param[i]),param[i].getClass()));
            }
            return expression.getValue(context).toString();
        } catch (Exception e) {
            return spELString;
        }
    }

    public static String getElValueByKey(ProceedingJoinPoint pjd,String spel){
        // 通过SpEL获取接口参数对象属性值
        StandardEvaluationContext standardEvaluationContext = new StandardEvaluationContext(pjd.getArgs());
        standardEvaluationContext = setContextVariables(standardEvaluationContext, pjd);
        return getElValue(spel,standardEvaluationContext);
    }

    public static StandardEvaluationContext setContextVariables(StandardEvaluationContext standardEvaluationContext,
                                                          ProceedingJoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method targetMethod = methodSignature.getMethod();
        LocalVariableTableParameterNameDiscoverer parameterNameDiscoverer
                = new LocalVariableTableParameterNameDiscoverer();
        String[] parametersName = parameterNameDiscoverer.getParameterNames(targetMethod);

        if (args == null || args.length <= 0) {
            return standardEvaluationContext;
        }
        for (int i = 0; i < args.length; i++) {
            standardEvaluationContext.setVariable(parametersName[i], args[i]);
        }
        return standardEvaluationContext;
    }

    private static String getElValue(String key, StandardEvaluationContext context) {
        if (StringUtils.isBlank(key)) {
            return "";
        }
        ExpressionParser parser = new SpelExpressionParser();
        Expression exp = parser.parseExpression(key);
        String value = exp.getValue(context, String.class);
        return value;

    }

}
