package com.fit2cloud.base.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fit2cloud.autoconfigure.JobSettingConfig;
import com.fit2cloud.base.entity.CloudAccount;
import com.fit2cloud.base.mapper.BaseCloudAccountMapper;
import com.fit2cloud.base.service.IBaseCloudAccountService;
import com.fit2cloud.common.constants.JobConstants;
import com.fit2cloud.common.constants.PlatformConstants;
import com.fit2cloud.common.constants.ProviderConstants;
import com.fit2cloud.common.exception.Fit2cloudException;
import com.fit2cloud.common.form.util.FormUtil;
import com.fit2cloud.common.form.vo.Form;
import com.fit2cloud.common.form.vo.FormObject;
import com.fit2cloud.common.platform.bill.Bill;
import com.fit2cloud.common.platform.credential.Credential;
import com.fit2cloud.common.provider.IBaseCloudProvider;
import com.fit2cloud.common.provider.entity.F2CBalance;
import com.fit2cloud.common.scheduler.SchedulerService;
import com.fit2cloud.common.scheduler.entity.QuartzJobDetail;
import com.fit2cloud.common.scheduler.handler.AsyncJob;
import com.fit2cloud.common.utils.JsonUtil;
import com.fit2cloud.common.utils.SpringUtil;
import com.fit2cloud.dto.job.JobModuleInfo;
import com.fit2cloud.dto.job.JobSetting;
import com.fit2cloud.request.cloud_account.CloudAccountJobItem;
import com.fit2cloud.request.cloud_account.CloudAccountModuleJob;
import com.fit2cloud.request.cloud_account.SyncRequest;
import com.fit2cloud.response.cloud_account.ResourceCountResponse;
import com.fit2cloud.response.cloud_account.SyncResource;
import com.fit2cloud.service.IResourceCountService;
import io.reactivex.rxjava3.functions.BiFunction;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.quartz.DateBuilder;
import org.quartz.Job;
import org.quartz.Trigger;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.function.Predicate;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author fit2cloud
 * @since 云账号相关接口
 */
@Slf4j
@Service
public class BaseCloudAccountServiceImpl extends ServiceImpl<BaseCloudAccountMapper, CloudAccount> implements IBaseCloudAccountService {
    @Resource
    private SchedulerService schedulerService;

    @Override
    public void initCloudAccountJob(String cloudAccountId) {
        CloudAccount cloudAccount = getById(cloudAccountId);
        Map<String, Object> defaultCloudAccountJobParams = getDefaultCloudAccountJobParams(cloudAccountId);
        JobModuleInfo moduleJobInfo = JobSettingConfig.getModuleJobInfo();
        moduleJobInfo.getJobDetails().stream()
                .filter(j -> j.getCloudAccountShow().test(cloudAccount.getPlatform()))
                .filter(job -> job.getJobGroup().equals(JobConstants.Group.CLOUD_ACCOUNT_RESOURCE_SYNC_GROUP.name()) || job.getJobGroup().equals(JobConstants.Group.CLOUD_ACCOUNT_BILL_SYNC_GROUP.name()))
                .forEach(job -> {
                    String cloudAccountJobName = JobConstants.CloudAccount.getCloudAccountJobName(job.getJobName(), cloudAccountId);
                    if (!schedulerService.inclusionJobDetails(cloudAccountJobName, job.getJobGroup())) {
                        schedulerService.addJob(
                                job.getJobHandler(),
                                cloudAccountJobName,
                                job.getJobGroup(),
                                job.getDescription(),
                                job.getCronExpression(),
                                job.getJobGroup().equals(JobConstants.Group.CLOUD_ACCOUNT_BILL_SYNC_GROUP.name()) ? getDefaultBillJobSettingParams(cloudAccountId, cloudAccount.getPlatform()) : defaultCloudAccountJobParams);
                    }
                });
    }

    /**
     * 获取云账号同步默认参数
     *
     * @param accountId 云账号id
     * @return 云账号定时任务默认参数
     */
    private Map<String, Object> getDefaultCloudAccountJobParams(String accountId) {
        List<Credential.Region> regions = getRegionByAccountId(accountId);
        return JobConstants.CloudAccount.getCloudAccountJobParams(accountId, regions);
    }

    /**
     * 获取默认账单设置参数
     *
     * @param accountId 云账号id
     * @param platform  供应商
     * @return 云账单任务参数
     */
    private Map<String, Object> getDefaultBillJobSettingParams(String accountId, String platform) {
        Map<String, Object> defaultParams = Bill.getDefaultParams(platform);
        return JobConstants.CloudAccount.getCloudAccountBillSettingarams(accountId, defaultParams);
    }

    @SneakyThrows
    private List<Credential.Region> getRegionByAccountId(String accountId) {
        CloudAccount cloudAccount = getById(accountId);
        PlatformConstants platformConstants = PlatformConstants.valueOf(cloudAccount.getPlatform());
        Credential credential = platformConstants.getCredentialClass().getConstructor().newInstance().deCode(cloudAccount.getCredential());
        return credential.regions();
    }

    @Override
    public CloudAccountModuleJob getCloudAccountJob(String accountId) {
        CloudAccount cloudAccount = getById(accountId);
        // todo 当前模块的任务详情
        JobModuleInfo moduleJobInfo = JobSettingConfig.getModuleJobInfo();
        CloudAccountModuleJob moduleJob = new CloudAccountModuleJob();
        BeanUtils.copyProperties(moduleJobInfo, moduleJob);
        // todo 查询到当前模块的定时任务
        List<QuartzJobDetail> quartzJobDetails = schedulerService.list();
        // todo 过滤出当前云账号的定时任务
        List<JobSetting> jobDetails = moduleJobInfo.getJobDetails().stream().filter(job -> job.getCloudAccountShow().test(cloudAccount.getPlatform())).toList();
        if (CollectionUtils.isEmpty(jobDetails)) {
            return null;
        }
        Map<String, Object> defaultCloudAccountJobParams = new HashMap<>();
        List<CloudAccountJobItem> jobItems = jobDetails.stream().map(job -> {
            String cloudAccountJobName = JobConstants.CloudAccount.getCloudAccountJobName(job.getJobName(), accountId);
            Optional<QuartzJobDetail> jobDetail = quartzJobDetails.stream().filter(j -> j.getTriggerName().equals(cloudAccountJobName)).findFirst();
            boolean activeReadOnly = job.getActiveReadOnly().test(cloudAccount.getPlatform());
            boolean cronReadOnly = job.getCronReadOnly().test(cloudAccount.getPlatform());
            if (jobDetail.isPresent()) {

                return getJobItem(jobDetail.get(), activeReadOnly, cronReadOnly);
            } else {
                // todo 针对资源同步参数设置
                if (job.getJobGroup().equals(JobConstants.Group.CLOUD_ACCOUNT_RESOURCE_SYNC_GROUP.name())) {
                    defaultCloudAccountJobParams.putAll(getDefaultCloudAccountJobParams(accountId));
                }
                // todo 针对账单同步参数设置
                else if (job.getJobGroup().equals(JobConstants.Group.CLOUD_ACCOUNT_BILL_SYNC_GROUP.name())) {
                    defaultCloudAccountJobParams.putAll(getDefaultBillJobSettingParams(accountId, cloudAccount.getPlatform()));
                }
                if (job.getJobType().equals(JobConstants.JobType.CRON)) {
                    // todo 添加定时任务
                    schedulerService.addJob(job.getJobHandler(),
                            cloudAccountJobName,
                            job.getJobGroup(),
                            job.getDescription(),
                            job.getCronExpression(),
                            defaultCloudAccountJobParams);
                } else {
                    // todo 添加定时任务
                    schedulerService.addJob(job.getJobHandler(),
                            cloudAccountJobName,
                            job.getJobGroup(),
                            job.getDescription(),
                            defaultCloudAccountJobParams,
                            job.getInterval(),
                            job.getUnit());
                }
                return getJobItem(job, accountId, defaultCloudAccountJobParams, activeReadOnly, cronReadOnly);
            }
        }).filter(Objects::nonNull).toList();
        moduleJob.setJobDetailsList(jobItems);
        return moduleJob;
    }


    @Override
    public CloudAccountModuleJob updateJob(CloudAccountModuleJob moduleJob, String accountId) {
        for (CloudAccountJobItem jobItem : moduleJob.getJobDetailsList()) {
            if (schedulerService.inclusionJobDetails(jobItem.getJobName(), jobItem.getJobGroup())) {
                Map<String, Object> params = jobItem.getParams();
                params.put(JobConstants.CloudAccount.CLOUD_ACCOUNT_ID.name(), accountId);
                if (jobItem.getJobType().equals(JobConstants.JobType.CRON)) {
                    schedulerService.updateJob(jobItem.getJobName(), jobItem.getJobGroup(), jobItem.getDescription(),
                            params, jobItem.getCronExpression(), jobItem.getActive() ? Trigger.TriggerState.NORMAL : Trigger.TriggerState.PAUSED);
                } else {
                    schedulerService.updateJob(jobItem.getJobName(), jobItem.getJobGroup(), jobItem.getDescription(),
                            params, jobItem.getInterval(), jobItem.getUnit(), jobItem.getActive() ? Trigger.TriggerState.NORMAL : Trigger.TriggerState.PAUSED);
                }

            } else {
                throw new Fit2cloudException(1, "");
            }
        }
        return moduleJob;
    }

    @Override
    public boolean deleteJobByCloudAccountId(String cloudAccountId) {
        JobModuleInfo moduleJobInfo = JobSettingConfig.getModuleJobInfo();
        for (JobSetting jobDetail : moduleJobInfo.getJobDetails()) {
            String jobName = JobConstants.CloudAccount.getCloudAccountJobName(jobDetail.getJobName(), cloudAccountId);
            schedulerService.deleteJob(jobName, jobDetail.getJobGroup());
        }
        return true;
    }

    @Override
    public List<SyncResource> getModuleResourceJob() {
        JobModuleInfo moduleJobInfo = JobSettingConfig.getModuleJobInfo();
        return moduleJobInfo.getJobDetails().stream().filter(item -> item.getJobGroup().equals(JobConstants.Group.CLOUD_ACCOUNT_RESOURCE_SYNC_GROUP.name())).map(item -> {
            SyncResource syncResource = new SyncResource();
            syncResource.setResourceDesc(item.getDescription());
            syncResource.setJobName(item.getJobName());
            syncResource.setJobGroup(item.getJobGroup());
            syncResource.setModule(moduleJobInfo.getModule());
            return syncResource;
        }).toList();
    }

    @Override
    public void sync(SyncRequest syncRequest) {
        JobModuleInfo moduleJobInfo = JobSettingConfig.getModuleJobInfo();
        for (SyncRequest.Job job : syncRequest.getSyncJob()) {
            moduleJobInfo.getJobDetails().stream().filter(j -> StringUtils.equals(job.getJobName(), j.getJobName()) && (StringUtils.equals(j.getJobGroup(), JobConstants.Group.CLOUD_ACCOUNT_RESOURCE_SYNC_GROUP.name()) || StringUtils.equals(j.getJobGroup(), JobConstants.Group.CLOUD_ACCOUNT_BILL_SYNC_GROUP.name()))).findAny().ifPresent(j -> {
                exec(syncRequest, j);
            });
        }
    }

    @Override
    public void sync(String jobName, String groupName, String cloudAccountId, Map<String, Object> params) {
        JobModuleInfo moduleJobInfo = JobSettingConfig.getModuleJobInfo();
        moduleJobInfo.getJobDetails().stream().filter(jobSettingParent -> jobSettingParent.getJobName().equals(jobName) && groupName.equals(jobSettingParent.getJobGroup())).findFirst().ifPresent(jobSettingParent -> exec(params, jobSettingParent, cloudAccountId));
    }


    @SneakyThrows
    private void exec(SyncRequest syncRequest, JobSetting j) {
        Job jobHandler = j.getJobHandler().getConstructor().newInstance();
        if (jobHandler instanceof AsyncJob) {
            QuartzJobDetail jobDetails = schedulerService.getJobDetails(JobConstants.CloudAccount.getCloudAccountJobName(j.getJobName(), syncRequest.getCloudAccountId()), j.getJobGroup());
            if (Objects.isNull(jobDetails)) {
                initCloudAccountJob(syncRequest.getCloudAccountId());
                jobDetails = schedulerService.getJobDetails(JobConstants.CloudAccount.getCloudAccountJobName(j.getJobName(), syncRequest.getCloudAccountId()), j.getJobGroup());
            }
            Map<String, Object> params = jobDetails.getTriggerJobData();
            params.put(JobConstants.CloudAccount.CLOUD_ACCOUNT_ID.name(), syncRequest.getCloudAccountId());
            params.putAll(syncRequest.getParams());
            ((AsyncJob) jobHandler).exec(params);
        }
    }

    @SneakyThrows
    private void exec(Map<String, Object> params, JobSetting j, String accountId) {
        Job jobHandler = j.getJobHandler().getConstructor().newInstance();
        QuartzJobDetail jobDetails = schedulerService.getJobDetails(JobConstants.CloudAccount.getCloudAccountJobName(j.getJobName(), accountId), j.getJobGroup());
        params.putAll(jobDetails.getTriggerJobData());
        if (jobHandler instanceof AsyncJob) {
            ((AsyncJob) jobHandler).exec(params);
        }
    }

    /**
     * @param quartzJobDetail 定时任务详细信息
     * @return JobItem
     */
    public CloudAccountJobItem getJobItem(QuartzJobDetail quartzJobDetail, boolean activeReadOnly, boolean cronReadOnly) {
        CloudAccountJobItem jobItem = new CloudAccountJobItem();
        jobItem.setJobGroup(quartzJobDetail.getTriggerGroup());
        jobItem.setJobName(quartzJobDetail.getTriggerName());
        jobItem.setActive(!quartzJobDetail.getTriggerState().equals(Trigger.TriggerState.PAUSED.name()));
        jobItem.setDescription(quartzJobDetail.getDescription());
        jobItem.setParams(quartzJobDetail.getTriggerJobData());
        jobItem.setCronExpression(quartzJobDetail.getCronExpression());
        jobItem.setInterval(quartzJobDetail.getInterval());
        jobItem.setUnit(StringUtils.isEmpty(quartzJobDetail.getUnit()) ? null : DateBuilder.IntervalUnit.valueOf(quartzJobDetail.getUnit()));
        jobItem.setActiveReadOnly(activeReadOnly);
        jobItem.setCronReadOnly(cronReadOnly);
        if (StringUtils.isNotEmpty(quartzJobDetail.getCronTimeZone())) {
            jobItem.setJobType(JobConstants.JobType.CRON);
        } else {
            jobItem.setJobType(JobConstants.JobType.INTERVAL);
        }

        return jobItem;
    }

    /**
     * @param jobSetting 定时任务初始化设置数据
     * @return JobItem          单个任务信息
     */

    private CloudAccountJobItem getJobItem(JobSetting jobSetting, String cloudAccountId, Map<String, Object> params, boolean activeReadOnly, boolean cronReadOnly) {
        CloudAccountJobItem jobItem = new CloudAccountJobItem();
        String cloudAccountJobName = JobConstants.CloudAccount.getCloudAccountJobName(jobSetting.getJobName(), cloudAccountId);
        jobItem.setJobGroup(jobSetting.getJobGroup());
        jobItem.setJobName(cloudAccountJobName);
        jobItem.setDescription(jobSetting.getDescription());
        jobItem.setCronExpression(jobItem.getCronExpression());
        jobItem.setActive(true);
        jobItem.setParams(params);
        jobItem.setJobType(jobSetting.getJobType());
        jobItem.setUnit(jobSetting.getUnit());
        jobItem.setInterval(jobSetting.getInterval());
        jobItem.setActiveReadOnly(activeReadOnly);
        jobItem.setCronReadOnly(cronReadOnly);
        return jobItem;
    }

    public List<ResourceCountResponse> getModuleResourceCount(String accountId) {
        IResourceCountService t = SpringUtil.getBeanWithoutException(IResourceCountService.class);
        if (Objects.isNull(t)) {
            return new ArrayList<>();
        } else {
            return t.count(accountId);
        }
    }

    @Override
    public List<? extends Form> getBillSettingFormByPlatform(String platform) {
        if (Arrays.stream(PlatformConstants.values()).anyMatch(p -> p.name().equals(platform))) {
            PlatformConstants platformConstants = PlatformConstants.valueOf(platform);
            FormObject formObject = FormUtil.toForm(platformConstants.getBillClass());
            return formObject == null ? null : formObject.getForms();
        } else {
            throw new Fit2cloudException(2000, "非法参数");
        }
    }

    private String getParams(String credential) {
        HashMap<String, String> params = new HashMap<>();
        params.put("credential", credential);
        return JsonUtil.toJSONString(params);
    }

    /**
     * 执行函数
     *
     * @param providerClass 执行处理器
     * @param req           请求参数
     * @param exec          执行函数
     * @param <T>           执行函数返回对象
     * @return 执行函数返回对象泛型
     */
    private <T> T exec(Class<? extends IBaseCloudProvider> providerClass, String req, BiFunction<IBaseCloudProvider, String, T> exec) {
        try {
            IBaseCloudProvider iCloudProvider = providerClass.getConstructor().newInstance();
            return exec.apply(iCloudProvider, req);
        } catch (Throwable e) {
            if (e instanceof RuntimeException) {
                throw (RuntimeException) e;
            } else {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public Object getAccountBalance(String accountId) {
        Object result = "--";
        F2CBalance f2CBalance = null;
        try {
            CloudAccount cloudAccount = this.getById(accountId);
            Class<? extends IBaseCloudProvider> cloudProvider = ProviderConstants.valueOf(cloudAccount.getPlatform()).getCloudProvider();
            f2CBalance = exec(cloudProvider, getParams(cloudAccount.getCredential()), IBaseCloudProvider::getAccountBalance);
        } catch (Exception e) {
            log.error("Error:getAccountBalance!" + e.getMessage(), e);
        }
        if (f2CBalance != null) {
            result = f2CBalance.getAmount();
        }
        return result;
    }
}
