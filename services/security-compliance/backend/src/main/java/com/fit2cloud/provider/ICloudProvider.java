package com.fit2cloud.provider;

import com.fit2cloud.constants.ResourceTypeConstants;
import com.fit2cloud.constants.SyncDimensionConstants;
import com.fit2cloud.es.entity.ResourceInstance;
import com.fit2cloud.provider.constants.ProviderConstants;
import com.fit2cloud.provider.entity.InstanceSearchField;
import com.vmware.vcenter.ResourcePool;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2022/12/5  15:41}
 * {@code @Version 1.0}
 * {@code @注释: todo 查询字段的获取未来存储到数据库,由程序进行增删改查。}
 */
public interface ICloudProvider {

    /**
     * 获取云的资源同步粒度
     *
     * @return 同步粒度表
     */
    Map<ResourceTypeConstants, SyncDimensionConstants> getResourceSyncDimensionConstants();

    /**
     * 获取云服务器列表
     *
     * @param req 请求参数 包括认证对象
     * @return 云服务器列表
     */
    List<ResourceInstance> listEcsInstance(String req);

    /**
     * 获取实例可查询字段
     *
     * @return 实例可查询字段
     */
    List<InstanceSearchField> listEcsInstanceSearchField();

    /**
     * 获取云数据库redis实例
     *
     * @param req 请求对象
     * @return 云数据库redis相关数据
     */
    List<ResourceInstance> listRedisInstance(String req);

    /**
     * 获取云数据库redis可查询字段
     *
     * @return 实例可查询字段
     */
    List<InstanceSearchField> listRedisInstanceSearchField();

    /**
     * 获取云数据库 mongodb实例
     *
     * @param req 请求对象
     * @return 云数据库Mongodb相关数据
     */
    List<ResourceInstance> listMongodbInstance(String req);

    /**
     * 获取云数据库Mongodb可查询字段
     *
     * @return mongodb实例可查询字段
     */
    List<InstanceSearchField> listMongodbInstanceSearchField();

    /**
     * 获取云数据库 mysql实例
     *
     * @param req 请求对象
     * @return 云数据库mysql相关数据
     */
    List<ResourceInstance> listMysqlInstance(String req);

    /**
     * 获取云数据库mysql可查询字段
     *
     * @return mysql实例可查询字段
     */
    List<InstanceSearchField> listMysqlInstanceSearchField();

    /**
     * 获取云数据库 sqlServer实例列表
     *
     * @param req 请求对象
     * @return 云数据库sqlserver 实力列表
     */
    List<ResourceInstance> listSqlServerInstance(String req);

    /**
     * 获取云数据库 SqlServer可查询字段
     *
     * @return sqlserver 可查询字段
     */
    List<InstanceSearchField> listSqlServerInstanceSearchField();

    /**
     * 获取 PostGreSql 实例列表
     *
     * @param req 请求对象
     * @return 云数据库 PostGreSql 实例列表
     */
    List<ResourceInstance> listPostGreSqlInstance(String req);

    /**
     * 获取云数据库 PostGreSql可查询字段
     *
     * @return PostGreSql 可查询字段
     */
    List<InstanceSearchField> listPostGreSqlInstanceSearchField();

    /**
     * 获取 MariaDB 实例列表
     *
     * @param req 请求对象
     * @return 云数据库 MariaDB 实例列表
     */
    List<ResourceInstance> listMariaDBInstance(String req);

    /**
     * 获取云数据库 MariaDB 可查询字段
     *
     * @return MariaDB 可查询字段
     */
    List<InstanceSearchField> listMariaDBInstanceSearchField();

    /**
     * 获取 Elasticsearch 实例列表
     *
     * @param req 请求对象
     * @return 云数据库 elasticsearch 实例列表
     */
    List<ResourceInstance> listElasticSearchInstance(String req);

    /**
     * 获取 云数据库 ElasticSearch 可查询字段
     *
     * @return ElasticSearch可查询字段
     */
    List<InstanceSearchField> listElasticSearchInstanceSearchField();

    /**
     * 获取 磁盘 实例列表
     *
     * @param req 请求对象
     * @return 磁盘实例列表
     */
    List<ResourceInstance> listDiskInstance(String req);

    /**
     * 获取磁盘可查询字段
     *
     * @return 磁盘可查询字段
     */
    List<InstanceSearchField> listDiskInstanceSearchField();

    /**
     * 获取负载均衡实例列表
     *
     * @param req 请求对象
     * @return 负载均衡实例列表
     */
    List<ResourceInstance> listLoadBalancerInstance(String req);

    /**
     * 获取负载均衡实例可查询字段
     *
     * @return 负载均衡可查询字段
     */
    List<InstanceSearchField> listLoadBalancerInstanceSearchField();

    /**
     * 获取公网ip 实例列表
     *
     * @param req 请求对象
     * @return 公网ip 实例列表
     */
    List<ResourceInstance> listPublicIpInstance(String req);

    /**
     * 获取公网ip实例可查询字段
     *
     * @return 公网ip可查询字段
     */
    List<InstanceSearchField> listPublicIpInstanceSearchField();

    /**
     * 获取 vpc 资源实例列表
     *
     * @param req 请求对象
     * @return vpc 资源实例列表
     */
    List<ResourceInstance> listVpcInstance(String req);

    /**
     * 获取 vpc 资源实例可查询字段
     *
     * @return vpc 资源实例可查询字段
     */
    List<InstanceSearchField> listVpcInstanceSearchField();

    /**
     * 查询所有RAM用户的详细信息
     *
     * @param req 请求对象
     * @return RAM用户实例列表
     */
    List<ResourceInstance> listRamInstance(String req);

    /**
     * 获取Ram 用户实例可查询字段
     *
     * @return ram 实例可查询字段
     */
    List<InstanceSearchField> listRamInstanceSearchField();


    /**
     * 获取存储桶 实例列表
     *
     * @param req 请求对象
     * @return 存储桶实例列表
     */
    List<ResourceInstance> listBucketInstance(String req);

    /**
     * 获取存储桶实例可查询字段
     *
     * @return 存储桶实例可查询字段
     */
    List<InstanceSearchField> listBucketInstanceSearchField();

    /**
     * 获取 安全组实例列表
     *
     * @param req 请对象
     * @return 安全组实力列表
     */
    List<ResourceInstance> listSecurityGroupInstance(String req);

    /**
     * 获取安全组实例可查询字段
     *
     * @return 安全组实例可查询字段
     */
    List<InstanceSearchField> listSecurityGroupInstanceSearchField();

    /**
     * 获取宿主机列表
     *
     * @param req 请求对象
     * @return 宿主机列表
     */
    List<ResourceInstance> listHostInstance(String req);

    /**
     * 获取宿主机实例可查询字段
     *
     * @return 宿主机实例可查询字段
     */
    List<InstanceSearchField> listHostInstanceSearchField();

    /**
     * 获取存储器实例列表
     *
     * @param req 请求对象
     * @return 存储器实例列表
     */
    List<ResourceInstance> listDataStoreInstance(String req);

    /**
     * 获取存储器可查询字段
     *
     * @return 存储器可查询字段
     */
    List<InstanceSearchField> listDataStoreInstanceSearchField();

    /**
     * 获取资源池实例列表
     *
     * @return 资源池实例列表
     */
    List<ResourceInstance> listResourcePoolInstance(String req);

    /**
     * 获取资源池可查询字段
     *
     * @return 资源池可查询字段
     */
    List<InstanceSearchField> listResourcePoolInstanceSearchField();

    /**
     * 根据供应商获取对应云平台处理器
     *
     * @param platform 供应商
     * @return 处理器
     */
    static Class<? extends ICloudProvider> of(String platform) {
        return (Class<? extends ICloudProvider>) Arrays.stream(ProviderConstants.values()).filter(providerConstants -> providerConstants.name().equals(platform)).findFirst().orElseThrow(() -> new RuntimeException("不支持的云平台")).getCloudProvider();
    }
}
