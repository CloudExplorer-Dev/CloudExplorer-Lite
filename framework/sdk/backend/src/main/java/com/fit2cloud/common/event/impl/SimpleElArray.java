package com.fit2cloud.common.event.impl;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2023/5/9  13:10}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
public class SimpleElArray<T> {
    private final EvaluationContext context = new StandardEvaluationContext();
    private Collection<T> dataList;

    public static <V> SimpleElArray<V> of(Collection<V> list) {
        SimpleElArray<V> simpleElArray = new SimpleElArray<V>();
        if (CollectionUtils.isEmpty(list)) {
            list = new ArrayList<>();
        }
        simpleElArray.dataList = list;
        return simpleElArray;
    }

    /**
     * 绑定上下文，用法：#name
     *
     * @param value 绑定方法时，必须是Method类型
     */
    public SimpleElArray<T> bind(String name, Object value) {
        context.setVariable(name, value);
        return this;
    }


    /**
     * 映射
     */
    public List<Object> map(String expression) {
        ExpressionParser parser = new SpelExpressionParser();
        return dataList.stream().map(item -> parser.parseExpression(expression).getValue(item)).toList();
    }

}
