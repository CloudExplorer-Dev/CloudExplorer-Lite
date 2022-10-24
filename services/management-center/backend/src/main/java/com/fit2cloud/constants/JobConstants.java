package com.fit2cloud.constants;

import com.fit2cloud.autoconfigure.JobSettingConfig;
import com.fit2cloud.dto.job.JobInitSettingDto;
import com.fit2cloud.dto.job.JobSettingParent;
import com.fit2cloud.quartz.VerificationCloudAccountJob;
import org.quartz.DateBuilder;

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
    public List<JobSettingParent> listJobInitSetting() {
        JobInitSettingDto verificationCloudAccountJob = new JobInitSettingDto(VerificationCloudAccountJob.class, VERIFICATION_CLOUDACCOUNT, com.fit2cloud.common.constants.JobConstants.Group.SYSTEM_GROUP.name(), "校验云账号", null, s -> false, 3, DateBuilder.IntervalUnit.HOUR);
        return List.of(verificationCloudAccountJob);
    }
}
