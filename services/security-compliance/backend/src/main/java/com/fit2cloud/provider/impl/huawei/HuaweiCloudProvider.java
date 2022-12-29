package com.fit2cloud.provider.impl.huawei;

import com.fit2cloud.common.constants.PlatformConstants;
import com.fit2cloud.common.provider.impl.huawei.entity.credential.HuaweiBaseCredential;
import com.fit2cloud.common.utils.JsonUtil;
import com.fit2cloud.constants.ResourceTypeConstants;
import com.fit2cloud.es.entity.ResourceInstance;
import com.fit2cloud.provider.AbstractCloudProvider;
import com.fit2cloud.provider.ICloudProvider;
import com.fit2cloud.provider.entity.InstanceSearchField;
import com.fit2cloud.provider.impl.huawei.api.HuaweiApi;
import com.fit2cloud.provider.impl.huawei.entity.request.ListEcsInstanceRequest;
import com.fit2cloud.provider.util.ResourceUtil;
import com.huaweicloud.sdk.ecs.v2.model.ServerDetail;

import java.util.ArrayList;
import java.util.List;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2022/10/18  5:51 PM}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
public class HuaweiCloudProvider extends AbstractCloudProvider<HuaweiBaseCredential> implements ICloudProvider {

    @Override
    public List<ResourceInstance> listEcsInstance(String req) {
        ListEcsInstanceRequest listEcsInstanceRequest = JsonUtil.parseObject(req, ListEcsInstanceRequest.class);
        List<ServerDetail> serverDetails = HuaweiApi.listEcsInstance(listEcsInstanceRequest);
        return serverDetails.stream()
                .map(instance -> ResourceUtil.toResourceInstance(PlatformConstants.fit2cloud_huawei_platform.name(), ResourceTypeConstants.ECS, instance.getId(), instance.getName(), instance))
                .toList();
    }

    @Override
    public List<InstanceSearchField> listEcsInstanceSearchField() {
        return new ArrayList<>();
    }
}
