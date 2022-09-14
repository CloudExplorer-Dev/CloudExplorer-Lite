package com.fit2cloud.constants;

import com.fit2cloud.autoconfigure.SettingJobConfig;
import com.fit2cloud.quartz.TestJob;
import org.quartz.DateBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author:张少虎
 * @Date: 2022/9/8  6:21 PM
 * @Version 1.0
 * @注释:
 */
public class SettingJobConstants implements SettingJobConfig.SettingJob {
    /**
     * 同步磁盘
     */
    private static final String SYNC_DISK = "SYNC_DISK";
    /**
     * 同步网络
     */
    private static final String SYNC_NETWORK = "SYNC_NETWORK";


    @Override
    public List<SettingJobConfig.JobDetails> listJobDetails() {
        // todo 同步网络和磁盘是属于虚拟机模块的定时任务,因为当前还没有虚拟机服务模块,就先加在了管理中心模块, 管理中心模块只配置管理中心的定时任务
        SettingJobConfig.JobDetails syncDisk = new SettingJobConfig.JobDetails(TestJob.class, SYNC_DISK, SettingJobConfig.RESOURCE_SYNC_GROUP, null, null, -1, null, "同步磁盘", 60, DateBuilder.IntervalUnit.MINUTE, null, false);
        SettingJobConfig.JobDetails syncNetwork = new SettingJobConfig.JobDetails(TestJob.class, SYNC_NETWORK, SettingJobConfig.RESOURCE_SYNC_GROUP, null, null, -1, null, "同步网络", 60, DateBuilder.IntervalUnit.MINUTE, null, false);
        return new ArrayList<>() {{
            add(syncDisk);
            add(syncNetwork);
        }};

    }
}
