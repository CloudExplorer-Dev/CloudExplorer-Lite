package com.fit2cloud.constants;

import com.fit2cloud.es.entity.ResourceInstance;
import com.fit2cloud.provider.ICloudProvider;
import com.fit2cloud.provider.entity.InstanceSearchField;
import io.reactivex.rxjava3.functions.BiFunction;
import org.redisson.connection.balancer.LoadBalancer;

import java.util.List;
import java.util.function.Function;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2022/12/6  14:03}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
public enum ResourceTypeConstants {
    /**
     * 云服务器
     */
    ECS("云服务器", ICloudProvider::listEcsInstance, ICloudProvider::listEcsInstanceSearchField),
    /**
     * redis
     */
    REDIS("云数据库-Redis", ICloudProvider::listRedisInstance, ICloudProvider::listRedisInstanceSearchField),
    /**
     * mongodb
     */
    MONGO_DB("云数据库-Mongodb", ICloudProvider::listMongodbInstance, ICloudProvider::listMongodbInstanceSearchField),
    /**
     * mysql
     */
    MYSQL("云数据库-Mysql", ICloudProvider::listMysqlInstance, ICloudProvider::listMysqlInstanceSearchField),
    /**
     * sqlServer
     */
    SQL_SERVER("云数据库-SqlServer", ICloudProvider::listSqlServerInstance, ICloudProvider::listSqlServerInstanceSearchField),
    /**
     * PostGreSql
     */
    POST_GRE_SQL("云数据库-PostGreSql", ICloudProvider::listPostGreSqlInstance, ICloudProvider::listPostGreSqlInstanceSearchField),
    /**
     * MariaDB
     */
    MARIA_DB("云数据库-MariaDB", ICloudProvider::listMariaDBInstance, ICloudProvider::listMariaDBInstanceSearchField),

    /**
     * elasticsearch
     */
    ELASTIC_SEARCH("云数据库-Elasticsearch", ICloudProvider::listElasticSearchInstance, ICloudProvider::listElasticSearchInstanceSearchField),
    /**
     * 云磁盘
     */
    DISK("云磁盘", ICloudProvider::listDiskInstance, ICloudProvider::listDiskInstanceSearchField),

    /**
     * 负载均衡
     */
    LOAD_BALANCER("负载均衡", ICloudProvider::listLoadBalancerInstance, ICloudProvider::listLoadBalancerInstanceSearchField),
    /**
     * 公网ip
     */
    PUBLIC_IP("公网IP", ICloudProvider::listPublicIpInstance, ICloudProvider::listPublicIpInstanceSearchField),

    /**
     * vpc
     */
    VPC("VPC", ICloudProvider::listVpcInstance, ICloudProvider::listVpcInstanceSearchField),

    /**
     * RAM
     */
    RAM("RAM用户", ICloudProvider::listRamInstance, ICloudProvider::listRamInstanceSearchField),

    /**
     * 对象存储
     */
    OSS("对象存储", ICloudProvider::listBucketInstance, ICloudProvider::listBucketInstanceSearchField),

    /**
     * 安全组
     */
    SECURITY_GROUP("安全组", ICloudProvider::listSecurityGroupInstance, ICloudProvider::listSecurityGroupInstanceSearchField),
    /**
     * 宿主机
     */
    HOST("宿主机", ICloudProvider::listHostInstance, ICloudProvider::listHostInstanceSearchField),
    /**
     * 存储器
     */
    DATA_STORE("存储器", ICloudProvider::listDataStoreInstance, ICloudProvider::listDataStoreInstanceSearchField),
    /**
     * 资源池
     */
    RESOURCE_POOL("资源池", ICloudProvider::listResourcePoolInstance, ICloudProvider::listResourcePoolInstanceSearchField);
    /**
     * 提示
     */
    private final String message;
    /**
     * 获取资源执行器
     */
    private BiFunction<ICloudProvider, String, List<ResourceInstance>> exec;

    /**
     * 获取查询子弹
     */
    private Function<ICloudProvider, List<InstanceSearchField>> listInstanceSearchField;

    ResourceTypeConstants(String message, BiFunction<ICloudProvider, String, List<ResourceInstance>> exec, Function<ICloudProvider, List<InstanceSearchField>> listInstanceSearchField) {
        this.exec = exec;
        this.listInstanceSearchField = listInstanceSearchField;
        this.message = message;
    }

    public Function<ICloudProvider, List<InstanceSearchField>> getListInstanceSearchField() {
        return listInstanceSearchField;
    }

    public BiFunction<ICloudProvider, String, List<ResourceInstance>> getExec() {
        return exec;
    }

    public String getMessage() {
        return message;
    }
}
