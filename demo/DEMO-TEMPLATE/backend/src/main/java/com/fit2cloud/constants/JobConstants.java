package com.fit2cloud.constants;

import com.fit2cloud.autoconfigure.JobSettingConfig;
import com.fit2cloud.dto.job.JobSetting;
import com.fit2cloud.quartz.DemoJob;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class JobConstants implements JobSettingConfig.JobConfig {
    public static final String DEMO_JOB = "DEMO_JOB";

    @Override
    public List<JobSetting> listJobInitSetting() {

        Map<String, Object> params = new HashMap<>();
        params.put("test-key", "test-value");

        JobSetting syncMetricMonitor = new JobSetting(
                DemoJob.MyDemoJob.class,
                DEMO_JOB,
                com.fit2cloud.common.constants.JobConstants.Group.SYSTEM_GROUP.name(),
                "demo job",
                params,
                "0 30 * * * ? *",
                p -> false,
                p -> false,
                p -> false
        );

        return List.of(syncMetricMonitor);
    }
}
