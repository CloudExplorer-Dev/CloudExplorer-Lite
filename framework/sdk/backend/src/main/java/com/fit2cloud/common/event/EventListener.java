package com.fit2cloud.common.event;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2023/5/8  15:44}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
public interface EventListener {
    /**
     * 事件接收
     *
     * @param event 事件名称
     * @param args  事件参数
     */
    void on(String event, Object[] args);
}
