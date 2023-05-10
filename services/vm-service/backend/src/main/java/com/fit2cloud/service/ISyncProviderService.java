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
     * 同步云主机
     *
     * @param cloudAccountId 云账号id
     * @param regions        同步区域
     */
    void syncCloudServer(String cloudAccountId, List<Credential.Region> regions);

    /**
     * 同步云主机
     *
     * @param params 同步云主机所需参数
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

    /**
     * 同步宿主机
     *
     * @param cloudAccountId 云账号id
     */
    void syncCloudHost(String cloudAccountId);

    /**
     * 同步宿主机
     *
     * @param cloudAccountId 云账号id
     * @param regions        同步区域
     */
    void syncCloudHost(String cloudAccountId, List<Credential.Region> regions);

    /**
     * 同步宿主机
     *
     * @param params 同步宿主机所需参数
     */
    void syncCloudHost(Map<String, Object> params);

    /**
     * 同步存储器
     *
     * @param cloudAccountId 云账号id
     */
    void syncCloudDatastore(String cloudAccountId);

    /**
     * 同步存储器
     *
     * @param cloudAccountId 云账号id
     * @param regions        同步区域
     */
    void syncCloudDatastore(String cloudAccountId, List<Credential.Region> regions);

    /**
     * 同步存储器
     *
     * @param params 同步宿主机所需参数
     */
    void syncCloudDatastore(Map<String, Object> params);


    /**
     * 同步云主机性能监控数据
     *
     * @param params 同步云主机所需要的参数
     */
    void syncCloudServerPerfMetricMonitor(Map<String, Object> params);

    /**
     * 同步宿主机性能监控数据
     *
     * @param params 同步宿主机所需要的参数
     */
    void syncCloudHostPerfMetricMonitor(Map<String, Object> params);

    /**
     * 同步云磁盘性能监控数据
     *
     * @param params 同步云磁盘所需要的参数
     */
    void syncCloudDiskPerfMetricMonitor(Map<String, Object> params);

    /**
     * 同步存储器性能监控数据
     *
     * @param params 同步存储器所需要的参数
     */
    void syncCloudDatastorePerfMetricMonitor(Map<String, Object> params);

    /**
     * 同步存储器性能监控数据
     *
     * @param cloudAccountId 云账号id
     */
    void syncCloudDatastorePerfMetricMonitor(String cloudAccountId);

    /**
     * 同步云磁盘监控数据
     *
     * @param cloudAccountId 云账号id
     */
    void syncCloudDiskPerfMetricMonitor(String cloudAccountId);

    /**
     * 同步宿主机性能监控数据
     *
     * @param cloudAccountId 云账号id
     */
    void syncCloudHostPerfMetricMonitor(String cloudAccountId);

    /**
     * 同步云主机性能监控数据
     *
     * @param cloudAccountId 云账号id
     */
    void syncCloudServerPerfMetricMonitor(String cloudAccountId);

    /**
     * 删除数据根据云账号id
     *
     * @param cloudAccountIds 云账号id
     */
    void deleteDataSource(List<String> cloudAccountIds);

    /**
     * 删除数据
     *
     * @param cloudAccountId 云账号id
     */
    void deleteDataSource(String cloudAccountId);

}
