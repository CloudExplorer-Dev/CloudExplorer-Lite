package com.fit2cloud.provider.impl.aliyun;

import com.fit2cloud.common.platform.credential.Credential;
import com.fit2cloud.common.provider.entity.F2CBalance;
import com.fit2cloud.common.provider.impl.aliyun.AliyunBaseCloudProvider;
import com.fit2cloud.common.provider.impl.aliyun.api.AliyunBaseMethodApi;
import com.fit2cloud.common.provider.impl.aliyun.entity.request.GetBucketsRequest;
import com.fit2cloud.common.provider.impl.aliyun.entity.request.GetRegionsRequest;
import com.fit2cloud.common.utils.JsonUtil;
import com.fit2cloud.es.entity.CloudBill;
import com.fit2cloud.provider.AbstractCloudProvider;
import com.fit2cloud.provider.ICloudProvider;
import com.fit2cloud.provider.entity.Bill;
import com.fit2cloud.provider.impl.aliyun.api.AliBucketApi;
import com.fit2cloud.provider.impl.aliyun.api.AliyunBillApi;
import com.fit2cloud.provider.impl.aliyun.entity.credential.AliyunBillCredential;
import com.fit2cloud.provider.impl.aliyun.entity.request.AliBill;
import com.fit2cloud.provider.impl.aliyun.entity.request.ListBucketMonthRequest;
import com.fit2cloud.provider.impl.aliyun.entity.request.SyncBillRequest;
import org.apache.commons.collections4.keyvalue.DefaultKeyValue;
import org.apache.commons.lang3.StringUtils;
import org.pf4j.Extension;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2022/10/13  4:31 PM}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
@Extension
public class AliyunCloudProvider extends AbstractCloudProvider<AliyunBillCredential> implements ICloudProvider {
    public static final AliyunBaseCloudProvider aliyunBaseCloudProvider = new AliyunBaseCloudProvider();

    private static final Info info = new Info("finance-management", List.of(), Map.of());

    @Override
    public Class<? extends Bill> getParamsClass() {
        return AliBill.class;
    }

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

    /**
     * 获取存储桶
     *
     * @param req 请求对象
     * @return 当前云平台所有桶
     */
    public Object getBuckets(String req) {
        return AliyunBaseMethodApi.getBuckets(JsonUtil.parseObject(req, GetBucketsRequest.class));
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
        return AliyunBaseMethodApi.getRegions(JsonUtil.parseObject(req, GetRegionsRequest.class));
    }

    @Override
    public F2CBalance getAccountBalance(String getAccountBalanceRequest) {
        return aliyunBaseCloudProvider.getAccountBalance(getAccountBalanceRequest);
    }

    @Override
    public CloudAccountMeta getCloudAccountMeta() {
        return aliyunBaseCloudProvider.getCloudAccountMeta();
    }

    @Override
    public Info getInfo() {
        return info;
    }
}
