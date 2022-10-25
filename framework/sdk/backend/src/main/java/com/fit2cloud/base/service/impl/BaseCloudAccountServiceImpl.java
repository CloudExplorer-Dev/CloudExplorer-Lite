package com.fit2cloud.base.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fit2cloud.autoconfigure.JobSettingConfig;
import com.fit2cloud.base.entity.CloudAccount;
import com.fit2cloud.base.mapper.BaseCloudAccountMapper;
import com.fit2cloud.base.service.IBaseCloudAccountService;
import com.fit2cloud.common.constants.JobConstants;
import com.fit2cloud.common.constants.PlatformConstants;
import com.fit2cloud.common.exception.Fit2cloudException;
import com.fit2cloud.common.form.util.FormUtil;
import com.fit2cloud.common.form.vo.Form;
import com.fit2cloud.common.form.vo.FormObject;
import com.fit2cloud.common.platform.bill.Bill;
import com.fit2cloud.common.platform.credential.Credential;
import com.fit2cloud.common.scheduler.SchedulerService;
import com.fit2cloud.common.scheduler.entity.QuzrtzJobDetail;
import com.fit2cloud.common.scheduler.handler.AsyncJob;
import com.fit2cloud.common.scheduler.util.CronUtils;
import com.fit2cloud.common.utils.JsonUtil;
import com.fit2cloud.common.utils.SpringUtil;
import com.fit2cloud.dto.job.JobCronSettingDto;
import com.fit2cloud.dto.job.JobInitSettingDto;
import com.fit2cloud.dto.job.JobModuleInfo;
import com.fit2cloud.dto.job.JobSettingParent;
import com.fit2cloud.request.cloud_account.CloudAccountJobItem;
import com.fit2cloud.request.cloud_account.CloudAccountModuleJob;
import com.fit2cloud.request.cloud_account.SyncRequest;
import com.fit2cloud.response.cloud_account.ResourceCountResponse;
import com.fit2cloud.response.cloud_account.SyncResource;
import com.fit2cloud.service.IResourceCountService;
import lombok.SneakyThrows;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.quartz.DateBuilder;
import org.quartz.Job;
import org.quartz.Trigger;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author fit2cloud
 * @since
 */
@Service
public class BaseCloudAccountServiceImpl extends ServiceImpl<BaseCloudAccountMapper, CloudAccount> implements IBaseCloudAccountService {
    @Resource
    private SchedulerService schedulerService;

    @Override
    public void initCloudAccountJob(String cloudAccountId) {
        CloudAccount cloudAccount = getById(cloudAccountId);
        Map<String, Object> defaultCloudAccountJobParams = getDefaultCloudAccountJobParams(cloudAccountId);
        Map<String, Object> defaultBillJobSettingParams = getDefaultBillJobSettingParams(cloudAccountId, cloudAccount.getPlatform());
        JobModuleInfo moduleJobInfo = JobSettingConfig.getModuleJobInfo();
        moduleJobInfo.getJobDetails().stream().filter(job -> job.getJobGroup().equals(JobConstants.Group.CLOUD_ACCOUNT_RESOURCE_SYNC_GROUP.name()) || job.getJobGroup().equals(JobConstants.Group.CLOUD_ACCOUNT_BILL_SYNC_GROUP.name())).forEach(job -> {
            String cloudAccountJobName = JobConstants.CloudAccount.getCloudAccountJobName(job.getJobName(), cloudAccountId);
            if (!schedulerService.inclusionJobDetails(cloudAccountJobName, job.getJobGroup())) {
                if (job instanceof JobInitSettingDto jobInitSettingDto) {
                    schedulerService.addJob(jobInitSettingDto.getJobHandler(), cloudAccountJobName, jobInitSettingDto.getJobGroup(), jobInitSettingDto.getDescription(), job.getJobGroup().equals(JobConstants.Group.CLOUD_ACCOUNT_BILL_SYNC_GROUP.name()) ? defaultBillJobSettingParams : defaultCloudAccountJobParams, jobInitSettingDto.getStartTimeDay(), jobInitSettingDto.getEndTimeDay(), jobInitSettingDto.getTimeInterval(), jobInitSettingDto.getUnit(), jobInitSettingDto.getRepeatCount(), jobInitSettingDto.getWeeks());
                } else if (job instanceof JobCronSettingDto jobCronSettingDto) {
                    schedulerService.addJob(jobCronSettingDto.getJobHandler(), cloudAccountJobName, jobCronSettingDto.getJobGroup(), jobCronSettingDto.getDescription(), CronUtils.createHourOfDay(jobCronSettingDto.getHoursOfDay()), job.getJobGroup().equals(JobConstants.Group.CLOUD_ACCOUNT_BILL_SYNC_GROUP.name()) ? defaultBillJobSettingParams : defaultCloudAccountJobParams);
                }
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
     * @return
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
        // 当前模块的任务详情
        JobModuleInfo moduleJobInfo = JobSettingConfig.getModuleJobInfo();
        CloudAccountModuleJob moduleJob = new CloudAccountModuleJob();
        BeanUtils.copyProperties(moduleJobInfo, moduleJob);
        List<QuzrtzJobDetail> quzrtzJobDetails = schedulerService.list(JobConstants.Group.CLOUD_ACCOUNT_RESOURCE_SYNC_GROUP.name());
        quzrtzJobDetails.addAll(schedulerService.list(JobConstants.Group.CLOUD_ACCOUNT_BILL_SYNC_GROUP.name()));
        List<JobSettingParent> jobDetails = moduleJobInfo.getJobDetails().stream().filter(job -> job.getCloudAccountShow().test(cloudAccount.getPlatform())).toList();
        if (CollectionUtils.isEmpty(jobDetails)) {
            return null;
        }
        Map<String, Object> defaultCloudAccountJobParams = new HashMap<>();
        List<CloudAccountJobItem> jobItems = jobDetails.stream().map(job -> {
            String cloudAccountJobName = JobConstants.CloudAccount.getCloudAccountJobName(job.getJobName(), accountId);
            Optional<QuzrtzJobDetail> jobDetail = quzrtzJobDetails.stream().filter(j -> j.getTriggerName().equals(cloudAccountJobName)).findFirst();
            if (jobDetail.isPresent()) {
                return getJobItem(jobDetail.get(), job);
            } else {
                // 针对资源同步参数设置
                if (job.getJobGroup().equals(JobConstants.Group.CLOUD_ACCOUNT_RESOURCE_SYNC_GROUP.name())) {
                    defaultCloudAccountJobParams.putAll(getDefaultCloudAccountJobParams(accountId));
                }
                // 针对账单同步参数设置
                if (job.getJobGroup().equals(JobConstants.Group.CLOUD_ACCOUNT_BILL_SYNC_GROUP.name())) {
                    defaultCloudAccountJobParams.putAll(getDefaultBillJobSettingParams(accountId, cloudAccount.getPlatform()));
                }
                if (job instanceof JobInitSettingDto jobInitSettingDto) {
                    schedulerService.addJob(jobInitSettingDto.getJobHandler(), cloudAccountJobName, jobInitSettingDto.getJobGroup(), jobInitSettingDto.getDescription(), defaultCloudAccountJobParams, jobInitSettingDto.getStartTimeDay(), jobInitSettingDto.getEndTimeDay(), jobInitSettingDto.getTimeInterval(), jobInitSettingDto.getUnit(), jobInitSettingDto.getRepeatCount(), jobInitSettingDto.getWeeks());
                    return getJobItem(jobInitSettingDto, defaultCloudAccountJobParams, accountId);
                } else if (job instanceof JobCronSettingDto jobCronSettingDto) {
                    schedulerService.addJob(jobCronSettingDto.getJobHandler(), cloudAccountJobName, jobCronSettingDto.getJobGroup(), jobCronSettingDto.getDescription(), CronUtils.createHourOfDay(jobCronSettingDto.getHoursOfDay()), defaultCloudAccountJobParams);
                    return getJobItem(jobCronSettingDto, accountId, defaultCloudAccountJobParams);
                }
                return null;
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
                if (jobItem.getJobGroup().equals(JobConstants.Group.CLOUD_ACCOUNT_RESOURCE_SYNC_GROUP.name())) {
                    schedulerService.updateJob(jobItem.getJobName(), jobItem.getJobGroup(), jobItem.getDescription(), params, null, null, jobItem.getTimeInterval().intValue(), jobItem.getUnit(), -1, jobItem.getActive() ? Trigger.TriggerState.NORMAL : Trigger.TriggerState.PAUSED, null);
                } else {
                    schedulerService.updateJob(jobItem.getJobName(), jobItem.getJobGroup(), jobItem.getDescription(), params, CronUtils.createHourOfDay(jobItem.getHoursOfDay().toArray(Integer[]::new)), jobItem.getActive() ? Trigger.TriggerState.NORMAL : Trigger.TriggerState.PAUSED);
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
        for (JobSettingParent jobDetail : moduleJobInfo.getJobDetails()) {
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
    private void exec(SyncRequest syncRequest, JobSettingParent j) {
        Job jobHandler = j.getJobHandler().getConstructor().newInstance();
        if (jobHandler instanceof AsyncJob) {
            QuzrtzJobDetail jobDetails = schedulerService.getJobDetails(JobConstants.CloudAccount.getCloudAccountJobName(j.getJobName(), syncRequest.getCloudAccountId()), j.getJobGroup());
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
    private void exec(Map<String, Object> params, JobSettingParent j, String accountId) {
        Job jobHandler = j.getJobHandler().getConstructor().newInstance();
        QuzrtzJobDetail jobDetails = schedulerService.getJobDetails(JobConstants.CloudAccount.getCloudAccountJobName(j.getJobName(), accountId), j.getJobGroup());
        params.putAll(jobDetails.getTriggerJobData());
        if (jobHandler instanceof AsyncJob) {
            ((AsyncJob) jobHandler).exec(params);
        }
    }

    /**
     * @param quzrtzJobDetail 定时任务详细信息
     * @return JobItem
     */
    public CloudAccountJobItem getJobItem(QuzrtzJobDetail quzrtzJobDetail, JobSettingParent job) {
        CloudAccountJobItem jobItem = new CloudAccountJobItem();
        jobItem.setJobGroup(quzrtzJobDetail.getTriggerGroup());
        jobItem.setJobName(quzrtzJobDetail.getTriggerName());
        jobItem.setActive(!quzrtzJobDetail.getTriggerState().equals(Trigger.TriggerState.PAUSED.name()));
        jobItem.setDescription(quzrtzJobDetail.getDescription());
        jobItem.setParams(quzrtzJobDetail.getTriggerJobData());
        if (jobItem.getJobGroup().equals(JobConstants.Group.CLOUD_ACCOUNT_BILL_SYNC_GROUP.name())) {
            jobItem.setHoursOfDay(getHoursByCron(quzrtzJobDetail.getCronExpressopn()));
        } else if (jobItem.getJobGroup().equals(JobConstants.Group.CLOUD_ACCOUNT_RESOURCE_SYNC_GROUP.name())) {
            jobItem.setTimeInterval(quzrtzJobDetail.getInterval().longValue());
            jobItem.setUnit(DateBuilder.IntervalUnit.valueOf(quzrtzJobDetail.getUnit()));
        }
        if (job instanceof JobCronSettingDto) {
            jobItem.setJobType(CloudAccountJobItem.JobType.SpecifyHour);
        }
        if (job instanceof JobInitSettingDto) {
            jobItem.setJobType(CloudAccountJobItem.JobType.Interval);
        }
        return jobItem;
    }

    private List<Integer> getHoursByCron(String cron) {
        if (StringUtils.isEmpty(cron)) {
            return List.of(24);
        }
        return Arrays.stream(cron.split(" ")[2].split(",")).map(Integer::parseInt).toList();
    }

    /**
     * @param jobInitSettingDto 定时任务初始化设置数据
     * @param params            定时任务默认参数
     * @return JobItem          单个任务信息
     */

    private CloudAccountJobItem getJobItem(JobInitSettingDto jobInitSettingDto, Map<String, Object> params, String cloudAccountId) {
        CloudAccountJobItem jobItem = new CloudAccountJobItem();
        String cloudAccountJobName = JobConstants.CloudAccount.getCloudAccountJobName(jobInitSettingDto.getJobName(), cloudAccountId);
        jobItem.setJobGroup(jobInitSettingDto.getJobGroup());
        jobItem.setJobName(cloudAccountJobName);
        jobItem.setDescription(jobInitSettingDto.getDescription());
        jobItem.setActive(true);
        jobItem.setJobType(CloudAccountJobItem.JobType.Interval);
        jobItem.setTimeInterval((long) jobInitSettingDto.getTimeInterval());
        jobItem.setUnit(jobInitSettingDto.getUnit());
        jobItem.setParams(params);
        return jobItem;

    }

    /**
     * @param jobCronSettingDto 定时任务初始化设置数据
     * @return JobItem          单个任务信息
     */

    private CloudAccountJobItem getJobItem(JobCronSettingDto jobCronSettingDto, String cloudAccountId, Map<String, Object> params) {
        CloudAccountJobItem jobItem = new CloudAccountJobItem();
        String cloudAccountJobName = JobConstants.CloudAccount.getCloudAccountJobName(jobCronSettingDto.getJobName(), cloudAccountId);
        jobItem.setJobGroup(jobCronSettingDto.getJobGroup());
        jobItem.setJobName(cloudAccountJobName);
        jobItem.setDescription(jobCronSettingDto.getDescription());
        jobItem.setActive(true);
        jobItem.setJobType(CloudAccountJobItem.JobType.SpecifyHour);
        jobItem.setHoursOfDay(Arrays.asList(jobCronSettingDto.getHoursOfDay()));
        jobItem.setParams(params);
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
}
