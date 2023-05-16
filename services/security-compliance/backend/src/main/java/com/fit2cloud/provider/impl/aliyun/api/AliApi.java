package com.fit2cloud.provider.impl.aliyun.api;

import com.aliyun.dds20151201.models.DescribeDBInstancesResponseBody;
import com.aliyun.dds20151201.models.DescribeShardingNetworkAddressRequest;
import com.aliyun.dds20151201.models.DescribeShardingNetworkAddressResponse;
import com.aliyun.dds20151201.models.DescribeShardingNetworkAddressResponseBody;
import com.aliyun.ecs20140526.Client;
import com.aliyun.ecs20140526.models.*;
import com.aliyun.elasticsearch20170613.models.ListInstanceResponseBody;
import com.aliyun.r_kvstore20150101.models.DescribeDBInstanceNetInfoRequest;
import com.aliyun.r_kvstore20150101.models.DescribeDBInstanceNetInfoResponse;
import com.aliyun.ram20150501.models.GetLoginProfileRequest;
import com.aliyun.ram20150501.models.GetLoginProfileResponse;
import com.aliyun.ram20150501.models.ListUsersResponseBody;
import com.aliyun.rds20140815.models.DescribeDBInstanceNetInfoResponseBody;
import com.aliyun.rds20140815.models.DescribeTagsResponse;
import com.aliyun.rds20140815.models.DescribeTagsResponseBody;
import com.aliyun.sdk.service.oss20190517.AsyncClient;
import com.aliyun.sdk.service.oss20190517.models.*;
import com.aliyun.slb20140515.models.DescribeLoadBalancersResponseBody;
import com.aliyun.teautil.models.RuntimeOptions;
import com.aliyun.vpc20160428.models.DescribeEipAddressesResponseBody;
import com.aliyun.vpc20160428.models.DescribeVSwitchesRequest;
import com.aliyun.vpc20160428.models.DescribeVSwitchesResponseBody;
import com.aliyun.vpc20160428.models.DescribeVpcsResponseBody;
import com.aliyun.vpc20160428.models.*;
import com.fit2cloud.common.exception.Fit2cloudException;
import com.fit2cloud.common.provider.exception.ReTryException;
import com.fit2cloud.common.provider.exception.SkipPageException;
import com.fit2cloud.common.provider.util.PageUtil;
import com.fit2cloud.provider.constants.ProviderConstants;
import com.fit2cloud.provider.impl.aliyun.entity.credential.AliSecurityComplianceCredential;
import com.fit2cloud.provider.impl.aliyun.entity.request.*;
import com.fit2cloud.provider.impl.aliyun.entity.response.*;
import jodd.util.StringUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2022/12/6  11:16}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
public class AliApi {

    /**
     * 获取阿里云实例列表
     *
     * @param request 请求对象
     * @return 阿里云ecs实例列表
     */
    public static List<DescribeInstancesResponseBody.DescribeInstancesResponseBodyInstancesInstance> listECSInstance(ListEcsInstancesRequest request) {
        Client ecsClient = request.getCredential().getEcsClient(request.getRegionId());
        request.setPageNumber(PageUtil.DefaultCurrentPage);
        request.setPageSize(PageUtil.DefaultPageSize);
        return PageUtil.page(request, req -> {
                    try {
                        return ecsClient.describeInstancesWithOptions(request, new RuntimeOptions());
                    } catch (Exception e) {
                        ReTryException.throwReTry(e);
                        SkipPageException.throwSkip(e);
                        throw new Fit2cloudException(10002, "获取阿里云云主机列表失败" + e.getMessage());
                    }
                }
                , res -> res.getBody().getInstances().instance
                , (req, res) -> res.getBody().getPageSize() <= res.getBody().getInstances().instance.size()
                , req -> req.setPageNumber(req.getPageNumber() + 1), ProviderConstants.retryNum);
    }

    public static List<EcsInstanceResponse> listECSInstanceCollection(ListEcsInstancesRequest request) {
        Client ecsClient = request.getCredential().getEcsClient(request.getRegionId());
        // 查询到ecs实例列表
        List<DescribeInstancesResponseBody.DescribeInstancesResponseBodyInstancesInstance> ecsInstances = listECSInstance(request);
        // 查询安全组数据
        ListSecurityGroupInstanceRequest listSecurityGroupInstanceRequest = new ListSecurityGroupInstanceRequest();
        BeanUtils.copyProperties(request, listSecurityGroupInstanceRequest);
        List<DescribeSecurityGroupsResponseBody.DescribeSecurityGroupsResponseBodySecurityGroupsSecurityGroup> securityGroups = listSecurityGroupInstance(listSecurityGroupInstanceRequest);
        DescribeDisksRequest describeDisksRequest = new DescribeDisksRequest();
        describeDisksRequest.setRegionId(request.getRegionId());
        // 磁盘相关数据
        List<DescribeDisksResponseBody.DescribeDisksResponseBodyDisksDisk> disks = listDisk(describeDisksRequest, ecsClient);
        // 查询包年包月是否开启自动续费
        DescribeInstanceAutoRenewAttributeRequest describeInstanceAutoRenewAttributeRequest = new DescribeInstanceAutoRenewAttributeRequest();
        describeInstanceAutoRenewAttributeRequest.setRegionId(request.getRegionId());
        List<DescribeInstanceAutoRenewAttributeResponseBody.DescribeInstanceAutoRenewAttributeResponseBodyInstanceRenewAttributesInstanceRenewAttribute> autoRenews = listInstanceAutoRenewAttributeAll(describeInstanceAutoRenewAttributeRequest, ecsClient);
        return ecsInstances.stream().map(ecs -> mergeEcsInstance(ecs, securityGroups, disks, autoRenews, ecsClient)).toList();
    }

    /**
     * 合并数据
     *
     * @param ecs            ecs 基本信息
     * @param securityGroups 安全性相关信息
     * @param disks          磁盘相关信息
     * @param autoRenews     自动续期相关信息
     * @param ecsClient      ecs客户端
     * @return Ecs组合数据
     */
    private static EcsInstanceResponse mergeEcsInstance(DescribeInstancesResponseBody.DescribeInstancesResponseBodyInstancesInstance ecs,
                                                        List<DescribeSecurityGroupsResponseBody.DescribeSecurityGroupsResponseBodySecurityGroupsSecurityGroup> securityGroups,
                                                        List<DescribeDisksResponseBody.DescribeDisksResponseBodyDisksDisk> disks,
                                                        List<DescribeInstanceAutoRenewAttributeResponseBody.DescribeInstanceAutoRenewAttributeResponseBodyInstanceRenewAttributesInstanceRenewAttribute> autoRenews,
                                                        Client ecsClient) {
        EcsInstanceResponse ecsInstanceResponse = new EcsInstanceResponse();
        BeanUtils.copyProperties(ecs, ecsInstanceResponse);
        // 设置安全组属性
        if (Objects.nonNull(ecs.securityGroupIds) && CollectionUtils.isNotEmpty(ecs.securityGroupIds.securityGroupId)) {
            List<SecurityGroupsSecurityGroupInstanceResponse> securityGroupsSecurityGroupInstanceResponses = securityGroups.stream().filter(group -> ecs.securityGroupIds.securityGroupId.contains(group.securityGroupId))
                    .map(group -> {
                        SecurityGroupsSecurityGroupInstanceResponse securityGroupsSecurityGroupInstanceResponse = new SecurityGroupsSecurityGroupInstanceResponse();
                        BeanUtils.copyProperties(group, securityGroupsSecurityGroupInstanceResponse);
                        securityGroupsSecurityGroupInstanceResponse.setRule(getSecurityGroupRuleInstance(ecsClient, group.securityGroupId));
                        return securityGroupsSecurityGroupInstanceResponse;
                    }).toList();
            ecsInstanceResponse.setSecurityGroupRules(securityGroupsSecurityGroupInstanceResponses);
        }
        // 设置磁盘属性
        List<DescribeDisksResponseBody.DescribeDisksResponseBodyDisksDisk> ecsDisks = disks.stream().filter(disk -> StringUtil.equals(disk.instanceId, ecs.instanceId)).toList();
        ecsInstanceResponse.setDisks(ecsDisks);
        // 设置包年包月自动续费属性
        autoRenews.stream().filter(auto -> StringUtil.equals(auto.instanceId, ecs.instanceId)).findFirst()
                .ifPresent(ecsInstanceResponse::setAutoRenew);
        return ecsInstanceResponse;

    }

    /**
     * 查询包年包月续费状态
     *
     * @return 实例续费状态
     */
    private static List<DescribeInstanceAutoRenewAttributeResponseBody.DescribeInstanceAutoRenewAttributeResponseBodyInstanceRenewAttributesInstanceRenewAttribute> listInstanceAutoRenewAttributeAll(DescribeInstanceAutoRenewAttributeRequest request, Client ecsClient) {
        ArrayList<DescribeInstanceAutoRenewAttributeResponseBody.DescribeInstanceAutoRenewAttributeResponseBodyInstanceRenewAttributesInstanceRenewAttribute> all = new ArrayList<>();

        for (String renewalStatus : List.of("AutoRenewal", "Normal", "NotRenewal")) {
            request.setRenewalStatus(renewalStatus);
            List<DescribeInstanceAutoRenewAttributeResponseBody.DescribeInstanceAutoRenewAttributeResponseBodyInstanceRenewAttributesInstanceRenewAttribute> item = listInstanceAutoRenewAttribute(request, ecsClient);
            if (CollectionUtils.isNotEmpty(item)) {
                all.addAll(item);
            }
        }
        return all;
    }

    /**
     * 查询包年包月续费状态
     *
     * @return 实例续费状态
     */
    private static List<DescribeInstanceAutoRenewAttributeResponseBody.DescribeInstanceAutoRenewAttributeResponseBodyInstanceRenewAttributesInstanceRenewAttribute> listInstanceAutoRenewAttribute(DescribeInstanceAutoRenewAttributeRequest request, Client ecsClient) {
        request.setPageNumber(PageUtil.DefaultCurrentPage.toString());
        request.setPageSize(PageUtil.DefaultPageSize.toString());

        // 自动续费实例
        return PageUtil.page(request, req -> {
                    try {
                        return ecsClient.describeInstanceAutoRenewAttributeWithOptions(request, new RuntimeOptions());
                    } catch (Exception e) {
                        ReTryException.throwReTry(e);
                        SkipPageException.throwSkip(e);
                        throw new Fit2cloudException(10002, "获取阿里云云主机列表失败" + e.getMessage());
                    }
                }
                , res -> res.getBody().getInstanceRenewAttributes().instanceRenewAttribute
                , (req, res) -> res.getBody().getPageSize() <= res.getBody().getInstanceRenewAttributes().instanceRenewAttribute.size()
                , req -> req.setPageNumber(req.getPageNumber() + 1), ProviderConstants.retryNum);


    }

    private static List<DescribeDisksResponseBody.DescribeDisksResponseBodyDisksDisk> listDisk(DescribeDisksRequest request, Client ecsClient) {
        request.setPageNumber(PageUtil.DefaultCurrentPage);
        request.setPageSize(PageUtil.DefaultPageSize);
        return PageUtil.page(request, req -> {
                    try {
                        return ecsClient.describeDisksWithOptions(request, new RuntimeOptions());
                    } catch (Exception e) {
                        ReTryException.throwReTry(e);
                        SkipPageException.throwSkip(e);
                        throw new Fit2cloudException(10002, "获取阿里云云主机列表失败" + e.getMessage());
                    }
                }
                , res -> res.getBody().getDisks().disk
                , (req, res) -> res.getBody().getPageSize() <= res.getBody().getDisks().disk.size()
                , req -> req.setPageNumber(req.getPageNumber() + 1), ProviderConstants.retryNum);

    }

    /**
     * 查询镜像实例列表数据
     *
     * @param request   请求对象
     * @param ecsClient ecs客户端
     * @return 镜像实例列表数据
     */
    private static List<DescribeImagesResponseBody.DescribeImagesResponseBodyImagesImage> listImagesInstance(DescribeImagesRequest request, Client ecsClient) {
        request.setPageNumber(PageUtil.DefaultCurrentPage);
        request.setPageSize(PageUtil.DefaultPageSize);
        return PageUtil.page(request, req -> {
                    try {
                        return ecsClient.describeImages(request);
                    } catch (Exception e) {
                        ReTryException.throwReTry(e);
                        SkipPageException.throwSkip(e);
                        throw new Fit2cloudException(10002, "获取阿里云云主机列表失败" + e.getMessage());
                    }
                }
                , res -> res.getBody().getImages().image
                , (req, res) -> res.getBody().getPageSize() <= res.getBody().getImages().image.size()
                , req -> req.setPageNumber(req.getPageNumber() + 1), ProviderConstants.retryNum);

    }

    /**
     * 获取 云数据库 redis 实例列表合集
     *
     * @param request 请求对象
     * @return 阿里云redis redisNetwork 实例列表
     */
    public static List<RedisInstanceResponse> listRedisInstanceCollection(ListRedisInstanceRequest request) {
        List<com.aliyun.r_kvstore20150101.models.DescribeInstancesResponseBody.DescribeInstancesResponseBodyInstancesKVStoreInstance> describeInstancesResponseBodyInstancesKVStoreInstances = listRedisInstance(request);
        com.aliyun.r_kvstore20150101.Client rdsClient = request.getCredential().getRedisClient(request.getRegionId());
        return describeInstancesResponseBodyInstancesKVStoreInstances.stream().map(redis -> {
            RedisInstanceResponse redisInstanceResponse = new RedisInstanceResponse(redis);
            com.aliyun.r_kvstore20150101.models.DescribeDBInstanceNetInfoResponseBody redisNetInfoInstance = getRedisNetInfoInstance(rdsClient, redis.getInstanceId());
            redisInstanceResponse.setNetwork(redisNetInfoInstance);
            return redisInstanceResponse;
        }).toList();
    }

    /**
     * 获取阿里云 云数据库redis实例列表
     *
     * @param request 请求对象
     * @return 阿里云 云数据库redis 实例列表
     */
    public static List<com.aliyun.r_kvstore20150101.models.DescribeInstancesResponseBody.DescribeInstancesResponseBodyInstancesKVStoreInstance> listRedisInstance(ListRedisInstanceRequest request) {
        com.aliyun.r_kvstore20150101.Client redisClient = request.getCredential().getRedisClient(request.getRegionId());
        request.setPageNumber(PageUtil.DefaultCurrentPage);
        request.setPageSize(PageUtil.DefaultPageSize);
        return PageUtil.page(request, req -> {
                    try {
                        return redisClient.describeInstancesWithOptions(request, new RuntimeOptions());
                    } catch (Exception e) {
                        ReTryException.throwReTry(e);
                        SkipPageException.throwSkip(e);
                        throw new Fit2cloudException(10002, "获取阿里云redis列表失败" + e.getMessage());
                    }
                }
                , res -> res.getBody().getInstances().getKVStoreInstance()
                , (req, res) -> res.getBody().getPageSize() <= res.getBody().getInstances().getKVStoreInstance().size()
                , req -> req.setPageNumber(req.getPageNumber() + 1), ProviderConstants.retryNum);

    }


    /**
     * 获取Redis网络数据
     *
     * @param request 请求对象
     * @return 网络信息
     */
    public static com.aliyun.r_kvstore20150101.models.DescribeDBInstanceNetInfoResponseBody getRedisNetInfoInstanceRequest(GetRedisNetInfoInstanceRequest request) {
        com.aliyun.r_kvstore20150101.Client redisClient = request.getCredential().getRedisClient(null);
        return getRedisNetInfoInstance(redisClient, request.getDBInstanceId());
    }

    /**
     * 获取redis网络信息
     *
     * @param rdsClient  redis客户端
     * @param instanceId 实例id
     * @return redis实例网络信息
     */
    private static com.aliyun.r_kvstore20150101.models.DescribeDBInstanceNetInfoResponseBody getRedisNetInfoInstance(com.aliyun.r_kvstore20150101.Client rdsClient, String instanceId) {
        DescribeDBInstanceNetInfoResponse describeDBInstanceNetInfoResponse = PageUtil.reTry(() -> {
            try {
                DescribeDBInstanceNetInfoRequest request = new DescribeDBInstanceNetInfoRequest();
                request.setInstanceId(instanceId);
                return rdsClient.describeDBInstanceNetInfoWithOptions(request, new RuntimeOptions());
            } catch (Exception e) {
                ReTryException.throwReTry(e);
                SkipPageException.throwSkip(e);
                throw new Fit2cloudException(10002, "获取阿里云Redis网络失败" + e.getMessage());
            }
        }, ProviderConstants.retryNum);
        assert describeDBInstanceNetInfoResponse != null;
        return describeDBInstanceNetInfoResponse.getBody();
    }

    /**
     * 获取mongodb (mongodb,mongodbNetwork)信息
     *
     * @param request 请求对象
     * @return mongodb实例与网络信息
     */
    public static List<MongoDBInstanceResponse> listMongoDBInstanceCollection(ListMongoDBRequest request) {
        List<DescribeDBInstancesResponseBody.DescribeDBInstancesResponseBodyDBInstancesDBInstance> instances = listMongoDBInstance(request);
        com.aliyun.dds20151201.Client mongodbClient = request.getCredential().getMongodbClient(request.getRegionId());
        return instances.stream().map(mongo -> {
            MongoDBInstanceResponse mongoDBInstanceResponse = new MongoDBInstanceResponse(mongo);
            DescribeShardingNetworkAddressResponseBody mongodbNetInfoInstance = getMongodbNetInfoInstance(mongodbClient, mongo.DBInstanceId);
            mongoDBInstanceResponse.setNetworkObj(mongodbNetInfoInstance);
            return mongoDBInstanceResponse;
        }).toList();
    }

    /**
     * 获取阿里云 云数据库Mongodb实例列表
     *
     * @param request 请求对象
     * @return 阿里云 云数据库MongoDB 实例列表
     */
    public static List<DescribeDBInstancesResponseBody.DescribeDBInstancesResponseBodyDBInstancesDBInstance> listMongoDBInstance(ListMongoDBRequest request) {
        com.aliyun.dds20151201.Client mongodbClient = request.getCredential().getMongodbClient(request.getRegionId());
        request.setPageNumber(PageUtil.DefaultCurrentPage);
        request.setPageSize(PageUtil.DefaultPageSize);
        return PageUtil.page(request, req -> {
                    try {
                        return mongodbClient.describeDBInstancesWithOptions(request, new RuntimeOptions());
                    } catch (Exception e) {
                        ReTryException.throwReTry(e);
                        SkipPageException.throwSkip(e);
                        throw new Fit2cloudException(10002, "获取阿里云MongoDB实例失败" + e.getMessage());
                    }
                }
                , res -> res.getBody().getDBInstances().getDBInstance()
                , (req, res) -> res.getBody().getPageSize() <= res.getBody().getDBInstances().getDBInstance().size()
                , req -> req.setPageNumber(req.getPageNumber() + 1), ProviderConstants.retryNum);

    }

    /**
     * 获取mongodb 实例网络信息
     *
     * @param request 请求对象
     * @return mongodb 实例网络对象
     */
    public static DescribeShardingNetworkAddressResponseBody getMongodbNetInfoInstance(GetMongodbNetInfoInstanceRequest request) {
        com.aliyun.dds20151201.Client mongodbClient = request.getCredential().getMongodbClient(null);
        return getMongodbNetInfoInstance(mongodbClient, request.getDBInstanceId());
    }

    /**
     * 获取mongodb 实例网络信息
     *
     * @param mongodbClient mongodb 客户端
     * @param instanceId    mongodb 实例id
     * @return mongodb实例网络数据
     */
    public static DescribeShardingNetworkAddressResponseBody getMongodbNetInfoInstance(com.aliyun.dds20151201.Client mongodbClient, String instanceId) {
        DescribeShardingNetworkAddressRequest describeShardingNetworkAddressRequest = new DescribeShardingNetworkAddressRequest();
        describeShardingNetworkAddressRequest.setDBInstanceId(instanceId);

        DescribeShardingNetworkAddressResponse describeShardingNetworkAddressResponse = PageUtil.reTry(() -> {
            try {
                return mongodbClient.describeShardingNetworkAddressWithOptions(describeShardingNetworkAddressRequest, new RuntimeOptions());
            } catch (Exception e) {
                ReTryException.throwReTry(e);
                SkipPageException.throwSkip(e);
                throw new Fit2cloudException(10002, "获取阿里云MongoDB实例失败" + e.getMessage());
            }
        }, ProviderConstants.retryNum);
        assert describeShardingNetworkAddressResponse != null;
        return describeShardingNetworkAddressResponse.getBody();
    }

    /**
     * 获取阿里云 云数据Mysql实例集合列表
     *
     * @param request 请求对象
     * @return 阿里云 云数据库 mysql实例列表
     */
    public static List<RdsInstanceResponse> listMysqlInstanceCollection(ListRdsInstanceRequest request) {
        request.setEngine("MySQL");
        return listRdsInstanceCollection(request);
    }

    /**
     * 获取阿里云 云数据Mysql实例列表
     *
     * @param request 请求对象
     * @return 阿里云 云数据库 mysql实例列表
     */
    public static List<com.aliyun.rds20140815.models.DescribeDBInstancesResponseBody.DescribeDBInstancesResponseBodyItemsDBInstance> listMysqlInstance(ListRdsInstanceRequest request) {
        request.setEngine("MySQL");
        return listRdsInstance(request);
    }

    /**
     * 获取阿里云 云数据SQLServer实例列表
     *
     * @param request 请求对象
     * @return 阿里云 云数据库 SQLServer实例列表
     */
    public static List<RdsInstanceResponse> listSqlServerInstanceCollection(ListRdsInstanceRequest request) {
        request.setEngine("SQLServer");
        return listRdsInstanceCollection(request);
    }

    /**
     * 获取阿里云 云数据SQLServer实例列表
     *
     * @param request 请求对象
     * @return 阿里云 云数据库 SQLServer实例列表
     */
    public static List<com.aliyun.rds20140815.models.DescribeDBInstancesResponseBody.DescribeDBInstancesResponseBodyItemsDBInstance> listSqlServerInstance(ListRdsInstanceRequest request) {
        request.setEngine("SQLServer");
        return listRdsInstance(request);
    }

    /**
     * 获取阿里云 云数据PostgreSQL实例列表
     *
     * @param request 请求对象
     * @return 阿里云 云数据库 PostgreSQL实例列表
     */
    public static List<RdsInstanceResponse> listPostgreSqlInstanceCollection(ListRdsInstanceRequest request) {
        request.setEngine("PostgreSQL");
        return listRdsInstanceCollection(request);
    }

    /**
     * 获取阿里云 云数据PostgreSQL实例列表
     *
     * @param request 请求对象
     * @return 阿里云 云数据库 PostgreSQL实例列表
     */
    public static List<com.aliyun.rds20140815.models.DescribeDBInstancesResponseBody.DescribeDBInstancesResponseBodyItemsDBInstance> listPostgreSqlInstance(ListRdsInstanceRequest request) {
        request.setEngine("PostgreSQL");
        return listRdsInstance(request);
    }

    /**
     * 获取阿里云 云数据MariaDB实例列表
     *
     * @param request 请求对象
     * @return 阿里云 云数据库 MariaDB实例列表
     */
    public static List<RdsInstanceResponse> listMariaDBInstanceCollection(ListRdsInstanceRequest request) {
        request.setEngine("MariaDB");
        return listRdsInstanceCollection(request);
    }

    /**
     * 获取阿里云 云数据MariaDB实例列表
     *
     * @param request 请求对象
     * @return 阿里云 云数据库 MariaDB实例列表
     */
    public static List<com.aliyun.rds20140815.models.DescribeDBInstancesResponseBody.DescribeDBInstancesResponseBodyItemsDBInstance> listMariaDBInstance(ListRdsInstanceRequest request) {
        request.setEngine("MariaDB");
        return listRdsInstance(request);
    }

    /**
     * 获取阿里云 云数据库
     * MySQL
     * SQLServer
     * PostgreSQL
     * MariaDB
     *
     * @param request 请求对象
     * @return 阿里云 云数据库 rds 实例列表
     */
    private static List<com.aliyun.rds20140815.models.DescribeDBInstancesResponseBody.DescribeDBInstancesResponseBodyItemsDBInstance> listRdsInstance(ListRdsInstanceRequest request) {
        com.aliyun.rds20140815.Client rdsClient = request.getCredential().getRdsClient(request.getRegionId());
        request.setPageNumber(PageUtil.DefaultCurrentPage);
        request.setPageSize(PageUtil.DefaultPageSize);
        return PageUtil.page(request, req -> {
                    try {
                        return rdsClient.describeDBInstancesWithOptions(request, new RuntimeOptions());
                    } catch (Exception e) {
                        ReTryException.throwReTry(e);
                        SkipPageException.throwSkip(e);
                        throw new Fit2cloudException(10002, "获取阿里云Mysql实例失败" + e.getMessage());
                    }
                }
                , res -> res.getBody().getItems().getDBInstance()
                , (req, res) -> req.getPageSize() <= res.getBody().getItems().getDBInstance().size()
                , req -> req.setPageNumber(req.getPageNumber() + 1), ProviderConstants.retryNum);
    }

    /**
     * 获取RDS实例集合 包括网络连接信息
     *
     * @param request 请求对象
     * @return Rds实例集合
     */
    public static List<RdsInstanceResponse> listRdsInstanceCollection(ListRdsInstanceRequest request) {
        List<com.aliyun.rds20140815.models.DescribeDBInstancesResponseBody.DescribeDBInstancesResponseBodyItemsDBInstance> instances = listRdsInstance(request);
        com.aliyun.rds20140815.Client rdsClient = request.getCredential().getRdsClient(request.getRegionId());
        return instances.stream().map(rds -> {
            RdsInstanceResponse rdsInstanceResponse = new RdsInstanceResponse(rds);
            DescribeDBInstanceNetInfoResponseBody rdsNetInfoInstance = getRdsNetInfoInstance(rdsClient, rds.DBInstanceId);
            List<DescribeTagsResponseBody.DescribeTagsResponseBodyItemsTagInfos> describeTagsResponseBodyItemsTagInfos = listTags(request);
            List<DescribeInstancesResponseBody.DescribeInstancesResponseBodyInstancesInstanceTagsTag> tags = describeTagsResponseBodyItemsTagInfos.stream().filter(tag -> tag.getDBInstanceIds().DBInstanceIds.contains(rds.DBInstanceId))
                    .map(tag -> {
                        DescribeInstancesResponseBody.DescribeInstancesResponseBodyInstancesInstanceTagsTag t = new DescribeInstancesResponseBody.DescribeInstancesResponseBodyInstancesInstanceTagsTag();
                        t.setTagKey(tag.getTagKey());
                        t.setTagValue(tag.getTagValue());
                        return t;
                    }).toList();
            rdsInstanceResponse.setTags(tags);
            rdsInstanceResponse.setNetworkObj(rdsNetInfoInstance);
            return rdsInstanceResponse;
        }).toList();
    }

    /**
     * 获取Rds数据库标签
     *
     * @param request Rds请求数据
     * @return Rds标签
     */
    public static List<DescribeTagsResponseBody.DescribeTagsResponseBodyItemsTagInfos> listTags(ListRdsInstanceRequest request) {
        com.aliyun.rds20140815.Client rdsClient = request.getCredential().getRdsClient(request.getRegionId());
        com.aliyun.rds20140815.models.DescribeTagsRequest describeTagsRequest = new com.aliyun.rds20140815.models.DescribeTagsRequest();
        describeTagsRequest.setRegionId(request.getRegionId());
        DescribeTagsResponse describeTagsResponse = PageUtil.reTry(() -> {
            try {
                return rdsClient.describeTagsWithOptions(describeTagsRequest, new RuntimeOptions());
            } catch (Exception e) {
                ReTryException.throwReTry(e);
                SkipPageException.throwSkip(e);
                throw new Fit2cloudException(10002, "获取Rds数据库标签错误" + e.getMessage());
            }
        }, 3);
        if (Objects.nonNull(describeTagsResponse)
                && Objects.nonNull(describeTagsResponse.getBody())
                && Objects.nonNull(describeTagsResponse.getBody().getItems())
                && CollectionUtils.isNotEmpty(describeTagsResponse.getBody().getItems().tagInfos)) {
            return describeTagsResponse.getBody().getItems().tagInfos;
        } else {
            return new ArrayList<>();
        }

    }

    /**
     * 获取Rds实例网络信息
     *
     * @param request 请求对象
     * @return RDS实例网络信息
     */
    public static DescribeDBInstanceNetInfoResponseBody getRdsNetInfoInstance(GetRdsNetInfoInstanceRequest request) {
        com.aliyun.rds20140815.Client rdsClient = request.getCredential().getRdsClient(null);
        return getRdsNetInfoInstance(rdsClient, request.getDBInstanceId());
    }

    /**
     * 获取Rds网络信息实例详细
     *
     * @param rdsClient  rds客户端
     * @param instanceId 实例id
     * @return Rds网络信息
     */
    private static DescribeDBInstanceNetInfoResponseBody getRdsNetInfoInstance(com.aliyun.rds20140815.Client rdsClient, @NotNull String instanceId) {
        com.aliyun.rds20140815.models.DescribeDBInstanceNetInfoRequest request = new com.aliyun.rds20140815.models.DescribeDBInstanceNetInfoRequest();
        request.setDBInstanceId(instanceId);
        com.aliyun.rds20140815.models.DescribeDBInstanceNetInfoResponse describeDBInstanceNetInfoResponse = PageUtil.reTry(() -> {
            try {
                return rdsClient.describeDBInstanceNetInfoWithOptions(request, new RuntimeOptions());
            } catch (Exception e) {
                ReTryException.throwReTry(e);
                SkipPageException.throwSkip(e);
                throw new Fit2cloudException(10002, "获取阿里云Mysql实例失败" + e.getMessage());
            }
        }, ProviderConstants.retryNum);
        assert describeDBInstanceNetInfoResponse != null;
        return describeDBInstanceNetInfoResponse.getBody();
    }

    /**
     * 获取阿里云 云数据库 elasticsearch 实例列表
     *
     * @param request 请求对象
     * @return 云数据库 elasticsearch 实例列表
     */
    public static List<ListInstanceResponseBody.ListInstanceResponseBodyResult> listElasticsearchInstance(ListElasticSearchInstanceRequest request) {
        com.aliyun.elasticsearch20170613.Client elasticSearchClient = request.getCredential().getElasticSearchClient(request.getRegionId());
        request.setPage(PageUtil.DefaultCurrentPage);
        request.setSize(PageUtil.DefaultPageSize);
        return PageUtil.page(request, req -> {
                    try {
                        return elasticSearchClient.listInstanceWithOptions(request, new HashMap<>(), new RuntimeOptions());
                    } catch (Exception e) {
                        ReTryException.throwReTry(e);
                        SkipPageException.throwSkip(e);
                        throw new Fit2cloudException(10002, "获取阿里云ElasticSearch实例失败" + e.getMessage());
                    }
                }
                , res -> res.getBody().getResult()
                , (req, res) -> req.getSize() <= res.getBody().getResult().size()
                , req -> req.setPage(req.getPage() + 1), ProviderConstants.retryNum);

    }

    /**
     * 获取 磁盘实例列表
     *
     * @param request 请求对象
     * @return 磁盘实例列表
     */
    public static List<DescribeDisksResponseBody.DescribeDisksResponseBodyDisksDisk> listDiskInstance(ListDiskInstanceRequest request) {
        Client ecsClient = request.getCredential().getEcsClient(request.getRegionId());
        request.setPageNumber(PageUtil.DefaultCurrentPage);
        request.setPageSize(PageUtil.DefaultPageSize);
        return PageUtil.page(request, req -> {
                    try {
                        return ecsClient.describeDisksWithOptions(request, new RuntimeOptions());
                    } catch (Exception e) {
                        ReTryException.throwReTry(e);
                        SkipPageException.throwSkip(e);
                        throw new Fit2cloudException(10002, "获取阿里云磁盘实例失败" + e.getMessage());
                    }
                }
                , res -> res.getBody().getDisks().disk
                , (req, res) -> res.getBody().getPageSize() <= res.getBody().getDisks().disk.size()
                , req -> req.setPageNumber(req.getPageNumber() + 1), ProviderConstants.retryNum);

    }

    /**
     * 获取 负载均衡实例列表
     *
     * @param request 请求对象
     * @return 负载均衡实例列表
     */
    public static List<DescribeLoadBalancersResponseBody.DescribeLoadBalancersResponseBodyLoadBalancersLoadBalancer> listLoadBalancerInstance(ListLoadBalancerInstanceRequest request) {
        com.aliyun.slb20140515.Client loadBalancerClient = request.getCredential().getLoadBalancerClient(request.getRegionId());
        request.setPageNumber(PageUtil.DefaultCurrentPage);
        request.setPageSize(PageUtil.DefaultPageSize);
        return PageUtil.page(request, req -> {
                    try {
                        return loadBalancerClient.describeLoadBalancersWithOptions(request, new RuntimeOptions());
                    } catch (Exception e) {
                        ReTryException.throwReTry(e);
                        SkipPageException.throwSkip(e);
                        throw new Fit2cloudException(10002, "获取阿里云负载均衡实例失败" + e.getMessage());
                    }
                }
                , res -> res.getBody().getLoadBalancers().loadBalancer
                , (req, res) -> res.getBody().getPageSize() <= res.getBody().getLoadBalancers().loadBalancer.size()
                , req -> req.setPageNumber(req.getPageNumber() + 1), ProviderConstants.retryNum);
    }

    /**
     * 获取 弹性公网ip 实例列表
     *
     * @param request 请求对象
     * @return 负载均衡实例列表
     */
    public static List<DescribeEipAddressesResponseBody.DescribeEipAddressesResponseBodyEipAddressesEipAddress> listPublicIpInstance(ListPublicIpInstanceRequest request) {
        com.aliyun.vpc20160428.Client vpcClient = request.getCredential().getVpcClient(request.getRegionId());
        request.setPageNumber(PageUtil.DefaultCurrentPage);
        request.setPageSize(PageUtil.DefaultPageSize);
        return PageUtil.page(request, req -> {
                    try {
                        return vpcClient.describeEipAddressesWithOptions(request, new RuntimeOptions());
                    } catch (Exception e) {
                        ReTryException.throwReTry(e);
                        SkipPageException.throwSkip(e);
                        throw new Fit2cloudException(10002, "获取阿里云弹性公网ip实例失败" + e.getMessage());
                    }
                }
                , res -> res.getBody().getEipAddresses().eipAddress
                , (req, res) -> res.getBody().getPageSize() <= res.getBody().getEipAddresses().eipAddress.size()
                , req -> req.setPageNumber(req.getPageNumber() + 1), ProviderConstants.retryNum);
    }

    /**
     * 查询vpc vpn网关 vpn连接信息数据集合
     *
     * @param request 请求对象
     * @return vpc 数据合集列表
     */
    public static List<VpcInstanceResponse> listVpcInstanceCollection(ListVpcInstanceRequest request) {
        com.aliyun.vpc20160428.Client vpcClient = request.getCredential().getVpcClient(request.getRegionId());

        // vpc实例列表
        List<DescribeVpcsResponseBody.DescribeVpcsResponseBodyVpcsVpc> vpcList = listVpcInstanceRequest(request);
        // vpn 网关列表
        DescribeVpnGatewaysRequest describeVpnGatewaysRequest = new DescribeVpnGatewaysRequest();
        describeVpnGatewaysRequest.setRegionId(request.getRegionId());
        List<DescribeVpnGatewaysResponseBody.DescribeVpnGatewaysResponseBodyVpnGatewaysVpnGateway> vpnGateways = listVpnGateway(describeVpnGatewaysRequest, vpcClient);
        // vpn 连接相关接口
        DescribeVpnConnectionsRequest describeVpnConnectionsRequest = new DescribeVpnConnectionsRequest();
        describeVpnConnectionsRequest.setRegionId(request.getRegionId());
        List<DescribeVpnConnectionsResponseBody.DescribeVpnConnectionsResponseBodyVpnConnectionsVpnConnection> vpnConnections = listVpnConnection(describeVpnConnectionsRequest, vpcClient);
        // vpc 流日志
        DescribeFlowLogsRequest describeFlowLogsRequest = new DescribeFlowLogsRequest();
        describeFlowLogsRequest.setRegionId(request.getRegionId());
        List<DescribeFlowLogsResponseBody.DescribeFlowLogsResponseBodyFlowLogsFlowLog> describeFlowLogsResponseBodyFlowLogsFlowLogs = listFlowLogs(describeFlowLogsRequest, vpcClient);
        // vpc 交换机
        DescribeVSwitchesRequest describeVSwitchesRequest = new DescribeVSwitchesRequest();
        describeVSwitchesRequest.setRegionId(request.getRegionId());
        List<DescribeVSwitchesResponseBody.DescribeVSwitchesResponseBodyVSwitchesVSwitch> describeVSwitchesResponseBodyVSwitchesVSwitches = listSwitch(describeVSwitchesRequest, vpcClient);
        return vpcList.stream().map(vpc -> mergeVpcInstance(vpc, vpnGateways, vpnConnections, describeFlowLogsResponseBodyFlowLogsFlowLogs, describeVSwitchesResponseBodyVSwitchesVSwitches)).toList();

    }


    /**
     * 合并vpc实例
     *
     * @param vpc            vpc实例
     * @param gateways       vpn网关数据
     * @param vpnConnections vpn连接数据
     * @return vpc对象合集
     */
    private static VpcInstanceResponse mergeVpcInstance(DescribeVpcsResponseBody.DescribeVpcsResponseBodyVpcsVpc vpc,
                                                        List<DescribeVpnGatewaysResponseBody.DescribeVpnGatewaysResponseBodyVpnGatewaysVpnGateway> gateways,
                                                        List<DescribeVpnConnectionsResponseBody.DescribeVpnConnectionsResponseBodyVpnConnectionsVpnConnection> vpnConnections,
                                                        List<DescribeFlowLogsResponseBody.DescribeFlowLogsResponseBodyFlowLogsFlowLog> flowLogsResponseBodyFlowLogsFlowLogs,
                                                        List<DescribeVSwitchesResponseBody.DescribeVSwitchesResponseBodyVSwitchesVSwitch> switches) {
        VpcInstanceResponse vpcInstanceResponse = new VpcInstanceResponse(vpc);
        gateways.stream().filter(gateway -> StringUtil.equals(gateway.vpcId, vpc.getVpcId())).findFirst().ifPresent(gateway -> {
            vpcInstanceResponse.setVpnGateway(gateway);

            vpnConnections.stream().filter(vpnConnection -> StringUtil.equals(gateway.getVpnGatewayId(), vpnConnection.getVpnGatewayId())).findFirst()
                    .ifPresent(vpcInstanceResponse::setVpnConnection);
        });
        flowLogsResponseBodyFlowLogsFlowLogs.stream().filter(flowLog -> StringUtil.equals(flowLog.resourceId, vpc.getVpcId())).findFirst().ifPresent(vpcInstanceResponse::setFlowLog);
        List<DescribeVSwitchesResponseBody.DescribeVSwitchesResponseBodyVSwitchesVSwitch> switchesList = switches.stream().filter(s -> StringUtil.equals(s.getVpcId(), vpc.vpcId)).toList();
        vpcInstanceResponse.setSwitchesList(switchesList);
        return vpcInstanceResponse;
    }


    /**
     * 获取 vpc 实例列表
     *
     * @param request 请求对象
     * @return vpc实例列表
     */
    public static List<DescribeVpcsResponseBody.DescribeVpcsResponseBodyVpcsVpc> listVpcInstanceRequest(ListVpcInstanceRequest request) {
        com.aliyun.vpc20160428.Client vpcClient = request.getCredential().getVpcClient(request.getRegionId());
        request.setPageNumber(PageUtil.DefaultCurrentPage);
        request.setPageSize(50);
        return PageUtil.page(request, req -> {
                    try {
                        return vpcClient.describeVpcsWithOptions(request, new RuntimeOptions());
                    } catch (Exception e) {
                        ReTryException.throwReTry(e);
                        SkipPageException.throwSkip(e);
                        throw new Fit2cloudException(10002, "获取阿里云vpc实例失败" + e.getMessage());
                    }
                }
                , res -> res.getBody().getVpcs().vpc
                , (req, res) -> res.getBody().getPageSize() <= res.getBody().getVpcs().vpc.size()
                , req -> req.setPageNumber(req.getPageNumber() + 1), ProviderConstants.retryNum);

    }

    /**
     * 查询流日志实例列表
     *
     * @param request   请求对象
     * @param vpcClient 客户端
     * @return 流日志实例列表数据
     */
    private static List<DescribeFlowLogsResponseBody.DescribeFlowLogsResponseBodyFlowLogsFlowLog> listFlowLogs(DescribeFlowLogsRequest request, com.aliyun.vpc20160428.Client vpcClient) {
        request.setPageNumber(PageUtil.DefaultCurrentPage);
        request.setPageSize(50);
        return PageUtil.page(request, req -> {
                    try {
                        return vpcClient.describeFlowLogsWithOptions(request, new RuntimeOptions());
                    } catch (Exception e) {
                        ReTryException.throwReTry(e);
                        SkipPageException.throwSkip(e);
                        throw new Fit2cloudException(10002, "获取阿里云RAM用户实例失败" + e.getMessage());
                    }
                }
                , res -> res.getBody().getFlowLogs().flowLog
                , (req, res) -> Integer.parseInt(res.getBody().getPageSize()) <= res.getBody().getFlowLogs().flowLog.size()
                , (req, res) -> req.setPageNumber(req.getPageNumber() + 1), ProviderConstants.retryNum);

    }

    /**
     * 列举所有的vpnGateWays相关数据
     *
     * @param request   请求对象
     * @param vpcClient vpc客户端
     * @return vpc 网关列表相关数据
     */
    public static List<DescribeVpnGatewaysResponseBody.DescribeVpnGatewaysResponseBodyVpnGatewaysVpnGateway> listVpnGateway(DescribeVpnGatewaysRequest request, com.aliyun.vpc20160428.Client vpcClient) {
        request.setPageNumber(PageUtil.DefaultCurrentPage);
        request.setPageSize(50);
        return PageUtil.page(request, req -> {
                    try {
                        return vpcClient.describeVpnGatewaysWithOptions(request, new RuntimeOptions());
                    } catch (Exception e) {
                        ReTryException.throwReTry(e);
                        SkipPageException.throwSkip(e);
                        throw new Fit2cloudException(10002, "获取阿里云RAM用户实例失败" + e.getMessage());
                    }
                }
                , res -> res.getBody().getVpnGateways().vpnGateway
                , (req, res) -> res.getBody().getPageSize() <= res.getBody().getVpnGateways().vpnGateway.size()
                , (req, res) -> req.setPageNumber(req.getPageNumber() + 1), ProviderConstants.retryNum);
    }

    /**
     * 查询交换机相关数据
     *
     * @param request   请求对象
     * @param vpcClient vpc客户端
     * @return 交换机相关数据
     */
    public static List<DescribeVSwitchesResponseBody.DescribeVSwitchesResponseBodyVSwitchesVSwitch> listSwitch(DescribeVSwitchesRequest request, com.aliyun.vpc20160428.Client vpcClient) {
        request.setPageNumber(PageUtil.DefaultCurrentPage);
        request.setPageSize(50);
        return
                PageUtil.page(request, req -> {
                            try {
                                return vpcClient.describeVSwitchesWithOptions(request, new RuntimeOptions());
                            } catch (Exception e) {
                                ReTryException.throwReTry(e);
                                SkipPageException.throwSkip(e);
                                throw new Fit2cloudException(10002, "获取阿里云RAM用户实例失败" + e.getMessage());
                            }
                        }
                        , res -> res.getBody().getVSwitches().vSwitch
                        , (req, res) -> res.getBody().getPageSize() <= res.getBody().getVSwitches().vSwitch.size()
                        , (req, res) -> req.setPageNumber(req.getPageNumber() + 1), ProviderConstants.retryNum);
    }

    /**
     * 查询 ipsec连接实例列表
     *
     * @param request   请求对象
     * @param vpcClient vpc客户端
     * @return ipsec连接实例列表数据
     */
    public static List<DescribeVpnConnectionsResponseBody.DescribeVpnConnectionsResponseBodyVpnConnectionsVpnConnection> listVpnConnection(DescribeVpnConnectionsRequest request, com.aliyun.vpc20160428.Client vpcClient) {
        request.setPageNumber(PageUtil.DefaultCurrentPage);
        request.setPageSize(50);
        return PageUtil.page(request, req -> {
                    try {
                        return vpcClient.describeVpnConnectionsWithOptions(request, new RuntimeOptions());
                    } catch (Exception e) {
                        ReTryException.throwReTry(e);
                        SkipPageException.throwSkip(e);
                        throw new Fit2cloudException(10002, "获取阿里云RAM用户实例失败" + e.getMessage());
                    }
                }
                , res -> res.getBody().getVpnConnections().vpnConnection
                , (req, res) -> res.getBody().getPageSize() <= res.getBody().getVpnConnections().vpnConnection.size()
                , (req, res) -> req.setPageNumber(req.getPageNumber() + 1), ProviderConstants.retryNum);

    }

    /**
     * 获取 Ram 用户 实例列表
     *
     * @param request 请求对象
     * @return RAM 用户实例列表
     */
    public static List<ListUsersResponseBody.ListUsersResponseBodyUsersUser> listRamInstanceRequest(ListRamInstanceRequest request) {
        com.aliyun.ram20150501.Client ramClient = request.getCredential().getRamClient();
        request.setMaxItems(PageUtil.DefaultPageSize);
        return PageUtil.page(request, req -> {
                    try {
                        return ramClient.listUsersWithOptions(request, new RuntimeOptions());
                    } catch (Exception e) {
                        ReTryException.throwReTry(e);
                        SkipPageException.throwSkip(e);
                        throw new Fit2cloudException(10002, "获取阿里云RAM用户实例失败" + e.getMessage());
                    }
                }
                , res -> res.getBody().getUsers().user
                , (req, res) -> res.getBody().isTruncated
                , (req, res) -> req.setMarker(res.getBody().marker), ProviderConstants.retryNum);
    }

    /**
     * 获取 登录配置
     *
     * @param request 请求对象
     * @return 登录配置
     */
    public static List<GetLoginProfileResponse> listLoginProfile(ListLoginProfileInstanceRequest request) {
        com.aliyun.ram20150501.Client ramClient = request.getCredential().getRamClient();
        List<String> usernameList = request.getUsernameList();
        return usernameList.stream().map(username -> {
            GetLoginProfileRequest getLoginProfileRequest = new GetLoginProfileRequest();
            getLoginProfileRequest.setUserName(username);
            return PageUtil.reTry(() -> {
                try {
                    return ramClient.getLoginProfileWithOptions(getLoginProfileRequest, new RuntimeOptions());
                } catch (Exception e) {
                    ReTryException.throwReTry(e);
                    SkipPageException.throwSkip(e);
                    return null;
                }
            }, ProviderConstants.retryNum);
        }).filter(Objects::nonNull).toList();

    }

    /**
     * 获取 存储桶实例集合列表
     *
     * @param request 请求对象
     * @return 存储桶实例列表
     */
    public static List<BucketInstanceResponse> listBucketCollectionInstance(ListBucketInstanceRequest request) {
        List<Bucket> buckets = listBucket(request.getCredential());
        return buckets.stream().map(bucket -> {
            AsyncClient ossClient = request.getCredential().getOssClient(bucket.getRegion());
            BucketInstanceResponse bucketInstanceResponse = new BucketInstanceResponse(bucket);
            GetBucketRefererResponseBody refererResponse = PageUtil.reTry(() -> {
                try {
                    return ossClient.getBucketReferer(GetBucketRefererRequest.builder().bucket(bucket.getName()).build()).join().getBody();
                } catch (Exception e) {
                    ReTryException.throwReTry(e);
                    return null;
                }

            }, ProviderConstants.retryNum);
            GetBucketEncryptionResponseBody encryptionResponse = PageUtil.reTry(() -> {
                try {
                    return ossClient.getBucketEncryption(GetBucketEncryptionRequest.builder().bucket(bucket.getName()).build()).join().getBody();
                } catch (Exception e) {
                    ReTryException.throwReTry(e);
                    return null;
                }

            }, ProviderConstants.retryNum);
            GetBucketAclResponseBody aclResponse = PageUtil.reTry(() -> {
                try {
                    return ossClient.getBucketAcl(GetBucketAclRequest.builder().bucket(bucket.getName()).build()).join().getBody();
                } catch (Exception e) {
                    ReTryException.throwReTry(e);
                    return null;
                }

            }, ProviderConstants.retryNum);
            bucketInstanceResponse.setAcl(aclResponse);
            bucketInstanceResponse.setReferer(refererResponse);
            bucketInstanceResponse.setEncryption(encryptionResponse);
            return bucketInstanceResponse;
        }).toList();
    }

    /**
     * 获取所有的桶
     *
     * @param aliyunBillCredential 阿里云认证信息
     * @return 获取所有的桶
     */
    public static List<Bucket> listBucket(AliSecurityComplianceCredential aliyunBillCredential) {
        AsyncClient ossClient = aliyunBillCredential.getOssClient();
        ListBucketsRequest listBucketsRequest = ListBucketsRequest.builder().maxKeys(100L).build();
        return PageUtil.pageNextF(listBucketsRequest,
                ossClient::listBuckets,
                res -> res.join().getBody().getBuckets(),
                (req, res) -> StringUtil.isNotEmpty(res.join().getBody().getNextMarker()),
                (req, res) -> ListBucketsRequest.builder().marker(res.join().getBody().getNextMarker()).maxKeys(100L).build());

    }


    /**
     * 获取 安全组与安全组规则实例合集列表
     *
     * @param request 请求对象
     * @return 安全组和安全组规则实例合集列表
     */
    public static List<SecurityGroupsSecurityGroupInstanceResponse> listSecurityGroupCollectionInstance(ListSecurityGroupInstanceRequest request) {
        List<DescribeSecurityGroupsResponseBody.DescribeSecurityGroupsResponseBodySecurityGroupsSecurityGroup> securityGroups = listSecurityGroupInstance(request);
        Client ecsClient = request.getCredential().getEcsClient(request.regionId);
        return securityGroups.stream().map(securityGroup -> {
            SecurityGroupsSecurityGroupInstanceResponse securityGroupsSecurityGroupInstanceResponse = new SecurityGroupsSecurityGroupInstanceResponse(securityGroup);
            DescribeSecurityGroupAttributeResponseBody securityGroupRuleInstance = getSecurityGroupRuleInstance(ecsClient, securityGroup.securityGroupId);
            securityGroupsSecurityGroupInstanceResponse.setRule(securityGroupRuleInstance);
            return securityGroupsSecurityGroupInstanceResponse;
        }).toList();
    }

    /**
     * 获取安全组实例列表
     *
     * @param request 请求对象
     * @return 安全组实例列表
     */
    public static List<DescribeSecurityGroupsResponseBody.DescribeSecurityGroupsResponseBodySecurityGroupsSecurityGroup> listSecurityGroupInstance(ListSecurityGroupInstanceRequest request) {
        Client ecsClient = request.getCredential().getEcsClient(request.regionId);
        request.setPageNumber(PageUtil.DefaultCurrentPage);
        request.setPageSize(PageUtil.DefaultPageSize);
        return PageUtil.page(request, req -> {
                    try {
                        return ecsClient.describeSecurityGroupsWithOptions(request, new RuntimeOptions());
                    } catch (Exception e) {
                        ReTryException.throwReTry(e);
                        SkipPageException.throwSkip(e);
                        throw new Fit2cloudException(10002, "获取阿里云安全组实例失败" + e.getMessage());
                    }
                }
                , res -> res.getBody().getSecurityGroups().securityGroup
                , (req, res) -> res.getBody().getPageSize() <= res.getBody().getSecurityGroups().securityGroup.size()
                , req -> req.setPageNumber(req.getPageNumber() + 1), ProviderConstants.retryNum);
    }

    /**
     * 获取安全组规则实例
     *
     * @param request 请对象
     * @return 安全组规则
     */
    public static DescribeSecurityGroupAttributeResponseBody getSecurityGroupRuleInstance(GetSecurityGroupRuleInstanceRequest request) {
        Client ecsClient = request.getCredential().getEcsClient(request.getRegionId());
        return getSecurityGroupRuleInstance(ecsClient, request.getSecurityGroupId());
    }

    /**
     * 获取安全组规则实例
     *
     * @param ecsClient       客户端换
     * @param securityGroupId 安全组id
     * @return 安全组规则对象
     */
    private static DescribeSecurityGroupAttributeResponseBody getSecurityGroupRuleInstance(Client ecsClient, String securityGroupId) {
        DescribeSecurityGroupAttributeRequest describeSecurityGroupAttributeRequest = new DescribeSecurityGroupAttributeRequest();
        describeSecurityGroupAttributeRequest.setSecurityGroupId(securityGroupId);
        describeSecurityGroupAttributeRequest.setRegionId(ecsClient._regionId);
        DescribeSecurityGroupAttributeResponse describeSecurityGroupAttributeResponse = PageUtil.reTry(() -> {
            try {
                return ecsClient.describeSecurityGroupAttributeWithOptions(describeSecurityGroupAttributeRequest, new RuntimeOptions());
            } catch (Exception e) {
                ReTryException.throwReTry(e);
                SkipPageException.throwSkip(e);
                throw new Fit2cloudException(10002, "获取阿里云安全组实例失败" + e.getMessage());
            }
        }, ProviderConstants.retryNum);
        assert describeSecurityGroupAttributeResponse != null;
        return describeSecurityGroupAttributeResponse.getBody();

    }
}
