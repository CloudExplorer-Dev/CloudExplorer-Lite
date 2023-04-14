package com.fit2cloud.common.provider.impl.aliyun.api;

import com.aliyun.sdk.service.oss20190517.AsyncClient;
import com.aliyun.sdk.service.oss20190517.models.ListBucketsRequest;
import com.fit2cloud.common.platform.credential.Credential;
import com.fit2cloud.common.provider.impl.aliyun.entity.credential.AliyunBaseCredential;
import com.fit2cloud.common.provider.impl.aliyun.entity.request.GetBucketsRequest;
import com.fit2cloud.common.provider.impl.aliyun.entity.request.GetRegionsRequest;
import com.fit2cloud.common.utils.JsonUtil;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2022/10/12  6:10 PM}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
public class AliyunBaseMethodApi {

    public static List<Credential.Region> getRegions(GetRegionsRequest request) {
        if (StringUtils.isNotEmpty(request.getCredential())) {
            try {
                AliyunBaseCredential credential = JsonUtil.parseObject(request.getCredential(), AliyunBaseCredential.class);
                return credential.regions();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return new ArrayList<>();
    }

    public static Object getBuckets(GetBucketsRequest request) {
        AliyunBaseCredential credential = JsonUtil.parseObject(request.getCredential(), AliyunBaseCredential.class);
        AsyncClient ossClient = credential.getOssClient();
        try {
            return ossClient.listBuckets(ListBucketsRequest.create()).get().getBody().getBuckets().stream().filter(item -> StringUtils.equals("oss-" + request.getRegionId(), item.getLocation())).toList();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
