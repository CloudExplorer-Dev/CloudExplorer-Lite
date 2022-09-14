package com.fit2cloud.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fit2cloud.autoconfigure.SettingJobConfig;
import com.fit2cloud.common.constants.PlatformConstants;
import com.fit2cloud.common.form.vo.Form;
import com.fit2cloud.common.platform.credential.Credential;
import com.fit2cloud.common.scheduler.impl.entity.QuzrtzJobDetail;
import com.fit2cloud.common.scheduler.SchedulerService;
import com.fit2cloud.constants.CloudAccountConstants;
import com.fit2cloud.controller.request.cloud_account.AddCloudAccountRequest;
import com.fit2cloud.controller.request.cloud_account.CloudAccountRequest;
import com.fit2cloud.controller.request.cloud_account.UpdateCloudAccountRequest;
import com.fit2cloud.controller.request.cloud_account.UpdateJobsRequest;
import com.fit2cloud.controller.response.cloud_account.CloudAccountJobDetailsResponse;
import com.fit2cloud.controller.response.cloud_account.PlatformResponse;
import com.fit2cloud.dao.entity.CloudAccount;
import com.fit2cloud.dao.mapper.CloudAccountMapper;
import com.fit2cloud.dto.module.ModuleJobInfo;
import com.fit2cloud.service.CommonService;
import com.fit2cloud.service.ICloudAccountService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.quartz.Trigger;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
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
public class CloudAccountServiceImpl extends ServiceImpl<CloudAccountMapper, CloudAccount> implements ICloudAccountService {
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    @Resource
    private SchedulerService schedulerService;
    @Resource
    private CommonService commonService;

    @Override
    public IPage<CloudAccount> page(CloudAccountRequest cloudAccountRequest) {
        Page<CloudAccount> page = Page.of(cloudAccountRequest.getCurrentPage(), cloudAccountRequest.getPageSize());
        if (cloudAccountRequest.getOrder() != null) {
            page.setOrders(new ArrayList<>() {{
                add(cloudAccountRequest.getOrder());
            }});
        }
        LambdaQueryWrapper<CloudAccount> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotEmpty(cloudAccountRequest.getName())) {
            wrapper.eq(CloudAccount::getName, cloudAccountRequest.getName());
        }
        if (CollectionUtils.isNotEmpty(cloudAccountRequest.getPlatform())) {
            wrapper.in(CloudAccount::getPlatform, cloudAccountRequest.getPlatform());
        }
        if (CollectionUtils.isNotEmpty(cloudAccountRequest.getState())) {
            wrapper.in(CloudAccount::getState, cloudAccountRequest.getState());
        }
        if (CollectionUtils.isNotEmpty(cloudAccountRequest.getStatus())) {
            wrapper.in(CloudAccount::getStatus, cloudAccountRequest.getStatus());
        }
        if (CollectionUtils.isNotEmpty(cloudAccountRequest.getUpdateTime())) {
            wrapper.between(CloudAccount::getUpdateTime, simpleDateFormat.format(cloudAccountRequest.getUpdateTime().get(0)), simpleDateFormat.format(cloudAccountRequest.getUpdateTime().get(1)));
        }
        if (CollectionUtils.isNotEmpty(cloudAccountRequest.getCreateTime())) {
            wrapper.between(CloudAccount::getCreateTime, simpleDateFormat.format(cloudAccountRequest.getCreateTime().get(0)), simpleDateFormat.format(cloudAccountRequest.getCreateTime().get(1)));
        }
        return page(page, wrapper);
    }

    @Override
    public List<PlatformResponse> getPlatforms() {
        List<PlatformResponse> platformResponses = Arrays.stream(PlatformConstants.values()).map(platform -> {
            PlatformResponse platformResponse = new PlatformResponse();
            Class<? extends Credential> credentialClass = platform.getCredentialClass();
            platformResponse.setLabel(platform.getMessage());
            platformResponse.setField(platform.name());
            try {
                List<Form> form = credentialClass.getConstructor().newInstance().toForm();
                platformResponse.setCredentialFrom(form);
            } catch (NoSuchMethodException | InvocationTargetException | InstantiationException |
                     IllegalAccessException e) {
                throw new RuntimeException(e);
            }
            return platformResponse;
        }).toList();
        return platformResponses;
    }

    @Override
    public CloudAccount save(AddCloudAccountRequest addCloudAccountRequest) {
        CloudAccount cloudAccount = new CloudAccount();
        List<Credential.Region> regions = addCloudAccountRequest.getCredential().regions();
        cloudAccount.setCredential(addCloudAccountRequest.getCredential().enCode());
        cloudAccount.setPlatform(addCloudAccountRequest.getPlatform());
        cloudAccount.setName(addCloudAccountRequest.getName());
        cloudAccount.setState(addCloudAccountRequest.getCredential().verification());
        cloudAccount.setStatus(CloudAccountConstants.Status.INIT);
        save(cloudAccount);
        HashMap<String, Object> params = getJobParams(regions, cloudAccount.getId());
        saveJob(params, cloudAccount.getId());
        return getById(cloudAccount.getId());
    }

    /**
     * 获取定时任务参数
     *
     * @param regions        区域信息
     * @param cloudAccountId 云账号id
     * @return 任务参数
     */
    @NotNull
    private HashMap<String, Object> getJobParams(List<Credential.Region> regions, String cloudAccountId) {
        // 插入定时任务
        HashMap<String, Object> params = new HashMap<>();
        params.put("region", regions);
        params.put("cloudAccountId", cloudAccountId);
        return params;
    }

    /**
     * 启动定时任务
     *
     * @param jobParams 定时任务参数
     */
    private void saveJob(HashMap<String, Object> jobParams, String accountId) {
        List<ModuleJobInfo> moduleJobs = commonService.getModuleJobs();
        for (ModuleJobInfo moduleJob : moduleJobs) {
            for (SettingJobConfig.JobDetails jobDetails : moduleJob.getJobDetailsList()) {
                if (jobDetails.getJobGroup().equals(SettingJobConfig.RESOURCE_SYNC_GROUP)) {
                    String jobName = getJobName(jobDetails.getJobName(), accountId);
                    if (!schedulerService.inclusionJobDetails(jobName, jobDetails.getJobGroup())) {
                        if (MapUtils.isEmpty(jobParams)) jobParams = new HashMap<>();
                        if (MapUtils.isNotEmpty(jobDetails.getParams())) {
                            jobParams.putAll(jobDetails.getParams());
                        }
                        schedulerService.addJob(jobDetails.getJobHandler(), jobName, jobDetails.getJobGroup(), jobDetails.getDescription(), jobParams, jobDetails.getStartTimeDay(), jobDetails.getEndTimeDay(), jobDetails.getDefaultTimeInterval(), jobDetails.getDefaultUnit(), jobDetails.getRepeatCount(), jobDetails.getWeeks());

                    }
                }
            }
        }
    }

    private String getJobName(String jobName, String accountId) {
        return jobName + "_" + accountId;
    }

    @Override
    public List<Credential.Region> listRegions(String accountId) {
        CloudAccount cloudAccount = getById(accountId);
        PlatformConstants platformConstants = PlatformConstants.valueOf(cloudAccount.getPlatform());
        try {
            Credential credential = (Credential) platformConstants.getCredentialClass().getConstructor().newInstance().deCode(cloudAccount.getCredential());
            return credential.regions();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public CloudAccountJobDetailsResponse jobs(String accountId) {
        List<ModuleJobInfo> moduleJobs = commonService.getModuleJobs();
        CloudAccountJobDetailsResponse cloudAccountJobDetailsResponse = new CloudAccountJobDetailsResponse();
        List<CloudAccountJobDetailsResponse.ModuleJob> moduleJobsList = new ArrayList<>();
        for (ModuleJobInfo moduleJobInfo : moduleJobs) {
            List<CloudAccountJobDetailsResponse.JobItem> jobItems = moduleJobInfo.getJobDetailsList().stream().map(jobDetails -> {
                String jobName = getJobName(jobDetails.getJobName(), accountId);
                QuzrtzJobDetail quzrtzJobDetail = schedulerService.getJobDetails(jobName, jobDetails.getJobGroup());
                if (quzrtzJobDetail != null) {
                    List<Credential.Region> regions = (List<Credential.Region>) quzrtzJobDetail.getParams().get("region");
                    cloudAccountJobDetailsResponse.setSelectRegion(regions);
                    return getJobItem(quzrtzJobDetail);
                }
                return null;
            }).filter(Objects::nonNull).toList();
            CloudAccountJobDetailsResponse.ModuleJob moduleJob = new CloudAccountJobDetailsResponse.ModuleJob();
            BeanUtils.copyProperties(moduleJobInfo, moduleJob);
            moduleJob.setJobDetailsList(jobItems);
            moduleJobsList.add(moduleJob);
        }
        cloudAccountJobDetailsResponse.setCloudAccountSyncJobs(moduleJobsList);
        return cloudAccountJobDetailsResponse;
    }

    @Override
    public CloudAccountJobDetailsResponse updateJob(UpdateJobsRequest updateJobsRequest) {
        HashMap<String, Object> params = new HashMap<>();
        if (CollectionUtils.isNotEmpty(updateJobsRequest.getSelectRegion())) {
            params.put("region", updateJobsRequest.getSelectRegion());
            params.put("cloudAccountId", updateJobsRequest.getCloudAccountId());
        }
        for (CloudAccountJobDetailsResponse.ModuleJob cloudAccountSyncJob : updateJobsRequest.getCloudAccountSyncJobs()) {
            List<CloudAccountJobDetailsResponse.JobItem> jobDetailsList = cloudAccountSyncJob.getJobDetailsList();
            for (CloudAccountJobDetailsResponse.JobItem jobItem : jobDetailsList) {
                schedulerService.updateJob(jobItem.getJobName(), jobItem.getJobGroup(), jobItem.getDescription(), params, null, null, jobItem.getTimeInterval().intValue(), jobItem.getUnit(), -1, jobItem.isActive() ? Trigger.TriggerState.NORMAL : Trigger.TriggerState.PAUSED, null);
            }
        }
        CloudAccountJobDetailsResponse cloudAccountJobDetailsResponse = new CloudAccountJobDetailsResponse();
        cloudAccountJobDetailsResponse.setCloudAccountSyncJobs(updateJobsRequest.getCloudAccountSyncJobs());
        cloudAccountJobDetailsResponse.setSelectRegion(updateJobsRequest.getSelectRegion());
        return cloudAccountJobDetailsResponse;
    }

    @Override
    public CloudAccount update(UpdateCloudAccountRequest updateCloudAccountRequest) {
        CloudAccount cloudAccount = new CloudAccount();
        // 校验ak sk
        updateCloudAccountRequest.getCredential().verification();
        cloudAccount.setCredential(updateCloudAccountRequest.getCredential().enCode());
        cloudAccount.setPlatform(updateCloudAccountRequest.getPlatform());
        cloudAccount.setName(updateCloudAccountRequest.getName());
        cloudAccount.setState(updateCloudAccountRequest.getCredential().verification());
        cloudAccount.setId(updateCloudAccountRequest.getId());
        updateById(cloudAccount);
        return this.getById(updateCloudAccountRequest.getId());
    }

    @Override
    public boolean delete(String accountId) {
        List<ModuleJobInfo> moduleJobs = commonService.getModuleJobs();
        for (ModuleJobInfo moduleJob : moduleJobs) {
            for (SettingJobConfig.JobDetails jobDetails : moduleJob.getJobDetailsList()) {
                schedulerService.deleteJob(getJobName(jobDetails.getJobName(), accountId), jobDetails.getJobGroup());
            }
        }
        return removeById(accountId);
    }

    @Override
    public boolean delete(ArrayList<String> cloudAccountIds) {
        for (String cloudAccountId : cloudAccountIds) {
            delete(cloudAccountId);
        }
        return true;
    }


    /**
     * 获取单个任务对象
     *
     * @param quzrtzJobDetail 任务数据
     * @return 云账号定时任务信息
     */
    private CloudAccountJobDetailsResponse.JobItem getJobItem(QuzrtzJobDetail quzrtzJobDetail) {
        CloudAccountJobDetailsResponse.JobItem jobItem = new CloudAccountJobDetailsResponse.JobItem();
        jobItem.setJobName(quzrtzJobDetail.getName());
        jobItem.setJobGroup(quzrtzJobDetail.getGroup());
        jobItem.setDescription(quzrtzJobDetail.getDescription());
        jobItem.setUnit(quzrtzJobDetail.getUnit());
        jobItem.setTimeInterval(quzrtzJobDetail.getRepeatInterval());
        Trigger.TriggerState triggerState = quzrtzJobDetail.getTriggerState();
        if (triggerState.equals(Trigger.TriggerState.PAUSED)) {
            jobItem.setActive(false);
        } else {
            jobItem.setActive(true);
        }

        return jobItem;
    }
}
