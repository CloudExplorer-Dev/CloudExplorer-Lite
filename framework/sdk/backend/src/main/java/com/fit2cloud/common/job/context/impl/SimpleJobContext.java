package com.fit2cloud.common.job.context.impl;

import com.fit2cloud.common.job.context.JobContext;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2023/5/12  20:46}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
public class SimpleJobContext<T> implements JobContext<T> {
    private T context;

    private SimpleJobContext(T context) {
        this.context = context;
    }

    public static <T> SimpleJobContext<T> of(T context) {
        return new SimpleJobContext<>(context);
    }

    @Override

    public T getContext() {
        return context;
    }

    @Override
    public void setContext(T context) {
        this.context = context;
    }
}
