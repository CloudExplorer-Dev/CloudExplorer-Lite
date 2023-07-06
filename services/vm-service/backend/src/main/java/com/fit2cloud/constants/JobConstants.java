package com.fit2cloud.constants;

import com.fit2cloud.autoconfigure.JobSettingConfig;
import com.fit2cloud.autoconfigure.PluginsContextHolder;
import com.fit2cloud.base.entity.CloudAccount;
import com.fit2cloud.common.constants.JobTypeConstants;
import com.fit2cloud.common.platform.credential.Credential;
import com.fit2cloud.common.utils.JsonUtil;
import com.fit2cloud.dto.job.JobSetting;
import com.fit2cloud.quartz.CloudAccountSyncJob;
import com.fit2cloud.vm.ICloudProvider;
import com.fit2cloud.vm.constants.ActionInfoConstants;
import org.aopalliance.intercept.MethodInterceptor;
import org.quartz.DateBuilder;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * @Author:张少虎
 * @Date: 2022/9/8  6:21 PM
 * @Version 1.0
 * @注释:
 */
public class JobConstants implements JobSettingConfig.JobConfig {
    public enum JobSyncResourceType {
        DISK("同步磁盘", JobTypeConstants.CLOUD_ACCOUNT_SYNC_JOB),
        VIRTUAL_MACHINE("同步云主机", JobTypeConstants.CLOUD_ACCOUNT_SYNC_JOB),
        IMAGE("同步镜像", JobTypeConstants.CLOUD_ACCOUNT_SYNC_JOB),
        HOST("同步宿主机", JobTypeConstants.CLOUD_ACCOUNT_SYNC_JOB),
        DATASTORE("同步存储器", JobTypeConstants.CLOUD_ACCOUNT_SYNC_JOB),
        VIRTUAL_MACHINE_PERF_METRIC_MONITOR("同步云主机监控", JobTypeConstants.CLOUD_ACCOUNT_SYNC_METRIC_MONITOR),
        HOST_PERF_METRIC_MONITOR("同步宿主机监控", JobTypeConstants.CLOUD_ACCOUNT_SYNC_METRIC_MONITOR),
        DISK_PERF_METRIC_MONITOR("同步云磁盘监控", JobTypeConstants.CLOUD_ACCOUNT_SYNC_METRIC_MONITOR),
        DATASTORE_PERF_METRIC_MONITOR("同步存储器监控", JobTypeConstants.CLOUD_ACCOUNT_SYNC_METRIC_MONITOR);
        private String message;

        private JobTypeConstants jobType;

        JobSyncResourceType(String message, JobTypeConstants jobTypeConstants) {
            this.message = message;
            this.jobType = jobTypeConstants;
        }

        public JobTypeConstants getJobType() {
            return jobType;
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
    public static final String SYNC_METRIC_MONITOR = "SYNC_METRIC_MONITOR";

    @Override
    public List<JobSetting> listJobInitSetting() {
        Function<CloudAccount, Map<String, Object>> defaultParams = cloudAccount -> {
            ICloudProvider platformExtension = PluginsContextHolder.getPlatformExtension(ICloudProvider.class, cloudAccount.getPlatform());
            Class<? extends Credential> credential = platformExtension.getCloudAccountMeta().credential;
            List<Credential.Region> regions = JsonUtil.parseObject(cloudAccount.getCredential(), credential).regions();
            return Map.of(com.fit2cloud.common.constants.JobConstants.CloudAccount.CLOUD_ACCOUNT_ID.name(), cloudAccount.getId(),
                    com.fit2cloud.common.constants.JobConstants.CloudAccount.REGIONS.name(), regions);
        };

        // 同步云主机
        JobSetting syncVirtual = new JobSetting(CloudAccountSyncJob.SyncVirtualMachineJob.class, SYNC_VIRTUAL_MACHINE,
                com.fit2cloud.common.constants.JobConstants.DefaultGroup.CLOUD_ACCOUNT_RESOURCE_SYNC_GROUP.name(),
                "同步云主机", p -> true, defaultParams);
        // 同步磁盘
        JobSetting syncDisk = new JobSetting(CloudAccountSyncJob.SyncDiskJob.class,
                SYNC_DISK,
                com.fit2cloud.common.constants.JobConstants.DefaultGroup.CLOUD_ACCOUNT_RESOURCE_SYNC_GROUP.name(),
                "同步磁盘",
                p -> true, defaultParams);
        // 同步镜像
        JobSetting syncImage = new JobSetting(CloudAccountSyncJob.SyncImageJob.class,
                SYNC_IMAGE,
                com.fit2cloud.common.constants.JobConstants.DefaultGroup.CLOUD_ACCOUNT_RESOURCE_SYNC_GROUP.name(),
                "同步镜像", p -> true, defaultParams);
        // 同步宿主机
        JobSetting syncHost = new JobSetting(CloudAccountSyncJob.SyncHostJob.class,
                SYNC_HOST,
                com.fit2cloud.common.constants.JobConstants.DefaultGroup.CLOUD_ACCOUNT_RESOURCE_SYNC_GROUP.name(),
                "同步宿主机",
                p -> PluginsContextHolder.getPlatformExtension(ICloudProvider.class, p).getInfo().supportActionList.contains(ActionInfoConstants.SYNC_HOST), defaultParams);

        // 同步存储器
        JobSetting syncDatastore = new JobSetting(CloudAccountSyncJob.SyncDatastoreJob.class,
                SYNC_DATASTORE,
                com.fit2cloud.common.constants.JobConstants.DefaultGroup.CLOUD_ACCOUNT_RESOURCE_SYNC_GROUP.name(),
                "同步存储器",
                p -> PluginsContextHolder.getPlatformExtension(ICloudProvider.class, p).getInfo().supportActionList.contains(ActionInfoConstants.SYNC_DATASTORE), defaultParams);

        // 同步监控数据
        JobSetting syncMetricMonitor = new JobSetting(CloudAccountSyncJob.SyncMetricMonitor.class,
                SYNC_METRIC_MONITOR,
                "CLOUD_RESOURCE_METRIC_SYNC_GROUP",
                "同步监控数据", "0 30 * * * ? *", p -> true, p -> false, p -> false);
        // 记录实例状态
        JobSetting recorderInstanceState = new JobSetting(CloudAccountSyncJob.recorderInstanceState.class,
                "RECORDER_INSTANCE_STATE", com.fit2cloud.common.constants.JobConstants.DefaultGroup.SYSTEM_GROUP.name(),
                "记录实例状态", 2, DateBuilder.IntervalUnit.MINUTE, p -> false, p -> false, p -> false);
        // 记录实例变更
        JobSetting recorderInstanceChange = new JobSetting(CloudAccountSyncJob.recorderInstanceChange.class, "RECORDER_INSTANCE_CHANGE", com.fit2cloud.common.constants.JobConstants.DefaultGroup.SYSTEM_GROUP.name(),
                "记录实例变更",
                1, DateBuilder.IntervalUnit.MINUTE, p -> false, p -> false, p -> false);

        return List.of(syncVirtual, syncDisk, syncImage, syncHost, syncDatastore, syncMetricMonitor, recorderInstanceState, recorderInstanceChange);
    }
}
