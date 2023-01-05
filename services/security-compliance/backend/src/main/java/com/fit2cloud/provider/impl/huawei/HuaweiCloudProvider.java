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
import com.fit2cloud.provider.impl.huawei.entity.request.*;
import com.fit2cloud.provider.util.ResourceUtil;
import com.huaweicloud.sdk.css.v1.model.ClusterList;
import com.huaweicloud.sdk.dcs.v2.model.InstanceListInfo;
import com.huaweicloud.sdk.dds.v3.model.QueryInstanceResponse;
import com.huaweicloud.sdk.ecs.v2.model.ServerDetail;
import com.huaweicloud.sdk.rds.v3.model.InstanceResponse;

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

    @Override
    public List<ResourceInstance> listRedisInstance(String req) {
        ListRedisInstanceRequest listRedisInstanceRequest = JsonUtil.parseObject(req, ListRedisInstanceRequest.class);
        List<InstanceListInfo> instanceListInfos = HuaweiApi.listRedisInstance(listRedisInstanceRequest);
        return instanceListInfos.stream()
                .map(instance -> ResourceUtil.toResourceInstance(PlatformConstants.fit2cloud_huawei_platform.name(), ResourceTypeConstants.REDIS, instance.getInstanceId(), instance.getName(), instance))
                .toList();
    }

    @Override
    public List<InstanceSearchField> listRedisInstanceSearchField() {
        return List.of();
    }

    @Override
    public List<ResourceInstance> listMongodbInstance(String req) {
        ListMongodbInstanceRequest listMongodbInstanceRequest = JsonUtil.parseObject(req, ListMongodbInstanceRequest.class);
        List<QueryInstanceResponse> queryInstanceResponses = HuaweiApi.listMongodbInstance(listMongodbInstanceRequest);
        return queryInstanceResponses.stream()
                .map(instance -> ResourceUtil.toResourceInstance(PlatformConstants.fit2cloud_huawei_platform.name(), ResourceTypeConstants.MONGO_DB, instance.getId(), instance.getName(), instance))
                .toList();
    }

    @Override
    public List<InstanceSearchField> listMongodbInstanceSearchField() {
        return null;
    }

    @Override
    public List<ResourceInstance> listMysqlInstance(String req) {
        ListRdsInstanceRequest listRdsInstanceRequest = JsonUtil.parseObject(req, ListRdsInstanceRequest.class);
        List<InstanceResponse> instanceResponses = HuaweiApi.listMysqlInstance(listRdsInstanceRequest);
        return instanceResponses.stream()
                .map(instance -> ResourceUtil.toResourceInstance(PlatformConstants.fit2cloud_huawei_platform.name(), ResourceTypeConstants.MYSQL, instance.getId(), instance.getName(), instance))
                .toList();
    }

    @Override
    public List<InstanceSearchField> listMysqlInstanceSearchField() {
        return null;
    }

    @Override
    public List<ResourceInstance> listSqlServerInstance(String req) {
        ListRdsInstanceRequest listRdsInstanceRequest = JsonUtil.parseObject(req, ListRdsInstanceRequest.class);
        List<InstanceResponse> instanceResponses = HuaweiApi.listSqlServer(listRdsInstanceRequest);
        return instanceResponses.stream()
                .map(instance -> ResourceUtil.toResourceInstance(PlatformConstants.fit2cloud_huawei_platform.name(), ResourceTypeConstants.SQL_SERVER, instance.getId(), instance.getName(), instance))
                .toList();
    }

    @Override
    public List<InstanceSearchField> listSqlServerInstanceSearchField() {
        return null;
    }

    @Override
    public List<ResourceInstance> listPostGreSqlInstance(String req) {
        ListRdsInstanceRequest listRdsInstanceRequest = JsonUtil.parseObject(req, ListRdsInstanceRequest.class);
        List<InstanceResponse> instanceResponses = HuaweiApi.listPostGreSqlInstance(listRdsInstanceRequest);
        return instanceResponses.stream()
                .map(instance -> ResourceUtil.toResourceInstance(PlatformConstants.fit2cloud_huawei_platform.name(), ResourceTypeConstants.POST_GRE_SQL, instance.getId(), instance.getName(), instance))
                .toList();
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
        ListElasticSearchInstanceRequest listElasticSearchInstanceRequest = JsonUtil.parseObject(req, ListElasticSearchInstanceRequest.class);
        List<ClusterList> clusterLists = HuaweiApi.listElasticSearchInstance(listElasticSearchInstanceRequest);
        return clusterLists.stream()
                .map(instance -> ResourceUtil.toResourceInstance(PlatformConstants.fit2cloud_huawei_platform.name(), ResourceTypeConstants.ELASTIC_SEARCH, instance.getId(), instance.getName(), instance))
                .toList();
    }

    @Override
    public List<InstanceSearchField> listElasticSearchInstanceSearchField() {
        return List.of();
    }
}
