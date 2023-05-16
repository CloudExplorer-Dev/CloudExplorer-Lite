package com.fit2cloud.common.job.context;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2023/5/10  15:40}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
public interface JobContext<T> {
    /**
     * 获取上下文
     *
     * @return 上下文参数
     */
    T getContext();

    /**
     * 设置上下文1
     *
     * @param context 上下文对象
     */
    void setContext(T context);

}
