package com.fit2cloud.event;

import com.fit2cloud.base.entity.CloudAccount;
import com.fit2cloud.base.service.IBaseCloudAccountService;
import com.fit2cloud.common.constants.PlatformConstants;
import com.fit2cloud.common.event.annotaion.Event;
import com.fit2cloud.common.scheduler.handler.AsyncJob;
import com.fit2cloud.service.ISyncProviderService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2023/5/8  17:54}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
@Event(value = "")
@Component
public class EventListener {
    @Resource
    private ISyncProviderService syncService;
    @Resource
    private IBaseCloudAccountService baseCloudAccountService;

    @Event("CREATE::CLOUD_ACCOUNT")
    public void createCloudAccount(String cloudAccountId) {
        CloudAccount cloudAccount = baseCloudAccountService.getById(cloudAccountId);
        // 初始化定时任务
        AsyncJob.run(() -> baseCloudAccountService.initCloudAccountJob(cloudAccountId));
        // -----------云资源(公)--------------------------------
        // 同步磁盘
        AsyncJob.run(() -> syncService.syncCloudDisk(cloudAccountId));
        // 同步镜像
        AsyncJob.run(() -> syncService.syncCloudImage(cloudAccountId));
        // 同步云主机
        AsyncJob.run(() -> syncService.syncCloudServer(cloudAccountId));
        // 同步磁盘监控
        AsyncJob.run(() -> syncService.syncCloudDiskPerfMetricMonitor(cloudAccountId));
        // 同步云服务器监控
        AsyncJob.run(() -> syncService.syncCloudServerPerfMetricMonitor(cloudAccountId));

        // -----------云资源(私)-------------------
        if (List.of(PlatformConstants.fit2cloud_openstack_platform.name(), PlatformConstants.fit2cloud_vsphere_platform.name()).contains(cloudAccount.getPlatform())) {
            // 同步存储器
            AsyncJob.run(() -> syncService.syncCloudDatastore(cloudAccountId));
            // 同步主机
            AsyncJob.run(() -> syncService.syncCloudHost(cloudAccountId));
            // 同步存储器监控
            AsyncJob.run(() -> syncService.syncCloudDatastorePerfMetricMonitor(cloudAccountId));
            // 同步宿主机监控
            AsyncJob.run(() -> syncService.syncCloudHostPerfMetricMonitor(cloudAccountId));
        }

    }


    @Event("DELETE::CLOUD_ACCOUNT")
    public void deleteCloudAccount(String cloudAccountId) {
        // 删除云账号的定时任务
        AsyncJob.run(() -> baseCloudAccountService.deleteJobByCloudAccountId(cloudAccountId));
        // 删除当前模块所有关于当前云账的数据
        syncService.deleteDataSource(cloudAccountId);
    }


    @Event("DELETE_BATCH::CLOUD_ACCOUNT")
    public void deleteBatchCloudAccount(List<String> cloudAccountIdList) {
        if (CollectionUtils.isNotEmpty(cloudAccountIdList)) {
            // 删除云账号的定时任务
            AsyncJob.run(() -> {
                for (String cloudAccountId : cloudAccountIdList) {
                    baseCloudAccountService.deleteJobByCloudAccountId(cloudAccountId);
                }
            });
            // 删除所有关于当前云账号的数据
            syncService.deleteDataSource(cloudAccountIdList);
        }
    }

}
