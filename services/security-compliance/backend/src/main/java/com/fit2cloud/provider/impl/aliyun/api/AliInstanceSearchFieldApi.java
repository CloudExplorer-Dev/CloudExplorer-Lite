package com.fit2cloud.provider.impl.aliyun.api;

import com.fit2cloud.provider.entity.InstanceFieldType;
import com.fit2cloud.provider.entity.InstanceSearchField;
import com.fit2cloud.provider.util.FieldUtil;
import io.swagger.models.auth.In;
import org.apache.commons.collections4.keyvalue.DefaultKeyValue;

import java.util.List;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2022/12/7  18:23}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
public class AliInstanceSearchFieldApi {

    public static final String ECS_INSTANCE_PREFIX = "instance.fit2cloud_ali_platform_ECS.";

    public static final String OSS_INSTANCE_PREFIX = "instance.fit2cloud_ali_platform_OSS.";

    public static final String RAM_INSTANCE_PREFIX = "instance.fit2cloud_ali_platform_RAM.";

    public static final String VPC_INSTANCE_PREFIX = "instance.fit2cloud_ali_platform_VPC.";

    public static final String EIP_INSTANCE_PREFIX = "instance.fit2cloud_ali_platform_PUBLIC_IP.";

    public static final String REDIS_INSTANCE_PREFIX = "instance.fit2cloud_ali_platform_REDIS.";

    public static final String MONGO_DB_INSTANCE_PREFIX = "instance.fit2cloud_ali_platform_MONGO_DB.";

    public static final String MYSQL_INSTANCE_PREFIX = "instance.fit2cloud_ali_platform_MYSQL.";

    public static final String SQL_SERVER_INSTANCE_PREFIX = "instance.fit2cloud_ali_platform_SQL_SERVER.";

    public static final String POST_GRE_SQL_INSTANCE_PREFIX = "instance.fit2cloud_ali_platform_POST_GRE_SQL.";

    public static final String MARIA_DB_INSTANCE_PREFIX = "instance.fit2cloud_ali_platform_MARIA_DB.";

    public static final String ELASTIC_SEARCH_INSTANCE_PREFIX = "instance.fit2cloud_ali_platform_ELASTIC_SEARCH.";

    public static final String SECURITY_GROUP_INSTANCE_PREFIX = "instance.fit2cloud_ali_platform_SECURITY_GROUP.";

    public static final String DISK_INSTANCE_PREFIX = "instance.fit2cloud_ali_platform_DISK.";

    public static final String LOADBALANCER_INSTANCE_PREFIX = "instance.fit2cloud_ali_platform_LOAD_BALANCER.";

    public static List<InstanceSearchField> listEcsInstanceSearchField() {
        InstanceSearchField cpu = new InstanceSearchField("cpu", "cpu", InstanceFieldType.Number);

        InstanceSearchField memory = new InstanceSearchField("内存", "memory", InstanceFieldType.Number);

        InstanceSearchField instanceStatus = new InstanceSearchField("实例状态", "status.keyword", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("创建中", "Pending"),
                        new DefaultKeyValue<>("运行中", "Running"),
                        new DefaultKeyValue<>("启动中", "Starting"),
                        new DefaultKeyValue<>("停止中", "Stopping"),
                        new DefaultKeyValue<>("已停止", "Stopped")));

        InstanceSearchField spotStrategy = new InstanceSearchField("竞价策略", "spotStrategy.keyword", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("正常按量付费实例", "NoSpot"),
                        new DefaultKeyValue<>("设置上限价格的抢占式实例", "SpotWithPriceLimit"),
                        new DefaultKeyValue<>("系统自动出价", "SpotAsPriceGo")));

        InstanceSearchField deviceAvailable = new InstanceSearchField("是否可挂载数据盘", "deviceAvailable", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("可挂载数据盘", true),
                        new DefaultKeyValue<>("不可挂载数据盘", false)));

        InstanceSearchField instanceNetworkType = new InstanceSearchField("网络类型", "instanceNetworkType.keyword", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("经典网络", "classic"),
                        new DefaultKeyValue<>("专有网络VPC", "vpc")));

        InstanceSearchField internetMaxBandwidthOut = new InstanceSearchField("公网出带宽最大值", "internetMaxBandwidthOut", InstanceFieldType.Number);

        InstanceSearchField internetMaxBandwidthIn = new InstanceSearchField("公网入带宽最大值", "internetMaxBandwidthIn", InstanceFieldType.Number);

        InstanceSearchField instanceChargeType = new InstanceSearchField("计费方式", "instanceChargeType.keyword", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("包年包月", "PrePaid"),
                        new DefaultKeyValue<>("按量付费", "PostPaid")));

        InstanceSearchField ioOptimized = new InstanceSearchField("是否为I/O优化型实例", "ioOptimized", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("是", true),
                        new DefaultKeyValue<>("否", false)));

        InstanceSearchField internetChargeType = new InstanceSearchField("网络计费类型", "internetChargeType.keyword", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("按固定带宽计费", "PayByBandwidth"),
                        new DefaultKeyValue<>("按使用流量计费", "PayByTraffic")));

        InstanceSearchField recyclable = new InstanceSearchField("是否可以回收", "recyclable", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("是", true),
                        new DefaultKeyValue<>("否", false)));
        InstanceSearchField deletionProtection = new InstanceSearchField("实例释放保护", "deletionProtection", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("已开启实例释放保护", true),
                        new DefaultKeyValue<>("未开启实例释放保护", false)));
        InstanceSearchField stoppedMode = new InstanceSearchField("实例停机后是否继续收费", "stoppedMode.keyword", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("停机后继续收费,继续保留库存资源", "KeepCharging"),
                        new DefaultKeyValue<>("停机后不收费,释放实例对应的资源", "StopCharging"),
                        new DefaultKeyValue<>("不支持停机不收费功能", "Not-applicable")));

        return FieldUtil.appendPrefixField(ECS_INSTANCE_PREFIX,
                List.of(cpu, memory, instanceStatus, spotStrategy, deviceAvailable, instanceNetworkType, internetMaxBandwidthOut,
                        internetMaxBandwidthIn, instanceChargeType, ioOptimized, internetChargeType, recyclable, deletionProtection,
                        stoppedMode
                ));
    }

    public static List<InstanceSearchField> listOSSInstanceSearchField() {
        InstanceSearchField encryption = new InstanceSearchField("加密方式", "encryption.applyServerSideEncryptionByDefault.ssealgorithm.keyword", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("KMS", "KMS"),
                        new DefaultKeyValue<>("AES256", "AES256"),
                        new DefaultKeyValue<>("未加密", null)
                ));
        InstanceSearchField referer = new InstanceSearchField("是否开启防盗链", "referer.refererList.referer.keyword", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("未开启", null)));

        InstanceSearchField refererAllowEmptyReferer = new InstanceSearchField("空Referer", "referer.allowEmptyReferer", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("允许", true),
                        new DefaultKeyValue<>("不允许", false)));

        InstanceSearchField instanceSearchField = new InstanceSearchField("访问控制", "acl.accessControlList.grant.keyword", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("私有", "private"),
                        new DefaultKeyValue<>("公共读", "public-read"),
                        new DefaultKeyValue<>("公共读写", "public-read-write")
                ));

        return FieldUtil.appendPrefixField(OSS_INSTANCE_PREFIX,
                List.of(encryption, instanceSearchField, referer, refererAllowEmptyReferer));
    }

    public static List<InstanceSearchField> listRAMInstanceSearchField() {
        InstanceSearchField loginProfile = new InstanceSearchField("用户是否配置登录配置", "loginProfile.userName.keyword", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("未配置", null)));

        InstanceSearchField loginProfileMfabindRequired = new InstanceSearchField("要求绑定多因素认证设备", "loginProfile.mfabindRequired", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("未开启", false),
                        new DefaultKeyValue<>("开启", true)));

        InstanceSearchField loginProfilePasswordResetRequired = new InstanceSearchField("要求下次登录时重设密码", "loginProfile.passwordResetRequired", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("重设", true),
                        new DefaultKeyValue<>("不重设", false)));

        return FieldUtil.appendPrefixField(RAM_INSTANCE_PREFIX,
                List.of(loginProfileMfabindRequired, loginProfilePasswordResetRequired, loginProfile));
    }

    public static List<InstanceSearchField> listVPCInstanceSearchField() {
        InstanceSearchField status = new InstanceSearchField("VPC状态", "status.keyword", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("配置中", "Pending"), new DefaultKeyValue<>("可用", "Available")));

        InstanceSearchField isDefault = new InstanceSearchField("默认VPC", "isDefault", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("是", true),
                        new DefaultKeyValue<>("不是", false)));
        InstanceSearchField cidrBlock = new InstanceSearchField("ipv4网段", "cidrBlock", InstanceFieldType.String);
        return FieldUtil.appendPrefixField(VPC_INSTANCE_PREFIX,
                List.of(status, isDefault, cidrBlock));
    }

    public static List<InstanceSearchField> listPublicIpInstanceSearchField() {
        InstanceSearchField status = new InstanceSearchField("弹性ip状态", "status.keyword", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("绑定中", "Associating"),
                        new DefaultKeyValue<>("解绑中", "Unassociating"),
                        new DefaultKeyValue<>("已分配", "InUse"),
                        new DefaultKeyValue<>("可用", "Available"),
                        new DefaultKeyValue<>("释放中", "Releasing")));

        InstanceSearchField isDefault = new InstanceSearchField("付费类型", "chargeType.keyword", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("按量计费", "PostPaid"),
                        new DefaultKeyValue<>("包年包月", "PrePaid")));

        InstanceSearchField deletionProtection = new InstanceSearchField("删除保护功能", "deletionProtection", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("是", true),
                        new DefaultKeyValue<>("否", false)));

        InstanceSearchField secondLimited = new InstanceSearchField("是否开启二级限速", "secondLimited", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("是", true),
                        new DefaultKeyValue<>("否", false)));
        InstanceSearchField bandwidth = new InstanceSearchField("带宽峰值", "bandwidth.keyword", InstanceFieldType.Number);
        return FieldUtil.appendPrefixField(EIP_INSTANCE_PREFIX,
                List.of(status, isDefault, deletionProtection, secondLimited, bandwidth));
    }

    public static List<InstanceSearchField> listDiskInstanceSearchField() {
        InstanceSearchField status = new InstanceSearchField("磁盘状态", "status.keyword", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("使用中", "In_use"),
                        new DefaultKeyValue<>("待挂载", "Available"),
                        new DefaultKeyValue<>("已分配", "Attaching"),
                        new DefaultKeyValue<>("卸载中", "Detaching"),
                        new DefaultKeyValue<>("创建中", "Creating"),
                        new DefaultKeyValue<>("初始化中", "ReIniting"),
                        new DefaultKeyValue<>("已删除", "Deleted")));

        InstanceSearchField type = new InstanceSearchField("磁盘类型", "type.keyword", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("系统盘", "system"),
                        new DefaultKeyValue<>("数据盘", "data")));

        InstanceSearchField performanceLevel = new InstanceSearchField("ESSD云盘的性能等级", "performanceLevel.keyword", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("单盘最高随机读写IOPS 1万", "PL0"),
                        new DefaultKeyValue<>("单盘最高随机读写IOPS 5万", "PL1"),
                        new DefaultKeyValue<>("单盘最高随机读写IOPS 10万", "PL2"),
                        new DefaultKeyValue<>("单盘最高随机读写IOPS 100万", "PL3"),
                        new DefaultKeyValue<>("其他", "")));

        InstanceSearchField deleteAutoSnapshot = new InstanceSearchField("是否同时删除自动快照", "deleteAutoSnapshot", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("删除云盘上的快照", true),
                        new DefaultKeyValue<>("保留云盘上的快照", false)));

        InstanceSearchField encrypted = new InstanceSearchField("是否为加密云盘", "encrypted", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("是", true),
                        new DefaultKeyValue<>("否", false)));

        InstanceSearchField iops = new InstanceSearchField("每秒读写（I/O）操作的次数上限", "iops", InstanceFieldType.Number);

        InstanceSearchField size = new InstanceSearchField("云盘或本地盘大小,单位GiB", "size", InstanceFieldType.Number);

        InstanceSearchField diskChargeType = new InstanceSearchField("计费方式", "diskChargeType.keyword", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("包年包月", "PrePaid"),
                        new DefaultKeyValue<>("按量付费", "PostPaid")));

        InstanceSearchField category = new InstanceSearchField("磁盘种类", "category.keyword", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("普通云盘", "cloud"),
                        new DefaultKeyValue<>("高效云盘", "cloud_efficiency"),
                        new DefaultKeyValue<>("SSD盘", "cloud_ssd"),
                        new DefaultKeyValue<>("ESSD云盘", "cloud_essd"),
                        new DefaultKeyValue<>("ESSD AutoPL云盘", "cloud_auto"),
                        new DefaultKeyValue<>("I/O密集型本地盘", "local_ssd_pro"),
                        new DefaultKeyValue<>("吞吐密集型本地盘", "local_hdd_pro"),
                        new DefaultKeyValue<>("本地盘", "ephemeral"),
                        new DefaultKeyValue<>("本地SSD盘", "ephemeral_ssd")));

        InstanceSearchField burstingEnabled = new InstanceSearchField("是否开启Burst(性能突发)", "burstingEnabled", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("是", true),
                        new DefaultKeyValue<>("否", false)));

        return FieldUtil.appendPrefixField(DISK_INSTANCE_PREFIX,
                List.of(status, type, performanceLevel, deleteAutoSnapshot, encrypted, iops, size, diskChargeType, category, burstingEnabled));
    }

    public static List<InstanceSearchField> listRedisInstanceSearchField() {
        InstanceSearchField status = new InstanceSearchField("实例状态", "instanceStatus.keyword", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("使用中", "Normal"),
                        new DefaultKeyValue<>("创建中", "Creating"),
                        new DefaultKeyValue<>("修改中", "Changing"),
                        new DefaultKeyValue<>("被禁用", "Inactive"),
                        new DefaultKeyValue<>("清除中", "Flushing"),
                        new DefaultKeyValue<>("已释放", "Released"),
                        new DefaultKeyValue<>("转换中", "Transforming"),
                        new DefaultKeyValue<>("服务停止", "Unavailable"),
                        new DefaultKeyValue<>("创建失败", "Error"),
                        new DefaultKeyValue<>("迁移中", "Migrating"),
                        new DefaultKeyValue<>("备份恢复中", "BackupRecovering"),
                        new DefaultKeyValue<>("小版本升级中", "MinorVersionUpgrading"),
                        new DefaultKeyValue<>("网络变更中", "NetworkModifying"),
                        new DefaultKeyValue<>("SSL变更中", "SSLModifying"),
                        new DefaultKeyValue<>("大版本升级中，可正常访问", "MajorVersionUpgrading")));


        InstanceSearchField chargeType = new InstanceSearchField("计费方式", "chargeType.keyword", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("包年包月", "PrePaid"),
                        new DefaultKeyValue<>("按量付费", "PostPaid")));
        InstanceSearchField networkType = new InstanceSearchField("网络类型", "networkType.keyword", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("经典网络", "CLASSIC"),
                        new DefaultKeyValue<>("专有网络", "VPC")));

        InstanceSearchField nodeType = new InstanceSearchField("节点类型", "nodeType.keyword", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("双副本", "double"),
                        new DefaultKeyValue<>("单副本", "single")));

        InstanceSearchField editionType = new InstanceSearchField("实例类型", "editionType.keyword", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("社区版", "Community"),
                        new DefaultKeyValue<>("企业版", "Enterprise")));

        InstanceSearchField engineVersion = new InstanceSearchField("实例类型", "engineVersion.keyword", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("2.8", "2.8"),
                        new DefaultKeyValue<>("4.0", "4.0"),
                        new DefaultKeyValue<>("5.0", "5.0"),
                        new DefaultKeyValue<>("6.0", "6.0")));

        InstanceSearchField bandwidth = new InstanceSearchField("实例带宽", "bandwidth", InstanceFieldType.Number);

        InstanceSearchField capacity = new InstanceSearchField("实例容量,单位：MB", "capacity", InstanceFieldType.Number);

        InstanceSearchField DBInstanceNetType = new InstanceSearchField("网络信息所属的网络类型", "network.netInfoItems.instanceNetInfo.dbinstanceNetType.keyword", InstanceFieldType.ArrayEnum,
                List.of(new DefaultKeyValue<>("公网", "0"),
                        new DefaultKeyValue<>("经典网络", "1"),
                        new DefaultKeyValue<>("专有网络", "2")));

        return FieldUtil.appendPrefixField(REDIS_INSTANCE_PREFIX,
                List.of(status, chargeType, networkType, nodeType, editionType, engineVersion, bandwidth, capacity, DBInstanceNetType));
    }


    public static List<InstanceSearchField> listMongodbInstanceSearchField() {
        InstanceSearchField chargeType = new InstanceSearchField("付费类型", "chargeType.keyword", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("包年包月", "PrePaid"),
                        new DefaultKeyValue<>("按量付费", "PostPaid")));
        InstanceSearchField networkType = new InstanceSearchField("实例网络类型", "networkType.keyword", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("经典网络", "Classic"),
                        new DefaultKeyValue<>("专有网络", "VPC")));

        InstanceSearchField lockMode = new InstanceSearchField("实例的锁定状态", "lockMode.keyword", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("正常", "Unlock"),
                        new DefaultKeyValue<>("手动触发锁定", "ManualLock"),
                        new DefaultKeyValue<>("实例过期自动锁定", "LockByExpiration"),
                        new DefaultKeyValue<>("实例回滚前自动锁定", "LockByRestoration"),
                        new DefaultKeyValue<>("实例空间满自动锁定", "LockByDiskQuota"),
                        new DefaultKeyValue<>("实例已释放", "Released")));

        InstanceSearchField engineVersion = new InstanceSearchField("数据库版本号", "engineVersion.keyword", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("5.0", "5.0"),
                        new DefaultKeyValue<>("4.4", "4.4"),
                        new DefaultKeyValue<>("4.2", "4.2"),
                        new DefaultKeyValue<>("4.0", "4.0"),
                        new DefaultKeyValue<>("3.4", "3.4")));

        InstanceSearchField dbinstanceType = new InstanceSearchField("实例类型", "dbinstanceType.keyword", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("分片集群实例", "sharding"),
                        new DefaultKeyValue<>("副本集实例和单节点实例", "replicate"),
                        new DefaultKeyValue<>("Serverless实例", "serverless")
                ));

        InstanceSearchField dbinstanceStatus = new InstanceSearchField("实例状态", "dbinstanceStatus.keyword", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("创建中", "Creating"),
                        new DefaultKeyValue<>("升降级中", "DBInstanceClassChanging"),
                        new DefaultKeyValue<>("内外网切换中", "DBInstanceNetTypeChanging"),
                        new DefaultKeyValue<>("删除中", "Deleting"),
                        new DefaultKeyValue<>("迁移版本中", "EngineVersionUpgrading"),
                        new DefaultKeyValue<>("容灾切换中", "GuardSwitching"),
                        new DefaultKeyValue<>("主备切换中", "HASwitching"),
                        new DefaultKeyValue<>("数据导入中", "Importing"),
                        new DefaultKeyValue<>("数据库导入中", "ImportingFromOthers"),
                        new DefaultKeyValue<>("链路切换中", "LinkSwitching"),
                        new DefaultKeyValue<>("小版本升级中", "MinorVersionUpgrading"),
                        new DefaultKeyValue<>("创建网络连接中", "NET_CREATING"),
                        new DefaultKeyValue<>("删除网络连接中", "NET_DELETING"),
                        new DefaultKeyValue<>("实例节点创建中", "NodeCreating"),
                        new DefaultKeyValue<>("实例节点删除中", "NodeDeleting"),
                        new DefaultKeyValue<>("重启中", "Rebooting"),
                        new DefaultKeyValue<>("备份恢复中", "Restoring"),
                        new DefaultKeyValue<>("运行中", "Running"),
                        new DefaultKeyValue<>("SSL变更中", "SSLModifying"),
                        new DefaultKeyValue<>("临时实例创建中", "TempDBInstanceCreating"),
                        new DefaultKeyValue<>("迁移中", "Transing"),
                        new DefaultKeyValue<>("迁移数据库中", "TransingToOthers"),
                        new DefaultKeyValue<>("审计日志开通中", "AuditLogOpening"),
                        new DefaultKeyValue<>("审计日志关闭中", "AuditLogClosing")
                ));
        InstanceSearchField dbinstanceStorage = new InstanceSearchField("实例存储空间(GB)", "dbinstanceStorage", InstanceFieldType.Number);

        InstanceSearchField engine = new InstanceSearchField("数据库引擎", "engine.keyword", InstanceFieldType.String);

        InstanceSearchField replicationFactor = new InstanceSearchField("实例中节点的个数", "replicationFactor.keyword", InstanceFieldType.Number);

        InstanceSearchField kindCode = new InstanceSearchField("实例的类型", "kindCode.keyword", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("物理机", "0"),
                        new DefaultKeyValue<>("ECS", "1"),
                        new DefaultKeyValue<>("DOCKER", "2"),
                        new DefaultKeyValue<>("k8s新架构实例", "18")
                ));

        InstanceSearchField storageType = new InstanceSearchField("存储类型", "storageType.keyword", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("ESSD云盘", "cloud_essd"),
                        new DefaultKeyValue<>("SSD本地盘", "local_ssd")
                ));

        InstanceSearchField networkAddressNetworkType = new InstanceSearchField("实例连接网络类型", "networkObj.networkAddresses.networkAddress.networkType.keyword", InstanceFieldType.ArrayEnum,
                List.of(new DefaultKeyValue<>("专有网络", "VPC"),
                        new DefaultKeyValue<>("经典网络", "Classic"),
                        new DefaultKeyValue<>("公网", "Public")
                ));

        return FieldUtil.appendPrefixField(MONGO_DB_INSTANCE_PREFIX,
                List.of(chargeType, lockMode, engineVersion, networkType, dbinstanceType, dbinstanceStatus, dbinstanceStorage, engine, replicationFactor, kindCode, storageType, networkAddressNetworkType));
    }

    public static List<InstanceSearchField> listMysqlInstanceSearchField() {
        return listRDSInstanceSearchField(MYSQL_INSTANCE_PREFIX);
    }

    public static List<InstanceSearchField> listPostGreSqlInstanceSearchField() {
        return listRDSInstanceSearchField(POST_GRE_SQL_INSTANCE_PREFIX);
    }

    public static List<InstanceSearchField> listSqlServerInstanceSearchField() {
        return listRDSInstanceSearchField(SQL_SERVER_INSTANCE_PREFIX);
    }

    public static List<InstanceSearchField> listMariadbInstanceSearchField() {
        return listRDSInstanceSearchField(MARIA_DB_INSTANCE_PREFIX);
    }

    /**
     * 获取Rds实例查询字段
     *
     * @param instancePrefix 对象前缀
     * @return 实例
     */
    private static List<InstanceSearchField> listRDSInstanceSearchField(String instancePrefix) {
        InstanceSearchField payType = new InstanceSearchField("付费类型", "payType.keyword", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("按量付费", "Postpaid"),
                        new DefaultKeyValue<>("包年包月", "Prepaid")
                ));

        InstanceSearchField mutriORsignle = new InstanceSearchField("是否是组合可用区", "mutriORsignle", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("是", true),
                        new DefaultKeyValue<>("否", false)
                ));

        InstanceSearchField engineVersion = new InstanceSearchField("数据库版本", "engineVersion.keyword", InstanceFieldType.String);

        InstanceSearchField instanceNetworkType = new InstanceSearchField("实例网络类型", "instanceNetworkType.keyword", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("经典网络", "Classic"),
                        new DefaultKeyValue<>("VPC网络", "VPC")
                ));

        InstanceSearchField tipsLevel = new InstanceSearchField("异常提示等级", "tipsLevel", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("正常", 1),
                        new DefaultKeyValue<>("只读实例和主实例规格不对齐", 2)
                ));

        InstanceSearchField connectionMode = new InstanceSearchField("实例的访问模式", "connectionMode.keyword", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("标准访问模式", "Standard"),
                        new DefaultKeyValue<>("数据库代理模式", "Safe")
                ));

        InstanceSearchField lockMode = new InstanceSearchField("实例的锁定状态", "lockMode.keyword", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("正常", "Unlock"),
                        new DefaultKeyValue<>("手动触发锁定", "ManualLock"),
                        new DefaultKeyValue<>("实例回滚前自动锁定", "LockByRestoration"),
                        new DefaultKeyValue<>("实例过期自动锁定", "LockByExpiration"),
                        new DefaultKeyValue<>("实例空间满自动锁定", "LockByDiskQuota"),
                        new DefaultKeyValue<>("实例已释放", "Released")
                ));
        InstanceSearchField dbinstanceType = new InstanceSearchField("实例类型", "dbinstanceType.keyword", InstanceFieldType.ArrayEnum,
                List.of(new DefaultKeyValue<>("主实例", "Primary"),
                        new DefaultKeyValue<>("只读实例", "Readonly"),
                        new DefaultKeyValue<>("灾备实例", "Guard"),
                        new DefaultKeyValue<>("临时实例", "Temp")
                ));
        InstanceSearchField deletionProtection = new InstanceSearchField("是否已开启释放保护功能", "deletionProtection", InstanceFieldType.ArrayEnum,
                List.of(new DefaultKeyValue<>("已开启", true),
                        new DefaultKeyValue<>("未开启", false)
                ));
        InstanceSearchField dbinstanceNetInfosIpType = new InstanceSearchField("实例网络连接类型", "networkObj.dbinstanceNetInfos.dbinstanceNetInfo.iptype.keyword", InstanceFieldType.ArrayEnum,
                List.of(new DefaultKeyValue<>("经典网络类型内网", "Inner"),
                        new DefaultKeyValue<>("经典网络类型外网", "Public"),
                        new DefaultKeyValue<>("VPC类型内网", "Private"),
                        new DefaultKeyValue<>("VPC类型外网", "Public")
                ));
        return FieldUtil.appendPrefixField(instancePrefix,
                List.of(payType, mutriORsignle, engineVersion, instanceNetworkType, dbinstanceType, tipsLevel, connectionMode,
                        lockMode, deletionProtection, dbinstanceNetInfosIpType));

    }

    public static List<InstanceSearchField> listSecurityGroupInstanceSearchField() {
        InstanceSearchField securityGroupType = new InstanceSearchField("安全组类型", "securityGroupType.keyword", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("普通安全组", "normal"),
                        new DefaultKeyValue<>("企业安全组", "enterprise")
                ));

        InstanceSearchField innerAccessPolicy = new InstanceSearchField("安全组内网络连通策略", "rule.innerAccessPolicy.keyword", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("内网互通", "Accept"),
                        new DefaultKeyValue<>("内网隔离", "Drop")
                ));

        InstanceSearchField ruleSourceCidrIp = new InstanceSearchField("源端IP地址段,用于入方向授权", "rule.permissions.permission.sourceCidrIp.keyword", InstanceFieldType.String);
        return FieldUtil.appendPrefixField(SECURITY_GROUP_INSTANCE_PREFIX,
                List.of(securityGroupType, innerAccessPolicy, ruleSourceCidrIp));

    }

    public static List<InstanceSearchField> listElasticSearchInstanceSearchField() {

        InstanceSearchField advancedDedicateMaster = new InstanceSearchField("是否包含专有主节点", "advancedDedicateMaster", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("包含", true),
                        new DefaultKeyValue<>("不包含", false)
                ));
        InstanceSearchField nodeAmount = new InstanceSearchField("实例的数据节点数量", "nodeAmount", InstanceFieldType.Number);
        InstanceSearchField status = new InstanceSearchField("实例的状态", "status.keyword", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("正常", "active"),
                        new DefaultKeyValue<>("生效中", "activating"),
                        new DefaultKeyValue<>("冻结", "inactive"),
                        new DefaultKeyValue<>("失效", "invalid")
                ));
        InstanceSearchField serviceVpc = new InstanceSearchField("是否为服务VPC", "serviceVpc", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("是", true),
                        new DefaultKeyValue<>("否", false)
                ));

        InstanceSearchField paymentType = new InstanceSearchField("付费类型", "paymentType.keyword", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("包年包月", "prepaid"),
                        new DefaultKeyValue<>("按量付费", "postpaid")
                ));
        InstanceSearchField postpaidServiceStatus = new InstanceSearchField("包年包月实例叠加的后付费服务状态", "postpaidServiceStatus.keyword", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("正常", "active"),
                        new DefaultKeyValue<>("关闭", "closed"),
                        new DefaultKeyValue<>("欠费冻结中", "indebt")
                ));

        InstanceSearchField esVersion = new InstanceSearchField("包年包月实例叠加的后付费服务状态", "esVersion.keyword", InstanceFieldType.String);

        InstanceSearchField nodeSpecDisk = new InstanceSearchField("节点的存储空间大小,单位为GB", "nodeSpec.disk", InstanceFieldType.Number);

        InstanceSearchField nodeSpecDiskEncryption = new InstanceSearchField("节点是否使用磁盘加密", "nodeSpec.diskEncryption", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("是", true),
                        new DefaultKeyValue<>("否", false)

                ));

        InstanceSearchField nodeSpecDiskType = new InstanceSearchField("节点的存储类型", "nodeSpec.diskType.keyword", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("SSD云盘", "cloud_ssd"),
                        new DefaultKeyValue<>("高效云盘", "cloud_efficiency")

                ));
        InstanceSearchField kibanaConfigurationAmount = new InstanceSearchField("Kibana节点数量", "kibanaConfiguration.amount", InstanceFieldType.Number);

        InstanceSearchField kibanaConfigurationSpec = new InstanceSearchField("Kibana节点规格", "kibanaConfiguration.spec.keyword", InstanceFieldType.String);

        InstanceSearchField kibanaConfigurationDisk = new InstanceSearchField("Kibana节点存储空间大小,单位为GB", "kibanaConfiguration.disk", InstanceFieldType.Number);

        return FieldUtil.appendPrefixField(ELASTIC_SEARCH_INSTANCE_PREFIX,
                List.of(advancedDedicateMaster, nodeAmount, status, serviceVpc, paymentType, postpaidServiceStatus, esVersion, nodeSpecDisk, nodeSpecDiskEncryption
                        , nodeSpecDiskType, kibanaConfigurationAmount, kibanaConfigurationSpec, kibanaConfigurationDisk));
    }

    public static List<InstanceSearchField> listLoadBalancerInstanceSearchField() {
        InstanceSearchField payType = new InstanceSearchField("负载均衡实例付费模式", "payType.keyword", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("按量付费", "PayOnDemand"),
                        new DefaultKeyValue<>("包年包月", "PrePay")

                ));
        InstanceSearchField addressType = new InstanceSearchField("实例的网络类型", "addressType.keyword", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("公网负载均衡实例", "internet"),
                        new DefaultKeyValue<>("内网负载均衡实例", "intranet")

                ));
        InstanceSearchField networkType = new InstanceSearchField("私网负载均衡实例的网络类型", "networkType.keyword", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("专有网络实例", "vpc"),
                        new DefaultKeyValue<>("经典网络实例", "classic")

                ));
        InstanceSearchField bandwidth = new InstanceSearchField("监听的带宽峰值,单位：Mbps,-1不限制带宽峰值", "bandwidth", InstanceFieldType.Number);

        InstanceSearchField internetChargeTypeAlias = new InstanceSearchField("公网计费方式", "internetChargeTypeAlias.keyword", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("按带宽计费", "paybybandwidth"),
                        new DefaultKeyValue<>("按流量计费", "paybytraffic")

                ));
        InstanceSearchField loadBalancerStatus = new InstanceSearchField("负载均衡实例状态", "loadBalancerStatus.keyword", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("实例已停止", "inactive"),
                        new DefaultKeyValue<>("实例运行中", "active"),
                        new DefaultKeyValue<>("实例已锁定", "locked")

                ));
        InstanceSearchField internetChargeType = new InstanceSearchField("公网类型实例付费方式", "internetChargeType.keyword", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("按带宽计费", "3"),
                        new DefaultKeyValue<>("按流量计费", "4")


                ));
        InstanceSearchField deleteProtection = new InstanceSearchField("公网类型实例付费方式", "deleteProtection.keyword", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("开启", "on"),
                        new DefaultKeyValue<>("关闭", "off")
                ));

        InstanceSearchField instanceChargeType = new InstanceSearchField("实例计费方式", "instanceChargeType.keyword", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("按规格计费", "PayBySpec"),
                        new DefaultKeyValue<>("按使用量计费", "PayByCLCU")
                ));

        return FieldUtil.appendPrefixField(LOADBALANCER_INSTANCE_PREFIX,
                List.of(payType, addressType, networkType, bandwidth, internetChargeTypeAlias, loadBalancerStatus, internetChargeType, deleteProtection
                        , instanceChargeType));


    }
}
