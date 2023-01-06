package com.fit2cloud.provider.impl.tencent.api;

import com.fit2cloud.common.provider.exception.ReTryException;
import com.fit2cloud.common.provider.util.PageUtil;
import com.fit2cloud.provider.impl.tencent.entity.request.*;
import com.tencentcloudapi.cbs.v20170312.CbsClient;
import com.tencentcloudapi.cbs.v20170312.models.DescribeDisksRequest;
import com.tencentcloudapi.cbs.v20170312.models.Disk;
import com.tencentcloudapi.cdb.v20170320.CdbClient;
import com.tencentcloudapi.cdb.v20170320.models.InstanceInfo;
import com.tencentcloudapi.clb.v20180317.ClbClient;
import com.tencentcloudapi.clb.v20180317.models.DescribeLoadBalancersRequest;
import com.tencentcloudapi.clb.v20180317.models.LoadBalancer;
import com.tencentcloudapi.cvm.v20170312.CvmClient;
import com.tencentcloudapi.cvm.v20170312.models.DescribeInstancesRequest;
import com.tencentcloudapi.cvm.v20170312.models.Instance;
import com.tencentcloudapi.es.v20180416.EsClient;
import com.tencentcloudapi.mariadb.v20170312.MariadbClient;
import com.tencentcloudapi.mongodb.v20190725.MongodbClient;
import com.tencentcloudapi.postgres.v20170312.PostgresClient;
import com.tencentcloudapi.redis.v20180412.RedisClient;
import com.tencentcloudapi.redis.v20180412.models.InstanceSet;
import com.tencentcloudapi.sqlserver.v20180328.SqlserverClient;
import com.tencentcloudapi.sqlserver.v20180328.models.DBInstance;
import com.tencentcloudapi.vpc.v20170312.VpcClient;
import com.tencentcloudapi.vpc.v20170312.models.DescribeNetworkInterfacesRequest;
import com.tencentcloudapi.vpc.v20170312.models.DescribeVpcsRequest;
import com.tencentcloudapi.vpc.v20170312.models.NetworkInterface;
import com.tencentcloudapi.vpc.v20170312.models.Vpc;
import org.springframework.beans.BeanUtils;
import com.tencentcloudapi.mongodb.v20190725.models.*;

import java.util.Arrays;
import java.util.List;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2022/12/6  12:01}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
public class TencentApi {
    /**
     * 获取腾讯云服务器实例列表
     *
     * @param request 请求对象
     * @return 腾讯云服务器实例列表
     */
    public static List<Instance> listEcsInstance(ListCvmInstanceRequest request) {
        CvmClient cvmClient = request.getCredential().getCvmClient(request.getRegionId());
        request.setOffset(PageUtil.DefaultCurrentPage.longValue() - 1);
        request.setLimit(PageUtil.DefaultPageSize.longValue());
        return PageUtil.page(request,
                req -> {
                    try {
                        DescribeInstancesRequest describeInstancesRequest = new DescribeInstancesRequest();
                        BeanUtils.copyProperties(req, describeInstancesRequest);
                        return cvmClient.DescribeInstances(describeInstancesRequest);
                    } catch (Exception e) {
                        ReTryException.throwReTry(e);
                        throw new RuntimeException(e);
                    }
                },
                res -> Arrays.stream(res.getInstanceSet()).toList(),
                (req, res) -> req.getLimit() <= res.getInstanceSet().length,
                req -> req.setOffset(req.getOffset() + req.getLimit()));

    }

    /**
     * 获取腾讯云 redis实例列表
     *
     * @param request 请求对象
     * @return redis实例列表
     */
    public static List<InstanceSet> listRedisInstance(ListRedisInstanceRequest request) {
        RedisClient redisClient = request.getCredential().getRedisClient(request.getRegionId());
        request.setOffset(PageUtil.DefaultCurrentPage.longValue() - 1);
        request.setLimit(PageUtil.DefaultPageSize.longValue());
        return PageUtil.page(request,
                req -> {
                    try {
                        com.tencentcloudapi.redis.v20180412.models.DescribeInstancesRequest describeInstancesRequest = new com.tencentcloudapi.redis.v20180412.models.DescribeInstancesRequest();
                        BeanUtils.copyProperties(req, describeInstancesRequest);
                        return redisClient.DescribeInstances(describeInstancesRequest);
                    } catch (Exception e) {
                        ReTryException.throwReTry(e);
                        throw new RuntimeException(e);
                    }
                },
                res -> Arrays.stream(res.getInstanceSet()).toList(),
                (req, res) -> req.getLimit() <= res.getInstanceSet().length,
                req -> req.setOffset(req.getOffset() + req.getLimit()));
    }

    /**
     * 获取腾讯云 mongodb 实例列表
     *
     * @param request 请求对象
     * @return mongodb实例列表
     */
    public static List<InstanceDetail> listMongodbInstance(ListMongoDBInstanceRequest request) {
        MongodbClient mongodbClient = request.getCredential().getMongodbClient(request.getRegionId());
        request.setOffset(PageUtil.DefaultCurrentPage.longValue() - 1);
        request.setLimit(PageUtil.DefaultPageSize.longValue());
        return PageUtil.page(request,
                req -> {
                    try {
                        DescribeDBInstancesRequest describeDBInstancesRequest = new DescribeDBInstancesRequest();
                        BeanUtils.copyProperties(req, describeDBInstancesRequest);
                        return mongodbClient.DescribeDBInstances(describeDBInstancesRequest);
                    } catch (Exception e) {
                        ReTryException.throwReTry(e);
                        throw new RuntimeException(e);
                    }
                },
                res -> Arrays.stream(res.getInstanceDetails()).toList(),
                (req, res) -> req.getLimit() <= res.getInstanceDetails().length,
                req -> req.setOffset(req.getOffset() + req.getLimit()));

    }

    /**
     * 获取mysql 实例列表
     *
     * @param request 请求对象
     * @return mysql实例列表
     */
    public static List<InstanceInfo> listMysqlInstance(ListMysqlInstanceRequest request) {
        CdbClient cdbClient = request.getCredential().getCdbClient(request.getRegionId());
        request.setOffset(PageUtil.DefaultCurrentPage.longValue() - 1);
        request.setLimit(PageUtil.DefaultPageSize.longValue());
        return PageUtil.page(request,
                req -> {
                    try {
                        com.tencentcloudapi.cdb.v20170320.models.DescribeDBInstancesRequest describeDBInstancesRequest =
                                new com.tencentcloudapi.cdb.v20170320.models.DescribeDBInstancesRequest();
                        BeanUtils.copyProperties(req, describeDBInstancesRequest);
                        return cdbClient.DescribeDBInstances(describeDBInstancesRequest);
                    } catch (Exception e) {
                        ReTryException.throwReTry(e);
                        throw new RuntimeException(e);
                    }
                },
                res -> Arrays.stream(res.getItems()).toList(),
                (req, res) -> req.getLimit() <= res.getItems().length,
                req -> req.setOffset(req.getOffset() + req.getLimit()));
    }

    /**
     * 获取sqlServer 实例列表
     *
     * @param request 请求对象
     * @return sqlServer实例列表
     */
    public static List<DBInstance> listSqlServerInstance(ListSqlServerInstanceRequest request) {
        SqlserverClient sqlServerClient = request.getCredential().getSqlServerClient(request.getRegionId());
        request.setOffset(PageUtil.DefaultCurrentPage.longValue() - 1);
        request.setLimit(PageUtil.DefaultPageSize.longValue());
        return PageUtil.page(request,
                req -> {
                    try {
                        com.tencentcloudapi.sqlserver.v20180328.models.DescribeDBInstancesRequest describeDBInstancesRequest = new com.tencentcloudapi.sqlserver.v20180328.models.DescribeDBInstancesRequest();
                        BeanUtils.copyProperties(req, describeDBInstancesRequest);
                        return sqlServerClient.DescribeDBInstances(describeDBInstancesRequest);
                    } catch (Exception e) {
                        ReTryException.throwReTry(e);
                        throw new RuntimeException(e);
                    }
                },
                res -> Arrays.stream(res.getDBInstances()).toList(),
                (req, res) -> req.getLimit() <= res.getDBInstances().length,
                req -> req.setOffset(req.getOffset() + req.getLimit()));
    }

    /**
     * 获取 postgresSql实例列表
     *
     * @param request 请求对象
     * @return PostGreSql 实例列表
     */
    public static List<com.tencentcloudapi.postgres.v20170312.models.DBInstance> listPostGreSqlInstance(ListPostGreSqlInstanceRequest request) {
        PostgresClient postgresClient = request.getCredential().getPostgresClient(request.getRegionId());
        request.setOffset(PageUtil.DefaultCurrentPage.longValue() - 1);
        request.setLimit(PageUtil.DefaultPageSize.longValue());
        return PageUtil.page(request,
                req -> {
                    try {
                        com.tencentcloudapi.postgres.v20170312.models.DescribeDBInstancesRequest describeDBInstancesRequest = new com.tencentcloudapi.postgres.v20170312.models.DescribeDBInstancesRequest();
                        BeanUtils.copyProperties(req, describeDBInstancesRequest);
                        return postgresClient.DescribeDBInstances(describeDBInstancesRequest);
                    } catch (Exception e) {
                        ReTryException.throwReTry(e);
                        throw new RuntimeException(e);
                    }
                },
                res -> Arrays.stream(res.getDBInstanceSet()).toList(),
                (req, res) -> req.getLimit() <= res.getDBInstanceSet().length,
                req -> req.setOffset(req.getOffset() + req.getLimit()));
    }

    /**
     * 获取 MariaDB 实例列表
     *
     * @param request 请求对象
     * @return MariaDB 实例列表
     */
    public static List<com.tencentcloudapi.mariadb.v20170312.models.DBInstance> listMariaDBInstance(ListMariaDBInstanceRequest request) {
        MariadbClient mariadbClient = request.getCredential().getMariadbClient(request.getRegionId());
        request.setOffset(PageUtil.DefaultCurrentPage.longValue() - 1);
        request.setLimit(PageUtil.DefaultPageSize.longValue());
        return PageUtil.page(request,
                req -> {
                    try {
                        com.tencentcloudapi.mariadb.v20170312.models.DescribeDBInstancesRequest describeDBInstancesRequest = new com.tencentcloudapi.mariadb.v20170312.models.DescribeDBInstancesRequest();
                        BeanUtils.copyProperties(req, describeDBInstancesRequest);
                        return mariadbClient.DescribeDBInstances(describeDBInstancesRequest);
                    } catch (Exception e) {
                        ReTryException.throwReTry(e);
                        throw new RuntimeException(e);
                    }
                },
                res -> Arrays.stream(res.getInstances()).toList(),
                (req, res) -> req.getLimit() <= res.getInstances().length,
                req -> req.setOffset(req.getOffset() + req.getLimit()));
    }


    /**
     * 获取 Elasticsearch 实例列表
     *
     * @param request 请求对象
     * @return elasticsearch 实例列表
     */
    public static List<com.tencentcloudapi.es.v20180416.models.InstanceInfo> listElasticSearchInstance(ListElasticsearchInstanceRequest request) {
        EsClient esClient = request.getCredential().getEsClient(request.getRegionId());
        request.setOffset(PageUtil.DefaultCurrentPage.longValue() - 1);
        request.setLimit(PageUtil.DefaultPageSize.longValue());
        return PageUtil.page(request,
                req -> {
                    try {
                        com.tencentcloudapi.es.v20180416.models.DescribeInstancesRequest describeInstancesRequest = new com.tencentcloudapi.es.v20180416.models.DescribeInstancesRequest();
                        BeanUtils.copyProperties(req, describeInstancesRequest);
                        return esClient.DescribeInstances(describeInstancesRequest);
                    } catch (Exception e) {
                        ReTryException.throwReTry(e);
                        throw new RuntimeException(e);
                    }
                },
                res -> Arrays.stream(res.getInstanceList()).toList(),
                (req, res) -> req.getLimit() <= res.getInstanceList().length,
                req -> req.setOffset(req.getOffset() + req.getLimit()));

    }

    /**
     * 获取 云磁盘 实例列表
     *
     * @param request 请求对象
     * @return 磁盘实例列表
     */
    public static List<Disk> listDiskInstance(ListDiskInstanceRequest request) {
        CbsClient cbsClient = request.getCredential().getCbsClient(request.getRegionId());
        request.setOffset(PageUtil.DefaultCurrentPage.longValue() - 1);
        request.setLimit(PageUtil.DefaultPageSize.longValue());
        return PageUtil.page(request,
                req -> {
                    try {
                        DescribeDisksRequest describeInstancesRequest = new DescribeDisksRequest();
                        BeanUtils.copyProperties(req, describeInstancesRequest);
                        return cbsClient.DescribeDisks(describeInstancesRequest);
                    } catch (Exception e) {
                        ReTryException.throwReTry(e);
                        throw new RuntimeException(e);
                    }
                },
                res -> Arrays.stream(res.getDiskSet()).toList(),
                (req, res) -> req.getLimit() <= res.getDiskSet().length,
                req -> req.setOffset(req.getOffset() + req.getLimit()));
    }

    /**
     * 获取 负载均衡 实例列表
     *
     * @param request 请求对象
     * @return 负载均衡实例列表
     */
    public static List<LoadBalancer> listLoadBalancerInstance(ListLoadBalancerInstanceRequest request) {
        ClbClient clbClient = request.getCredential().getClbClient(request.getRegionId());
        request.setOffset(PageUtil.DefaultCurrentPage.longValue() - 1);
        request.setLimit(PageUtil.DefaultPageSize.longValue());
        return PageUtil.page(request,
                req -> {
                    try {
                        DescribeLoadBalancersRequest describeInstancesRequest = new DescribeLoadBalancersRequest();
                        BeanUtils.copyProperties(req, describeInstancesRequest);
                        return clbClient.DescribeLoadBalancers(describeInstancesRequest);
                    } catch (Exception e) {
                        ReTryException.throwReTry(e);
                        throw new RuntimeException(e);
                    }
                },
                res -> Arrays.stream(res.getLoadBalancerSet()).toList(),
                (req, res) -> req.getLimit() <= res.getLoadBalancerSet().length,
                req -> req.setOffset(req.getOffset() + req.getLimit()));
    }

    /**
     * 获取 弹性公网ip 实例列表
     *
     * @param request 请求对象
     * @return 弹性公网ip实例列表
     */
    public static List<NetworkInterface> listPublicIpInstance(ListPublicIpInstanceRequest request) {
        VpcClient vpcClient = request.getCredential().getVpcClient(request.getRegionId());
        request.setOffset(PageUtil.DefaultCurrentPage.longValue() - 1);
        request.setLimit(PageUtil.DefaultPageSize.longValue());
        return PageUtil.page(request,
                req -> {
                    try {
                        DescribeNetworkInterfacesRequest describeInstancesRequest = new DescribeNetworkInterfacesRequest();
                        BeanUtils.copyProperties(req, describeInstancesRequest);
                        return vpcClient.DescribeNetworkInterfaces(describeInstancesRequest);
                    } catch (Exception e) {
                        ReTryException.throwReTry(e);
                        throw new RuntimeException(e);
                    }
                },
                res -> Arrays.stream(res.getNetworkInterfaceSet()).toList(),
                (req, res) -> req.getLimit() <= res.getNetworkInterfaceSet().length,
                req -> req.setOffset(req.getOffset() + req.getLimit()));
    }

    /**
     * 获取 vpc 实例列表
     *
     * @param request 请求对象
     * @return vpc 实例列表
     */
    public static List<Vpc> listVpcInstance(ListVpcInstanceRequest request) {
        VpcClient vpcClient = request.getCredential().getVpcClient(request.getRegionId());
        request.setOffset(String.valueOf(PageUtil.DefaultCurrentPage.longValue() - 1));
        request.setLimit(String.valueOf(PageUtil.DefaultPageSize.longValue()));
        return PageUtil.page(request,
                req -> {
                    try {
                        DescribeVpcsRequest describeInstancesRequest = new DescribeVpcsRequest();
                        BeanUtils.copyProperties(req, describeInstancesRequest);
                        return vpcClient.DescribeVpcs(describeInstancesRequest);
                    } catch (Exception e) {
                        ReTryException.throwReTry(e);
                        throw new RuntimeException(e);
                    }
                },
                res -> Arrays.stream(res.getVpcSet()).toList(),
                (req, res) -> Integer.parseInt(req.getLimit()) <= res.getVpcSet().length,
                req -> req.setOffset(req.getOffset() + req.getLimit()));

    }
}
