package com.fit2cloud.autoconfigure;

import com.fit2cloud.common.scheduler.SchedulerService;
import com.fit2cloud.common.utils.ClassScanUtil;
import com.fit2cloud.dto.module.ModuleInfo;
import com.fit2cloud.dto.module.ModuleJobInfo;
import com.fit2cloud.security.CeGrantedAuthority;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.collections4.MapUtils;
import org.quartz.DateBuilder;
import org.quartz.Job;
import org.quartz.TimeOfDay;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * @Author:张少虎
 * @Date: 2022/9/9  2:17 PM
 * @Version 1.0
 * @注释:
 */
@Configuration
public class SettingJobConfig implements ApplicationContextAware {
    private static final String JOB_MAP = "JOB:";
    @Resource
    private ServerInfo serverInfo;
    /**
     * 模块定时任务对象
     */
    private static ModuleJobInfo moduleJobInfo = new ModuleJobInfo();
    /**
     * 所有任务数据
     */
    private static List<JobDetails> jobDetailsList;
    /**
     * 资源同步组
     */
    public static final String RESOURCE_SYNC_GROUP = "RESOURCE_SYNC_GROUP";

    /**
     * 系统定时任务
     */
    public static final String SYSTEM_GROUP = "SYSTEM_GROUP";

    @Resource
    private RedissonClient redissonClient;

    @Resource
    private SchedulerService schedulerService;

    /**
     * 获取系统定时任务
     *
     * @return 返回当前系统的定时任务
     */
    private static List<JobDetails> getModuleJob() {
        List<Class<?>> classList = ClassScanUtil.getClassList("com.fit2cloud");
        try {
            List<JobDetails> settingJobDetails1 = classList.stream().filter(clazz -> {
                return SettingJob.class.isAssignableFrom(clazz);
            }).flatMap(clazz -> {
                SettingJob settingJob = null;
                try {
                    settingJob = (SettingJob) clazz.getConstructor().newInstance();
                } catch (Exception e) {
                    return new ArrayList<JobDetails>().stream();
                }
                List<JobDetails> settingJobDetails = settingJob.listJobDetails();
                return settingJobDetails.stream();
            }).toList();
            return settingJobDetails1;
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    /**
     * @return 模块定时任务
     */
    public static ModuleJobInfo getModuleJobInfo() {
        return moduleJobInfo;
    }

    /**
     * 初始化定时任务
     *
     * @param settingJobDetails 定时任务详情
     */
    public void initSystemJob(List<JobDetails> settingJobDetails) {
        settingJobDetails = settingJobDetails.stream().filter(item -> !item.getJobGroup().equals(RESOURCE_SYNC_GROUP)).toList();
        for (JobDetails settingJobDetail : settingJobDetails) {
            Map<String, Object> params = settingJobDetail.getParams();
            if (MapUtils.isEmpty(params)) {
                params = new HashMap<>();
            }
            boolean exist = schedulerService.inclusionJobDetails(settingJobDetail.getJobName(), settingJobDetail.getJobGroup());
            if (exist && settingJobDetail.override) {
                schedulerService.deleteJob(settingJobDetail.getJobName(), settingJobDetail.getJobGroup());
                schedulerService.addJob(settingJobDetail.getJobHandler(), settingJobDetail.getJobName(), settingJobDetail.getJobGroup(), settingJobDetail.getDescription()
                        , params, settingJobDetail.getStartTimeDay(), settingJobDetail.getEndTimeDay(), settingJobDetail.getDefaultTimeInterval(), settingJobDetail.getDefaultUnit(), settingJobDetail.getRepeatCount(), settingJobDetail.getWeeks());

            } else if (!exist) {
                schedulerService.addJob(settingJobDetail.getJobHandler(), settingJobDetail.getJobName(), settingJobDetail.getJobGroup(), settingJobDetail.getDescription()
                        , params, settingJobDetail.getStartTimeDay(), settingJobDetail.getEndTimeDay(), settingJobDetail.getDefaultTimeInterval(), settingJobDetail.getDefaultUnit(), settingJobDetail.getRepeatCount(), settingJobDetail.getWeeks());
            }

        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        List<JobDetails> moduleJob = getModuleJob();
        initSystemJob(moduleJob);
        initModuleJob(moduleJob);
        putRedisModuleJob();
    }

    /**
     * 初始化模型
     *
     * @param moduleJobs 模块任务
     */
    private void initModuleJob(List<JobDetails> moduleJobs) {
        ModuleInfo moduleInfo = ServerInfo.moduleInfo;
        BeanUtils.copyProperties(moduleInfo, moduleJobInfo);
        moduleJobInfo.setJobDetailsList(moduleJobs);
    }

    private void putRedisModuleJob() {
        RMap<String, ModuleInfo> map = redissonClient.getMap(JOB_MAP);
        map.put(ServerInfo.module, moduleJobInfo);


    }

    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    public static class JobDetails implements Serializable {
        /**
         * 任务处理器
         */
        private Class<? extends Job> jobHandler;
        /**
         * 任务名称
         */
        private String jobName;
        /**
         * 任务分组
         */
        private String jobGroup;
        /**
         * 每天开始时间
         */
        private TimeOfDay startTimeDay = null;
        /**
         * 每天结束时间
         */
        private TimeOfDay endTimeDay = null;
        /**
         * 每天执行次数, -1 无限执行
         */
        private Integer repeatCount = -1;
        /**
         * 指定每周执行那几天
         * SUNDAY    周日
         * MONDAY    周一
         * TUESDAY   周二
         * WEDNESDAY 周三
         * THURSDAY  周四
         * FRIDAY    周五
         * SATURDAY  周六
         */
        private Integer[] weeks = new Integer[]{Calendar.SUNDAY, Calendar.MONDAY, Calendar.TUESDAY, Calendar.WEDNESDAY, Calendar.THURSDAY, Calendar.FRIDAY, Calendar.SATURDAY};
        /**
         * 描述
         */
        private String description = "";
        /**
         * 默认执行间隔
         */
        private int defaultTimeInterval = 1;
        /**
         * 默认间隔单位
         */
        private DateBuilder.IntervalUnit defaultUnit = DateBuilder.IntervalUnit.HOUR;
        /**
         * 定时任务执行参数
         */
        private Map<String, Object> params = null;
        /**
         * 是否在每次启动的时候,覆盖
         */
        private boolean override = false;
    }

    public static interface SettingJob {
        /**
         * 获取所有的任务详情
         *
         * @return 任务详情
         */
        List<JobDetails> listJobDetails();
    }
}
