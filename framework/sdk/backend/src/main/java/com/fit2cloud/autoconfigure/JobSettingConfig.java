package com.fit2cloud.autoconfigure;

import com.fit2cloud.common.constants.JobConstants;
import com.fit2cloud.common.scheduler.SchedulerService;
import com.fit2cloud.common.utils.ClassScanUtil;
import com.fit2cloud.dto.job.JobInitSettingDto;
import com.fit2cloud.dto.job.JobModuleInfo;
import com.fit2cloud.dto.module.ModuleInfo;
import org.apache.commons.collections4.MapUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @Author:张少虎
 * @Date: 2022/9/9  2:17 PM
 * @Version 1.0
 * @注释:
 */
public class JobSettingConfig implements ApplicationContextAware {
    @Resource
    private ServerInfo serverInfo;
    /**
     * 模块定时任务对象
     */
    private static JobModuleInfo jobModuleInfo = new JobModuleInfo();

    @Resource
    private SchedulerService schedulerService;

    /**
     * @return 模块定时任务
     */
    public static JobModuleInfo getModuleJobInfo() {
        return jobModuleInfo;
    }

    /**
     * 获取系统定时任务
     *
     * @return 返回当前系统的定时任务设置
     */
    private static List<JobInitSettingDto> getModuleJob() {
        List<Class<?>> classList = ClassScanUtil.getClassList("com.fit2cloud");
        return classList.stream().filter(JobConfig.class::isAssignableFrom).flatMap(clazz -> {
            try {
                return ((JobConfig) clazz.getConstructor().newInstance()).listJobInitSetting().stream();
            } catch (Exception e) {
                return null;
            }
        }).filter(Objects::nonNull).toList();

    }

    /**
     * 初始化定时任务
     *
     * @param settingJobDetails 定时任务详情
     */
    public void initSystemJob(List<JobInitSettingDto> settingJobDetails) {
        // 只初始化系统定时任务
        settingJobDetails = settingJobDetails.stream().filter(item -> item.getJobGroup().equals(JobConstants.Group.SYSTEM_GROUP.name())).toList();
        for (JobInitSettingDto settingJobDetail : settingJobDetails) {
            Map<String, Object> params = settingJobDetail.getParams();
            if (MapUtils.isEmpty(params)) {
                params = new HashMap<>();
            }
            boolean exist = schedulerService.inclusionJobDetails(settingJobDetail.getJobName(), settingJobDetail.getJobGroup());
            if (exist) {
                schedulerService.deleteJob(settingJobDetail.getJobName(), settingJobDetail.getJobGroup());
                schedulerService.addJob(settingJobDetail.getJobHandler(), settingJobDetail.getJobName(), settingJobDetail.getJobGroup(), settingJobDetail.getDescription()
                        , params, settingJobDetail.getStartTimeDay(), settingJobDetail.getEndTimeDay(), settingJobDetail.getTimeInterval(), settingJobDetail.getUnit(), settingJobDetail.getRepeatCount(), settingJobDetail.getWeeks());
            } else {
                schedulerService.addJob(settingJobDetail.getJobHandler(), settingJobDetail.getJobName(), settingJobDetail.getJobGroup(), settingJobDetail.getDescription()
                        , params, settingJobDetail.getStartTimeDay(), settingJobDetail.getEndTimeDay(), settingJobDetail.getTimeInterval(), settingJobDetail.getUnit(), settingJobDetail.getRepeatCount(), settingJobDetail.getWeeks());
            }
        }
    }

    @Override
    public void setApplicationContext(@NotNull ApplicationContext applicationContext) throws BeansException {
        List<JobInitSettingDto> moduleJob = getModuleJob();
        initSystemJob(moduleJob);
        initModuleJob(moduleJob);
    }

    /**
     * 初始化模型
     *
     * @param moduleJobs 模块任务
     */
    private void initModuleJob(List<JobInitSettingDto> moduleJobs) {
        ModuleInfo moduleInfo = serverInfo.getModuleInfo();
        BeanUtils.copyProperties(moduleInfo, jobModuleInfo);
        jobModuleInfo.setModule(serverInfo.getModule());
        jobModuleInfo.setJobDetails(moduleJobs);
    }


    public interface JobConfig {
        /**
         * 获取所有的任务详情
         *
         * @return 任务详情
         */
        List<JobInitSettingDto> listJobInitSetting();
    }
}
