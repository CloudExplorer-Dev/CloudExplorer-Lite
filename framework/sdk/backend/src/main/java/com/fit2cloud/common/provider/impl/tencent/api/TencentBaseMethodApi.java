package com.fit2cloud.common.provider.impl.tencent.api;

import com.fit2cloud.common.platform.credential.Credential;
import com.fit2cloud.common.provider.impl.tencent.entity.credential.TencentBaseCredential;
import com.fit2cloud.common.provider.impl.tencent.entity.request.GetBucketsRequest;
import com.fit2cloud.common.provider.impl.tencent.entity.request.GetRegionsRequest;
import com.fit2cloud.common.utils.JsonUtil;
import com.qcloud.cos.model.Bucket;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2022/10/12  6:10 PM}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
public class TencentBaseMethodApi {

    public static List<Credential.Region> getRegions(GetRegionsRequest request) {
        if (StringUtils.isNotEmpty(request.getCredential())) {
            try {
                TencentBaseCredential credential = JsonUtil.parseObject(request.getCredential(), TencentBaseCredential.class);
                return credential.regions();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return new ArrayList<>();
    }

    public static List<Bucket> getBuckets(GetBucketsRequest request) {
        TencentBaseCredential credential = JsonUtil.parseObject(request.getCredential(), TencentBaseCredential.class);
        return credential.getCOSClient(request.getRegionId()).listBuckets().stream().filter(b -> StringUtils.equals(request.getRegionId(), b.getLocation())).toList();
    }
}
