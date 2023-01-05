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
import com.fit2cloud.provider.impl.tencent.api.TencentInstanceSearchFieldApi;
import com.fit2cloud.provider.impl.tencent.entity.request.*;
import com.fit2cloud.provider.util.ResourceUtil;
import com.tencentcloudapi.cdb.v20170320.models.InstanceInfo;
import com.tencentcloudapi.cvm.v20170312.models.Instance;
import com.tencentcloudapi.mongodb.v20190725.models.InstanceDetail;
import com.tencentcloudapi.redis.v20180412.models.InstanceSet;
import com.tencentcloudapi.sqlserver.v20180328.models.DBInstance;

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
        return instances
                .stream()
                .map(instance -> ResourceUtil.toResourceInstance(PlatformConstants.fit2cloud_tencent_platform.name(), ResourceTypeConstants.ECS, instance.getInstanceId(), instance.getInstanceName(), instance))
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
                .map(instance -> ResourceUtil.toResourceInstance(PlatformConstants.fit2cloud_tencent_platform.name(), ResourceTypeConstants.REDIS, instance.getInstanceId(), instance.getInstanceName(), instance))
                .toList();
    }

    @Override
    public List<InstanceSearchField> listRedisInstanceSearchField() {
        return List.of();
    }

    @Override
    public List<ResourceInstance> listMongodbInstance(String req) {
        ListMongoDBInstanceRequest listMongoDBInstanceRequest = JsonUtil.parseObject(req, ListMongoDBInstanceRequest.class);
        List<InstanceDetail> instances = TencentApi.listMongodbInstance(listMongoDBInstanceRequest);
        return instances
                .stream()
                .map(instance -> ResourceUtil.toResourceInstance(PlatformConstants.fit2cloud_tencent_platform.name(), ResourceTypeConstants.MONGO_DB, instance.getInstanceId(), instance.getInstanceName(), instance))
                .toList();
    }

    @Override
    public List<InstanceSearchField> listMongodbInstanceSearchField() {
        return List.of();
    }

    @Override
    public List<ResourceInstance> listMysqlInstance(String req) {
        ListMysqlInstanceRequest listMysqlInstanceRequest = JsonUtil.parseObject(req, ListMysqlInstanceRequest.class);
        List<InstanceInfo> instances = TencentApi.listMysqlInstance(listMysqlInstanceRequest);
        return instances
                .stream()
                .map(instance -> ResourceUtil.toResourceInstance(PlatformConstants.fit2cloud_tencent_platform.name(), ResourceTypeConstants.MYSQL, instance.getInstanceId(), instance.getInstanceName(), instance))
                .toList();
    }

    @Override
    public List<InstanceSearchField> listMysqlInstanceSearchField() {
        return List.of();
    }

    @Override
    public List<ResourceInstance> listSqlServerInstance(String req) {
        ListSqlServerInstanceRequest listSqlServerInstanceRequest = JsonUtil.parseObject(req, ListSqlServerInstanceRequest.class);
        List<DBInstance> instances = TencentApi.listSqlServerInstance(listSqlServerInstanceRequest);
        return instances
                .stream()
                .map(instance -> ResourceUtil.toResourceInstance(PlatformConstants.fit2cloud_tencent_platform.name(), ResourceTypeConstants.SQL_SERVER, instance.getInstanceId(), instance.getName(), instance))
                .toList();
    }

    @Override
    public List<InstanceSearchField> listSqlServerInstanceSearchField() {
        return List.of();
    }

    @Override
    public List<ResourceInstance> listPostGreSqlInstance(String req) {
        ListPostGreSqlInstanceRequest listPostGreSqlInstanceRequest = JsonUtil.parseObject(req, ListPostGreSqlInstanceRequest.class);
        List<com.tencentcloudapi.postgres.v20170312.models.DBInstance> instances = TencentApi.listPostGreSqlInstance(listPostGreSqlInstanceRequest);
        return instances
                .stream()
                .map(instance -> ResourceUtil.toResourceInstance(PlatformConstants.fit2cloud_tencent_platform.name(), ResourceTypeConstants.POST_GRE_SQL, instance.getDBInstanceId(), instance.getDBInstanceName(), instance))
                .toList();
    }

    @Override
    public List<InstanceSearchField> listPostGreSqlInstanceSearchField() {
        return List.of();
    }

    @Override
    public List<ResourceInstance> listMariaDBInstance(String req) {
        ListMariaDBInstanceRequest listMariaDBInstanceRequest = JsonUtil.parseObject(req, ListMariaDBInstanceRequest.class);
        List<com.tencentcloudapi.mariadb.v20170312.models.DBInstance> instances = TencentApi.listMariaDBInstance(listMariaDBInstanceRequest);
        return instances
                .stream()
                .map(instance -> ResourceUtil.toResourceInstance(PlatformConstants.fit2cloud_tencent_platform.name(), ResourceTypeConstants.MARIA_DB, instance.getInstanceId(), instance.getInstanceName(), instance))
                .toList();
    }

    @Override
    public List<InstanceSearchField> listMariaDBInstanceSearchField() {
        return List.of();
    }

    @Override
    public List<ResourceInstance> listElasticSearchInstance(String req) {
        ListElasticsearchInstanceRequest listElasticsearchInstanceRequest = JsonUtil.parseObject(req, ListElasticsearchInstanceRequest.class);
        List<com.tencentcloudapi.es.v20180416.models.InstanceInfo> instances = TencentApi.listElasticSearchInstance(listElasticsearchInstanceRequest);
        return instances
                .stream()
                .map(instance -> ResourceUtil.toResourceInstance(PlatformConstants.fit2cloud_tencent_platform.name(), ResourceTypeConstants.ELASTIC_SEARCH, instance.getInstanceId(), instance.getInstanceName(), instance))
                .toList();
    }

    @Override
    public List<InstanceSearchField> listElasticSearchInstanceSearchField() {
        return List.of();
    }


}
