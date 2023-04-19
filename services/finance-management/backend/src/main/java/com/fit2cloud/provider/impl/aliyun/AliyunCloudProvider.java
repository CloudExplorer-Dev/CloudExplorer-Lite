package com.fit2cloud.provider.impl.aliyun;

import com.fit2cloud.common.utils.JsonUtil;
import com.fit2cloud.es.entity.CloudBill;
import com.fit2cloud.provider.AbstractCloudProvider;
import com.fit2cloud.provider.ICloudProvider;
import com.fit2cloud.provider.impl.aliyun.api.AliBucketApi;
import com.fit2cloud.provider.impl.aliyun.api.AliyunBillApi;
import com.fit2cloud.provider.impl.aliyun.entity.credential.AliyunBillCredential;
import com.fit2cloud.provider.impl.aliyun.entity.request.ListBucketMonthRequest;
import com.fit2cloud.provider.impl.aliyun.entity.request.SyncBillRequest;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Objects;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2022/10/13  4:31 PM}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
public class AliyunCloudProvider extends AbstractCloudProvider<AliyunBillCredential> implements ICloudProvider {
    @Override
    public List<CloudBill> syncBill(String request) {
        SyncBillRequest syncBillRequest = JsonUtil.parseObject(request, SyncBillRequest.class);
        return Objects.nonNull(syncBillRequest.getBill()) && StringUtils.equals(syncBillRequest.getBill().getSyncMode(), "bucket") ? AliBucketApi.listBill(syncBillRequest) : AliyunBillApi.listBill(syncBillRequest);
    }

    @Override
    public List<String> listBucketFileMonth(String request) {
        ListBucketMonthRequest listBucketMonthRequest = JsonUtil.parseObject(request, ListBucketMonthRequest.class);
        return AliBucketApi.listBucketFileMonth(listBucketMonthRequest);
    }
}
