package com.fit2cloud.provider.impl.openstack;

import com.fit2cloud.common.constants.PlatformConstants;
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
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2023/2/6  10:26}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
public class OpenstackProvider extends AbstractCloudProvider<OpenstackComplianceCredential> implements ICloudProvider {
    @Override
    public Map<ResourceTypeConstants, SyncDimensionConstants> getResourceSyncDimensionConstants() {
        return Map.of(ResourceTypeConstants.ECS, SyncDimensionConstants.REGION,
                ResourceTypeConstants.SECURITY_GROUP, SyncDimensionConstants.REGION);
    }

    @Override
    public List<ResourceInstance> listEcsInstance(String req) {
        ListEcsInstanceRequest request = JsonUtil.parseObject(req, ListEcsInstanceRequest.class);
        return OpenstackApi.listVirtualMachine(request).stream()
                .map(resource ->
                        ResourceUtil.toResourceInstance(
                                PlatformConstants.fit2cloud_openstack_platform.name(),
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
                        ResourceUtil.appendFilterArray(PlatformConstants.fit2cloud_openstack_platform.name(), ResourceTypeConstants.SECURITY_GROUP, "group_rule",
                                ResourceUtil.toResourceInstance(
                                        PlatformConstants.fit2cloud_openstack_platform.name(),
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
}
