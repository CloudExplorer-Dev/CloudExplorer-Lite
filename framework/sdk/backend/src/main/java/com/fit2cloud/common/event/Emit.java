package com.fit2cloud.common.event;

import javax.validation.constraints.NotNull;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2023/5/9  9:46}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
public interface Emit {
    /**
     * 发送
     *
     * @param event    事件
     * @param excludes 需要排除的微服务
     * @param args     参数
     */
    void emit(String event, @NotNull String[] excludes, Object... args);

    void emit(String event, Object... args);
}
