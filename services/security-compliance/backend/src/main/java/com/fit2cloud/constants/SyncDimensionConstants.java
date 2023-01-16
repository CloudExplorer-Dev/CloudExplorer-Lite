package com.fit2cloud.constants;

import com.fit2cloud.base.entity.CloudAccount;
import com.fit2cloud.common.job_record.JobRecordParam;
import com.fit2cloud.common.platform.credential.Credential;
import com.fit2cloud.es.entity.ResourceInstance;
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
            Credential.of(cloudAccount.getPlatform(), cloudAccount.getCredential())
                    .regions()
                    .stream()
                    .map(region -> ResourceUtil.objectsToMap(Map.of("regionId", region.getRegionId()), otherParams, new HashMap<String, Object>() {{
                        put("credential", Credential.of(cloudAccount.getPlatform(), cloudAccount.getCredential()));
                        put("cloudAccount", cloudAccount);
                        put("regionObj", region);
                    }}))
                    .toList()
            , map -> new HashMap<String, Object>() {{
        put("region", map.get("regionObj"));
    }}),

    /**
     * 云账户粒度
     */
    CloudAccount("云账户", (cloudAccount, otherParams) -> List.of(ResourceUtil.objectsToMap(otherParams, new HashMap<String, Object>() {{
        put("credential", Credential.of(cloudAccount.getPlatform(), cloudAccount.getCredential()));
        put("cloudAccount", cloudAccount);
    }})), map -> new HashMap<>());

    /**
     * 提示
     */
    private String message;
    /**
     * 获取维度执行参数
     */
    private BiFunction<CloudAccount, Map<String, Object>, List<Map<String, Object>>> dimensionExecParams;

    private Function<Map<String, Object>, Map<String, Object>> jobParams;

    SyncDimensionConstants(String message, BiFunction<CloudAccount, Map<String, Object>, List<Map<String, Object>>> dimensionExecParams, Function<Map<String, Object>, Map<String, Object>> jobParams) {
        this.dimensionExecParams = dimensionExecParams;
        this.message = message;
        this.jobParams = jobParams;
    }

    public BiFunction<com.fit2cloud.base.entity.CloudAccount, Map<String, Object>, List<Map<String, Object>>> getDimensionExecParams() {
        return dimensionExecParams;
    }

    public Function<Map<String, Object>, Map<String, Object>> getJobParams() {
        return jobParams;
    }

    public static SyncDimensionConstants getInstance() {
        return REGION;
    }
}
