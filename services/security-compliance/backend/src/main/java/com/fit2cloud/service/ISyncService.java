package com.fit2cloud.service;

import com.fit2cloud.common.job.job.Job;
import com.fit2cloud.common.job.job.SimpleJob;
import com.fit2cloud.constants.ResourceTypeConstants;

import java.util.List;

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

    /**
     * 同步实例数据
     *
     * @param cloudAccountId 云账户id
     * @param instanceType   实例类型
     */
    void syncInstanceByInstanceType(String cloudAccountId, List<String> instanceType);

    /**
     * 同步实例数据
     *
     * @param cloudAccountId 云账号id
     * @param ruleGroupId    规则组id
     */
    void syncInstance(String cloudAccountId, List<String> ruleGroupId);

    /**
     * 根据云账号同步实例数据
     *
     * @param cloudAccountId 云账号id
     */
    void syncInstance(String cloudAccountId);
}
