package com.fit2cloud.provider.impl.aliyun;

import com.aliyun.ecs20140526.models.DescribeDisksResponseBody;
import com.aliyun.elasticsearch20170613.models.ListInstanceResponseBody;
import com.aliyun.ram20150501.models.GetLoginProfileResponse;
import com.aliyun.ram20150501.models.ListUsersResponseBody;
import com.aliyun.slb20140515.models.DescribeLoadBalancersResponseBody;
import com.aliyun.vpc20160428.models.DescribeEipAddressesResponseBody;
import com.fit2cloud.common.constants.PlatformConstants;
import com.fit2cloud.common.utils.JsonUtil;
import com.fit2cloud.constants.ResourceTypeConstants;
import com.fit2cloud.constants.SyncDimensionConstants;
import com.fit2cloud.es.entity.ResourceInstance;
import com.fit2cloud.provider.AbstractCloudProvider;
import com.fit2cloud.provider.ICloudProvider;
import com.fit2cloud.provider.entity.InstanceSearchField;
import com.fit2cloud.provider.impl.aliyun.api.AliApi;
import com.fit2cloud.provider.impl.aliyun.api.AliInstanceSearchFieldApi;
import com.fit2cloud.provider.impl.aliyun.entity.credential.AliSecurityComplianceCredential;
import com.fit2cloud.provider.impl.aliyun.entity.request.*;
import com.fit2cloud.provider.impl.aliyun.entity.response.*;
import com.fit2cloud.provider.util.ResourceUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.keyvalue.DefaultKeyValue;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2022/10/13  4:31 PM}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
public class AliCloudProvider extends AbstractCloudProvider<AliSecurityComplianceCredential> implements ICloudProvider {

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
        ListEcsInstancesRequest listEcsInstancesRequest = JsonUtil.parseObject(req, ListEcsInstancesRequest.class);
        List<EcsInstanceResponse> ecsInstanceResponses = AliApi.listECSInstanceCollection(listEcsInstancesRequest);
        return ecsInstanceResponses
                .stream()
                .map(instance -> ResourceUtil.toResourceInstance(PlatformConstants.fit2cloud_ali_platform.name(), ResourceTypeConstants.ECS,
                        instance.getInstanceId(), instance.getInstanceName(),
                        Map.of(
                                "disks", instance.getDisks(),
                                "securityGroupRules", instance.getSecurityGroupRules(),
                                "tags", Objects.nonNull(instance.getTags()) ? instance.getTags().tag : List.of()
                        ),
                        instance))
                .toList();
    }

    @Override
    public List<InstanceSearchField> listEcsInstanceSearchField() {
        return AliInstanceSearchFieldApi.listEcsInstanceSearchField();
    }

    @Override
    public List<ResourceInstance> listRedisInstance(String req) {
        ListRedisInstanceRequest listRdsInstanceRequest = JsonUtil.parseObject(req, ListRedisInstanceRequest.class);
        List<RedisInstanceResponse> redisInstanceResponses = AliApi.listRedisInstanceCollection(listRdsInstanceRequest);
        return redisInstanceResponses
                .stream()
                .map(instance -> ResourceUtil.toResourceInstance(PlatformConstants.fit2cloud_ali_platform.name(),
                        ResourceTypeConstants.REDIS,
                        instance.getInstanceId(),
                        instance.getInstanceName(),
                        Map.of("tags", Objects.nonNull(instance.getTags()) ? instance.getTags().tag : List.of()),
                        instance))
                .toList();
    }

    @Override
    public List<InstanceSearchField> listRedisInstanceSearchField() {
        return AliInstanceSearchFieldApi.listRedisInstanceSearchField();
    }

    @Override
    public List<ResourceInstance> listMongodbInstance(String req) {
        ListMongoDBRequest listMongoDBRequest = JsonUtil.parseObject(req, ListMongoDBRequest.class);
        List<MongoDBInstanceResponse> instances = AliApi.listMongoDBInstanceCollection(listMongoDBRequest);
        return instances
                .stream()
                .map(instance -> ResourceUtil.toResourceInstance(PlatformConstants.fit2cloud_ali_platform.name(), ResourceTypeConstants.MONGO_DB, instance.getDBInstanceId(), instance.getDBInstanceDescription(),
                        Map.of("tags", Objects.nonNull(instance.getTags()) ? instance.getTags().tag : List.of()), instance))
                .toList();
    }

    @Override
    public List<InstanceSearchField> listMongodbInstanceSearchField() {
        return AliInstanceSearchFieldApi.listMongodbInstanceSearchField();
    }

    @Override
    public List<ResourceInstance> listMysqlInstance(String req) {
        ListRdsInstanceRequest listRdsInstanceRequest = JsonUtil.parseObject(req, ListRdsInstanceRequest.class);
        List<RdsInstanceResponse> instances = AliApi.listMysqlInstanceCollection(listRdsInstanceRequest);
        return instances
                .stream()
                .map(instance -> ResourceUtil.toResourceInstance(PlatformConstants.fit2cloud_ali_platform.name(),
                        ResourceTypeConstants.MYSQL,
                        instance.getDBInstanceId(),
                        instance.getDBInstanceDescription(),
                        Map.of("tags", CollectionUtils.isNotEmpty(instance.getTags()) ? instance.getTags() : List.of()),
                        instance))
                .toList();
    }

    @Override
    public List<InstanceSearchField> listMysqlInstanceSearchField() {
        return AliInstanceSearchFieldApi.listMysqlInstanceSearchField();
    }

    @Override
    public List<ResourceInstance> listSqlServerInstance(String req) {
        ListRdsInstanceRequest listRdsInstanceRequest = JsonUtil.parseObject(req, ListRdsInstanceRequest.class);
        List<RdsInstanceResponse> instances = AliApi.listSqlServerInstanceCollection(listRdsInstanceRequest);
        return instances
                .stream()
                .map(instance -> ResourceUtil.toResourceInstance(PlatformConstants.fit2cloud_ali_platform.name(),
                        ResourceTypeConstants.SQL_SERVER,
                        instance.getDBInstanceId(),
                        instance.getDBInstanceDescription(),
                        Map.of("tags", CollectionUtils.isNotEmpty(instance.getTags()) ? instance.getTags() : List.of()),
                        instance))
                .toList();
    }

    @Override
    public List<InstanceSearchField> listSqlServerInstanceSearchField() {
        return AliInstanceSearchFieldApi.listSqlServerInstanceSearchField();
    }

    @Override
    public List<ResourceInstance> listPostGreSqlInstance(String req) {
        ListRdsInstanceRequest listRdsInstanceRequest = JsonUtil.parseObject(req, ListRdsInstanceRequest.class);
        List<RdsInstanceResponse> instances = AliApi.listPostgreSqlInstanceCollection(listRdsInstanceRequest);
        return instances
                .stream()
                .map(instance -> ResourceUtil.toResourceInstance(PlatformConstants.fit2cloud_ali_platform.name(),
                        ResourceTypeConstants.POST_GRE_SQL,
                        instance.getDBInstanceId(),
                        instance.getDBInstanceDescription(),
                        Map.of("tags", CollectionUtils.isNotEmpty(instance.getTags()) ? instance.getTags() : List.of()),
                        instance))
                .toList();
    }

    @Override
    public List<InstanceSearchField> listPostGreSqlInstanceSearchField() {
        return AliInstanceSearchFieldApi.listPostGreSqlInstanceSearchField();
    }

    @Override
    public List<ResourceInstance> listMariaDBInstance(String req) {
        ListRdsInstanceRequest listRdsInstanceRequest = JsonUtil.parseObject(req, ListRdsInstanceRequest.class);
        List<RdsInstanceResponse> instances = AliApi.listMariaDBInstanceCollection(listRdsInstanceRequest);
        return instances
                .stream()
                .map(instance -> ResourceUtil.toResourceInstance(PlatformConstants.fit2cloud_ali_platform.name(),
                        ResourceTypeConstants.MARIA_DB,
                        instance.getDBInstanceId(),
                        instance.getDBInstanceDescription(),
                        Map.of("tags", CollectionUtils.isNotEmpty(instance.getTags()) ? instance.getTags() : List.of()),
                        instance))
                .toList();
    }

    @Override
    public List<InstanceSearchField> listMariaDBInstanceSearchField() {
        return AliInstanceSearchFieldApi.listMariadbInstanceSearchField();
    }

    @Override
    public List<ResourceInstance> listElasticSearchInstance(String req) {
        ListElasticSearchInstanceRequest listElasticSearchInstanceRequest = JsonUtil.parseObject(req, ListElasticSearchInstanceRequest.class);
        List<ListInstanceResponseBody.ListInstanceResponseBodyResult> instances = AliApi.listElasticsearchInstance(listElasticSearchInstanceRequest);
        return instances
                .stream()
                .map(instance -> ResourceUtil.toResourceInstance(PlatformConstants.fit2cloud_ali_platform.name(),
                        ResourceTypeConstants.ELASTIC_SEARCH,
                        instance.getInstanceId(),
                        instance.getDescription(),
                        Map.of("tags", CollectionUtils.isNotEmpty(instance.getTags()) ? instance.getTags() : List.of()),
                        instance))
                .toList();
    }

    @Override
    public List<InstanceSearchField> listElasticSearchInstanceSearchField() {
        return AliInstanceSearchFieldApi.listElasticSearchInstanceSearchField();
    }

    @Override
    public List<ResourceInstance> listDiskInstance(String req) {
        ListDiskInstanceRequest listDiskInstanceRequest = JsonUtil.parseObject(req, ListDiskInstanceRequest.class);
        List<DescribeDisksResponseBody.DescribeDisksResponseBodyDisksDisk> instances = AliApi.listDiskInstance(listDiskInstanceRequest);
        return instances
                .stream()
                .map(instance -> ResourceUtil.toResourceInstance(PlatformConstants.fit2cloud_ali_platform.name(),
                        ResourceTypeConstants.DISK, instance.getDiskId(), instance.getDiskName()
                        , Map.of("tags", Objects.nonNull(instance.getTags()) ? instance.getTags().tag : List.of()), instance))
                .toList();
    }

    @Override
    public List<InstanceSearchField> listDiskInstanceSearchField() {
        return AliInstanceSearchFieldApi.listDiskInstanceSearchField();
    }

    @Override
    public List<ResourceInstance> listLoadBalancerInstance(String req) {
        ListLoadBalancerInstanceRequest listLoadBalancerInstanceRequest = JsonUtil.parseObject(req, ListLoadBalancerInstanceRequest.class);
        List<DescribeLoadBalancersResponseBody.DescribeLoadBalancersResponseBodyLoadBalancersLoadBalancer> instances = AliApi.listLoadBalancerInstance(listLoadBalancerInstanceRequest);
        return instances
                .stream()
                .map(instance -> ResourceUtil.toResourceInstance(PlatformConstants.fit2cloud_ali_platform.name(), ResourceTypeConstants.LOAD_BALANCER,
                        instance.getLoadBalancerId(),
                        instance.getLoadBalancerName(),
                        Map.of("tags", Objects.nonNull(instance.getTags()) ? instance.getTags().tag : List.of()),
                        instance))
                .toList();
    }

    @Override
    public List<InstanceSearchField> listLoadBalancerInstanceSearchField() {
        return AliInstanceSearchFieldApi.listLoadBalancerInstanceSearchField();
    }

    @Override
    public List<ResourceInstance> listPublicIpInstance(String req) {
        ListPublicIpInstanceRequest listPublicIpInstanceRequest = JsonUtil.parseObject(req, ListPublicIpInstanceRequest.class);
        List<DescribeEipAddressesResponseBody.DescribeEipAddressesResponseBodyEipAddressesEipAddress> instances = AliApi.listPublicIpInstance(listPublicIpInstanceRequest);
        return instances
                .stream()
                .map(instance -> ResourceUtil.toResourceInstance(PlatformConstants.fit2cloud_ali_platform.name(),
                        ResourceTypeConstants.PUBLIC_IP,
                        instance.getAllocationId(),
                        instance.getName(),
                        Map.of("tags", Objects.nonNull(instance.getTags()) ? instance.getTags().tag : List.of()),
                        instance))
                .toList();
    }

    @Override
    public List<InstanceSearchField> listPublicIpInstanceSearchField() {
        return AliInstanceSearchFieldApi.listPublicIpInstanceSearchField();
    }

    @Override
    public List<ResourceInstance> listVpcInstance(String req) {
        ListVpcInstanceRequest listVpcInstanceRequest = JsonUtil.parseObject(req, ListVpcInstanceRequest.class);
        List<VpcInstanceResponse> instances = AliApi.listVpcInstanceCollection(listVpcInstanceRequest);
        return instances
                .stream()
                .map(instance -> ResourceUtil.toResourceInstance(PlatformConstants.fit2cloud_ali_platform.name(),
                        ResourceTypeConstants.VPC,
                        instance.vpcId,
                        instance.vpcName,
                        Map.of("switchesList", instance.getSwitchesList(), "tags", Objects.nonNull(instance.getTags()) ? instance.getTags().tag : List.of()),
                        instance))
                .toList();
    }

    @Override
    public List<InstanceSearchField> listVpcInstanceSearchField() {
        return AliInstanceSearchFieldApi.listVPCInstanceSearchField();
    }

    @Override
    public List<ResourceInstance> listRamInstance(String req) {
        ListRamInstanceRequest listRamInstanceRequest = JsonUtil.parseObject(req, ListRamInstanceRequest.class);
        List<ListUsersResponseBody.ListUsersResponseBodyUsersUser> instances = AliApi.listRamInstanceRequest(listRamInstanceRequest);
        ListLoginProfileInstanceRequest listLoginProfileInstanceRequest = new ListLoginProfileInstanceRequest();
        listLoginProfileInstanceRequest.setUsernameList(instances.stream().map(ListUsersResponseBody.ListUsersResponseBodyUsersUser::getUserName).toList());
        listLoginProfileInstanceRequest.setCredential(listRamInstanceRequest.getCredential());
        List<GetLoginProfileResponse> getLoginProfileResponses = AliApi.listLoginProfile(listLoginProfileInstanceRequest);
        return instances
                .stream()
                .map(instance -> ResourceUtil.toResourceInstance(PlatformConstants.fit2cloud_ali_platform.name(), ResourceTypeConstants.RAM,
                        instance.getUserId(), instance.getUserName(), instance, "loginProfile", getLoginProfileResponses.stream().map(r -> r.body.loginProfile).filter(r -> r.getUserName().equals(instance.getUserName())).findAny().orElse(null)))
                .toList();
    }

    @Override
    public List<InstanceSearchField> listRamInstanceSearchField() {
        return AliInstanceSearchFieldApi.listRAMInstanceSearchField();
    }

    @Override
    public List<ResourceInstance> listBucketInstance(String req) {
        ListBucketInstanceRequest listBucketInstanceRequest = JsonUtil.parseObject(req, ListBucketInstanceRequest.class);
        List<BucketInstanceResponse> instances = AliApi.listBucketCollectionInstance(listBucketInstanceRequest);
        return instances
                .stream()
                .map(instance -> ResourceUtil.toResourceInstance(PlatformConstants.fit2cloud_ali_platform.name(), ResourceTypeConstants.OSS,
                        instance.getName(), instance.getName(), instance))
                .toList();
    }

    @Override
    public List<InstanceSearchField> listBucketInstanceSearchField() {
        return AliInstanceSearchFieldApi.listOSSInstanceSearchField();
    }

    @Override
    public List<ResourceInstance> listSecurityGroupInstance(String req) {
        ListSecurityGroupInstanceRequest listSecurityGroupInstanceRequest = JsonUtil.parseObject(req, ListSecurityGroupInstanceRequest.class);
        List<SecurityGroupsSecurityGroupInstanceResponse> instances = AliApi.listSecurityGroupCollectionInstance(listSecurityGroupInstanceRequest);
        return instances
                .stream()
                .map(instance -> ResourceUtil.toResourceInstance(PlatformConstants.fit2cloud_ali_platform.name(),
                        ResourceTypeConstants.SECURITY_GROUP,
                        instance.getSecurityGroupId(),
                        instance.getSecurityGroupName(),
                        Map.of("tags", Objects.nonNull(instance.getTags()) ? instance.getTags().tag : List.of()),
                        instance))
                .toList();
    }

    @Override
    public List<InstanceSearchField> listSecurityGroupInstanceSearchField() {
        return AliInstanceSearchFieldApi.listSecurityGroupInstanceSearchField();
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
