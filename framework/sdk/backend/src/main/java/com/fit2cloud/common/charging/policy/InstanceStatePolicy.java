package com.fit2cloud.common.charging.policy;

import com.fit2cloud.base.entity.ResourceInstanceState;

import java.util.List;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2023/6/25  11:02}
 * {@code @Version 1.0}
 * {@code @注释: 实例状态存储策略 }
 */
public interface InstanceStatePolicy {

    /**
     * 查询资源实例状态
     *
     * @param cloudAccountId 云账号id
     * @param resourceType   资源类型
     * @param month          月份
     * @return 实例状态信息
     */
    List<ResourceInstanceState> list(String cloudAccountId, String resourceType, String month);

    /**
     * 查询实例状态
     *
     * @param resourceType 资源类型
     * @param month        月份
     * @return 资源实例状态
     */
    List<ResourceInstanceState> list(String resourceType, String month);

    /**
     * 批量插入数据
     *
     * @param resourceType 资源类型
     * @param month        月份
     * @param states       实例状态
     */
    void saveBatch(String resourceType, String month, List<ResourceInstanceState> states);
}
