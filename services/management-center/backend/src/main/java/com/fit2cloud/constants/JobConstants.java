package com.fit2cloud.constants;

import com.fit2cloud.autoconfigure.JobSettingConfig;
import com.fit2cloud.dto.job.JobInitSettingDto;
import com.fit2cloud.quartz.VerificationCloudAccount;
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
    public List<JobInitSettingDto> listJobInitSetting() {
        JobInitSettingDto verificationCloudAccountJob = JobInitSettingDto.builder().jobHandler(VerificationCloudAccount.class)
                .jobName(VERIFICATION_CLOUDACCOUNT)
                .jobGroup(com.fit2cloud.common.constants.JobConstants.Group.SYSTEM_GROUP.name())
                .description("校验云账号")
                .timeInterval(3)
                .repeatCount(-1)
                .unit(DateBuilder.IntervalUnit.HOUR).build();
        return List.of(verificationCloudAccountJob);
    }
}
