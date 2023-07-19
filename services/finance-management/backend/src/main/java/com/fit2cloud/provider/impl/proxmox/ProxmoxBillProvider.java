package com.fit2cloud.provider.impl.proxmox;

import com.fit2cloud.common.provider.entity.F2CBalance;
import com.fit2cloud.common.provider.impl.proxmox.ProxmoxBaseCloudProvider;
import com.fit2cloud.common.utils.JsonUtil;
import com.fit2cloud.es.entity.CloudBill;
import com.fit2cloud.provider.AbstractCloudProvider;
import com.fit2cloud.provider.ICloudProvider;
import com.fit2cloud.provider.client.PrivateLocalCloudClient;
import com.fit2cloud.provider.entity.Bill;
import com.fit2cloud.provider.impl.openstack.entity.request.SyncBillRequest;
import com.fit2cloud.provider.impl.proxmox.request.ProxmoxBill;
import com.fit2cloud.provider.impl.vsphere.entity.credential.VsphereBillCredential;
import org.apache.commons.collections4.keyvalue.DefaultKeyValue;
import org.pf4j.Extension;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2023/7/19  15:14}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
@Extension
public class ProxmoxBillProvider extends AbstractCloudProvider<VsphereBillCredential> implements ICloudProvider {
    public static final ProxmoxBaseCloudProvider proxmoxBaseCloudProvider = new ProxmoxBaseCloudProvider();

    private static final Info info = new Info("finance-management", List.of(), Map.of());

    @Override
    public F2CBalance getAccountBalance(String getAccountBalanceRequest) {
        return proxmoxBaseCloudProvider.getAccountBalance(getAccountBalanceRequest);
    }

    @Override
    public CloudAccountMeta getCloudAccountMeta() {
        return proxmoxBaseCloudProvider.getCloudAccountMeta();
    }

    @Override
    public Info getInfo() {
        return info;
    }

    @Override
    public Class<? extends Bill> getParamsClass() {
        return ProxmoxBill.class;
    }

    @Override
    public List<CloudBill> syncBill(String request) {
        SyncBillRequest syncBillRequest = JsonUtil.parseObject(request, SyncBillRequest.class);
        syncBillRequest.setPlatform(proxmoxBaseCloudProvider.getCloudAccountMeta().getPlatform());
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
}
