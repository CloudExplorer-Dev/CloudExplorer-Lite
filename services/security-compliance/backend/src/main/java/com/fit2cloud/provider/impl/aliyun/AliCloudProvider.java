package com.fit2cloud.provider.impl.aliyun;

import com.aliyun.dds20151201.models.DescribeDBInstancesResponseBody;
import com.aliyun.ecs20140526.models.DescribeInstancesResponseBody;
import com.aliyun.elasticsearch20170613.models.ListInstanceResponseBody;
import com.fit2cloud.common.constants.PlatformConstants;
import com.fit2cloud.common.utils.JsonUtil;
import com.fit2cloud.constants.ResourceTypeConstants;
import com.fit2cloud.es.entity.ResourceInstance;
import com.fit2cloud.provider.AbstractCloudProvider;
import com.fit2cloud.provider.ICloudProvider;
import com.fit2cloud.provider.entity.InstanceSearchField;
import com.fit2cloud.provider.impl.aliyun.api.AliApi;
import com.fit2cloud.provider.impl.aliyun.api.AliInstanceSearchFieldApi;
import com.fit2cloud.provider.impl.aliyun.entity.credential.AliSecurityComplianceCredential;
import com.fit2cloud.provider.impl.aliyun.entity.request.*;
import com.fit2cloud.provider.util.ResourceUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2022/10/13  4:31 PM}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
public class AliCloudProvider extends AbstractCloudProvider<AliSecurityComplianceCredential> implements ICloudProvider {

    @Override
    public List<ResourceInstance> listEcsInstance(String req) {
        ListEcsInstancesRequest listEcsInstancesRequest = JsonUtil.parseObject(req, ListEcsInstancesRequest.class);
        List<DescribeInstancesResponseBody.DescribeInstancesResponseBodyInstancesInstance> describeInstancesResponseBodyInstancesInstances = AliApi.listECSInstance(listEcsInstancesRequest);
        return describeInstancesResponseBodyInstancesInstances
                .stream()
                .map(instance -> ResourceUtil.toResourceInstance(PlatformConstants.fit2cloud_ali_platform.name(), ResourceTypeConstants.ECS, instance.getInstanceId(), instance.getInstanceName(), instance))
                .toList();
    }

    @Override
    public List<InstanceSearchField> listEcsInstanceSearchField() {
        return AliInstanceSearchFieldApi.listEcsInstanceSearchField();
    }

    @Override
    public List<ResourceInstance> listRedisInstance(String req) {
        ListRedisInstanceRequest listRdsInstanceRequest = JsonUtil.parseObject(req, ListRedisInstanceRequest.class);
        List<com.aliyun.r_kvstore20150101.models.DescribeInstancesResponseBody.DescribeInstancesResponseBodyInstancesKVStoreInstance> describeInstancesResponseBodyInstancesKVStoreInstances = AliApi.listRedisInstance(listRdsInstanceRequest);
        return describeInstancesResponseBodyInstancesKVStoreInstances
                .stream()
                .map(instance -> ResourceUtil.toResourceInstance(PlatformConstants.fit2cloud_ali_platform.name(), ResourceTypeConstants.REDIS, instance.getInstanceId(), instance.getInstanceName(), instance))
                .toList();
    }

    @Override
    public List<InstanceSearchField> listRedisInstanceSearchField() {
        return new ArrayList<>();
    }

    @Override
    public List<ResourceInstance> listMongodbInstance(String req) {
        ListMongoDBRequest listMongoDBRequest = JsonUtil.parseObject(req, ListMongoDBRequest.class);
        List<DescribeDBInstancesResponseBody.DescribeDBInstancesResponseBodyDBInstancesDBInstance> instances = AliApi.listMongoDBInstance(listMongoDBRequest);
        return instances
                .stream()
                .map(instance -> ResourceUtil.toResourceInstance(PlatformConstants.fit2cloud_ali_platform.name(), ResourceTypeConstants.MONGO_DB, instance.getDBInstanceId(), instance.getDBInstanceDescription(), instance))
                .toList();
    }

    @Override
    public List<InstanceSearchField> listMongodbInstanceSearchField() {
        return List.of();
    }

    @Override
    public List<ResourceInstance> listMysqlInstance(String req) {
        ListRdsInstanceRequest listRdsInstanceRequest = JsonUtil.parseObject(req, ListRdsInstanceRequest.class);
        List<com.aliyun.rds20140815.models.DescribeDBInstancesResponseBody.DescribeDBInstancesResponseBodyItemsDBInstance> instances = AliApi.listMysqlInstance(listRdsInstanceRequest);
        return instances
                .stream()
                .map(instance -> ResourceUtil.toResourceInstance(PlatformConstants.fit2cloud_ali_platform.name(), ResourceTypeConstants.MYSQL, instance.getDBInstanceId(), instance.getDBInstanceDescription(), instance))
                .toList();
    }

    @Override
    public List<InstanceSearchField> listMysqlInstanceSearchField() {
        return List.of();
    }

    @Override
    public List<ResourceInstance> listSqlServerInstance(String req) {
        ListRdsInstanceRequest listRdsInstanceRequest = JsonUtil.parseObject(req, ListRdsInstanceRequest.class);
        List<com.aliyun.rds20140815.models.DescribeDBInstancesResponseBody.DescribeDBInstancesResponseBodyItemsDBInstance> instances = AliApi.listSqlServerInstance(listRdsInstanceRequest);
        return instances
                .stream()
                .map(instance -> ResourceUtil.toResourceInstance(PlatformConstants.fit2cloud_ali_platform.name(), ResourceTypeConstants.SQL_SERVER, instance.getDBInstanceId(), instance.getDBInstanceDescription(), instance))
                .toList();
    }

    @Override
    public List<InstanceSearchField> listSqlServerInstanceSearchField() {
        return List.of();
    }

    @Override
    public List<ResourceInstance> listPostGreSqlInstance(String req) {
        ListRdsInstanceRequest listRdsInstanceRequest = JsonUtil.parseObject(req, ListRdsInstanceRequest.class);
        List<com.aliyun.rds20140815.models.DescribeDBInstancesResponseBody.DescribeDBInstancesResponseBodyItemsDBInstance> instances = AliApi.listPostgreSqlInstance(listRdsInstanceRequest);
        return instances
                .stream()
                .map(instance -> ResourceUtil.toResourceInstance(PlatformConstants.fit2cloud_ali_platform.name(), ResourceTypeConstants.POST_GRE_SQL, instance.getDBInstanceId(), instance.getDBInstanceDescription(), instance))
                .toList();
    }

    @Override
    public List<InstanceSearchField> listPostGreSqlInstanceSearchField() {
        return null;
    }

    @Override
    public List<ResourceInstance> listMariaDBInstance(String req) {
        ListRdsInstanceRequest listRdsInstanceRequest = JsonUtil.parseObject(req, ListRdsInstanceRequest.class);
        List<com.aliyun.rds20140815.models.DescribeDBInstancesResponseBody.DescribeDBInstancesResponseBodyItemsDBInstance> instances = AliApi.listMariaDBInstance(listRdsInstanceRequest);
        return instances
                .stream()
                .map(instance -> ResourceUtil.toResourceInstance(PlatformConstants.fit2cloud_ali_platform.name(), ResourceTypeConstants.MARIA_DB, instance.getDBInstanceId(), instance.getDBInstanceDescription(), instance))
                .toList();
    }

    @Override
    public List<InstanceSearchField> listMariaDBInstanceSearchField() {
        return List.of();
    }

    @Override
    public List<ResourceInstance> listElasticSearchInstance(String req) {
        ListElasticSearchInstanceRequest listElasticSearchInstanceRequest = JsonUtil.parseObject(req, ListElasticSearchInstanceRequest.class);
        List<ListInstanceResponseBody.ListInstanceResponseBodyResult> instances = AliApi.listElasticsearchInstance(listElasticSearchInstanceRequest);
        return instances
                .stream()
                .map(instance -> ResourceUtil.toResourceInstance(PlatformConstants.fit2cloud_ali_platform.name(), ResourceTypeConstants.ELASTIC_SEARCH, instance.getInstanceId(), instance.getDescription(), instance))
                .toList();
    }

    @Override
    public List<InstanceSearchField> listElasticSearchInstanceSearchField() {
        return List.of();
    }
}
