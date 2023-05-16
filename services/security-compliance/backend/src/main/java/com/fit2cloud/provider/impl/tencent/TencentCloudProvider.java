package com.fit2cloud.provider.impl.tencent;

import com.fit2cloud.common.constants.PlatformConstants;
import com.fit2cloud.common.provider.impl.tencent.entity.credential.TencentBaseCredential;
import com.fit2cloud.common.utils.JsonUtil;
import com.fit2cloud.constants.ResourceTypeConstants;
import com.fit2cloud.constants.SyncDimensionConstants;
import com.fit2cloud.es.entity.ResourceInstance;
import com.fit2cloud.provider.AbstractCloudProvider;
import com.fit2cloud.provider.ICloudProvider;
import com.fit2cloud.provider.entity.InstanceSearchField;
import com.fit2cloud.provider.impl.tencent.api.TencentApi;
import com.fit2cloud.provider.impl.tencent.api.TencentInstanceSearchFieldApi;
import com.fit2cloud.provider.impl.tencent.entity.request.*;
import com.fit2cloud.provider.impl.tencent.entity.response.BucketInstanceResponse;
import com.fit2cloud.provider.impl.tencent.entity.response.RamInstanceResponse;
import com.fit2cloud.provider.impl.tencent.entity.response.SecurityGroupInstanceResponse;
import com.fit2cloud.provider.util.ResourceUtil;
import com.tencentcloudapi.cbs.v20170312.models.Disk;
import com.tencentcloudapi.cdb.v20170320.models.InstanceInfo;
import com.tencentcloudapi.clb.v20180317.models.LoadBalancer;
import com.tencentcloudapi.cvm.v20170312.models.Instance;
import com.tencentcloudapi.cvm.v20170312.models.Tag;
import com.tencentcloudapi.mongodb.v20190725.models.InstanceDetail;
import com.tencentcloudapi.redis.v20180412.models.InstanceSet;
import com.tencentcloudapi.redis.v20180412.models.InstanceTagInfo;
import com.tencentcloudapi.sqlserver.v20180328.models.DBInstance;
import com.tencentcloudapi.vpc.v20170312.models.Address;
import com.tencentcloudapi.vpc.v20170312.models.Vpc;
import org.apache.commons.collections4.keyvalue.DefaultKeyValue;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2022/10/18  5:52 PM}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
public class TencentCloudProvider extends AbstractCloudProvider<TencentBaseCredential> implements ICloudProvider {

    @Override
    public List<DefaultKeyValue<ResourceTypeConstants, SyncDimensionConstants>> getResourceSyncDimensionConstants() {
        return List.of(
                new DefaultKeyValue<>(ResourceTypeConstants.ECS, SyncDimensionConstants.REGION),
                new DefaultKeyValue<>(ResourceTypeConstants.REDIS, SyncDimensionConstants.REGION),
                new DefaultKeyValue<>(ResourceTypeConstants.MONGO_DB, SyncDimensionConstants.REGION),
                new DefaultKeyValue<>(ResourceTypeConstants.MYSQL, SyncDimensionConstants.REGION),
                new DefaultKeyValue<>(ResourceTypeConstants.SQL_SERVER, SyncDimensionConstants.REGION),
                new DefaultKeyValue<>(ResourceTypeConstants.POST_GRE_SQL, SyncDimensionConstants.REGION),
                new DefaultKeyValue<>(ResourceTypeConstants.MARIA_DB, SyncDimensionConstants.REGION),
                new DefaultKeyValue<>(ResourceTypeConstants.ELASTIC_SEARCH, SyncDimensionConstants.REGION),
                new DefaultKeyValue<>(ResourceTypeConstants.DISK, SyncDimensionConstants.REGION),
                new DefaultKeyValue<>(ResourceTypeConstants.LOAD_BALANCER, SyncDimensionConstants.REGION),
                new DefaultKeyValue<>(ResourceTypeConstants.PUBLIC_IP, SyncDimensionConstants.REGION),
                new DefaultKeyValue<>(ResourceTypeConstants.VPC, SyncDimensionConstants.REGION),
                new DefaultKeyValue<>(ResourceTypeConstants.RAM, SyncDimensionConstants.CloudAccount),
                new DefaultKeyValue<>(ResourceTypeConstants.OSS, SyncDimensionConstants.CloudAccount),
                new DefaultKeyValue<>(ResourceTypeConstants.SECURITY_GROUP, SyncDimensionConstants.REGION));

    }

    @Override
    public List<ResourceInstance> listEcsInstance(String req) {
        ListCvmInstanceRequest listCvmInstanceRequest = JsonUtil.parseObject(req, ListCvmInstanceRequest.class);
        List<Instance> instances = TencentApi.listEcsInstance(listCvmInstanceRequest);
        return instances
                .stream()
                .map(instance -> ResourceUtil.toResourceInstance(PlatformConstants.fit2cloud_tencent_platform.name(),
                        ResourceTypeConstants.ECS,
                        instance.getInstanceId(),
                        instance.getInstanceName(),
                        Map.of("tags", Arrays.stream(Objects.isNull(instance.getTags()) ? new Tag[]{} : instance.getTags()).toList()),
                        instance))
                .toList();
    }


    @Override
    public List<InstanceSearchField> listEcsInstanceSearchField() {
        return TencentInstanceSearchFieldApi.listEcsInstanceSearchField();
    }

    @Override
    public List<ResourceInstance> listRedisInstance(String req) {
        ListRedisInstanceRequest listRedisInstanceRequest = JsonUtil.parseObject(req, ListRedisInstanceRequest.class);
        List<InstanceSet> instances = TencentApi.listRedisInstance(listRedisInstanceRequest);
        return instances
                .stream()
                .map(instance -> ResourceUtil.toResourceInstance(PlatformConstants.fit2cloud_tencent_platform.name(),
                        ResourceTypeConstants.REDIS,
                        instance.getInstanceId(),
                        instance.getInstanceName(),
                        Map.of("tags", Arrays.stream(Objects.isNull(instance.getInstanceTags()) ? new InstanceTagInfo[]{} : instance.getInstanceTags()).toList()),
                        instance))
                .toList();
    }

    @Override
    public List<InstanceSearchField> listRedisInstanceSearchField() {
        return TencentInstanceSearchFieldApi.listRedisInstanceSearchField();
    }

    @Override
    public List<ResourceInstance> listMongodbInstance(String req) {
        ListMongoDBInstanceRequest listMongoDBInstanceRequest = JsonUtil.parseObject(req, ListMongoDBInstanceRequest.class);
        List<InstanceDetail> instances = TencentApi.listMongodbInstance(listMongoDBInstanceRequest);
        return instances
                .stream()
                .map(instance -> ResourceUtil.toResourceInstance(PlatformConstants.fit2cloud_tencent_platform.name(),
                        ResourceTypeConstants.MONGO_DB,
                        instance.getInstanceId(),
                        instance.getInstanceName(),
                        Map.of("tags", Arrays.stream(Objects.isNull(instance.getTags()) ? new Tag[]{} : instance.getTags()).toList()),
                        instance))
                .toList();
    }

    @Override
    public List<InstanceSearchField> listMongodbInstanceSearchField() {
        return TencentInstanceSearchFieldApi.listMongodbInstanceSearchField();
    }

    @Override
    public List<ResourceInstance> listMysqlInstance(String req) {
        ListMysqlInstanceRequest listMysqlInstanceRequest = JsonUtil.parseObject(req, ListMysqlInstanceRequest.class);
        List<InstanceInfo> instances = TencentApi.listMysqlInstance(listMysqlInstanceRequest);
        return instances
                .stream()
                .map(instance -> ResourceUtil.toResourceInstance(PlatformConstants.fit2cloud_tencent_platform.name(),
                        ResourceTypeConstants.MYSQL,
                        instance.getInstanceId(),
                        instance.getInstanceName(),
                        Map.of("tags", Arrays.stream(Objects.isNull(instance.getTagList()) ? new Tag[]{} : instance.getTagList()).toList()),
                        instance))
                .toList();
    }

    @Override
    public List<InstanceSearchField> listMysqlInstanceSearchField() {
        return TencentInstanceSearchFieldApi.listMysqlInstanceSearchField();
    }

    @Override
    public List<ResourceInstance> listSqlServerInstance(String req) {
        ListSqlServerInstanceRequest listSqlServerInstanceRequest = JsonUtil.parseObject(req, ListSqlServerInstanceRequest.class);
        List<DBInstance> instances = TencentApi.listSqlServerInstance(listSqlServerInstanceRequest);
        return instances
                .stream()
                .map(instance -> ResourceUtil.toResourceInstance(PlatformConstants.fit2cloud_tencent_platform.name(),
                        ResourceTypeConstants.SQL_SERVER,
                        instance.getInstanceId(),
                        instance.getName(),
                        Map.of("tags", Arrays.stream(Objects.isNull(instance.getResourceTags()) ? new Tag[]{} : instance.getResourceTags()).toList()),
                        instance))
                .toList();
    }

    @Override
    public List<InstanceSearchField> listSqlServerInstanceSearchField() {
        return TencentInstanceSearchFieldApi.listSqlServerInstanceSearchField();
    }

    @Override
    public List<ResourceInstance> listPostGreSqlInstance(String req) {
        ListPostGreSqlInstanceRequest listPostGreSqlInstanceRequest = JsonUtil.parseObject(req, ListPostGreSqlInstanceRequest.class);
        List<com.tencentcloudapi.postgres.v20170312.models.DBInstance> instances = TencentApi.listPostGreSqlInstance(listPostGreSqlInstanceRequest);
        return instances
                .stream()
                .map(instance -> ResourceUtil.toResourceInstance(PlatformConstants.fit2cloud_tencent_platform.name(),
                        ResourceTypeConstants.POST_GRE_SQL,
                        instance.getDBInstanceId(),
                        instance.getDBInstanceName(),
                        Map.of("tags", Arrays.stream(Objects.isNull(instance.getTagList()) ? new Tag[]{} : instance.getTagList()).toList()),
                        instance))
                .toList();
    }

    @Override
    public List<InstanceSearchField> listPostGreSqlInstanceSearchField() {
        return TencentInstanceSearchFieldApi.listPostgresInstanceSearchField();
    }

    @Override
    public List<ResourceInstance> listMariaDBInstance(String req) {
        ListMariaDBInstanceRequest listMariaDBInstanceRequest = JsonUtil.parseObject(req, ListMariaDBInstanceRequest.class);
        List<com.tencentcloudapi.mariadb.v20170312.models.DBInstance> instances = TencentApi.listMariaDBInstance(listMariaDBInstanceRequest);
        return instances
                .stream()
                .map(instance -> ResourceUtil.toResourceInstance(PlatformConstants.fit2cloud_tencent_platform.name(),
                        ResourceTypeConstants.MARIA_DB,
                        instance.getInstanceId(),
                        instance.getInstanceName(),
                        Map.of("tags", Arrays.stream(Objects.isNull(instance.getResourceTags()) ? new Tag[]{} : instance.getResourceTags()).toList()),
                        instance))
                .toList();
    }

    @Override
    public List<InstanceSearchField> listMariaDBInstanceSearchField() {
        return TencentInstanceSearchFieldApi.listMariadbInstanceSearchField();
    }

    @Override
    public List<ResourceInstance> listElasticSearchInstance(String req) {
        ListElasticsearchInstanceRequest listElasticsearchInstanceRequest = JsonUtil.parseObject(req, ListElasticsearchInstanceRequest.class);
        List<com.tencentcloudapi.es.v20180416.models.InstanceInfo> instances = TencentApi.listElasticSearchInstance(listElasticsearchInstanceRequest);
        return instances
                .stream()
                .map(instance -> ResourceUtil.toResourceInstance(PlatformConstants.fit2cloud_tencent_platform.name(),
                        ResourceTypeConstants.ELASTIC_SEARCH,
                        instance.getInstanceId(),
                        instance.getInstanceName(),
                        Map.of("tags", Arrays.stream(Objects.isNull(instance.getTagList()) ? new Tag[]{} : instance.getTagList()).toList()),
                        instance))
                .toList();
    }

    @Override
    public List<InstanceSearchField> listElasticSearchInstanceSearchField() {
        return TencentInstanceSearchFieldApi.listElasticSearchInstanceSearchField();
    }

    @Override
    public List<ResourceInstance> listDiskInstance(String req) {
        ListDiskInstanceRequest listDiskInstanceRequest = JsonUtil.parseObject(req, ListDiskInstanceRequest.class);
        List<Disk> instances = TencentApi.listDiskInstance(listDiskInstanceRequest);
        return instances
                .stream()
                .map(instance -> ResourceUtil.toResourceInstance(PlatformConstants.fit2cloud_tencent_platform.name(),
                        ResourceTypeConstants.DISK,
                        instance.getDiskId(),
                        instance.getDiskName(),
                        Map.of("tags", Arrays.stream(Objects.isNull(instance.getTags()) ? new Tag[]{} : instance.getTags()).toList()),
                        instance))
                .toList();
    }

    @Override
    public List<InstanceSearchField> listDiskInstanceSearchField() {
        return TencentInstanceSearchFieldApi.listDISKInstanceSearchField();
    }

    @Override
    public List<ResourceInstance> listLoadBalancerInstance(String req) {
        ListLoadBalancerInstanceRequest listLoadBalancerInstanceRequest = JsonUtil.parseObject(req, ListLoadBalancerInstanceRequest.class);
        List<LoadBalancer> instances = TencentApi.listLoadBalancerInstance(listLoadBalancerInstanceRequest);
        return instances
                .stream()
                .map(instance -> ResourceUtil.toResourceInstance(PlatformConstants.fit2cloud_tencent_platform.name(),
                        ResourceTypeConstants.LOAD_BALANCER,
                        instance.getLoadBalancerId(),
                        instance.getLoadBalancerName(),
                        Map.of("tags", Arrays.stream(Objects.isNull(instance.getTags()) ? new Tag[]{} : instance.getTags()).toList()),
                        instance))
                .toList();
    }

    @Override
    public List<InstanceSearchField> listLoadBalancerInstanceSearchField() {
        return TencentInstanceSearchFieldApi.listLoadBalancerInstanceSearchField();
    }

    @Override
    public List<ResourceInstance> listPublicIpInstance(String req) {
        ListPublicIpInstanceRequest listPublicIpInstanceRequest = JsonUtil.parseObject(req, ListPublicIpInstanceRequest.class);
        List<Address> instances = TencentApi.listPublicIpInstance(listPublicIpInstanceRequest);
        return instances
                .stream()
                .map(instance -> ResourceUtil.toResourceInstance(PlatformConstants.fit2cloud_tencent_platform.name(),
                        ResourceTypeConstants.PUBLIC_IP,
                        instance.getInstanceId(),
                        instance.getAddressName(),
                        Map.of("tags", Arrays.stream(Objects.isNull(instance.getTagSet()) ? new Tag[]{} : instance.getTagSet()).toList()),
                        instance))
                .toList();
    }

    @Override
    public List<InstanceSearchField> listPublicIpInstanceSearchField() {
        return TencentInstanceSearchFieldApi.listPublicIpInstanceSearchField();
    }

    @Override
    public List<ResourceInstance> listVpcInstance(String req) {
        ListVpcInstanceRequest listVpcInstanceRequest = JsonUtil.parseObject(req, ListVpcInstanceRequest.class);
        List<Vpc> instances = TencentApi.listVpcInstance(listVpcInstanceRequest);
        return instances
                .stream()
                .map(instance -> ResourceUtil.toResourceInstance(PlatformConstants.fit2cloud_tencent_platform.name(),
                        ResourceTypeConstants.VPC,
                        instance.getVpcId(),
                        instance.getVpcName(),
                        Map.of("tags", Arrays.stream(Objects.isNull(instance.getTagSet()) ? new Tag[]{} : instance.getTagSet()).toList()),
                        instance))
                .toList();
    }

    @Override
    public List<InstanceSearchField> listVpcInstanceSearchField() {
        return TencentInstanceSearchFieldApi.listVpcInstanceSearchField();
    }

    @Override
    public List<ResourceInstance> listRamInstance(String req) {
        ListUsersInstanceRequest listUsersInstanceRequest = JsonUtil.parseObject(req, ListUsersInstanceRequest.class);
        List<RamInstanceResponse> instances = TencentApi.listSubUserInstanceCollection(listUsersInstanceRequest);
        return instances
                .stream()
                .map(instance -> ResourceUtil.toResourceInstance(PlatformConstants.fit2cloud_tencent_platform.name(),
                        ResourceTypeConstants.RAM,
                        instance.getUid().toString(),
                        instance.getName(),
                        instance))
                .toList();
    }

    @Override
    public List<InstanceSearchField> listRamInstanceSearchField() {
        return List.of();
    }

    @Override
    public List<ResourceInstance> listBucketInstance(String req) {
        ListBucketInstanceRequest listBucketInstanceRequest = JsonUtil.parseObject(req, ListBucketInstanceRequest.class);
        List<BucketInstanceResponse> instances = TencentApi.listBucketInstanceCollection(listBucketInstanceRequest);
        return instances
                .stream()
                .map(instance -> ResourceUtil.toResourceInstance(PlatformConstants.fit2cloud_tencent_platform.name(),
                        ResourceTypeConstants.OSS,
                        instance.getName(),
                        instance.getName(),
                        instance))
                .toList();
    }

    @Override
    public List<InstanceSearchField> listBucketInstanceSearchField() {
        return TencentInstanceSearchFieldApi.listOSSInstanceSearchField();
    }

    @Override
    public List<ResourceInstance> listSecurityGroupInstance(String req) {
        ListSecurityGroupInstanceRequest listSecurityGroupInstanceRequest = JsonUtil.parseObject(req, ListSecurityGroupInstanceRequest.class);
        List<SecurityGroupInstanceResponse> instances = TencentApi.listSecurityGroupCollectionInstance(listSecurityGroupInstanceRequest);
        return instances
                .stream()
                .map(instance -> ResourceUtil.toResourceInstance(PlatformConstants.fit2cloud_tencent_platform.name(),
                        ResourceTypeConstants.SECURITY_GROUP,
                        instance.getSecurityGroupId(),
                        instance.getSecurityGroupName(),
                        Map.of("tags", Arrays.stream(Objects.isNull(instance.getTagSet()) ? new Tag[]{} : instance.getTagSet()).toList()),
                        instance))
                .toList();
    }

    @Override
    public List<InstanceSearchField> listSecurityGroupInstanceSearchField() {
        return TencentInstanceSearchFieldApi.listSecurityGroupInstanceSearchField();
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
