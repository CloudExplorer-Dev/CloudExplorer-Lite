package com.fit2cloud.service;

import com.fit2cloud.constants.ResourceTypeConstants;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2022/12/6  14:29}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
public interface ISyncService {
    /**
     * 同步实例数据
     *
     * @param cloudAccountId 云账户id
     * @param instanceType   实例类型
     */
    void syncInstance(String cloudAccountId, ResourceTypeConstants instanceType);
}
