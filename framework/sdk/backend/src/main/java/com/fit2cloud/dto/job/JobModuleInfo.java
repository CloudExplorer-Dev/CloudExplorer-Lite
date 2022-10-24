package com.fit2cloud.dto.job;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fit2cloud.dto.module.ModuleInfo;
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
public class JobModuleInfo extends ModuleInfo implements Serializable {

    private String module;
    /**
     * 模块定时任务
     */
    private List<JobSettingParent> jobDetails;
}
