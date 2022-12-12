package com.fit2cloud.constants;

import com.fit2cloud.autoconfigure.JobSettingConfig;
import com.fit2cloud.dto.job.JobSettingParent;

import java.util.List;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2022/12/5  15:41}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
public class JobConstants implements JobSettingConfig.JobConfig {


    @Override
    public List<JobSettingParent> listJobInitSetting() {
        return List.of();
    }
}
