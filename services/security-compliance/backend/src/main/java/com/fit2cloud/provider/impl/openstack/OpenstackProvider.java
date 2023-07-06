package com.fit2cloud.provider.impl.openstack;

import com.fit2cloud.common.provider.entity.F2CBalance;
import com.fit2cloud.common.provider.impl.openstack.OpenStackBaseCloudProvider;
import com.fit2cloud.common.utils.JsonUtil;
import com.fit2cloud.constants.ResourceTypeConstants;
import com.fit2cloud.constants.SyncDimensionConstants;
import com.fit2cloud.es.entity.ResourceInstance;
import com.fit2cloud.provider.AbstractCloudProvider;
import com.fit2cloud.provider.ICloudProvider;
import com.fit2cloud.provider.entity.InstanceSearchField;
import com.fit2cloud.provider.impl.openstack.api.OpenstackApi;
import com.fit2cloud.provider.impl.openstack.api.OpenstackSearchFieldApi;
import com.fit2cloud.provider.impl.openstack.entity.credential.OpenstackComplianceCredential;
import com.fit2cloud.provider.impl.openstack.entity.request.ListEcsInstanceRequest;
import com.fit2cloud.provider.impl.openstack.entity.request.ListSecurityGroupInstanceRequest;
import com.fit2cloud.provider.util.ResourceUtil;
import org.apache.commons.collections4.keyvalue.DefaultKeyValue;
import org.pf4j.Extension;

import java.util.List;
import java.util.Map;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2023/2/6  10:26}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
@Extension
public class OpenstackProvider extends AbstractCloudProvider<OpenstackComplianceCredential> implements ICloudProvider {
    public static final OpenStackBaseCloudProvider openStackBaseCloudProvider = new OpenStackBaseCloudProvider();
    public static final Info info = new Info("security-compliance", List.of(), Map.of());

    @Override
    public List<DefaultKeyValue<ResourceTypeConstants, SyncDimensionConstants>> getResourceSyncDimensionConstants() {
        return List.of(
                new DefaultKeyValue<>(ResourceTypeConstants.ECS, SyncDimensionConstants.REGION),
                new DefaultKeyValue<>(ResourceTypeConstants.SECURITY_GROUP, SyncDimensionConstants.REGION));
    }

    @Override
    public List<ResourceInstance> listEcsInstance(String req) {
        ListEcsInstanceRequest request = JsonUtil.parseObject(req, ListEcsInstanceRequest.class);
        return OpenstackApi.listVirtualMachine(request).stream()
                .map(resource ->
                        ResourceUtil.toResourceInstance(
                                getCloudAccountMeta().platform,
                                ResourceTypeConstants.ECS,
                                resource.getInstanceId(),
                                resource.getName(), resource)
                ).toList();
    }

    @Override
    public List<InstanceSearchField> listEcsInstanceSearchField() {
        return OpenstackSearchFieldApi.listEcsInstanceSearchField();
    }

    @Override
    public List<ResourceInstance> listRedisInstance(String req) {
        return List.of();
    }

    @Override
    public List<InstanceSearchField> listRedisInstanceSearchField() {
        return List.of();
    }

    @Override
    public List<ResourceInstance> listMongodbInstance(String req) {
        return List.of();
    }

    @Override
    public List<InstanceSearchField> listMongodbInstanceSearchField() {
        return List.of();
    }

    @Override
    public List<ResourceInstance> listMysqlInstance(String req) {
        return List.of();
    }

    @Override
    public List<InstanceSearchField> listMysqlInstanceSearchField() {
        return List.of();
    }

    @Override
    public List<ResourceInstance> listSqlServerInstance(String req) {
        return List.of();
    }

    @Override
    public List<InstanceSearchField> listSqlServerInstanceSearchField() {
        return List.of();
    }

    @Override
    public List<ResourceInstance> listPostGreSqlInstance(String req) {
        return List.of();
    }

    @Override
    public List<InstanceSearchField> listPostGreSqlInstanceSearchField() {
        return List.of();
    }

    @Override
    public List<ResourceInstance> listMariaDBInstance(String req) {
        return List.of();
    }

    @Override
    public List<InstanceSearchField> listMariaDBInstanceSearchField() {
        return List.of();
    }

    @Override
    public List<ResourceInstance> listElasticSearchInstance(String req) {
        return List.of();
    }

    @Override
    public List<InstanceSearchField> listElasticSearchInstanceSearchField() {
        return List.of();
    }

    @Override
    public List<ResourceInstance> listDiskInstance(String req) {
        return List.of();
    }

    @Override
    public List<InstanceSearchField> listDiskInstanceSearchField() {
        return List.of();
    }

    @Override
    public List<ResourceInstance> listLoadBalancerInstance(String req) {
        return List.of();
    }

    @Override
    public List<InstanceSearchField> listLoadBalancerInstanceSearchField() {
        return List.of();
    }

    @Override
    public List<ResourceInstance> listPublicIpInstance(String req) {
        return List.of();
    }

    @Override
    public List<InstanceSearchField> listPublicIpInstanceSearchField() {
        return List.of();
    }

    @Override
    public List<ResourceInstance> listVpcInstance(String req) {
        return List.of();
    }

    @Override
    public List<InstanceSearchField> listVpcInstanceSearchField() {
        return List.of();
    }

    @Override
    public List<ResourceInstance> listRamInstance(String req) {
        return List.of();
    }

    @Override
    public List<InstanceSearchField> listRamInstanceSearchField() {
        return List.of();
    }

    @Override
    public List<ResourceInstance> listBucketInstance(String req) {
        return List.of();
    }

    @Override
    public List<InstanceSearchField> listBucketInstanceSearchField() {
        return List.of();
    }

    @Override
    public List<ResourceInstance> listSecurityGroupInstance(String req) {
        ListSecurityGroupInstanceRequest request = JsonUtil.parseObject(req, ListSecurityGroupInstanceRequest.class);
        return OpenstackApi.listSecurityGroup(request).stream()
                .map(resource ->
                        ResourceUtil.appendFilterArray(getCloudAccountMeta().platform, ResourceTypeConstants.SECURITY_GROUP, "group_rule",
                                ResourceUtil.toResourceInstance(
                                        getCloudAccountMeta().platform,
                                        ResourceTypeConstants.SECURITY_GROUP,
                                        resource.getId(),
                                        resource.getName(),
                                        resource), (List) resource.getRules())
                )
                .toList();
    }

    @Override
    public List<InstanceSearchField> listSecurityGroupInstanceSearchField() {
        return OpenstackSearchFieldApi.listSecurityInstanceSearchField();
    }

    @Override
    public List<ResourceInstance> listHostInstance(String req) {
        return List.of();
    }

    @Override
    public List<InstanceSearchField> listHostInstanceSearchField() {
        return List.of();
    }

    @Override
    public List<ResourceInstance> listDataStoreInstance(String req) {
        return List.of();
    }

    @Override
    public List<InstanceSearchField> listDataStoreInstanceSearchField() {
        return List.of();
    }

    @Override
    public List<ResourceInstance> listResourcePoolInstance(String req) {
        return List.of();
    }

    @Override
    public List<InstanceSearchField> listResourcePoolInstanceSearchField() {
        return List.of();
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
