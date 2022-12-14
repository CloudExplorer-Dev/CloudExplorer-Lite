package com.fit2cloud.provider.impl.tencent;

import com.fit2cloud.common.constants.PlatformConstants;
import com.fit2cloud.common.provider.impl.tencent.entity.credential.TencentBaseCredential;
import com.fit2cloud.common.utils.JsonUtil;
import com.fit2cloud.constants.ResourceTypeConstants;
import com.fit2cloud.es.entity.ResourceInstance;
import com.fit2cloud.provider.AbstractCloudProvider;
import com.fit2cloud.provider.ICloudProvider;
import com.fit2cloud.provider.entity.InstanceSearchField;
import com.fit2cloud.provider.impl.tencent.api.TencentApi;
import com.fit2cloud.provider.impl.tencent.entity.request.ListCvmInstanceRequest;
import com.tencentcloudapi.cvm.v20170312.models.Instance;

import java.util.ArrayList;
import java.util.List;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2022/10/18  5:52 PM}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
public class TencentCloudProvider extends AbstractCloudProvider<TencentBaseCredential> implements ICloudProvider {

    @Override
    public List<ResourceInstance> listEcsInstance(String req) {
        ListCvmInstanceRequest listCvmInstanceRequest = JsonUtil.parseObject(req, ListCvmInstanceRequest.class);
        List<Instance> instances = TencentApi.listEcsInstance(listCvmInstanceRequest);
        return instances.stream().map(instance -> toResourceInstance(PlatformConstants.fit2cloud_tencent_platform.name(), ResourceTypeConstants.ECS, instance)).toList();
    }

    @Override
    public List<InstanceSearchField> listEcsInstanceSearchField() {
        return new ArrayList<>();
    }
}
