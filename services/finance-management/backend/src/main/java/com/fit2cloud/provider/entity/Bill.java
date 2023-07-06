package com.fit2cloud.provider.entity;

import java.util.Map;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2023/7/6  4:29 PM}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
public interface Bill {
    /**
     * 校验参数
     */
    void verification();

    /**
     * 获取默认参数
     *
     * @return 默认参数
     */
    Map<String, Object> getDefaultParams();
}
