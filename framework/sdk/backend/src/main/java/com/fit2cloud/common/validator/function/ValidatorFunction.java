package com.fit2cloud.common.validator.function;

/**
 * @Author:张少虎
 * @Date: 2022/8/24  11:41 PM
 * @Version 1.0
 * @注释:
 */
public interface ValidatorFunction<T, U, A, R> {
    R apply(T t, U u, A a);
}
