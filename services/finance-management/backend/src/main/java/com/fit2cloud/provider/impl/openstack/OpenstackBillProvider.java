package com.fit2cloud.provider.impl.openstack;

import com.fit2cloud.common.utils.JsonUtil;
import com.fit2cloud.es.entity.CloudBill;
import com.fit2cloud.provider.AbstractCloudProvider;
import com.fit2cloud.provider.ICloudProvider;
import com.fit2cloud.provider.client.PrivateLocalCloudClient;
import com.fit2cloud.provider.constants.ProviderConstants;
import com.fit2cloud.provider.impl.openstack.entity.credential.OpenstackBillCredential;
import com.fit2cloud.provider.impl.openstack.entity.request.SyncBillRequest;

import java.util.List;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2023/6/9  14:37}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
public class OpenstackBillProvider extends AbstractCloudProvider<OpenstackBillCredential> implements ICloudProvider {
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
}
