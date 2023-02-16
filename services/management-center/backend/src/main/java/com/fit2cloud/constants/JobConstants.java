package com.fit2cloud.constants;

import com.fit2cloud.autoconfigure.JobSettingConfig;
import com.fit2cloud.common.scheduler.util.CronUtils;
import com.fit2cloud.dto.job.JobSetting;
import com.fit2cloud.quartz.VerificationCloudAccountJob;
import org.quartz.DateBuilder;

import java.util.Calendar;
import java.util.List;

/**
 * @Author:张少虎
 * @Date: 2022/9/8  6:21 PM
 * @Version 1.0
 * @注释:
 */
public class JobConstants implements JobSettingConfig.JobConfig {
    private final static String VERIFICATION_CLOUDACCOUNT = "VERIFICATION_CLOUDACCOUNT";


    @Override
    public List<JobSetting> listJobInitSetting() {
        JobSetting verificationCloudAccountJob = new JobSetting(VerificationCloudAccountJob.class, VERIFICATION_CLOUDACCOUNT,
                com.fit2cloud.common.constants.JobConstants.Group.SYSTEM_GROUP.name(), "校验云账号",
                null, CronUtils.create(new Integer[]{0}, Calendar.MINUTE), s -> false, p -> false, p -> false);
        return List.of(verificationCloudAccountJob);
    }
}
