package com.fit2cloud.constants;

import com.fit2cloud.autoconfigure.JobSettingConfig;
import com.fit2cloud.common.constants.PlatformConstants;
import com.fit2cloud.dto.job.JobSetting;
import com.fit2cloud.quartz.CloudAccountSyncJob;

import java.util.Arrays;
import java.util.List;

/**
 * @Author:张少虎
 * @Date: 2022/9/8  6:21 PM
 * @Version 1.0
 * @注释:
 */
public class JobConstants implements JobSettingConfig.JobConfig {
    public enum JobSyncResourceType {
        DISK("同步磁盘"),
        VIRTUAL_MACHINE("同步云主机"),
        IMAGE("同步镜像"),
        HOST("同步宿主机"),
        DATASTORE("同步存储器"),
        VIRTUAL_MACHINE_PERF_METRIC_MONITOR("同步云主机监控"),
        HOST_PERF_METRIC_MONITOR("同步宿主机监控"),
        DISK_PERF_METRIC_MONITOR("同步云磁盘监控"),
        DATASTORE_PERF_METRIC_MONITOR("同步存储器监控");
        private String message;

        JobSyncResourceType(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }

        public static JobSyncResourceType of(String message) {
            return Arrays.stream(JobSyncResourceType.values()).filter(item -> item.message.equals(message)).findFirst().orElse(null);
        }
    }

    /**
     * 同步磁盘
     */
    private static final String SYNC_DISK = "SYNC_DISK";

    /**
     * 同步云主机
     */
    private static final String SYNC_VIRTUAL_MACHINE = "SYNC_VIRTUAL_MACHINE";

    /**
     * 同步镜像
     */
    private static final String SYNC_IMAGE = "SYNC_IMAGE";

    /**
     * 同步宿主机
     */
    private static final String SYNC_HOST = "SYNC_HOST";

    /**
     * 同步存储器
     */
    private static final String SYNC_DATASTORE = "SYNC_DATASTORE";

    /**
     * 同步监控所需数据
     */
    private static final String SYNC_METRIC_MONITOR = "SYNC_METRIC_MONITOR";

    @Override
    public List<JobSetting> listJobInitSetting() {
        // 同步虚拟机
        JobSetting syncVirtual = new JobSetting(CloudAccountSyncJob.SyncVirtualMachineJob.class, SYNC_VIRTUAL_MACHINE, com.fit2cloud.common.constants.JobConstants.Group.CLOUD_ACCOUNT_RESOURCE_SYNC_GROUP.name(), "同步云主机", null, p -> true);
        // 同步磁盘
        JobSetting syncDisk = new JobSetting(CloudAccountSyncJob.SyncDiskJob.class, SYNC_DISK, com.fit2cloud.common.constants.JobConstants.Group.CLOUD_ACCOUNT_RESOURCE_SYNC_GROUP.name(), "同步磁盘", null, p -> true);
        // 同步镜像
        JobSetting syncImage = new JobSetting(CloudAccountSyncJob.SyncImageJob.class, SYNC_IMAGE, com.fit2cloud.common.constants.JobConstants.Group.CLOUD_ACCOUNT_RESOURCE_SYNC_GROUP.name(), "同步镜像", null, p -> true);
        // 同步宿主机
        JobSetting syncHost = new JobSetting(CloudAccountSyncJob.SyncHostJob.class, SYNC_HOST, com.fit2cloud.common.constants.JobConstants.Group.CLOUD_ACCOUNT_RESOURCE_SYNC_GROUP.name(), "同步宿主机", null, p -> p.equals(PlatformConstants.fit2cloud_vsphere_platform.name()) || p.equals(PlatformConstants.fit2cloud_openstack_platform.name()));
        // 同步存储器
        JobSetting syncDatastore = new JobSetting(CloudAccountSyncJob.SyncDatastoreJob.class, SYNC_DATASTORE, com.fit2cloud.common.constants.JobConstants.Group.CLOUD_ACCOUNT_RESOURCE_SYNC_GROUP.name(), "同步存储器", null, p -> p.equals(PlatformConstants.fit2cloud_vsphere_platform.name()) || p.equals(PlatformConstants.fit2cloud_openstack_platform.name()));
        // 同步监控数据
        JobSetting syncMetricMonitor = new JobSetting(CloudAccountSyncJob.SyncMetricMonitor.class, SYNC_METRIC_MONITOR, com.fit2cloud.common.constants.JobConstants.Group.SYSTEM_GROUP.name(), "同步监控数据", null, "0 5 * * * ? *", p -> true, p -> false, p -> false);
        return List.of(syncDisk, syncVirtual, syncImage, syncHost, syncDatastore, syncMetricMonitor);
    }
}
