package com.fit2cloud.common.provider.impl.huawei.api;

import com.fit2cloud.common.platform.credential.Credential;
import com.fit2cloud.common.provider.impl.huawei.entity.credential.HuaweiBaseCredential;
import com.fit2cloud.common.provider.impl.huawei.entity.request.GetBucketsRequest;
import com.fit2cloud.common.provider.impl.huawei.entity.request.GetRegionsRequest;
import com.fit2cloud.common.utils.JsonUtil;
import com.obs.services.ObsClient;
import com.obs.services.model.ListBucketsRequest;
import com.obs.services.model.ObsBucket;
import com.obs.services.model.S3Bucket;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2022/10/12  11:16 AM}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
public class HuaweiBaseMethodApi {
    public static List<Credential.Region> getRegions(GetRegionsRequest request) {
        if (StringUtils.isNotEmpty(request.getCredential())) {
            HuaweiBaseCredential credential = JsonUtil.parseObject(request.getCredential(), HuaweiBaseCredential.class);
            return credential.regions();
        }
        return new ArrayList<>();
    }

    public static List<ObsBucket> getBuckets(GetBucketsRequest request) {
        if (StringUtils.isNotEmpty(request.getCredential())) {
            HuaweiBaseCredential credential = JsonUtil.parseObject(request.getCredential(), HuaweiBaseCredential.class);
            ObsClient obsClient = credential.getObsClient();
            return obsClient.listBuckets(null).stream().filter(item -> StringUtils.equals(item.getLocation(), request.getRegionId())).toList();
        }
        return new ArrayList<>();
    }
}
