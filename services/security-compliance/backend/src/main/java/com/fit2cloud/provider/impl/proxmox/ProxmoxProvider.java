package com.fit2cloud.provider.impl.proxmox;

import com.fit2cloud.common.provider.entity.F2CBalance;
import com.fit2cloud.common.provider.impl.proxmox.ProxmoxBaseCloudProvider;
import com.fit2cloud.common.utils.JsonUtil;
import com.fit2cloud.constants.ResourceTypeConstants;
import com.fit2cloud.constants.SyncDimensionConstants;
import com.fit2cloud.es.entity.ResourceInstance;
import com.fit2cloud.provider.AbstractCloudProvider;
import com.fit2cloud.provider.ICloudProvider;
import com.fit2cloud.provider.entity.InstanceSearchField;
import com.fit2cloud.provider.impl.proxmox.api.ProxmoxApi;
import com.fit2cloud.provider.impl.proxmox.api.ProxmoxSearchFieldApi;
import com.fit2cloud.provider.impl.proxmox.entity.credential.ProxmoxComplianceCredential;
import com.fit2cloud.provider.impl.proxmox.entity.request.ProxmoxBaseRequest;
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
public class ProxmoxProvider extends AbstractCloudProvider<ProxmoxComplianceCredential> implements ICloudProvider {
    public static final ProxmoxBaseCloudProvider proxmoxBaseCloudProvider = new ProxmoxBaseCloudProvider();
    public static final Info info = new Info("security-compliance", List.of(), Map.of());

    @Override
    public List<DefaultKeyValue<ResourceTypeConstants, SyncDimensionConstants>> getResourceSyncDimensionConstants() {
        return List.of(
                new DefaultKeyValue<>(ResourceTypeConstants.ECS, SyncDimensionConstants.REGION),
                new DefaultKeyValue<>(ResourceTypeConstants.HOST, SyncDimensionConstants.REGION),
                new DefaultKeyValue<>(ResourceTypeConstants.DATA_STORE, SyncDimensionConstants.REGION),
                new DefaultKeyValue<>(ResourceTypeConstants.DISK, SyncDimensionConstants.REGION));
    }

    @Override
    public List<ResourceInstance> listEcsInstance(String req) {
        ProxmoxBaseRequest request = JsonUtil.parseObject(req, ProxmoxBaseRequest.class);
        return ProxmoxApi.listVirtualMachine(request).stream()
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
        return ProxmoxSearchFieldApi.listEcsInstanceSearchField();
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
        ProxmoxBaseRequest request = JsonUtil.parseObject(req, ProxmoxBaseRequest.class);
        return ProxmoxApi.listF2CDisk(request).stream()
                .map(resource ->
                        ResourceUtil.toResourceInstance(
                                getCloudAccountMeta().platform,
                                ResourceTypeConstants.DISK,
                                resource.getDiskId(),
                                resource.getDiskName(), resource)
                ).toList();
    }

    @Override
    public List<InstanceSearchField> listDiskInstanceSearchField() {
        return ProxmoxSearchFieldApi.listDiskInstanceSearchField();
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
        return List.of();
    }

    @Override
    public List<InstanceSearchField> listSecurityGroupInstanceSearchField() {
        return List.of();
    }

    @Override
    public List<ResourceInstance> listHostInstance(String req) {
        ProxmoxBaseRequest request = JsonUtil.parseObject(req, ProxmoxBaseRequest.class);
        return ProxmoxApi.listF2CHost(request).stream()
                .map(resource ->
                        ResourceUtil.toResourceInstance(
                                getCloudAccountMeta().platform,
                                ResourceTypeConstants.HOST,
                                resource.getHostId(),
                                resource.getHostName(), resource)
                ).toList();
    }

    @Override
    public List<InstanceSearchField> listHostInstanceSearchField() {
        return ProxmoxSearchFieldApi.listHostInstanceSearchField();
    }

    @Override
    public List<ResourceInstance> listDataStoreInstance(String req) {
        ProxmoxBaseRequest request = JsonUtil.parseObject(req, ProxmoxBaseRequest.class);
        return ProxmoxApi.listF2CDatastore(request).stream()
                .map(resource ->
                        ResourceUtil.toResourceInstance(
                                getCloudAccountMeta().platform,
                                ResourceTypeConstants.DATA_STORE,
                                resource.getDataStoreId(),
                                resource.getDataStoreName(), resource)
                ).toList();
    }

    @Override
    public List<InstanceSearchField> listDataStoreInstanceSearchField() {
        return ProxmoxSearchFieldApi.listDataStoreInstanceSearchField();
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
}
