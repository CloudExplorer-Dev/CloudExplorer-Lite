package com.fit2cloud.service;

import com.fit2cloud.common.job.actuator.JobActuator;
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
     * 同步资源
     *
     * @param cloudAccountId  云账号id
     * @param instanceType    资源类型
     * @param executeStepList 执行步骤
     */
    void syncInstance(String cloudAccountId, ResourceTypeConstants instanceType, List<JobActuator.ExecuteStepData> executeStepList);

    void scanInstance(String complianceRuleId, List<JobActuator.ExecuteStepData> executeStepList);

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
