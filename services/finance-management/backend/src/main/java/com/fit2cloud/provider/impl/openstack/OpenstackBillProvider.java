package com.fit2cloud.provider.impl.openstack;

import com.fit2cloud.common.platform.credential.Credential;
import com.fit2cloud.common.provider.entity.F2CBalance;
import com.fit2cloud.common.provider.impl.openstack.OpenStackBaseCloudProvider;
import com.fit2cloud.common.provider.impl.openstack.api.OpenStackBaseMethodApi;
import com.fit2cloud.common.provider.impl.openstack.entity.request.OpenStackBaseRequest;
import com.fit2cloud.common.utils.JsonUtil;
import com.fit2cloud.es.entity.CloudBill;
import com.fit2cloud.provider.AbstractCloudProvider;
import com.fit2cloud.provider.ICloudProvider;
import com.fit2cloud.provider.client.PrivateLocalCloudClient;
import com.fit2cloud.provider.constants.ProviderConstants;
import com.fit2cloud.provider.entity.Bill;
import com.fit2cloud.provider.impl.openstack.entity.credential.OpenstackBillCredential;
import com.fit2cloud.provider.impl.openstack.entity.request.OpenStackBill;
import com.fit2cloud.provider.impl.openstack.entity.request.SyncBillRequest;
import org.apache.commons.collections4.keyvalue.DefaultKeyValue;
import org.pf4j.Extension;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2023/6/9  14:37}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
@Extension
public class OpenstackBillProvider extends AbstractCloudProvider<OpenstackBillCredential> implements ICloudProvider {
    public static final OpenStackBaseCloudProvider openStackBaseCloudProvider = new OpenStackBaseCloudProvider();

    private static final Info info = new Info("finance-management", List.of(), Map.of());

    @Override
    public Class<? extends Bill> getParamsClass() {
        return OpenStackBill.class;
    }

    @Override
    public List<CloudBill> syncBill(String request) {
        SyncBillRequest syncBillRequest = JsonUtil.parseObject(request, SyncBillRequest.class);
        syncBillRequest.setPlatform(ProviderConstants.fit2cloud_openstack_platform.name());
        PrivateLocalCloudClient client = syncBillRequest.getCredential().getClient();
        return client.listCloudBill(syncBillRequest);
    }

    @Override
    public List<String> listBucketFileMonth(String request) {
        return List.of();
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
        }};
    }

    public List<Credential.Region> getRegions(String req) {
        return OpenStackBaseMethodApi.getRegions(JsonUtil.parseObject(req, OpenStackBaseRequest.class));
    }

    @Override
    public F2CBalance getAccountBalance(String getAccountBalanceRequest) {
        return openStackBaseCloudProvider.getAccountBalance(getAccountBalanceRequest);
    }

    @Override
    public CloudAccountMeta getCloudAccountMeta() {
        return openStackBaseCloudProvider.getCloudAccountMeta();
    }

    @Override
    public Info getInfo() {
        return info;
    }
}
