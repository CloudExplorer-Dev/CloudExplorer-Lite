package com.fit2cloud.service;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2022/10/18  4:03 PM}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
public interface SyncService {
    /**
     * @param cloudAccountId 云账号id
     */
    void syncBill(String cloudAccountId);
}
