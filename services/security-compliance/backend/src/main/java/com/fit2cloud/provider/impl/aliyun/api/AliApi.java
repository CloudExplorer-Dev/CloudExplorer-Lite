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
import com.aliyun.sdk.gateway.oss.exception.OSSServerException;
import com.aliyun.sdk.service.oss20190517.AsyncClient;
import com.aliyun.sdk.service.oss20190517.models.*;
import com.aliyun.slb20140515.models.DescribeLoadBalancersResponseBody;
import com.aliyun.teautil.models.RuntimeOptions;
import com.aliyun.vpc20160428.models.DescribeEipAddressesResponseBody;
import com.aliyun.vpc20160428.models.DescribeVpcsResponseBody;
import com.fit2cloud.common.exception.Fit2cloudException;
import com.fit2cloud.common.provider.exception.ReTryException;
import com.fit2cloud.common.provider.exception.SkipPageException;
import com.fit2cloud.common.provider.util.PageUtil;
import com.fit2cloud.provider.constants.ProviderConstants;
import com.fit2cloud.provider.impl.aliyun.entity.credential.AliSecurityComplianceCredential;
import com.fit2cloud.provider.impl.aliyun.entity.request.*;
import com.fit2cloud.provider.util.ResourceUtil;
import jodd.util.StringUtil;
import org.openstack4j.model.heat.Resource;
import org.springframework.beans.BeanUtils;

import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

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

    /**
     * 获取 云数据库 redis 实例列表合集
     *
     * @param request 请求对象
     * @return 阿里云redis redisNetwork 实例列表
     */
    public static List<Map<String, Object>> listRedisInstanceCollection(ListRedisInstanceRequest request) {
        List<com.aliyun.r_kvstore20150101.models.DescribeInstancesResponseBody.DescribeInstancesResponseBodyInstancesKVStoreInstance> describeInstancesResponseBodyInstancesKVStoreInstances = listRedisInstance(request);
        com.aliyun.r_kvstore20150101.Client rdsClient = request.getCredential().getRedisClient(request.getRegionId());
        return describeInstancesResponseBodyInstancesKVStoreInstances.stream().map(redis -> {
            Map<String, Object> redisMap = ResourceUtil.objectsToMap(redis);
            com.aliyun.r_kvstore20150101.models.DescribeDBInstanceNetInfoResponseBody redisNetInfoInstance = getRedisNetInfoInstance(rdsClient, redis.getInstanceId());
            redisMap.put("network", redisNetInfoInstance);
            return redisMap;
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
    public static List<Map<String, Object>> listMongoDBInstanceCollection(ListMongoDBRequest request) {
        List<DescribeDBInstancesResponseBody.DescribeDBInstancesResponseBodyDBInstancesDBInstance> instances = listMongoDBInstance(request);
        com.aliyun.dds20151201.Client mongodbClient = request.getCredential().getMongodbClient(request.getRegionId());
        return instances.stream().map(mongo -> {
            Map<String, Object> mongoMap = ResourceUtil.objectsToMap(mongo);
            DescribeShardingNetworkAddressResponseBody mongodbNetInfoInstance = getMongodbNetInfoInstance(mongodbClient, mongo.DBInstanceId);
            mongoMap.put("networkObj", mongodbNetInfoInstance);
            return mongoMap;
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
    public static List<Map<String, Object>> listMysqlInstanceCollection(ListRdsInstanceRequest request) {
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
    public static List<Map<String, Object>> listSqlServerInstanceCollection(ListRdsInstanceRequest request) {
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
    public static List<Map<String, Object>> listPostgreSqlInstanceCollection(ListRdsInstanceRequest request) {
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
    public static List<Map<String, Object>> listMariaDBInstanceCollection(ListRdsInstanceRequest request) {
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
    public static List<Map<String, Object>> listRdsInstanceCollection(ListRdsInstanceRequest request) {
        List<com.aliyun.rds20140815.models.DescribeDBInstancesResponseBody.DescribeDBInstancesResponseBodyItemsDBInstance> instances = listRdsInstance(request);
        com.aliyun.rds20140815.Client rdsClient = request.getCredential().getRdsClient(request.getRegionId());
        return instances.stream().map(rds -> {
            Map<String, Object> rdsMap = ResourceUtil.objectsToMap(rds);
            DescribeDBInstanceNetInfoResponseBody rdsNetInfoInstance = getRdsNetInfoInstance(rdsClient, rds.DBInstanceId);
            rdsMap.put("networkObj", rdsNetInfoInstance);
            return rdsMap;
        }).toList();
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
    public static List<Map<String, Object>> listBucketCollectionInstance(ListBucketInstanceRequest request) {
        List<Bucket> buckets = listBucket(request.getCredential());
        return buckets.stream().map(bucket -> {
            AsyncClient ossClient = request.getCredential().getOssClient(bucket.getRegion());
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
            Map<String, Object> bucketMap = ResourceUtil.objectToMap(bucket);
            bucketMap.put("acl", aclResponse);
            bucketMap.put("referer", refererResponse);
            bucketMap.put("encryption", encryptionResponse);
            return bucketMap;
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
        ListBucketsRequest listBucketsRequest = ListBucketsRequest.builder().maxKeys(100l).build();
        return PageUtil.pageNextF(listBucketsRequest,
                ossClient::listBuckets,
                res -> res.join().getBody().getBuckets(),
                (req, res) -> StringUtil.isNotEmpty(res.join().getBody().getNextMarker()),
                (req, res) -> ListBucketsRequest.builder().marker(res.join().getBody().getNextMarker()).maxKeys(100l).build());

    }


    /**
     * 获取 安全组与安全组规则实例合集列表
     *
     * @param request 请求对象
     * @return 安全组和安全组规则实例合集列表
     */
    public static List<Map<String, Object>> listSecurityGroupCollectionInstance(ListSecurityGroupInstanceRequest request) {
        List<DescribeSecurityGroupsResponseBody.DescribeSecurityGroupsResponseBodySecurityGroupsSecurityGroup> securityGroups = listSecurityGroupInstance(request);
        Client ecsClient = request.getCredential().getEcsClient(request.regionId);
        return securityGroups.stream().map(securityGroup -> {
            Map<String, Object> securityGroupMap = ResourceUtil.objectToMap(securityGroup);
            DescribeSecurityGroupAttributeResponseBody securityGroupRuleInstance = getSecurityGroupRuleInstance(ecsClient, securityGroup.securityGroupId);
            securityGroupMap.put("rule", securityGroupRuleInstance);
            return securityGroupMap;
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
