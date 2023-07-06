package com.fit2cloud.common.provider.entity;

import java.util.List;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2023/7/6  11:50 AM}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
public interface ActionInfo {
    /**
     * 执行 Code
     *
     * @return 执行 Code
     */
    String code();

    /**
     * 执行 message
     *
     * @return 执行 message
     */
    String message();

    /**
     * 当前主要用于提示用户 执行某一个操作 需要重写那些函数
     * 执行当前操作 需要重写的函数
     *
     * @return 需要重写的函数
     */
    List<String> methods();
}
