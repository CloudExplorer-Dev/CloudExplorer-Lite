package com.fit2cloud.service;

import com.fit2cloud.common.platform.credential.Credential;

import java.util.List;
import java.util.Map;

/**
 * @Author:张少虎
 * @Date: 2022/9/21  11:37 AM
 * @Version 1.0
 * @注释:
 */
public interface ISyncProviderService {
    /**
     * 云账号id
     *
     * @param cloudAccountId 云账号id
     */
    void syncCloudServer(String cloudAccountId);

    /**
     * 同步虚拟机
     *
     * @param cloudAccountId 云账号id
     * @param regions        同步区域
     */
    void syncCloudServer(String cloudAccountId, List<Credential.Region> regions);

    /**
     * 同步虚拟机
     *
     * @param params 同步虚拟机所需参数
     */
    void syncCloudServer(Map<String, Object> params);

    /**
     * 同步镜像
     *
     * @param cloudAccountId 云账号id
     */
    void syncCloudImage(String cloudAccountId);

    /**
     * 同步镜像
     *
     * @param cloudAccountId 云账号id
     * @param regions        同步区域
     */
    void syncCloudImage(String cloudAccountId, List<Credential.Region> regions);

    /**
     * 同步镜像
     *
     * @param params 同步镜像所需参数
     */
    void syncCloudImage(Map<String, Object> params);

    /**
     * 同步磁盘
     *
     * @param cloudAccountId 云账号id
     */
    void syncCloudDisk(String cloudAccountId);

    /**
     * 同步磁盘
     *
     * @param cloudAccountId 云账号id
     * @param regions        同步区域
     */
    void syncCloudDisk(String cloudAccountId, List<Credential.Region> regions);

    /**
     * 同步磁盘
     *
     * @param params 同步磁盘所需参数
     */
    void syncCloudDisk(Map<String, Object> params);

}
