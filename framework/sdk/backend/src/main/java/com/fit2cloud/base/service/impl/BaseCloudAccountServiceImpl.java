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
import com.fit2cloud.common.utils.JsonUtil;
import com.fit2cloud.common.utils.SpringUtil;
import com.fit2cloud.dto.job.JobInitSettingDto;
import com.fit2cloud.dto.job.JobModuleInfo;
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
        Map<String, Object> defaultCloudAccountJobParams = getDefaultCloudAccountJobParams(cloudAccountId);
        JobModuleInfo moduleJobInfo = JobSettingConfig.getModuleJobInfo();
        moduleJobInfo.getJobDetails().stream().filter(job -> job.getJobGroup().equals(JobConstants.Group.CLOUD_ACCOUNT_RESOURCE_SYNC_GROUP.name())).forEach(job -> {
            String cloudAccountJobName = JobConstants.CloudAccount.getCloudAccountJobName(job.getJobName(), cloudAccountId);
            if (!schedulerService.inclusionJobDetails(cloudAccountJobName, job.getJobGroup())) {
                schedulerService.addJob(job.getJobHandler(), cloudAccountJobName, job.getJobGroup(), job.getDescription(), defaultCloudAccountJobParams, job.getStartTimeDay(), job.getEndTimeDay(), job.getTimeInterval(), job.getUnit(), job.getRepeatCount(), job.getWeeks());
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

    @SneakyThrows
    private List<Credential.Region> getRegionByAccountId(String accountId) {
        CloudAccount cloudAccount = getById(accountId);
        PlatformConstants platformConstants = PlatformConstants.valueOf(cloudAccount.getPlatform());
        Credential credential = platformConstants.getCredentialClass().getConstructor().newInstance().deCode(cloudAccount.getCredential());
        return credential.regions();
    }

    @Override
    public CloudAccountModuleJob getCloudAccountJob(String accountId) {
        // 当前模块的任务详情
        JobModuleInfo moduleJobInfo = JobSettingConfig.getModuleJobInfo();
        CloudAccountModuleJob moduleJob = new CloudAccountModuleJob();
        BeanUtils.copyProperties(moduleJobInfo, moduleJob);
        List<QuzrtzJobDetail> quzrtzJobDetails = schedulerService.list(JobConstants.Group.CLOUD_ACCOUNT_RESOURCE_SYNC_GROUP.name());
        List<JobInitSettingDto> jobDetails = moduleJobInfo.getJobDetails().stream().filter(job -> job.getJobGroup().equals(JobConstants.Group.CLOUD_ACCOUNT_RESOURCE_SYNC_GROUP.name())).toList();
        if (CollectionUtils.isEmpty(jobDetails)) {
            return null;
        }
        Map<String, Object> defaultCloudAccountJobParams = new HashMap<>();
        List<CloudAccountJobItem> jobItems = jobDetails.stream().map(job -> {
            String cloudAccountJobName = JobConstants.CloudAccount.getCloudAccountJobName(job.getJobName(), accountId);
            Optional<QuzrtzJobDetail> jobDetail = quzrtzJobDetails.stream().filter(j -> j.getTriggerName().equals(cloudAccountJobName)).findFirst();
            if (jobDetail.isPresent()) {
                return getJobItem(jobDetail.get());
            } else {
                if (MapUtils.isEmpty(defaultCloudAccountJobParams)) {
                    defaultCloudAccountJobParams.putAll(getDefaultCloudAccountJobParams(accountId));
                }
                schedulerService.addJob(job.getJobHandler(), cloudAccountJobName, job.getJobGroup(), job.getDescription(), defaultCloudAccountJobParams, job.getStartTimeDay(), job.getEndTimeDay(), job.getTimeInterval(), job.getUnit(), job.getRepeatCount(), job.getWeeks());
                return getJobItem(job, defaultCloudAccountJobParams, accountId);
            }
        }).toList();
        moduleJob.setJobDetailsList(jobItems);
        return moduleJob;
    }


    @Override
    public CloudAccountModuleJob updateJob(CloudAccountModuleJob moduleJob, String accountId) {
        for (CloudAccountJobItem jobItem : moduleJob.getJobDetailsList()) {
            Map<String, Object> params = JobConstants.CloudAccount.getCloudAccountJobParams(accountId, jobItem.getRegions());
            if (schedulerService.inclusionJobDetails(jobItem.getJobName(), jobItem.getJobGroup())) {
                schedulerService.updateJob(jobItem.getJobName(), jobItem.getJobGroup(), jobItem.getDescription(), params, null, null, jobItem.getTimeInterval().intValue(), jobItem.getUnit(), -1, jobItem.isActive() ? Trigger.TriggerState.NORMAL : Trigger.TriggerState.PAUSED, null);
            } else {
                throw new Fit2cloudException(1, "");
            }
        }
        return moduleJob;
    }

    @Override
    public boolean deleteJobByCloudAccountId(String cloudAccountId) {
        JobModuleInfo moduleJobInfo = JobSettingConfig.getModuleJobInfo();
        for (JobInitSettingDto jobDetail : moduleJobInfo.getJobDetails()) {
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
            syncResource.setModule(moduleJobInfo.getModule());
            return syncResource;
        }).toList();
    }

    @Override
    public void sync(SyncRequest syncRequest) {
        JobModuleInfo moduleJobInfo = JobSettingConfig.getModuleJobInfo();
        for (SyncRequest.Job job : syncRequest.getSyncJob()) {
            moduleJobInfo.getJobDetails().stream().filter(j -> StringUtils.equals(job.getJobName(), j.getJobName()) && StringUtils.equals(j.getJobGroup(), JobConstants.Group.CLOUD_ACCOUNT_RESOURCE_SYNC_GROUP.name())).findAny().ifPresent(j -> {
                exec(syncRequest, j);
            });
        }
    }


    @SneakyThrows
    private void exec(SyncRequest syncRequest, JobInitSettingDto j) {
        Job jobHandler = j.getJobHandler().getConstructor().newInstance();
        if (jobHandler instanceof AsyncJob) {
            HashMap<String, Object> params = new HashMap<>();
            params.put(JobConstants.CloudAccount.CLOUD_ACCOUNT_ID.name(), syncRequest.getCloudAccountId());
            params.put(JobConstants.CloudAccount.REGIONS.name(), syncRequest.getRegions());
            ((AsyncJob) jobHandler).exec(params);
        }
    }

    /**
     * @param quzrtzJobDetail 定时任务详细信息
     * @return JobItem
     */
    public CloudAccountJobItem getJobItem(QuzrtzJobDetail quzrtzJobDetail) {
        CloudAccountJobItem jobItem = new CloudAccountJobItem();
        jobItem.setJobGroup(quzrtzJobDetail.getTriggerGroup());
        jobItem.setJobName(quzrtzJobDetail.getTriggerName());
        jobItem.setActive(!quzrtzJobDetail.getTriggerState().equals(Trigger.TriggerState.PAUSED));
        jobItem.setTimeInterval(quzrtzJobDetail.getInterval().longValue());
        jobItem.setDescription(quzrtzJobDetail.getDescription());
        jobItem.setUnit(DateBuilder.IntervalUnit.valueOf(quzrtzJobDetail.getUnit()));
        Object region = quzrtzJobDetail.getTriggerJobData().get(JobConstants.CloudAccount.REGIONS.name());
        jobItem.setRegions((List<Credential.Region>) region);
        return jobItem;
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
        jobItem.setTimeInterval((long) jobInitSettingDto.getTimeInterval());
        jobItem.setUnit(jobInitSettingDto.getUnit());
        Object region = params.get(JobConstants.CloudAccount.REGIONS.name());
        jobItem.setRegions((List<Credential.Region>) region);
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

    @Override
    public CloudAccount saveOrUpdateBillSetting(String cloudAccountId, Map params) {
        CloudAccount cloudAccount = getById(cloudAccountId);
        PlatformConstants platformConstants = PlatformConstants.valueOf(cloudAccount.getPlatform());
        if (platformConstants.getBillClass() == null) {
            throw new Fit2cloudException(1003, "不支持的云平台");
        }
        Bill bill = JsonUtil.parseObject(JsonUtil.toJSONString(params), platformConstants.getBillClass());
        bill.verification();
        cloudAccount.setBillSetting(params);
        saveOrUpdate(cloudAccount);
        return cloudAccount;
    }
}
