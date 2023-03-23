package com.fit2cloud.common.provider.impl.aliyun;

import com.fit2cloud.common.platform.credential.Credential;
import com.fit2cloud.common.platform.credential.impl.AliCredential;
import com.fit2cloud.common.provider.AbstractBaseCloudProvider;
import com.fit2cloud.common.provider.entity.F2CBalance;
import com.fit2cloud.common.provider.impl.aliyun.api.AliyunBaseCloudApi;
import com.fit2cloud.common.provider.impl.aliyun.api.AliyunBaseMethodApi;
import com.fit2cloud.common.provider.impl.aliyun.entity.request.GetAccountBalanceRequest;
import com.fit2cloud.common.provider.impl.aliyun.entity.request.GetBucketsRequest;
import com.fit2cloud.common.provider.impl.aliyun.entity.request.GetRegionsRequest;
import com.fit2cloud.common.utils.JsonUtil;
import org.apache.commons.collections4.keyvalue.DefaultKeyValue;

import java.util.ArrayList;
import java.util.List;


public class AliyunBaseCloudProvider extends AbstractBaseCloudProvider<AliCredential> {

    @Override
    public F2CBalance getAccountBalance(String getAccountBalanceRequest) {
        return AliyunBaseCloudApi.getAccountBalance(JsonUtil.parseObject(getAccountBalanceRequest, GetAccountBalanceRequest.class));
    }

    public List<Credential.Region> getRegions(String req) {
        return AliyunBaseMethodApi.getRegions(JsonUtil.parseObject(req, GetRegionsRequest.class));
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
}
