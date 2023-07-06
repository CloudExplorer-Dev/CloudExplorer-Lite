package com.fit2cloud.constants;

import com.fit2cloud.autoconfigure.PluginsContextHolder;
import com.fit2cloud.base.entity.CloudAccount;
import com.fit2cloud.base.entity.JobRecord;
import com.fit2cloud.common.job_record.JobLink;
import com.fit2cloud.common.job_record.JobLinkTypeConstants;
import com.fit2cloud.common.platform.credential.Credential;
import com.fit2cloud.common.provider.IBaseCloudProvider;
import com.fit2cloud.common.utils.JsonUtil;
import com.fit2cloud.provider.util.ResourceUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2023/1/10  14:02}
 * {@code @Version 1.0}
 * {@code @注释: 同步维度 }
 */
public enum SyncDimensionConstants {
    /**
     * 区域粒度
     */
    REGION("区域", (cloudAccount, otherParams) ->
    {
        Class<? extends Credential> credential = PluginsContextHolder.getPlatformExtension(IBaseCloudProvider.class, cloudAccount.getPlatform())
                .getCloudAccountMeta().credential;
        return JsonUtil.parseObject(cloudAccount.getCredential(), credential)
                .regions()
                .stream()
                .map(region -> ResourceUtil.objectsToMap(Map.of("regionId", region.getRegionId()), otherParams, new HashMap<String, Object>() {{
                    put("credential", JsonUtil.parseObject(cloudAccount.getCredential(), credential));
                    put("cloudAccount", cloudAccount);
                    put("regionObj", region);
                }}))
                .toList();

    }
            , map -> new HashMap<String, Object>() {
        {
            put("region", map.get("regionObj"));
        }
    }, (jobRecord, map) -> {
        JobLink jobLink = new JobLink();
        jobLink.setType(JobLinkTypeConstants.CLOUD_SYNC_REGION_RESOURCE);
        jobLink.setDescription("同步" + ((Map) map.get("regionObj")).getOrDefault("name", "未知区域").toString() + "区域资源");
        return jobLink;

    }),

    /**
     * 云账户粒度
     */
    CloudAccount("云账户", (cloudAccount, otherParams) -> List.of(ResourceUtil.objectsToMap(otherParams, new HashMap<String, Object>() {{
        put("credential", JsonUtil.parseObject(cloudAccount.getCredential(), PluginsContextHolder.getPlatformExtension(IBaseCloudProvider.class, cloudAccount.getPlatform())
                .getCloudAccountMeta().credential));
        put("cloudAccount", cloudAccount);
    }})), map -> new HashMap<>(), ((jobRecord, stringObjectMap) -> {
        JobLink jobLink = new JobLink();
        jobLink.setType(JobLinkTypeConstants.CLOUD_SYNC_RESOURCE);
        jobLink.setDescription(jobRecord.getDescription() + "资源");
        return jobLink;
    }));

    /**
     * 提示
     */
    private String message;
    /**
     * 获取维度执行参数
     */
    private BiFunction<CloudAccount, Map<String, Object>, List<Map<String, Object>>> dimensionExecParams;

    /**
     * 当前任务参数
     */
    private Function<Map<String, Object>, Map<String, Object>> jobParams;

    /**
     * 返回任务环节
     */
    private BiFunction<JobRecord, Map<String, Object>, JobLink> jobLink;

    SyncDimensionConstants(String message,
                           BiFunction<CloudAccount, Map<String, Object>, List<Map<String, Object>>> dimensionExecParams,
                           Function<Map<String, Object>, Map<String, Object>> jobParams,
                           BiFunction<JobRecord, Map<String, Object>, JobLink> jobLink) {
        this.dimensionExecParams = dimensionExecParams;
        this.message = message;
        this.jobParams = jobParams;
        this.jobLink = jobLink;
    }

    public BiFunction<com.fit2cloud.base.entity.CloudAccount, Map<String, Object>, List<Map<String, Object>>> getDimensionExecParams() {
        return dimensionExecParams;
    }

    public Function<Map<String, Object>, Map<String, Object>> getJobParams() {
        return jobParams;
    }

    public BiFunction<JobRecord, Map<String, Object>, JobLink> getJobLink() {
        return jobLink;
    }

    public static SyncDimensionConstants getInstance() {
        return REGION;
    }
}
