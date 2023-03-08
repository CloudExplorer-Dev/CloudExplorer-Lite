package com.fit2cloud.constants;

import com.fit2cloud.autoconfigure.JobSettingConfig;
import com.fit2cloud.common.scheduler.util.CronUtils;
import com.fit2cloud.dto.job.JobSetting;
import com.fit2cloud.quartz.JobTimeOutJob;
import com.fit2cloud.quartz.VerificationCloudAccountJob;

import java.util.Calendar;
import java.util.List;

/**
 * { @Author:张少虎}
 * { @Date: 2022/9/8  6:21 PM}
 * { @Version 1.0}
 * { @注释:}
 */
public class JobConstants implements JobSettingConfig.JobConfig {

    private final static String VERIFICATION_CLOUD_ACCOUNT = "VERIFICATION_CLOUD_ACCOUNT";

    private final static String TIME_OUT_JOB = "TIME_OUT_JOB";

    @Override
    public List<JobSetting> listJobInitSetting() {
        JobSetting verificationCloudAccountJob = new JobSetting(VerificationCloudAccountJob.class, VERIFICATION_CLOUD_ACCOUNT,
                com.fit2cloud.common.constants.JobConstants.Group.SYSTEM_GROUP.name(), "校验云账号",
                null, CronUtils.create(new Integer[]{0}, Calendar.MINUTE), s -> false, p -> false, p -> false);

        JobSetting timeOutJob = new JobSetting(JobTimeOutJob.class, TIME_OUT_JOB,
                com.fit2cloud.common.constants.JobConstants.Group.SYSTEM_GROUP.name(), "检验任务超时状态",
                null, CronUtils.create(new Integer[]{0}, Calendar.MINUTE), s -> false, p -> false, p -> false);
        return List.of(verificationCloudAccountJob, timeOutJob);
    }
}
