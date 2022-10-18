package com.fit2cloud.provider.impl.aliyun;

import com.aliyun.bssopenapi20171214.models.DescribeInstanceBillResponseBody;
import com.fit2cloud.common.utils.JsonUtil;
import com.fit2cloud.es.entity.CloudBill;
import com.fit2cloud.provider.AbstractCloudProvider;
import com.fit2cloud.provider.ICloudProvider;
import com.fit2cloud.provider.impl.aliyun.api.AliyunBillApi;
import com.fit2cloud.provider.impl.aliyun.entity.credential.AliyunBillCredential;
import com.fit2cloud.provider.impl.aliyun.entity.request.SyncBillRequest;

import java.util.List;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2022/10/13  4:31 PM}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
public class AliyunCloudProvider extends AbstractCloudProvider<AliyunBillCredential> implements ICloudProvider {
    @Override
    public List<CloudBill> syncBill(String request) {
        return AliyunBillApi.listBill(JsonUtil.parseObject(request, SyncBillRequest.class));
    }
}
