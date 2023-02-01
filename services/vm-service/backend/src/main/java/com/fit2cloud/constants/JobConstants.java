package com.fit2cloud.constants;

import com.fit2cloud.autoconfigure.JobSettingConfig;
import com.fit2cloud.common.constants.PlatformConstants;
import com.fit2cloud.dto.job.JobInitSettingDto;
import com.fit2cloud.dto.job.JobSettingParent;
import com.fit2cloud.quartz.CloudAccountSyncJob;

import java.util.List;

/**
 * @Author:张少虎
 * @Date: 2022/9/8  6:21 PM
 * @Version 1.0
 * @注释:
 */
public class JobConstants implements JobSettingConfig.JobConfig {

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
     * 同步性能监控数据
     */
    private static final String SYNC_PERF_METRIC_MONITOR = "SYNC_PERF_METRIC_MONITOR";

    /**
     * 同步云主机性能监控数据
     */
    private static final String SYNC_VIRTUAL_MACHINE_PERF_METRIC_MONITOR = "SYNC_VIRTUAL_MACHINE_PERF_METRIC_MONITOR";

    /**
     * 同步宿主机性能监控数据
     */
    private static final String SYNC_HOST_PERF_METRIC_MONITOR = "SYNC_HOST_PERF_METRIC_MONITOR";

    /**
     * 同步云磁盘性能监控数据
     */
    private static final String SYNC_DISK_PERF_METRIC_MONITOR = "SYNC_DISK_PERF_METRIC_MONITOR";

    /**
     * 同步存储器性能监控数据
     */
    private static final String SYNC_DATASTORE_PERF_METRIC_MONITOR = "SYNC_DATASTORE_PERF_METRIC_MONITOR";


    @Override
    public List<JobSettingParent> listJobInitSetting() {
        // 同步虚拟机
        JobInitSettingDto syncVirtual = new JobInitSettingDto(CloudAccountSyncJob.SyncVirtualMachineJob.class, SYNC_VIRTUAL_MACHINE, com.fit2cloud.common.constants.JobConstants.Group.CLOUD_ACCOUNT_RESOURCE_SYNC_GROUP.name(), "同步云主机", null, p -> true);
        // 同步磁盘
        JobInitSettingDto syncDisk = new JobInitSettingDto(CloudAccountSyncJob.SyncDiskJob.class, SYNC_DISK, com.fit2cloud.common.constants.JobConstants.Group.CLOUD_ACCOUNT_RESOURCE_SYNC_GROUP.name(), "同步磁盘", null, p -> true);
        // 同步镜像
        JobInitSettingDto syncImage = new JobInitSettingDto(CloudAccountSyncJob.SyncImageJob.class, SYNC_IMAGE, com.fit2cloud.common.constants.JobConstants.Group.CLOUD_ACCOUNT_RESOURCE_SYNC_GROUP.name(), "同步镜像", null, p -> true);
        // 同步宿主机
        JobInitSettingDto syncHost = new JobInitSettingDto(CloudAccountSyncJob.SyncHostJob.class, SYNC_HOST, com.fit2cloud.common.constants.JobConstants.Group.CLOUD_ACCOUNT_RESOURCE_SYNC_GROUP.name(), "同步宿主机", null, p -> p.equals(PlatformConstants.fit2cloud_vsphere_platform.name()) || p.equals(PlatformConstants.fit2cloud_openstack_platform.name()));
        // 同步存储器
        JobInitSettingDto syncDatastore = new JobInitSettingDto(CloudAccountSyncJob.SyncDatastoreJob.class, SYNC_DATASTORE, com.fit2cloud.common.constants.JobConstants.Group.CLOUD_ACCOUNT_RESOURCE_SYNC_GROUP.name(), "同步存储器", null, p -> p.equals(PlatformConstants.fit2cloud_vsphere_platform.name()) || p.equals(PlatformConstants.fit2cloud_openstack_platform.name()));
        // 同步云主机监控数据
        JobInitSettingDto syncVmPerfMetricMonitor = new JobInitSettingDto(CloudAccountSyncJob.SyncCloudServerPerfMetricMonitor.class, SYNC_VIRTUAL_MACHINE_PERF_METRIC_MONITOR, com.fit2cloud.common.constants.JobConstants.Group.CLOUD_ACCOUNT_RESOURCE_SYNC_GROUP.name(), "同步云主机监控", null, p -> true);
        // 同步宿主机监控数据
        JobInitSettingDto syncHostPerfMetricMonitor = new JobInitSettingDto(CloudAccountSyncJob.SyncCloudHostPerfMetricMonitor.class, SYNC_HOST_PERF_METRIC_MONITOR, com.fit2cloud.common.constants.JobConstants.Group.CLOUD_ACCOUNT_RESOURCE_SYNC_GROUP.name(), "同步宿主机监控", null, p -> p.equals(PlatformConstants.fit2cloud_vsphere_platform.name()) || p.equals(PlatformConstants.fit2cloud_openstack_platform.name()));
        // 同步云磁盘监控数据
        JobInitSettingDto syncDiskPerfMetricMonitor = new JobInitSettingDto(CloudAccountSyncJob.SyncCloudDiskPerfMetricMonitor.class, SYNC_DISK_PERF_METRIC_MONITOR, com.fit2cloud.common.constants.JobConstants.Group.CLOUD_ACCOUNT_RESOURCE_SYNC_GROUP.name(), "同步云磁盘监控", null, p -> !p.equals(PlatformConstants.fit2cloud_openstack_platform.name()));
        // 同步存储器监控数据
        JobInitSettingDto syncDatastorePerfMetricMonitor = new JobInitSettingDto(CloudAccountSyncJob.SyncCloudDatastorePerfMetricMonitor.class, SYNC_DATASTORE_PERF_METRIC_MONITOR, com.fit2cloud.common.constants.JobConstants.Group.CLOUD_ACCOUNT_RESOURCE_SYNC_GROUP.name(), "同步存储器监控", null, p -> p.equals(PlatformConstants.fit2cloud_vsphere_platform.name()) || p.equals(PlatformConstants.fit2cloud_openstack_platform.name()));
        // 同步监控数据
        //JobInitSettingDto syncResourcePerfMetricMonitor = new JobInitSettingDto(CloudAccountSyncMonitorJob.class, SYNC_PERF_METRIC_MONITOR, com.fit2cloud.common.constants.JobConstants.Group.SYSTEM_GROUP.name(), "同步云监控数据", null, s -> false, 10, DateBuilder.IntervalUnit.MINUTE);
        return List.of(syncDisk, syncVirtual, syncImage, syncHost, syncDatastore, syncVmPerfMetricMonitor, syncHostPerfMetricMonitor, syncDiskPerfMetricMonitor, syncDatastorePerfMetricMonitor);
    }
}
