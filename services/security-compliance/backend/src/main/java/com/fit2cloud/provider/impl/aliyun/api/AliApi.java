package com.fit2cloud.provider.impl.aliyun.api;

import com.aliyun.dds20151201.models.DescribeDBInstancesResponseBody;
import com.aliyun.ecs20140526.Client;
import com.aliyun.ecs20140526.models.DescribeDisksResponseBody;
import com.aliyun.ecs20140526.models.DescribeInstancesResponseBody;
import com.aliyun.elasticsearch20170613.models.ListInstanceResponseBody;
import com.aliyun.ram20150501.models.GetLoginProfileRequest;
import com.aliyun.ram20150501.models.GetLoginProfileResponse;
import com.aliyun.ram20150501.models.ListUsersResponseBody;
import com.aliyun.slb20140515.models.DescribeLoadBalancersResponseBody;
import com.aliyun.teautil.models.RuntimeOptions;
import com.aliyun.vpc20160428.models.DescribeEipAddressesResponseBody;
import com.aliyun.vpc20160428.models.DescribeVpcsResponseBody;
import com.fit2cloud.common.exception.Fit2cloudException;
import com.fit2cloud.common.provider.exception.ReTryException;
import com.fit2cloud.common.provider.exception.SkipPageException;
import com.fit2cloud.common.provider.util.PageUtil;
import com.fit2cloud.provider.impl.aliyun.entity.request.*;

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
                , req -> req.setPageNumber(req.getPageNumber() + 1));
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
                , req -> req.setPageNumber(req.getPageNumber() + 1));

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
                , req -> req.setPageNumber(req.getPageNumber() + 1));

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
    public static List<com.aliyun.rds20140815.models.DescribeDBInstancesResponseBody.DescribeDBInstancesResponseBodyItemsDBInstance> listSqlServerInstance(ListRdsInstanceRequest request) {
        request.setEngine("SQLServer");
        return listRdsInstance(request);
    }

    /**
     * 获取阿里云 云数据SQLServer实例列表
     *
     * @param request 请求对象
     * @return 阿里云 云数据库 SQLServer实例列表
     */
    public static List<com.aliyun.rds20140815.models.DescribeDBInstancesResponseBody.DescribeDBInstancesResponseBodyItemsDBInstance> listPostgreSqlInstance(ListRdsInstanceRequest request) {
        request.setEngine("PostgreSQL");
        return listRdsInstance(request);
    }

    /**
     * 获取阿里云 云数据SQLServer实例列表
     *
     * @param request 请求对象
     * @return 阿里云 云数据库 SQLServer实例列表
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
                , (req, res) -> res.getBody().getPageRecordCount() <= res.getBody().getItems().getDBInstance().size()
                , req -> req.setPageNumber(req.getPageNumber() + 1));
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
                , req -> req.setPage(req.getPage() + 1));

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
                , req -> req.setPageNumber(req.getPageNumber() + 1));

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
                , req -> req.setPageNumber(req.getPageNumber() + 1));
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
                        throw new Fit2cloudException(10002, "获取阿里云负载均衡实例失败" + e.getMessage());
                    }
                }
                , res -> res.getBody().getEipAddresses().eipAddress
                , (req, res) -> res.getBody().getPageSize() <= res.getBody().getEipAddresses().eipAddress.size()
                , req -> req.setPageNumber(req.getPageNumber() + 1));
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
        request.setPageSize(PageUtil.DefaultPageSize);
        return PageUtil.page(request, req -> {
                    try {
                        return vpcClient.describeVpcsWithOptions(request, new RuntimeOptions());
                    } catch (Exception e) {
                        ReTryException.throwReTry(e);
                        SkipPageException.throwSkip(e);
                        throw new Fit2cloudException(10002, "获取阿里云负载均衡实例失败" + e.getMessage());
                    }
                }
                , res -> res.getBody().getVpcs().vpc
                , (req, res) -> res.getBody().getPageSize() <= res.getBody().getVpcs().vpc.size()
                , req -> req.setPageNumber(req.getPageNumber() + 1));

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
                        throw new Fit2cloudException(10002, "获取阿里云负载均衡实例失败" + e.getMessage());
                    }
                }
                , res -> res.getBody().getUsers().user
                , (req, res) -> res.getBody().isTruncated
                , (req, res) -> req.setMarker(res.getBody().marker));
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
            }, 5);
        }).filter(Objects::nonNull).toList();

    }
}
