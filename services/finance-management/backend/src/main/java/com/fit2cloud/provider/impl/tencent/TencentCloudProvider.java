package com.fit2cloud.provider.impl.tencent;

import com.fit2cloud.common.utils.JsonUtil;
import com.fit2cloud.es.entity.CloudBill;
import com.fit2cloud.provider.AbstractCloudProvider;
import com.fit2cloud.provider.ICloudProvider;
import com.fit2cloud.provider.impl.tencent.api.TencentBillApi;
import com.fit2cloud.provider.impl.tencent.entity.credential.TencentBillCredential;
import com.fit2cloud.provider.impl.tencent.entity.request.SyncBillRequest;

import java.util.List;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2022/10/18  5:52 PM}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
public class TencentCloudProvider extends AbstractCloudProvider<TencentBillCredential> implements ICloudProvider {
    @Override
    public List<CloudBill> syncBill(String request) {
        return TencentBillApi.listBill(JsonUtil.parseObject(request, SyncBillRequest.class));
    }
}
