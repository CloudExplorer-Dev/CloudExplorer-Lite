package com.fit2cloud.dto.module;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fit2cloud.autoconfigure.SettingJobConfig;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Author:张少虎
 * @Date: 2022/9/12  2:15 PM
 * @Version 1.0
 * @注释: 模块定时任务
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class ModuleJobInfo extends ModuleInfo implements Serializable {
    /**
     * 模块定时任务
     */
    private List<SettingJobConfig.JobDetails> jobDetailsList;
}
