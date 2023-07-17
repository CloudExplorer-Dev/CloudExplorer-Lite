package com.fit2cloud.autoconfigure;

import com.fit2cloud.base.entity.CloudAccount;
import com.fit2cloud.base.service.IBaseCloudAccountService;
import com.fit2cloud.common.constants.JobConstants;
import com.fit2cloud.common.scheduler.SchedulerService;
import com.fit2cloud.common.scheduler.entity.QuartzJobDetail;
import com.fit2cloud.common.utils.ClassScanUtil;
import com.fit2cloud.dto.job.JobModuleInfo;
import com.fit2cloud.dto.job.JobSetting;
import com.fit2cloud.dto.module.ModuleInfo;
import jakarta.annotation.Resource;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.quartz.Trigger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.*;

/**
 * @Author:张少虎
 * @Date: 2022/9/9  2:17 PM
 * @Version 1.0
 * @注释:
 */
@Slf4j
public class JobSettingConfig implements ApplicationContextAware {
    @Resource
    private ServerInfo serverInfo;
    @Resource
    private IBaseCloudAccountService cloudAccountService;
    /**
     * 模块定时任务对象
     */
    private static final JobModuleInfo jobModuleInfo = new JobModuleInfo();

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
    private static List<JobSetting> getModuleJob() {
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
     * 初始化任务
     * 系统定时任务   : 根据新的任务配置修改定时任务
     * 云账号定时任务 : 对于存量的定时任务,如果当前云账号的任务没有则添加
     *
     * @param jobSettings 定时任务设置
     */
    public void initJob(List<JobSetting> jobSettings) {
        // 获取所有的云账号
        List<CloudAccount> cloudAccounts = cloudAccountService.listSupportCloudAccount();
        // 当前模块所有已存在的定时任务
        List<QuartzJobDetail> quartzJobDetails = schedulerService.list();
        // 默认参数缓存, 因为获取默认参数的区域是调取api
        HashMap<String, Map<String, Map<String, Object>>> paramsCache = new HashMap<>();
        for (JobSetting localJob : jobSettings) {
            Map<String, Object> params = new HashMap<>();
            if (localJob.getJobGroup().equals(JobConstants.DefaultGroup.SYSTEM_GROUP.name())) {
                initSystemJob(quartzJobDetails, localJob, params);
            } else {
                initCloudAccountJob(cloudAccounts, quartzJobDetails, paramsCache, localJob, params);

            }
        }
    }

    /**
     * 初始化云账号相关定时任务
     *
     * @param cloudAccounts    云账号
     * @param quartzJobDetails 存量任务
     * @param paramsCache      任务参数缓存
     * @param localJob         本地任务
     * @param params           本地任务参数
     */
    private void initCloudAccountJob(List<CloudAccount> cloudAccounts, List<QuartzJobDetail> quartzJobDetails, HashMap<String, Map<String, Map<String, Object>>> paramsCache, JobSetting localJob, Map<String, Object> params) {
        for (CloudAccount cloudAccount : cloudAccounts) {
            if (localJob.getCloudAccountShow().test(cloudAccount.getPlatform())) {
                quartzJobDetails
                        .stream()
                        .filter(job -> StringUtils.equals(localJob.getJobGroup(), job.getJobGroup()) &&
                                StringUtils.equals(JobConstants.getCloudAccountJobName(localJob.getJobName(), cloudAccount.getId()), job.getJobName()))
                        .findFirst().orElseGet(() -> {
                            // 初始化任务参数
                            if (!paramsCache.getOrDefault(localJob.getJobGroup(), new HashMap<>()).containsKey(cloudAccount.getId())) {
                                Map<String, Object> defaultParams = localJob.getDefaultParams().apply(cloudAccount);
                                if (MapUtils.isNotEmpty(paramsCache.get(localJob.getJobGroup()))) {
                                    paramsCache.get(localJob.getJobGroup()).put(cloudAccount.getId(), defaultParams);
                                } else {
                                    paramsCache.put(localJob.getJobGroup(), new HashMap<>(Map.of(cloudAccount.getId(), defaultParams)));
                                }
                            }
                            HashMap<String, Object> cloudAccountJobParams = new HashMap<>(params);
                            cloudAccountJobParams.putAll(paramsCache.get(localJob.getJobGroup()).get(cloudAccount.getId()));
                            // todo 云账号相关定时任务不存在 则创建
                            if (localJob.getJobType().equals(JobConstants.JobType.CRON)) {
                                schedulerService.addJob(
                                        localJob.getJobHandler(),
                                        JobConstants.getCloudAccountJobName(localJob.getJobName(), cloudAccount.getId()),
                                        localJob.getJobGroup(),
                                        localJob.getDescription(),
                                        localJob.getCronExpression(),
                                        cloudAccountJobParams);
                            } else if (localJob.getJobType().equals(JobConstants.JobType.INTERVAL)) {
                                schedulerService.addJob(
                                        localJob.getJobHandler(),
                                        JobConstants.getCloudAccountJobName(localJob.getJobName(), cloudAccount.getId()),
                                        localJob.getJobGroup(),
                                        localJob.getDescription(),
                                        cloudAccountJobParams,
                                        localJob.getInterval(),
                                        localJob.getUnit()
                                );
                            }
                            return null;
                        });
            }
        }
    }

    /**
     * 初始化系统定时任务
     *
     * @param quartzJobDetails 存量任务
     * @param localJob         本地任务
     * @param params           本地任务参数
     */
    private void initSystemJob(List<QuartzJobDetail> quartzJobDetails, JobSetting localJob, Map<String, Object> params) {
        quartzJobDetails
                .stream()
                .filter(job -> StringUtils.equals(localJob.getJobGroup(), job.getJobGroup()) &&
                        StringUtils.equals(localJob.getJobName(), job.getJobName()))
                .findFirst()
                .map(job -> {
                    // 系统定时任务直接修改
                    if (StringUtils.isNotEmpty(localJob.getCronExpression())) {
                        schedulerService.updateJob(job.getJobName(), job.getJobGroup(), localJob.getDescription(), params, localJob.getCronExpression(), Trigger.TriggerState.NORMAL);

                    }
                    if (Objects.nonNull(localJob.getUnit())) {
                        schedulerService.updateJob(job.getJobName(), job.getJobGroup(), localJob.getDescription(), params, localJob.getInterval(), localJob.getUnit(), Trigger.TriggerState.NORMAL);
                    }
                    return job;
                }).orElseGet(() -> {
                    // 云账号相关定时任务不存在 则创建
                    if (localJob.getJobType().equals(JobConstants.JobType.CRON)) {
                        schedulerService.addJob(localJob.getJobHandler(), localJob.getJobName(),
                                localJob.getJobGroup(), localJob.getDescription(), localJob.getCronExpression(), params);
                    }
                    if (localJob.getJobType().equals(JobConstants.JobType.INTERVAL)) {
                        schedulerService.addJob(localJob.getJobHandler(), localJob.getJobName(),
                                localJob.getJobGroup(), localJob.getDescription(), params, localJob.getInterval(), localJob.getUnit());
                    }
                    return null;
                });
    }

    @Override
    public void setApplicationContext(@NotNull ApplicationContext applicationContext) throws BeansException {
        List<JobSetting> moduleJob = getModuleJob();
        initJob(moduleJob);
        initModuleJob(moduleJob);
    }

    /**
     * 初始化模型
     *
     * @param moduleJobs 模块任务
     */
    private void initModuleJob(List<JobSetting> moduleJobs) {
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
        List<JobSetting> listJobInitSetting();
    }
}
