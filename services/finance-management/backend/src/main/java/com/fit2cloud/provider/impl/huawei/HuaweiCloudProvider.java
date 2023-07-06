package com.fit2cloud.provider.impl.huawei;

import com.fit2cloud.common.platform.credential.Credential;
import com.fit2cloud.common.provider.entity.F2CBalance;
import com.fit2cloud.common.provider.impl.huawei.HuaweiBaseCloudProvider;
import com.fit2cloud.common.provider.impl.huawei.api.HuaweiBaseMethodApi;
import com.fit2cloud.common.provider.impl.huawei.entity.request.GetBucketsRequest;
import com.fit2cloud.common.provider.impl.huawei.entity.request.GetRegionsRequest;
import com.fit2cloud.common.utils.JsonUtil;
import com.fit2cloud.es.entity.CloudBill;
import com.fit2cloud.provider.AbstractCloudProvider;
import com.fit2cloud.provider.ICloudProvider;
import com.fit2cloud.provider.entity.Bill;
import com.fit2cloud.provider.impl.huawei.api.HuaweiBillApi;
import com.fit2cloud.provider.impl.huawei.api.HuaweiBucketApi;
import com.fit2cloud.provider.impl.huawei.entity.credential.HuaweiBillCredential;
import com.fit2cloud.provider.impl.huawei.entity.request.HuaweiBill;
import com.fit2cloud.provider.impl.huawei.entity.request.ListBucketMonthRequest;
import com.fit2cloud.provider.impl.huawei.entity.request.SyncBillRequest;
import org.apache.commons.collections4.keyvalue.DefaultKeyValue;
import org.apache.commons.lang3.StringUtils;
import org.pf4j.Extension;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2022/10/18  5:51 PM}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
@Extension
public class HuaweiCloudProvider extends AbstractCloudProvider<HuaweiBillCredential> implements ICloudProvider {

    public static final HuaweiBaseCloudProvider huaweiBaseCloudProvider = new HuaweiBaseCloudProvider();

    private static final Info info = new Info("finance-management", List.of(), Map.of());

    @Override
    public Class<? extends Bill> getParamsClass() {
        return HuaweiBill.class;
    }

    @Override
    public List<CloudBill> syncBill(String request) {
        SyncBillRequest syncBillRequest = JsonUtil.parseObject(request, SyncBillRequest.class);
        return Objects.nonNull(syncBillRequest.getBill()) && StringUtils.equals(syncBillRequest.getBill().getSyncMode(), "bucket") ? HuaweiBucketApi.listBill(syncBillRequest) : HuaweiBillApi.listBill(syncBillRequest);
    }

    @Override
    public List<String> listBucketFileMonth(String request) {
        ListBucketMonthRequest listBucketMonthRequest = JsonUtil.parseObject(request, ListBucketMonthRequest.class);
        return HuaweiBucketApi.listBucketFileMonth(listBucketMonthRequest);
    }


    public Object getBuckets(String req) {
        return HuaweiBaseMethodApi.getBuckets(JsonUtil.parseObject(req, GetBucketsRequest.class));
    }

    /**
     * 获取账单同步方式
     *
     * @param req 请求字符串
     * @return 账单所有同步方式
     */
    public List<DefaultKeyValue<String, String>> getSyncModes(String req) {
        return new ArrayList<>() {{
            add(new DefaultKeyValue<>("从API获取", "api"));
            add(new DefaultKeyValue<>("从存储桶获取", "bucket"));
        }};
    }

    public List<Credential.Region> getRegions(String req) {
        return HuaweiBaseMethodApi.getRegions(JsonUtil.parseObject(req, GetRegionsRequest.class));
    }

    @Override
    public F2CBalance getAccountBalance(String getAccountBalanceRequest) {
        return huaweiBaseCloudProvider.getAccountBalance(getAccountBalanceRequest);
    }

    @Override
    public CloudAccountMeta getCloudAccountMeta() {
        return huaweiBaseCloudProvider.getCloudAccountMeta();
    }

    @Override
    public Info getInfo() {
        return info;
    }
}
