package com.fit2cloud.provider.impl.huawei;

import com.fit2cloud.common.utils.JsonUtil;
import com.fit2cloud.es.entity.CloudBill;
import com.fit2cloud.provider.AbstractCloudProvider;
import com.fit2cloud.provider.ICloudProvider;
import com.fit2cloud.provider.impl.huawei.api.HuaweiBillApi;
import com.fit2cloud.provider.impl.huawei.api.HuaweiBucketApi;
import com.fit2cloud.provider.impl.huawei.entity.credential.HuaweiBillCredential;
import com.fit2cloud.provider.impl.huawei.entity.request.SyncBillRequest;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Objects;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2022/10/18  5:51 PM}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
public class HuaweiCloudProvider extends AbstractCloudProvider<HuaweiBillCredential> implements ICloudProvider {
    @Override
    public List<CloudBill> syncBill(String request) {
        SyncBillRequest syncBillRequest = JsonUtil.parseObject(request, SyncBillRequest.class);
        return Objects.nonNull(syncBillRequest.getBill()) && StringUtils.equals(syncBillRequest.getBill().getSyncMode(), "bucket") ? HuaweiBucketApi.listBill(syncBillRequest) : HuaweiBillApi.listBill(syncBillRequest);
    }
}
