package com.fit2cloud.provider.impl.tencent.api;

import com.fit2cloud.common.constants.PlatformConstants;
import com.fit2cloud.constants.ResourceTypeConstants;
import com.fit2cloud.provider.entity.InstanceFieldType;
import com.fit2cloud.provider.entity.InstanceSearchField;
import com.fit2cloud.provider.util.FieldUtil;
import com.vmware.vim25.SAMLTokenAuthentication;
import io.swagger.models.auth.In;
import org.apache.commons.collections4.keyvalue.DefaultKeyValue;

import java.util.List;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2023/1/4  14:14}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
public class TencentInstanceSearchFieldApi {

    /**
     * 获取云服务 查询字段
     *
     * @return 云服务器查询字段
     */
    public static List<InstanceSearchField> listEcsInstanceSearchField() {
        InstanceSearchField instanceChargeType = new InstanceSearchField("计费模式", "instanceChargeType", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("包年包月", "PREPAID"),
                        new DefaultKeyValue<>("按量计费", "POSTPAID_BY_HOUR"),
                        new DefaultKeyValue<>("专用宿主机付费", "CDHPAID"),
                        new DefaultKeyValue<>("竞价实例付费", "SPOTPAID")
                )).resetInstanceField(PlatformConstants.fit2cloud_tencent_platform, ResourceTypeConstants.ECS);

        InstanceSearchField instanceState = new InstanceSearchField("实例状态", "instanceState", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("创建中", "PENDING"),
                        new DefaultKeyValue<>("创建失败", "LAUNCH_FAILED"),
                        new DefaultKeyValue<>("运行中", "RUNNING"),
                        new DefaultKeyValue<>("关机", "STOPPED"),
                        new DefaultKeyValue<>("开机中", "STARTING"),
                        new DefaultKeyValue<>("关机中", "STOPPING"),
                        new DefaultKeyValue<>("重启中", "REBOOTING"),
                        new DefaultKeyValue<>("停止待销毁", "SHUTDOWN"),
                        new DefaultKeyValue<>("销毁中", "TERMINATING")))
                .resetInstanceField(PlatformConstants.fit2cloud_tencent_platform, ResourceTypeConstants.ECS);

        InstanceSearchField cpu = new InstanceSearchField("CPU", "cpu", InstanceFieldType.Number)
                .resetInstanceField(PlatformConstants.fit2cloud_tencent_platform, ResourceTypeConstants.ECS);

        InstanceSearchField memory = new InstanceSearchField("内存", "memory", InstanceFieldType.Number)
                .resetInstanceField(PlatformConstants.fit2cloud_tencent_platform, ResourceTypeConstants.ECS);

        InstanceSearchField restrictState = new InstanceSearchField("实例业务状态", "RestrictState", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("正常状态的实例", "NORMAL"),
                        new DefaultKeyValue<>("过期的实例", "EXPIRED"),
                        new DefaultKeyValue<>("被安全隔离的实例", "PROTECTIVELY_ISOLATED")))
                .resetInstanceField(PlatformConstants.fit2cloud_tencent_platform, ResourceTypeConstants.ECS);

        InstanceSearchField renewFlag = new InstanceSearchField("自动续费标识", "renewFlag", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("通知即将过期，但不自动续费", "NOTIFY_AND_MANUAL_RENEW"),
                        new DefaultKeyValue<>("通知即将过期，而且自动续费", "NOTIFY_AND_AUTO_RENEW"),
                        new DefaultKeyValue<>("不通知即将过期，也不自动续费", "DISABLE_NOTIFY_AND_MANUAL_RENEW")))
                .resetInstanceField(PlatformConstants.fit2cloud_tencent_platform, ResourceTypeConstants.ECS);

        InstanceSearchField stopChargingMode = new InstanceSearchField("关机计费模式", "stopChargingMode", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("关机继续收费", "KEEP_CHARGING"),
                        new DefaultKeyValue<>("关机停止收费", "STOP_CHARGING"),
                        new DefaultKeyValue<>("实例处于非关机状态或者不适用关机停止计费的条件", "NOT_APPLICABLE")))
                .resetInstanceField(PlatformConstants.fit2cloud_tencent_platform, ResourceTypeConstants.ECS);

        InstanceSearchField isolatedSource = new InstanceSearchField("实例隔离类型", "isolatedSource", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("欠费隔离", "ARREAR"),
                        new DefaultKeyValue<>("到期隔离", "EXPIRE"),
                        new DefaultKeyValue<>("主动退还隔离", "MANMADE"),
                        new DefaultKeyValue<>("未隔离", "NOTISOLATED")))
                .resetInstanceField(PlatformConstants.fit2cloud_tencent_platform, ResourceTypeConstants.ECS);

        InstanceSearchField defaultLoginUser = new InstanceSearchField("默认登录用户", "defaultLoginUser", InstanceFieldType.String)
                .resetInstanceField(PlatformConstants.fit2cloud_tencent_platform, ResourceTypeConstants.ECS);

        InstanceSearchField defaultLoginPort = new InstanceSearchField("默认登录端口", "defaultLoginPort", InstanceFieldType.String)
                .resetInstanceField(PlatformConstants.fit2cloud_tencent_platform, ResourceTypeConstants.ECS);

        InstanceSearchField systemDiskType = new InstanceSearchField("系统盘类型", "systemDisk.diskType", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("本地硬盘", "LOCAL_BASIC"),
                        new DefaultKeyValue<>("本地SSD硬盘", "LOCAL_SSD"),
                        new DefaultKeyValue<>("普通云硬盘", "CLOUD_BASIC"),
                        new DefaultKeyValue<>("SSD云硬盘", "CLOUD_SSD"),
                        new DefaultKeyValue<>("高性能云硬盘", "CLOUD_PREMIUM"),
                        new DefaultKeyValue<>("通用性SSD云硬盘", "CLOUD_BSSD")))
                .resetInstanceField(PlatformConstants.fit2cloud_tencent_platform, ResourceTypeConstants.ECS);

        InstanceSearchField systemDiskSize = new InstanceSearchField("系统盘大小", "systemDisk.diskSize", InstanceFieldType.Number)
                .resetInstanceField(PlatformConstants.fit2cloud_tencent_platform, ResourceTypeConstants.ECS);

        InstanceSearchField dataDiskSize = new InstanceSearchField("数据盘大小", "dataDisks.diskSize", InstanceFieldType.ArrayNumber)
                .resetInstanceField(PlatformConstants.fit2cloud_tencent_platform, ResourceTypeConstants.ECS);

        InstanceSearchField dataDiskDeleteWithInstance = new InstanceSearchField("数据盘是否随实例删除", "dataDisks.deleteWithInstance", InstanceFieldType.ArrayEnum,
                List.of(new DefaultKeyValue<>("销毁数据盘", true),
                        new DefaultKeyValue<>("保留数据盘", false)))
                .resetInstanceField(PlatformConstants.fit2cloud_tencent_platform, ResourceTypeConstants.ECS);

        InstanceSearchField dataDiskEncrypt = new InstanceSearchField("数据盘是否加密", "dataDisks.encrypt", InstanceFieldType.ArrayEnum,
                List.of(new DefaultKeyValue<>("加密", true),
                        new DefaultKeyValue<>("不加密", false)))
                .resetInstanceField(PlatformConstants.fit2cloud_tencent_platform, ResourceTypeConstants.ECS);

        InstanceSearchField dataDiskType = new InstanceSearchField("数据盘类型", "dataDisks.diskType", InstanceFieldType.ArrayEnum,
                List.of(new DefaultKeyValue<>("本地硬盘", "LOCAL_BASIC"),
                        new DefaultKeyValue<>("本地SSD硬盘", "LOCAL_SSD"),
                        new DefaultKeyValue<>("本地NVME硬盘", "LOCAL_NVME"),
                        new DefaultKeyValue<>("本地HDD硬盘", "LOCAL_PRO"),
                        new DefaultKeyValue<>("普通云硬盘", "CLOUD_BASIC"),
                        new DefaultKeyValue<>("高性能云硬盘", "CLOUD_PREMIUM"),
                        new DefaultKeyValue<>("SSD云硬盘", "CLOUD_SSD"),
                        new DefaultKeyValue<>("增强型SSD云硬盘", "CLOUD_HSSD"),
                        new DefaultKeyValue<>("极速型SSD云硬盘", "CLOUD_TSSD"),
                        new DefaultKeyValue<>("通用型SSD云硬盘", "CLOUD_BSSD")))
                .resetInstanceField(PlatformConstants.fit2cloud_tencent_platform, ResourceTypeConstants.ECS);

        InstanceSearchField internetAccessibleInternetChargeType = new InstanceSearchField("网络计费类型", "internetAccessible.internetChargeType", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("预付费按带宽结算", "BANDWIDTH_PREPAID"),
                        new DefaultKeyValue<>("流量按小时后付费", "TRAFFIC_POSTPAID_BY_HOUR"),
                        new DefaultKeyValue<>("带宽按小时后付费", "BANDWIDTH_POSTPAID_BY_HOUR"),
                        new DefaultKeyValue<>("带宽包用户", "BANDWIDTH_PACKAGE")))
                .resetInstanceField(PlatformConstants.fit2cloud_tencent_platform, ResourceTypeConstants.ECS);

        InstanceSearchField internetAccessiblePublicIpAssigned = new InstanceSearchField("是否分配公网IP", "internetAccessible.publicIpAssigned", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("分配公网IP", true),
                        new DefaultKeyValue<>("不分配公网IP", false)
                ))
                .resetInstanceField(PlatformConstants.fit2cloud_tencent_platform, ResourceTypeConstants.ECS);

        InstanceSearchField internetAccessibleInternetAccessible = new InstanceSearchField("公网出带宽上限", "internetAccessible.internetMaxBandwidthOut", InstanceFieldType.Number)
                .resetInstanceField(PlatformConstants.fit2cloud_tencent_platform, ResourceTypeConstants.ECS);

        return List.of(instanceChargeType, instanceState, cpu, memory, renewFlag, restrictState, stopChargingMode, isolatedSource,
                defaultLoginUser, defaultLoginPort, systemDiskType, systemDiskSize, dataDiskSize, dataDiskDeleteWithInstance, dataDiskEncrypt, dataDiskType
                , internetAccessibleInternetChargeType, internetAccessiblePublicIpAssigned, internetAccessibleInternetAccessible);
    }

    /**
     * 获取 OSS 实例查询字段
     *
     * @return oss 对象存储实例查询字段
     */
    public static List<InstanceSearchField> listOSSInstanceSearchField() {
        InstanceSearchField refererStatus = new InstanceSearchField("是否开启防盗链", "referer.status", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("开启", "Enabled"),
                        new DefaultKeyValue<>("关闭", null)
                ))
                .resetInstanceField(PlatformConstants.fit2cloud_tencent_platform, ResourceTypeConstants.OSS);

        InstanceSearchField refererType = new InstanceSearchField("防盗链类型", "referer.refererType", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("黑名单", "Black-List"),
                        new DefaultKeyValue<>("白名单", "White-List")
                ))
                .resetInstanceField(PlatformConstants.fit2cloud_tencent_platform, ResourceTypeConstants.OSS);

        InstanceSearchField refererDomainList = new InstanceSearchField("生效域名列表", "referer.domainList", InstanceFieldType.ArrayString)
                .resetInstanceField(PlatformConstants.fit2cloud_tencent_platform, ResourceTypeConstants.OSS);

        InstanceSearchField refererEmptyReferConfiguration = new InstanceSearchField("是否允许空Referer访问", "referer.emptyReferConfiguration", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("允许", "Allow"),
                        new DefaultKeyValue<>("不允许", "Deny")
                ))
                .resetInstanceField(PlatformConstants.fit2cloud_tencent_platform, ResourceTypeConstants.OSS);

        InstanceSearchField accessCannedAccessControl = new InstanceSearchField("公共权限", "access.cannedAccessControl", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("私有读写", "Private"),
                        new DefaultKeyValue<>("公有读私有写", "PublicRead"),
                        new DefaultKeyValue<>("公有读写", "PublicReadWrite")
                ))
                .resetInstanceField(PlatformConstants.fit2cloud_tencent_platform, ResourceTypeConstants.OSS);
        InstanceSearchField encryptionSseAlgorithm = new InstanceSearchField("加密类型", "encryption.rule.applyServerSideEncryptionByDefault.sseAlgorithm", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("未开启", null),
                        new DefaultKeyValue<>("KMS", "KMS"),
                        new DefaultKeyValue<>("AES256", "AES256")))
                .resetInstanceField(PlatformConstants.fit2cloud_tencent_platform, ResourceTypeConstants.OSS);

        return List.of(refererStatus, refererType, refererDomainList, refererEmptyReferConfiguration, accessCannedAccessControl, encryptionSseAlgorithm);

    }

    /**
     * 获取磁盘 查询字段
     *
     * @return 磁盘查询字段
     */
    public static List<InstanceSearchField> listDISKInstanceSearchField() {
        InstanceSearchField deleteWithInstance = new InstanceSearchField("云盘是否与挂载的实例一起销毁", "deleteWithInstance", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("销毁实例时会同时销毁云盘", true),
                        new DefaultKeyValue<>("销毁实例时不销毁云盘", false)))
                .resetInstanceField(PlatformConstants.fit2cloud_tencent_platform, ResourceTypeConstants.DISK);

        InstanceSearchField renewFlag = new InstanceSearchField("自动续费标识", "renewFlag", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("通知过期且自动续费", "NOTIFY_AND_AUTO_RENEW"),
                        new DefaultKeyValue<>("通知过期不自动续费", "NOTIFY_AND_MANUAL_RENEW"),
                        new DefaultKeyValue<>("不通知过期不自动续费", "DISABLE_NOTIFY_AND_MANUAL_RENEW"),
                        new DefaultKeyValue<>("取不到有效值", null)))
                .resetInstanceField(PlatformConstants.fit2cloud_tencent_platform, ResourceTypeConstants.DISK);

        InstanceSearchField diskType = new InstanceSearchField("硬盘介质类型", "diskType", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("普通云硬盘", "CLOUD_BASIC"),
                        new DefaultKeyValue<>("高性能云硬盘", "CLOUD_PREMIUM"),
                        new DefaultKeyValue<>("通用型SSD云硬盘", "CLOUD_BSSD"),
                        new DefaultKeyValue<>("SSD云硬盘", "CLOUD_SSD"),
                        new DefaultKeyValue<>("增强型SSD云硬盘", "CLOUD_HSSD"),
                        new DefaultKeyValue<>("极速型SSD云硬盘", "CLOUD_TSSD")))
                .resetInstanceField(PlatformConstants.fit2cloud_tencent_platform, ResourceTypeConstants.DISK);

        InstanceSearchField diskState = new InstanceSearchField("云盘状态", "diskState", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("未挂载", "UNATTACHED"),
                        new DefaultKeyValue<>("挂载中", "ATTACHING"),
                        new DefaultKeyValue<>("已挂载", "ATTACHED"),
                        new DefaultKeyValue<>("解挂中", "DETACHING"),
                        new DefaultKeyValue<>("扩容中", "EXPANDING"),
                        new DefaultKeyValue<>("回滚中", "ROLLBACKING"),
                        new DefaultKeyValue<>("待回收", "TORECYCLE"),
                        new DefaultKeyValue<>("拷贝硬盘中", "DUMPING")))
                .resetInstanceField(PlatformConstants.fit2cloud_tencent_platform, ResourceTypeConstants.DISK);

        InstanceSearchField snapshotCount = new InstanceSearchField("云盘拥有的快照总数", "snapshotCount", InstanceFieldType.Number)
                .resetInstanceField(PlatformConstants.fit2cloud_tencent_platform, ResourceTypeConstants.DISK);

        InstanceSearchField autoRenewFlagError = new InstanceSearchField("是否自动续费", "autoRenewFlagError", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("主机设置了自动续费标识，但云盘未设置", true),
                        new DefaultKeyValue<>("云盘自动续费标识正常", false)))
                .resetInstanceField(PlatformConstants.fit2cloud_tencent_platform, ResourceTypeConstants.DISK);

        InstanceSearchField rollbacking = new InstanceSearchField("云盘是否处于快照回滚状态", "rollbacking", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("不处于快照回滚状态", false),
                        new DefaultKeyValue<>("处于快照回滚状态", true)))
                .resetInstanceField(PlatformConstants.fit2cloud_tencent_platform, ResourceTypeConstants.DISK);

        InstanceSearchField encrypt = new InstanceSearchField("云盘是否为加密盘", "encrypt", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("非加密盘", false),
                        new DefaultKeyValue<>("加密盘", true)))
                .resetInstanceField(PlatformConstants.fit2cloud_tencent_platform, ResourceTypeConstants.DISK);

        InstanceSearchField backupDisk = new InstanceSearchField("云硬盘因欠费销毁或者到期销毁时,是否使用快照备份数据的标识", "backupDisk", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("表示直接销毁", false),
                        new DefaultKeyValue<>("销毁时创建快照进行数据备份", true)))
                .resetInstanceField(PlatformConstants.fit2cloud_tencent_platform, ResourceTypeConstants.DISK);

        InstanceSearchField throughputPerformance = new InstanceSearchField("云硬盘额外性能值,单位MB/s", "throughputPerformance", InstanceFieldType.Number)
                .resetInstanceField(PlatformConstants.fit2cloud_tencent_platform, ResourceTypeConstants.DISK);

        InstanceSearchField migrating = new InstanceSearchField("云盘是否处于类型变更中", "migrating", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("云盘不处于类型变更中", false),
                        new DefaultKeyValue<>("云盘已发起类型变更，正处于迁移中", true)))
                .resetInstanceField(PlatformConstants.fit2cloud_tencent_platform, ResourceTypeConstants.DISK);

        InstanceSearchField snapshotSize = new InstanceSearchField("云盘拥有的快照总容量,单位为MB", "snapshotSize", InstanceFieldType.Number)
                .resetInstanceField(PlatformConstants.fit2cloud_tencent_platform, ResourceTypeConstants.DISK);

        InstanceSearchField isReturnable = new InstanceSearchField("预付费的云盘是否支持主动退还", "isReturnable", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("不支持主动退还", false),
                        new DefaultKeyValue<>("支持主动退还", true)))
                .resetInstanceField(PlatformConstants.fit2cloud_tencent_platform, ResourceTypeConstants.DISK);

        InstanceSearchField attached = new InstanceSearchField("云盘是否挂载到云主机上", "attached", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("未挂载", false),
                        new DefaultKeyValue<>("已挂载", true)))
                .resetInstanceField(PlatformConstants.fit2cloud_tencent_platform, ResourceTypeConstants.DISK);

        InstanceSearchField diskSize = new InstanceSearchField("云硬盘大小,单位GB", "diskSize", InstanceFieldType.Number)
                .resetInstanceField(PlatformConstants.fit2cloud_tencent_platform, ResourceTypeConstants.DISK);

        InstanceSearchField diskUsage = new InstanceSearchField("云盘类型", "diskUsage", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("系统盘", "SYSTEM_DISK"),
                        new DefaultKeyValue<>("数据盘", "DATA_DISK")))
                .resetInstanceField(PlatformConstants.fit2cloud_tencent_platform, ResourceTypeConstants.DISK);

        InstanceSearchField diskChargeType = new InstanceSearchField("付费模式", "diskChargeType", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("包年包月", "PREPAID"),
                        new DefaultKeyValue<>("按量计费", "POSTPAID_BY_HOUR")))
                .resetInstanceField(PlatformConstants.fit2cloud_tencent_platform, ResourceTypeConstants.DISK);

        InstanceSearchField portable = new InstanceSearchField("是否为弹性云盘", "portable", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("非弹性云盘", false),
                        new DefaultKeyValue<>("弹性云盘", true)))
                .resetInstanceField(PlatformConstants.fit2cloud_tencent_platform, ResourceTypeConstants.DISK);

        InstanceSearchField snapshotAbility = new InstanceSearchField("云盘是否具备创建快照的能力", "snapshotAbility", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("不具备", false),
                        new DefaultKeyValue<>("具备", true)))
                .resetInstanceField(PlatformConstants.fit2cloud_tencent_platform, ResourceTypeConstants.DISK);

        InstanceSearchField deadlineError = new InstanceSearchField("云盘到期时间与实例到期时间状态", "deadlineError", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("云盘到期时间早于实例", true),
                        new DefaultKeyValue<>("云盘到期时间晚于实例", false)))
                .resetInstanceField(PlatformConstants.fit2cloud_tencent_platform, ResourceTypeConstants.DISK);

        InstanceSearchField differDaysOfDeadline = new InstanceSearchField("当前时间距离盘到期的天数(仅对包年包月盘有意义)", "differDaysOfDeadline", InstanceFieldType.Number)
                .resetInstanceField(PlatformConstants.fit2cloud_tencent_platform, ResourceTypeConstants.DISK);

        InstanceSearchField shareable = new InstanceSearchField("云盘是否为共享型云盘", "shareable", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("是", true),
                        new DefaultKeyValue<>("否", false)))
                .resetInstanceField(PlatformConstants.fit2cloud_tencent_platform, ResourceTypeConstants.DISK);

        InstanceSearchField deleteSnapshot = new InstanceSearchField("销毁云盘时删除关联的非永久保留快照", "deleteSnapshot", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("非永久快照不随云盘销毁而销毁", 0),
                        new DefaultKeyValue<>("非永久快照随云盘销毁而销毁", 1)))
                .resetInstanceField(PlatformConstants.fit2cloud_tencent_platform, ResourceTypeConstants.DISK);

        InstanceSearchField diskBackupCount = new InstanceSearchField("云硬盘备份点已使用的数量", "diskBackupCount", InstanceFieldType.Number)
                .resetInstanceField(PlatformConstants.fit2cloud_tencent_platform, ResourceTypeConstants.DISK);

        InstanceSearchField instanceType = new InstanceSearchField("云硬盘挂载实例的类型", "instanceType", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("CVM", "CVM"),
                        new DefaultKeyValue<>("EKS", "EKS")))
                .resetInstanceField(PlatformConstants.fit2cloud_tencent_platform, ResourceTypeConstants.DISK);

        return List.of(deleteWithInstance, renewFlag, diskType, diskState
                , snapshotCount, autoRenewFlagError, rollbacking, encrypt, backupDisk, throughputPerformance, migrating, snapshotSize, isReturnable,
                attached, diskSize, diskUsage, diskChargeType, portable, snapshotAbility, deadlineError, differDaysOfDeadline, shareable, deleteSnapshot, diskBackupCount,
                instanceType);
    }

    /**
     * 获取 vpc实例查询字段
     *
     * @return vpc实例查询字段
     */
    public static List<InstanceSearchField> listVpcInstanceSearchField() {
        InstanceSearchField isDefault = new InstanceSearchField("是否默认VPC", "isDefault", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("是", true),
                        new DefaultKeyValue<>("否", false)))
                .resetInstanceField(PlatformConstants.fit2cloud_tencent_platform, ResourceTypeConstants.VPC);

        InstanceSearchField enableMulticast = new InstanceSearchField("是否开启组播", "enableMulticast", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("是", true),
                        new DefaultKeyValue<>("否", false)))
                .resetInstanceField(PlatformConstants.fit2cloud_tencent_platform, ResourceTypeConstants.VPC);

        InstanceSearchField cidrBlock = new InstanceSearchField("VPC的IPv4 CIDR", "cidrBlock", InstanceFieldType.String)
                .resetInstanceField(PlatformConstants.fit2cloud_tencent_platform, ResourceTypeConstants.VPC);

        InstanceSearchField enableDhcp = new InstanceSearchField("是否开启DHCP", "enableDhcp", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("是", true),
                        new DefaultKeyValue<>("否", false)))
                .resetInstanceField(PlatformConstants.fit2cloud_tencent_platform, ResourceTypeConstants.VPC);

        return List.of(isDefault, enableMulticast, cidrBlock, enableDhcp);
    }

    /**
     * 获取公网ip 实例查询字段
     *
     * @return 公网ip 实例查询字段
     */
    public static List<InstanceSearchField> listPublicIpInstanceSearchField() {

        InstanceSearchField addressStatus = new InstanceSearchField("EIP状态", "addressStatus", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("创建中", "CREATING"),
                        new DefaultKeyValue<>("绑定中", "BINDING"),
                        new DefaultKeyValue<>("已绑定", "BIND"),
                        new DefaultKeyValue<>("解绑中", "UNBINDING"),
                        new DefaultKeyValue<>("已解绑", "UNBIND"),
                        new DefaultKeyValue<>("释放中", "OFFLINING"),
                        new DefaultKeyValue<>("绑定悬空弹性网卡", "BIND_ENI")))
                .resetInstanceField(PlatformConstants.fit2cloud_tencent_platform, ResourceTypeConstants.PUBLIC_IP);

        InstanceSearchField isArrears = new InstanceSearchField("资源隔离状态", "isArrears", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("隔离状态", true),
                        new DefaultKeyValue<>("未隔离状态", false)))
                .resetInstanceField(PlatformConstants.fit2cloud_tencent_platform, ResourceTypeConstants.PUBLIC_IP);

        InstanceSearchField isBlocked = new InstanceSearchField("资源封堵状态", "isBlocked", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("封堵状态", true),
                        new DefaultKeyValue<>("未封堵状态", false)))
                .resetInstanceField(PlatformConstants.fit2cloud_tencent_platform, ResourceTypeConstants.PUBLIC_IP);

        InstanceSearchField isEipDirectConnection = new InstanceSearchField("支持直通模式", "isEipDirectConnection", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("支持直通模式", true),
                        new DefaultKeyValue<>("不支持直通模式", false)))
                .resetInstanceField(PlatformConstants.fit2cloud_tencent_platform, ResourceTypeConstants.PUBLIC_IP);

        InstanceSearchField addressType = new InstanceSearchField("资源类型", "addressType", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("设备 IP", "CalcIP"),
                        new DefaultKeyValue<>("普通公网 IP", "WanIP"),
                        new DefaultKeyValue<>("弹性公网 IP", "EIP"),
                        new DefaultKeyValue<>("加速 EIP", "AnycastEip"),
                        new DefaultKeyValue<>("高防EIP", "AntiDDoSEIP")))
                .resetInstanceField(PlatformConstants.fit2cloud_tencent_platform, ResourceTypeConstants.PUBLIC_IP);


        InstanceSearchField cascadeRelease = new InstanceSearchField("eip是否在解绑后自动释放", "cascadeRelease", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("解绑后自动释放", true),
                        new DefaultKeyValue<>("解绑后不会自动释放", false)))
                .resetInstanceField(PlatformConstants.fit2cloud_tencent_platform, ResourceTypeConstants.PUBLIC_IP);

        InstanceSearchField bandwidth = new InstanceSearchField("带宽", "bandwidth", InstanceFieldType.Number)
                .resetInstanceField(PlatformConstants.fit2cloud_tencent_platform, ResourceTypeConstants.PUBLIC_IP);

        InstanceSearchField internetChargeType = new InstanceSearchField("网络计费模式", "internetChargeType", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("包月带宽预付费", "BANDWIDTH_PREPAID_BY_MONTH"),
                        new DefaultKeyValue<>("按小时流量后付费", "TRAFFIC_POSTPAID_BY_HOUR"),
                        new DefaultKeyValue<>("按小时带宽后付费", "BANDWIDTH_POSTPAID_BY_HOUR"),
                        new DefaultKeyValue<>("共享带宽包", "BANDWIDTH_PACKAGE"),
                        new DefaultKeyValue<>("未知", null)))
                .resetInstanceField(PlatformConstants.fit2cloud_tencent_platform, ResourceTypeConstants.PUBLIC_IP);

        return List.of(addressStatus, isArrears, isBlocked, isEipDirectConnection, addressType, cascadeRelease, bandwidth, internetChargeType);

    }


    /**
     * 获取安全组实例查询字段
     *
     * @return 安全组实例查询字段
     */
    public static List<InstanceSearchField> listSecurityGroupInstanceSearchField() {
        InstanceSearchField isDefault = new InstanceSearchField("是否是默认安全组", "isDefault", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("是", true),
                        new DefaultKeyValue<>("否", false)))
                .resetInstanceField(PlatformConstants.fit2cloud_tencent_platform, ResourceTypeConstants.SECURITY_GROUP);

        InstanceSearchField ruleIngressProtocol = new InstanceSearchField("安全组入站协议", "rule.ingress.protocol", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("TCP", "TCP"),
                        new DefaultKeyValue<>("UDP", "UDP"),
                        new DefaultKeyValue<>("ICMP", "ICMP"),
                        new DefaultKeyValue<>("ICMPv6", "ICMPv6"),
                        new DefaultKeyValue<>("ALL", "ALL")
                ))
                .resetInstanceField(PlatformConstants.fit2cloud_tencent_platform, ResourceTypeConstants.SECURITY_GROUP);

        InstanceSearchField ruleIngressCidrBlock = new InstanceSearchField("安全组入站网段", "rule.ingress.cidrBlock", InstanceFieldType.String)
                .resetInstanceField(PlatformConstants.fit2cloud_tencent_platform, ResourceTypeConstants.SECURITY_GROUP);

        InstanceSearchField ruleEgressProtocol = new InstanceSearchField("安全组出站协议", "rule.egress.protocol", InstanceFieldType.ArrayEnum,
                List.of(new DefaultKeyValue<>("TCP", "TCP"),
                        new DefaultKeyValue<>("UDP", "UDP"),
                        new DefaultKeyValue<>("ICMP", "ICMP"),
                        new DefaultKeyValue<>("ICMPv6", "ICMPv6"),
                        new DefaultKeyValue<>("ALL", "ALL")
                ))
                .resetInstanceField(PlatformConstants.fit2cloud_tencent_platform, ResourceTypeConstants.SECURITY_GROUP);

        InstanceSearchField ruleEgressCidrBlock = new InstanceSearchField("安全组出站网段", "rule.egress.cidrBlock", InstanceFieldType.ArrayString)
                .resetInstanceField(PlatformConstants.fit2cloud_tencent_platform, ResourceTypeConstants.SECURITY_GROUP);

        return List.of(isDefault, ruleIngressProtocol, ruleIngressCidrBlock, ruleEgressProtocol, ruleEgressCidrBlock);
    }

    public static List<InstanceSearchField> listUserInstanceSearchField() {
        return List.of();
    }

    /**
     * 获取 mysql 实例可查询字段
     *
     * @return mysql实例查询字段
     */
    public static List<InstanceSearchField> listMysqlInstanceSearchField() {
        InstanceSearchField wanStatus = new InstanceSearchField("外网状态", "wanStatus", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("未开通外网", 0),
                        new DefaultKeyValue<>("已开通外网", 1),
                        new DefaultKeyValue<>("已关闭外网", 2)
                ))
                .resetInstanceField(PlatformConstants.fit2cloud_tencent_platform, ResourceTypeConstants.MYSQL);

        InstanceSearchField initFlag = new InstanceSearchField("初始化标志", "initFlag", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("未初始化", 0),
                        new DefaultKeyValue<>("已初始化", 1)
                ))
                .resetInstanceField(PlatformConstants.fit2cloud_tencent_platform, ResourceTypeConstants.MYSQL);

        InstanceSearchField memory = new InstanceSearchField("内存容量,单位为 MB", "memory", InstanceFieldType.Number)
                .resetInstanceField(PlatformConstants.fit2cloud_tencent_platform, ResourceTypeConstants.MYSQL);

        InstanceSearchField status = new InstanceSearchField("实例状态", "status", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("创建中", 0),
                        new DefaultKeyValue<>("运行中", 1),
                        new DefaultKeyValue<>("隔离中", 4),
                        new DefaultKeyValue<>("已隔离", 5)
                ))
                .resetInstanceField(PlatformConstants.fit2cloud_tencent_platform, ResourceTypeConstants.MYSQL);

        InstanceSearchField volume = new InstanceSearchField("硬盘容量,单位为 GB", "volume", InstanceFieldType.Number)
                .resetInstanceField(PlatformConstants.fit2cloud_tencent_platform, ResourceTypeConstants.MYSQL);

        InstanceSearchField autoRenew = new InstanceSearchField("自动续费", "autoRenew", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("未开通自动续费", 0),
                        new DefaultKeyValue<>("已开通自动续费", 1),
                        new DefaultKeyValue<>("已关闭自动续费", 2)
                ))
                .resetInstanceField(PlatformConstants.fit2cloud_tencent_platform, ResourceTypeConstants.MYSQL);

            InstanceSearchField protectMode = new InstanceSearchField("数据复制方式", "protectMode", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("异步复制", 0),
                        new DefaultKeyValue<>("半同步复制", 1),
                        new DefaultKeyValue<>("强同步复制", 2)
                ))
                .resetInstanceField(PlatformConstants.fit2cloud_tencent_platform, ResourceTypeConstants.MYSQL);

        InstanceSearchField instanceType = new InstanceSearchField("实例类型", "instanceType", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("主实例", 1),
                        new DefaultKeyValue<>("灾备实例", 2),
                        new DefaultKeyValue<>("只读实例", 3)
                ))
                .resetInstanceField(PlatformConstants.fit2cloud_tencent_platform, ResourceTypeConstants.MYSQL);

        InstanceSearchField deployMode = new InstanceSearchField("可用区部署方式", "deployMode", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("单可用区", 0),
                        new DefaultKeyValue<>("多可用区", 1)
                ))
                .resetInstanceField(PlatformConstants.fit2cloud_tencent_platform, ResourceTypeConstants.MYSQL);

        InstanceSearchField taskStatus = new InstanceSearchField("实例任务状态", "taskStatus", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("没有任务", 0),
                        new DefaultKeyValue<>("升级中", 1),
                        new DefaultKeyValue<>("数据导入中", 2),
                        new DefaultKeyValue<>("开放Slave中", 3),
                        new DefaultKeyValue<>("外网访问开通中", 4),
                        new DefaultKeyValue<>("批量操作执行中", 5),
                        new DefaultKeyValue<>("回档中", 6),
                        new DefaultKeyValue<>("外网访问关闭中", 7),
                        new DefaultKeyValue<>("密码修改中", 8),
                        new DefaultKeyValue<>("实例名修改中", 9),
                        new DefaultKeyValue<>("重启中", 10),
                        new DefaultKeyValue<>("自建迁移中", 12),
                        new DefaultKeyValue<>("删除库表中", 13),
                        new DefaultKeyValue<>("灾备实例创建同步中", 14),
                        new DefaultKeyValue<>("升级待切换", 15),
                        new DefaultKeyValue<>("升级切换中", 16),
                        new DefaultKeyValue<>("升级切换完成", 17)
                ))
                .resetInstanceField(PlatformConstants.fit2cloud_tencent_platform, ResourceTypeConstants.MYSQL);

        InstanceSearchField deviceType = new InstanceSearchField("数据库类型", "deviceType", InstanceFieldType.String)
                .resetInstanceField(PlatformConstants.fit2cloud_tencent_platform, ResourceTypeConstants.MYSQL);

        InstanceSearchField engineVersion = new InstanceSearchField("内核版本", "engineVersion", InstanceFieldType.String)
                .resetInstanceField(PlatformConstants.fit2cloud_tencent_platform, ResourceTypeConstants.MYSQL);

        InstanceSearchField payType = new InstanceSearchField("付费类型", "payType", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("包年包月", 0),
                        new DefaultKeyValue<>("按量计费", 1)
                ))
                .resetInstanceField(PlatformConstants.fit2cloud_tencent_platform, ResourceTypeConstants.MYSQL);


        InstanceSearchField cdbError = new InstanceSearchField("磁盘写入是否被锁定", "cdbError", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("未被锁定", 0),
                        new DefaultKeyValue<>("已被锁定", 1)
                ))
                .resetInstanceField(PlatformConstants.fit2cloud_tencent_platform, ResourceTypeConstants.MYSQL);


        InstanceSearchField cpu = new InstanceSearchField("CPU核心数", "cpu", InstanceFieldType.Number)
                .resetInstanceField(PlatformConstants.fit2cloud_tencent_platform, ResourceTypeConstants.MYSQL);

        InstanceSearchField qps = new InstanceSearchField("每秒查询数量", "qps", InstanceFieldType.Number)
                .resetInstanceField(PlatformConstants.fit2cloud_tencent_platform, ResourceTypeConstants.MYSQL);

        InstanceSearchField instanceNodes = new InstanceSearchField("节点数", "instanceNodes", InstanceFieldType.Number)
                .resetInstanceField(PlatformConstants.fit2cloud_tencent_platform, ResourceTypeConstants.MYSQL);

        return List.of(wanStatus, initFlag, memory, status, volume, autoRenew, protectMode, instanceType, deployMode, taskStatus, deviceType, engineVersion, payType,
                cdbError, cpu, qps, instanceNodes);
    }

    /**
     * 获取 sqlserver 实例查询字段
     *
     * @return sqlserver实例查询字段
     */
    public static List<InstanceSearchField> listSqlServerInstanceSearchField() {

        InstanceSearchField status = new InstanceSearchField("实例状态", "status", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("申请中", 1),
                        new DefaultKeyValue<>("运行中", 2),
                        new DefaultKeyValue<>("受限运行中 (主备切换中)", 3),
                        new DefaultKeyValue<>("已隔离", 4),
                        new DefaultKeyValue<>("回收中", 5),
                        new DefaultKeyValue<>("已回收", 6),
                        new DefaultKeyValue<>("任务执行中 (实例做备份、回档等操作)", 7),
                        new DefaultKeyValue<>("已下线 (主备切换中)", 8),
                        new DefaultKeyValue<>("实例扩容中", 9),
                        new DefaultKeyValue<>("实例迁移中", 10),
                        new DefaultKeyValue<>("只读", 11),
                        new DefaultKeyValue<>("重启中", 12),
                        new DefaultKeyValue<>("实例修改中且待切换", 13),
                        new DefaultKeyValue<>("订阅发布创建中", 14),
                        new DefaultKeyValue<>("订阅发布修改中", 15),
                        new DefaultKeyValue<>("实例修改中且切换中", 16),
                        new DefaultKeyValue<>("创建RO副本中", 17)
                )).resetInstanceField(PlatformConstants.fit2cloud_tencent_platform, ResourceTypeConstants.SQL_SERVER);

        InstanceSearchField vport = new InstanceSearchField("实例访问端口", "vport", InstanceFieldType.Number)
                .resetInstanceField(PlatformConstants.fit2cloud_tencent_platform, ResourceTypeConstants.SQL_SERVER);

        InstanceSearchField memory = new InstanceSearchField("实例内存大小,单位G", "memory", InstanceFieldType.Number)
                .resetInstanceField(PlatformConstants.fit2cloud_tencent_platform, ResourceTypeConstants.SQL_SERVER);

        InstanceSearchField usedStorage = new InstanceSearchField("实例已经使用存储空间大小,单位G", "usedStorage", InstanceFieldType.Number)
                .resetInstanceField(PlatformConstants.fit2cloud_tencent_platform, ResourceTypeConstants.SQL_SERVER);

        InstanceSearchField storage = new InstanceSearchField("实例存储空间大小,单位G", "storage", InstanceFieldType.Number)
                .resetInstanceField(PlatformConstants.fit2cloud_tencent_platform, ResourceTypeConstants.SQL_SERVER);

        InstanceSearchField versionName = new InstanceSearchField("实例版本", "versionName", InstanceFieldType.String)
                .resetInstanceField(PlatformConstants.fit2cloud_tencent_platform, ResourceTypeConstants.SQL_SERVER);

        InstanceSearchField renewFlag = new InstanceSearchField("实例续费标记", "renewFlag", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("正常续费", 0),
                        new DefaultKeyValue<>("自动续费", 1),
                        new DefaultKeyValue<>("到期不续费", 2)
                ))
                .resetInstanceField(PlatformConstants.fit2cloud_tencent_platform, ResourceTypeConstants.SQL_SERVER);

        InstanceSearchField model = new InstanceSearchField("实例高可用", "model", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("双机高可用", 1),
                        new DefaultKeyValue<>("单机", 2),
                        new DefaultKeyValue<>("跨可用区", 3),
                        new DefaultKeyValue<>("集群跨可用区", 4),
                        new DefaultKeyValue<>("集群", 5),
                        new DefaultKeyValue<>("自研机房", 9)
                ))
                .resetInstanceField(PlatformConstants.fit2cloud_tencent_platform, ResourceTypeConstants.SQL_SERVER);

        InstanceSearchField payMode = new InstanceSearchField("实例付费模式", "payMode", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("按量计费", 0),
                        new DefaultKeyValue<>("包年包月", 1)
                ))
                .resetInstanceField(PlatformConstants.fit2cloud_tencent_platform, ResourceTypeConstants.SQL_SERVER);

        InstanceSearchField cpu = new InstanceSearchField("实例cpu核心数", "cpu", InstanceFieldType.Number)
                .resetInstanceField(PlatformConstants.fit2cloud_tencent_platform, ResourceTypeConstants.SQL_SERVER);

        InstanceSearchField dnsPodDomain = new InstanceSearchField("外网地址域名", "dnsPodDomain", InstanceFieldType.String)
                .resetInstanceField(PlatformConstants.fit2cloud_tencent_platform, ResourceTypeConstants.SQL_SERVER);

        InstanceSearchField collation = new InstanceSearchField("系统字符集排序规则", "collation", InstanceFieldType.String)
                .resetInstanceField(PlatformConstants.fit2cloud_tencent_platform, ResourceTypeConstants.SQL_SERVER);

        InstanceSearchField timeZone = new InstanceSearchField("系统时区", "timeZone", InstanceFieldType.String)
                .resetInstanceField(PlatformConstants.fit2cloud_tencent_platform, ResourceTypeConstants.SQL_SERVER);

        InstanceSearchField instanceType = new InstanceSearchField("实例类型", "instanceType", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("高可用", "HA"),
                        new DefaultKeyValue<>("只读实例", "RO"),
                        new DefaultKeyValue<>("基础版", "SI"),
                        new DefaultKeyValue<>("商业智能服务", "BI")
                ))
                .resetInstanceField(PlatformConstants.fit2cloud_tencent_platform, ResourceTypeConstants.SQL_SERVER);

        InstanceSearchField backupSaveDays = new InstanceSearchField("数据(日志)备份保留时间", "backupSaveDays", InstanceFieldType.Number)
                .resetInstanceField(PlatformConstants.fit2cloud_tencent_platform, ResourceTypeConstants.SQL_SERVER);

        InstanceSearchField crossBackupEnabled = new InstanceSearchField("跨地域备份状态", "crossBackupEnabled", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("开启", "enable"),
                        new DefaultKeyValue<>("关闭", "disable")
                ))
                .resetInstanceField(PlatformConstants.fit2cloud_tencent_platform, ResourceTypeConstants.SQL_SERVER);

        return List.of(status, vport, memory, usedStorage, storage, versionName, renewFlag, model, payMode, cpu, dnsPodDomain,
                collation, timeZone, instanceType, backupSaveDays, crossBackupEnabled);
    }

    /**
     * 获取 mariadb 实例查询字段
     *
     * @return 获取 mariadb 实例查询字段
     */
    public static List<InstanceSearchField> listMariadbInstanceSearchField() {
        InstanceSearchField status = new InstanceSearchField("实例状态", "status", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("创建中", 0),
                        new DefaultKeyValue<>("流程处理中", 1),
                        new DefaultKeyValue<>("运行中", 2),
                        new DefaultKeyValue<>("实例未初始化", 3),
                        new DefaultKeyValue<>("实例已隔离", -1),
                        new DefaultKeyValue<>("实例初始化中", 4),
                        new DefaultKeyValue<>("实例删除中", 5),
                        new DefaultKeyValue<>("实例重启中", 6),
                        new DefaultKeyValue<>("数据迁移中", 7)
                )).resetInstanceField(PlatformConstants.fit2cloud_tencent_platform, ResourceTypeConstants.MARIA_DB);

        InstanceSearchField wanStatus = new InstanceSearchField("外网状态", "wanStatus", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("未开通", 0),
                        new DefaultKeyValue<>("已开通", 1),
                        new DefaultKeyValue<>("关闭", 2),
                        new DefaultKeyValue<>("开通中", 3)
                ))
                .resetInstanceField(PlatformConstants.fit2cloud_tencent_platform, ResourceTypeConstants.MARIA_DB);

        InstanceSearchField wanDomain = new InstanceSearchField("外网访问的域名", "wanDomain", InstanceFieldType.String)
                .resetInstanceField(PlatformConstants.fit2cloud_tencent_platform, ResourceTypeConstants.MARIA_DB);

        InstanceSearchField autoRenewFlag = new InstanceSearchField("自动续费标志", "autoRenewFlag", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("否", 0),
                        new DefaultKeyValue<>("是", 1)
                ))
                .resetInstanceField(PlatformConstants.fit2cloud_tencent_platform, ResourceTypeConstants.MARIA_DB);

        InstanceSearchField memory = new InstanceSearchField("实例内存大小,单位 GB", "memory", InstanceFieldType.Number)
                .resetInstanceField(PlatformConstants.fit2cloud_tencent_platform, ResourceTypeConstants.MARIA_DB);

        InstanceSearchField storage = new InstanceSearchField("实例存储大小,单位 GB", "storage", InstanceFieldType.Number)
                .resetInstanceField(PlatformConstants.fit2cloud_tencent_platform, ResourceTypeConstants.MARIA_DB);

        InstanceSearchField nodeCount = new InstanceSearchField("节点数", "nodeCount", InstanceFieldType.Number)
                .resetInstanceField(PlatformConstants.fit2cloud_tencent_platform, ResourceTypeConstants.MARIA_DB);

        InstanceSearchField isTmp = new InstanceSearchField("是否临时实例", "isTmp", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("否", 0),
                        new DefaultKeyValue<>("是", 1)
                ))
                .resetInstanceField(PlatformConstants.fit2cloud_tencent_platform, ResourceTypeConstants.MARIA_DB);

        InstanceSearchField qps = new InstanceSearchField("最大 Qps 值", "qps", InstanceFieldType.Number)
                .resetInstanceField(PlatformConstants.fit2cloud_tencent_platform, ResourceTypeConstants.MARIA_DB);

        InstanceSearchField isAuditSupported = new InstanceSearchField("该实例是否支持审计", "isAuditSupported", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("不支持", 0),
                        new DefaultKeyValue<>("支持", 1)
                ))
                .resetInstanceField(PlatformConstants.fit2cloud_tencent_platform, ResourceTypeConstants.MARIA_DB);

        InstanceSearchField isEncryptSupported = new InstanceSearchField("是否支持数据加密", "isEncryptSupported", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("不支持", 0),
                        new DefaultKeyValue<>("支持", 1)
                ))
                .resetInstanceField(PlatformConstants.fit2cloud_tencent_platform, ResourceTypeConstants.MARIA_DB);

        InstanceSearchField cpu = new InstanceSearchField("实例CPU核数", "cpu", InstanceFieldType.Number)
                .resetInstanceField(PlatformConstants.fit2cloud_tencent_platform, ResourceTypeConstants.MARIA_DB);

        InstanceSearchField instanceType = new InstanceSearchField("实例类型", "instanceType", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("主实例（独享型）", 1),
                        new DefaultKeyValue<>("主实例", 2),
                        new DefaultKeyValue<>("灾备实例", 3),
                        new DefaultKeyValue<>("灾备实例（独享型）", 4)
                ))
                .resetInstanceField(PlatformConstants.fit2cloud_tencent_platform, ResourceTypeConstants.MARIA_DB);

        return List.of(status, wanStatus, memory, wanDomain, storage, autoRenewFlag, nodeCount, isTmp, qps, cpu, isAuditSupported,
                isEncryptSupported, instanceType);
    }

    /**
     * 获取 postgresql 实例查询字段
     *
     * @return PostGreSql 实例查询字段
     */
    public static List<InstanceSearchField> listPostgresInstanceSearchField() {

        InstanceSearchField status = new InstanceSearchField("实例状态", "dbinstanceStatus", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("申请中", "applying"),
                        new DefaultKeyValue<>("待初始化", "init"),
                        new DefaultKeyValue<>("初始化中", "initing"),
                        new DefaultKeyValue<>("运行中", "running"),
                        new DefaultKeyValue<>("受限运行", "limited run"),
                        new DefaultKeyValue<>("已隔离", "isolated"),
                        new DefaultKeyValue<>("回收中", "recycling"),
                        new DefaultKeyValue<>("已回收", "recycled"),
                        new DefaultKeyValue<>("任务执行中", "job running"),
                        new DefaultKeyValue<>("下线", "offline"),
                        new DefaultKeyValue<>("迁移中", "migrating"),
                        new DefaultKeyValue<>("扩容中", "expanding"),
                        new DefaultKeyValue<>("等待切换", "waitSwitch"),
                        new DefaultKeyValue<>("切换中", "switching"),
                        new DefaultKeyValue<>("只读", "readonly"),
                        new DefaultKeyValue<>("重启中", "restarting"),
                        new DefaultKeyValue<>("网络变更中", "network changing")
                ))
                .resetInstanceField(PlatformConstants.fit2cloud_tencent_platform, ResourceTypeConstants.POST_GRE_SQL);
        InstanceSearchField dbinstanceMemory = new InstanceSearchField("实例分配的内存大小,单位：GB", "dbinstanceMemory", InstanceFieldType.Number)
                .resetInstanceField(PlatformConstants.fit2cloud_tencent_platform, ResourceTypeConstants.POST_GRE_SQL);

        InstanceSearchField dbinstanceStorage = new InstanceSearchField("实例分配的存储空间大小,单位：GB", "dbinstanceStorage", InstanceFieldType.Number)
                .resetInstanceField(PlatformConstants.fit2cloud_tencent_platform, ResourceTypeConstants.POST_GRE_SQL);

        InstanceSearchField dbinstanceCpu = new InstanceSearchField("实例分配的CPU数量", "dbinstanceCpu", InstanceFieldType.Number)
                .resetInstanceField(PlatformConstants.fit2cloud_tencent_platform, ResourceTypeConstants.POST_GRE_SQL);

        InstanceSearchField dbinstanceType = new InstanceSearchField("实例类型", "dbinstanceType", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("主实例", "primary"),
                        new DefaultKeyValue<>("只读实例", "readonly"),
                        new DefaultKeyValue<>("灾备实例", "guard"),
                        new DefaultKeyValue<>("临时实例", "temp")))
                .resetInstanceField(PlatformConstants.fit2cloud_tencent_platform, ResourceTypeConstants.POST_GRE_SQL);

        InstanceSearchField dbcharset = new InstanceSearchField("字符集", "dbcharset", InstanceFieldType.String)
                .resetInstanceField(PlatformConstants.fit2cloud_tencent_platform, ResourceTypeConstants.POST_GRE_SQL);

        InstanceSearchField dbversion = new InstanceSearchField("PostgreSQL版本", "dbversion", InstanceFieldType.String)
                .resetInstanceField(PlatformConstants.fit2cloud_tencent_platform, ResourceTypeConstants.POST_GRE_SQL);

        InstanceSearchField payType = new InstanceSearchField("计费模式", "payType", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("包年包月", "prepaid"),
                        new DefaultKeyValue<>("按量计费", "postpaid")
                ))
                .resetInstanceField(PlatformConstants.fit2cloud_tencent_platform, ResourceTypeConstants.POST_GRE_SQL);

        InstanceSearchField autoRenew = new InstanceSearchField("自动续费标识", "autoRenew", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("自动续费", 1),
                        new DefaultKeyValue<>("不自动续费", 0)
                ))
                .resetInstanceField(PlatformConstants.fit2cloud_tencent_platform, ResourceTypeConstants.POST_GRE_SQL);

        InstanceSearchField supportIpv6 = new InstanceSearchField("是否支持ipv6", "supportIpv6", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("支持", 1),
                        new DefaultKeyValue<>("不支持", 0)
                ))
                .resetInstanceField(PlatformConstants.fit2cloud_tencent_platform, ResourceTypeConstants.POST_GRE_SQL);

        InstanceSearchField dbinstanceNetInfoNetType = new InstanceSearchField("网络类型", "dbinstanceNetInfo.netType", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("基础网络内网地址", "inner"),
                        new DefaultKeyValue<>("私有网络内网地址", "private"),
                        new DefaultKeyValue<>("基础网络或私有网络的外网地址", "public")
                ))
                .resetInstanceField(PlatformConstants.fit2cloud_tencent_platform, ResourceTypeConstants.POST_GRE_SQL);
        return List.of(status, dbinstanceMemory, dbinstanceStorage, dbinstanceCpu, dbinstanceType, dbcharset, dbversion, payType, autoRenew, supportIpv6,
                dbinstanceNetInfoNetType);
    }

    /**
     * redis 实例查询字段
     *
     * @return redis实例查询字段
     */
    public static List<InstanceSearchField> listRedisInstanceSearchField() {
        InstanceSearchField status = new InstanceSearchField("实例状态", "status", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("待初始化", 0),
                        new DefaultKeyValue<>("实例在流程中", 1),
                        new DefaultKeyValue<>("实例运行中", 2),
                        new DefaultKeyValue<>("实例已隔离", -2),
                        new DefaultKeyValue<>("实例待删除", -3)

                ))
                .resetInstanceField(PlatformConstants.fit2cloud_tencent_platform, ResourceTypeConstants.REDIS);

        InstanceSearchField port = new InstanceSearchField("实例端口", "port", InstanceFieldType.Number)
                .resetInstanceField(PlatformConstants.fit2cloud_tencent_platform, ResourceTypeConstants.REDIS);

        InstanceSearchField size = new InstanceSearchField("实例容量大小,单位：MB", "size", InstanceFieldType.Number)
                .resetInstanceField(PlatformConstants.fit2cloud_tencent_platform, ResourceTypeConstants.REDIS);

        InstanceSearchField type = new InstanceSearchField("实例类型", "type", InstanceFieldType.Enum,
                List.of(
                        new DefaultKeyValue<>("Redis2.8内存版（集群架构）", 1),
                        new DefaultKeyValue<>("Redis2.8内存版（标准架构）", 2),
                        new DefaultKeyValue<>("CKV 3.2内存版(标准架构)", 3),
                        new DefaultKeyValue<>("CKV 3.2内存版(集群架构)", 4),
                        new DefaultKeyValue<>("Redis2.8内存版（单机）", 5),
                        new DefaultKeyValue<>("Redis4.0内存版（标准架构）", 6),
                        new DefaultKeyValue<>("Redis4.0内存版（集群架构）", 7),
                        new DefaultKeyValue<>("Redis5.0内存版（标准架构）", 8),
                        new DefaultKeyValue<>("Redis5.0内存版（集群架构）", 9)
                ))
                .resetInstanceField(PlatformConstants.fit2cloud_tencent_platform, ResourceTypeConstants.REDIS);

        InstanceSearchField autoRenewFlag = new InstanceSearchField("实例是否设置自动续费标识", "autoRenewFlag", InstanceFieldType.Enum,
                List.of(
                        new DefaultKeyValue<>("设置自动续费", 1),
                        new DefaultKeyValue<>("未设置自动续费", 2)
                ))
                .resetInstanceField(PlatformConstants.fit2cloud_tencent_platform, ResourceTypeConstants.REDIS);

        InstanceSearchField productType = new InstanceSearchField("产品类型", "productType", InstanceFieldType.Enum,
                List.of(
                        new DefaultKeyValue<>("标准版", "standalone"),
                        new DefaultKeyValue<>("集群版", "cluster")
                ))
                .resetInstanceField(PlatformConstants.fit2cloud_tencent_platform, ResourceTypeConstants.REDIS);

        InstanceSearchField billingMode = new InstanceSearchField("计费模式", "billingMode", InstanceFieldType.Enum,
                List.of(
                        new DefaultKeyValue<>("按量计费", 0),
                        new DefaultKeyValue<>("包年包月", 1)
                ))
                .resetInstanceField(PlatformConstants.fit2cloud_tencent_platform, ResourceTypeConstants.REDIS);

        InstanceSearchField noAuth = new InstanceSearchField("是否为免密实例", "noAuth", InstanceFieldType.Enum,
                List.of(
                        new DefaultKeyValue<>("免密实例", true),
                        new DefaultKeyValue<>("非免密实例", false)
                ))
                .resetInstanceField(PlatformConstants.fit2cloud_tencent_platform, ResourceTypeConstants.REDIS);

        InstanceSearchField clientLimit = new InstanceSearchField("客户端连接数", "clientLimit", InstanceFieldType.Number)
                .resetInstanceField(PlatformConstants.fit2cloud_tencent_platform, ResourceTypeConstants.REDIS);

        InstanceSearchField netLimit = new InstanceSearchField("分片带宽上限,单位MB", "netLimit", InstanceFieldType.Number)
                .resetInstanceField(PlatformConstants.fit2cloud_tencent_platform, ResourceTypeConstants.REDIS);

        return List.of(status, port, size, type, autoRenewFlag, productType, billingMode, noAuth, clientLimit, netLimit);
    }

    /**
     * mongodb 实例查询字段
     *
     * @return mongodb 实例查询字段
     */
    public static List<InstanceSearchField> listMongodbInstanceSearchField() {
        InstanceSearchField payMode = new InstanceSearchField("付费类型", "payMode", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("包年包月", 1),
                        new DefaultKeyValue<>("按量计费", 0)
                ))
                .resetInstanceField(PlatformConstants.fit2cloud_tencent_platform, ResourceTypeConstants.MONGO_DB);

        InstanceSearchField clusterType = new InstanceSearchField("集群类型", "clusterType", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("分片实例", 1),
                        new DefaultKeyValue<>("副本集实例", 0)
                )).resetInstanceField(PlatformConstants.fit2cloud_tencent_platform, ResourceTypeConstants.MONGO_DB);

        InstanceSearchField netType = new InstanceSearchField("网络类型", "netType", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("私有网络", 1),
                        new DefaultKeyValue<>("基础网络", 0)
                )).resetInstanceField(PlatformConstants.fit2cloud_tencent_platform, ResourceTypeConstants.MONGO_DB);

        InstanceSearchField status = new InstanceSearchField("实例状态", "status", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("待初始化", 0),
                        new DefaultKeyValue<>("流程处理中", 1),
                        new DefaultKeyValue<>("运行中", 2),
                        new DefaultKeyValue<>("实例已过期", -2)
                )).resetInstanceField(PlatformConstants.fit2cloud_tencent_platform, ResourceTypeConstants.MONGO_DB);

        InstanceSearchField vport = new InstanceSearchField("端口号", "vport", InstanceFieldType.Number)
                .resetInstanceField(PlatformConstants.fit2cloud_tencent_platform, ResourceTypeConstants.MONGO_DB);

        InstanceSearchField mongoVersion = new InstanceSearchField("实例版本信息", "mongoVersion", InstanceFieldType.String)
                .resetInstanceField(PlatformConstants.fit2cloud_tencent_platform, ResourceTypeConstants.MONGO_DB);

        InstanceSearchField memory = new InstanceSearchField("实例内存规格,单位为MB", "memory", InstanceFieldType.Number)
                .resetInstanceField(PlatformConstants.fit2cloud_tencent_platform, ResourceTypeConstants.MONGO_DB);

        InstanceSearchField volume = new InstanceSearchField("实例磁盘规格,单位为MB", "volume", InstanceFieldType.Number)
                .resetInstanceField(PlatformConstants.fit2cloud_tencent_platform, ResourceTypeConstants.MONGO_DB);

        InstanceSearchField cpuNum = new InstanceSearchField("实例CPU核心数", "cpuNum", InstanceFieldType.Number)
                .resetInstanceField(PlatformConstants.fit2cloud_tencent_platform, ResourceTypeConstants.MONGO_DB);

        InstanceSearchField secondaryNum = new InstanceSearchField("实例从节点数", "secondaryNum", InstanceFieldType.Number)
                .resetInstanceField(PlatformConstants.fit2cloud_tencent_platform, ResourceTypeConstants.MONGO_DB);

        InstanceSearchField replicationSetNum = new InstanceSearchField("实例分片数", "replicationSetNum", InstanceFieldType.Number)
                .resetInstanceField(PlatformConstants.fit2cloud_tencent_platform, ResourceTypeConstants.MONGO_DB);

        InstanceSearchField autoRenewFlag = new InstanceSearchField("实例自动续费标志", "autoRenewFlag", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("手动续费", 0),
                        new DefaultKeyValue<>("自动续费", 1),
                        new DefaultKeyValue<>("确认不续费", 2)
                ))
                .resetInstanceField(PlatformConstants.fit2cloud_tencent_platform, ResourceTypeConstants.MONGO_DB);

        InstanceSearchField usedVolume = new InstanceSearchField("已用容量,单位MB", "usedVolume", InstanceFieldType.Number)
                .resetInstanceField(PlatformConstants.fit2cloud_tencent_platform, ResourceTypeConstants.MONGO_DB);

        InstanceSearchField instanceType = new InstanceSearchField("实例类型", "instanceType", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("正式实例", 1),
                        new DefaultKeyValue<>("临时实例", 2),
                        new DefaultKeyValue<>("只读实例", 3),
                        new DefaultKeyValue<>("灾备实例", 4)
                ))
                .resetInstanceField(PlatformConstants.fit2cloud_tencent_platform, ResourceTypeConstants.MONGO_DB);

        return List.of(status, payMode, clusterType, netType, autoRenewFlag, vport, mongoVersion, memory, volume, cpuNum,
                secondaryNum, replicationSetNum, usedVolume, instanceType);
    }

    /**
     * elasticsearch 实例查询字段
     *
     * @return elasticsearch  实例查询字段
     */
    public static List<InstanceSearchField> listElasticSearchInstanceSearchField() {
        InstanceSearchField status = new InstanceSearchField("实例状态", "status", InstanceFieldType.Enum,
                List.of(
                        new DefaultKeyValue<>("处理中", 0),
                        new DefaultKeyValue<>("正常", 1),
                        new DefaultKeyValue<>("停止", -1),
                        new DefaultKeyValue<>("销毁中", -2),
                        new DefaultKeyValue<>("已销毁", -3),
                        new DefaultKeyValue<>("创建集群时初始化中", 2)
                ))
                .resetInstanceField(PlatformConstants.fit2cloud_tencent_platform, ResourceTypeConstants.ELASTIC_SEARCH);

        InstanceSearchField chargeType = new InstanceSearchField("实例计费模式", "chargeType", InstanceFieldType.Enum,
                List.of(
                        new DefaultKeyValue<>("按量计费", "POSTPAID_BY_HOUR"),
                        new DefaultKeyValue<>("包年包月", "PREPAID"),
                        new DefaultKeyValue<>("CDH付费，即只对CDH计费", "CDHPAID")
                ))
                .resetInstanceField(PlatformConstants.fit2cloud_tencent_platform, ResourceTypeConstants.ELASTIC_SEARCH);

        InstanceSearchField chargePeriod = new InstanceSearchField("包年包月购买时长,单位:月", "chargePeriod", InstanceFieldType.Number)
                .resetInstanceField(PlatformConstants.fit2cloud_tencent_platform, ResourceTypeConstants.ELASTIC_SEARCH);

        InstanceSearchField nodeType = new InstanceSearchField("实例计费模式", "nodeType", InstanceFieldType.Enum,
                List.of(
                        new DefaultKeyValue<>("1核2G", "ES.S1.SMALL2"),
                        new DefaultKeyValue<>("2核4G", "ES.S1.MEDIUM4"),
                        new DefaultKeyValue<>("2核8G", "ES.S1.MEDIUM8"),
                        new DefaultKeyValue<>("4核16G", "ES.S1.LARGE16"),
                        new DefaultKeyValue<>("8核32G", "ES.S1.2XLARGE32"),
                        new DefaultKeyValue<>("16核32G", "ES.S1.4XLARGE32"),
                        new DefaultKeyValue<>("16核64G", "ES.S1.4XLARGE64")
                ))
                .resetInstanceField(PlatformConstants.fit2cloud_tencent_platform, ResourceTypeConstants.ELASTIC_SEARCH);

        InstanceSearchField nodeNum = new InstanceSearchField("节点个数", "nodeNum", InstanceFieldType.Number)
                .resetInstanceField(PlatformConstants.fit2cloud_tencent_platform, ResourceTypeConstants.ELASTIC_SEARCH);

        InstanceSearchField cpuNum = new InstanceSearchField("节点CPU核数", "cpuNum", InstanceFieldType.Number)
                .resetInstanceField(PlatformConstants.fit2cloud_tencent_platform, ResourceTypeConstants.ELASTIC_SEARCH);

        InstanceSearchField memSize = new InstanceSearchField("节点内存大小,单位GB", "memSize", InstanceFieldType.Number)
                .resetInstanceField(PlatformConstants.fit2cloud_tencent_platform, ResourceTypeConstants.ELASTIC_SEARCH);

        InstanceSearchField diskSize = new InstanceSearchField("节点磁盘大小,单位GB", "diskSize", InstanceFieldType.Number)
                .resetInstanceField(PlatformConstants.fit2cloud_tencent_platform, ResourceTypeConstants.ELASTIC_SEARCH);

        InstanceSearchField kibanaWhiteIpList = new InstanceSearchField("kibana白名单", "esAcl.whiteIpList", InstanceFieldType.ArrayString)
                .resetInstanceField(PlatformConstants.fit2cloud_tencent_platform, ResourceTypeConstants.ELASTIC_SEARCH);

        InstanceSearchField licenseType = new InstanceSearchField("License类型", "licenseType", InstanceFieldType.Enum,
                List.of(
                        new DefaultKeyValue<>("开源版", "oss"),
                        new DefaultKeyValue<>("基础版", "basic"),
                        new DefaultKeyValue<>("白金版", "platinum")
                ))
                .resetInstanceField(PlatformConstants.fit2cloud_tencent_platform, ResourceTypeConstants.ELASTIC_SEARCH);

        InstanceSearchField enableHotWarmMode = new InstanceSearchField("是否为冷热集群", "enableHotWarmMode", InstanceFieldType.Enum,
                List.of(
                        new DefaultKeyValue<>("冷热集群", true),
                        new DefaultKeyValue<>("非冷热集群", false),
                        new DefaultKeyValue<>("未知", null)
                ))
                .resetInstanceField(PlatformConstants.fit2cloud_tencent_platform, ResourceTypeConstants.ELASTIC_SEARCH);

        InstanceSearchField deployMode = new InstanceSearchField("部署模式", "deployMode", InstanceFieldType.Enum,
                List.of(
                        new DefaultKeyValue<>("单可用区", 0),
                        new DefaultKeyValue<>("多可用区", 1)
                ))
                .resetInstanceField(PlatformConstants.fit2cloud_tencent_platform, ResourceTypeConstants.ELASTIC_SEARCH);

        InstanceSearchField publicAccess = new InstanceSearchField("ES公网访问状态", "publicAccess", InstanceFieldType.Enum,
                List.of(
                        new DefaultKeyValue<>("开启", "OPEN"),
                        new DefaultKeyValue<>("关闭", "CLOSE")
                ))
                .resetInstanceField(PlatformConstants.fit2cloud_tencent_platform, ResourceTypeConstants.ELASTIC_SEARCH);

        InstanceSearchField kibanaPublicAccess = new InstanceSearchField("Kibana公网访问状态", "kibanaPublicAccess", InstanceFieldType.Enum,
                List.of(
                        new DefaultKeyValue<>("开启", "OPEN"),
                        new DefaultKeyValue<>("关闭", "CLOSE")
                ))
                .resetInstanceField(PlatformConstants.fit2cloud_tencent_platform, ResourceTypeConstants.ELASTIC_SEARCH);

        InstanceSearchField kibanaPrivateAccess = new InstanceSearchField("Kibana内网访问状态", "kibanaPrivateAccess", InstanceFieldType.Enum,
                List.of(
                        new DefaultKeyValue<>("开启", "OPEN"),
                        new DefaultKeyValue<>("关闭", "CLOSE")
                ))
                .resetInstanceField(PlatformConstants.fit2cloud_tencent_platform, ResourceTypeConstants.ELASTIC_SEARCH);

        InstanceSearchField securityType = new InstanceSearchField("Kibana内网访问状态", "securityType", InstanceFieldType.Enum,
                List.of(
                        new DefaultKeyValue<>("不开启", 1),
                        new DefaultKeyValue<>("开启", 2)
                ))
                .resetInstanceField(PlatformConstants.fit2cloud_tencent_platform, ResourceTypeConstants.ELASTIC_SEARCH);

        InstanceSearchField sceneType = new InstanceSearchField("Kibana内网访问状态", "sceneType", InstanceFieldType.Enum,
                List.of(
                        new DefaultKeyValue<>("不开启", 0),
                        new DefaultKeyValue<>("通用场景", 1),
                        new DefaultKeyValue<>("日志场景", 2),
                        new DefaultKeyValue<>("搜索场景", 3)
                ))
                .resetInstanceField(PlatformConstants.fit2cloud_tencent_platform, ResourceTypeConstants.ELASTIC_SEARCH);

        InstanceSearchField jdk = new InstanceSearchField("JDK类型", "jdk", InstanceFieldType.Enum,
                List.of(
                        new DefaultKeyValue<>("oracle", "oracle"),
                        new DefaultKeyValue<>("kona", "kona"),
                        new DefaultKeyValue<>("未知", null)
                ))
                .resetInstanceField(PlatformConstants.fit2cloud_tencent_platform, ResourceTypeConstants.ELASTIC_SEARCH);

        InstanceSearchField kibanaAlteringPublicAccess = new InstanceSearchField("Kibana的altering外网告警策略", "kibanaAlteringPublicAccess", InstanceFieldType.Enum,
                List.of(
                        new DefaultKeyValue<>("不开启", 1),
                        new DefaultKeyValue<>("开启", 2)
                ))
                .resetInstanceField(PlatformConstants.fit2cloud_tencent_platform, ResourceTypeConstants.ELASTIC_SEARCH);

        return List.of(status, chargeType, chargePeriod, nodeType, nodeNum, cpuNum, memSize, diskSize, kibanaWhiteIpList, licenseType,
                enableHotWarmMode, deployMode, publicAccess, kibanaPublicAccess, kibanaPrivateAccess, securityType, sceneType,
                jdk, kibanaAlteringPublicAccess
        );

    }

    /**
     * 获取负载均衡实例可查询字段
     *
     * @return 负载均衡实例可查询字段
     */
    public static List<InstanceSearchField> listLoadBalancerInstanceSearchField() {
        InstanceSearchField loadBalancerType = new InstanceSearchField("负载均衡实例的网络类型", "loadBalancerType", InstanceFieldType.Enum,
                List.of(
                        new DefaultKeyValue<>("公网属性", "OPEN"),
                        new DefaultKeyValue<>("内网属性", "INTERNAL")
                ))
                .resetInstanceField(PlatformConstants.fit2cloud_tencent_platform, ResourceTypeConstants.LOAD_BALANCER);

        InstanceSearchField forward = new InstanceSearchField("负载均衡类型标识", "forward", InstanceFieldType.Enum,
                List.of(
                        new DefaultKeyValue<>("负载均衡", 1),
                        new DefaultKeyValue<>("传统型负载均衡", 0)
                ))
                .resetInstanceField(PlatformConstants.fit2cloud_tencent_platform, ResourceTypeConstants.LOAD_BALANCER);

        InstanceSearchField status = new InstanceSearchField("负载均衡实例的状态", "status", InstanceFieldType.Enum,
                List.of(
                        new DefaultKeyValue<>("创建中", 0),
                        new DefaultKeyValue<>("正常运行", 1)
                ))
                .resetInstanceField(PlatformConstants.fit2cloud_tencent_platform, ResourceTypeConstants.LOAD_BALANCER);

        InstanceSearchField openBgp = new InstanceSearchField("高防 LB 的标识", "openBgp", InstanceFieldType.Enum,
                List.of(
                        new DefaultKeyValue<>("非高防负载均衡", 0),
                        new DefaultKeyValue<>("高防负载均衡", 1)
                ))
                .resetInstanceField(PlatformConstants.fit2cloud_tencent_platform, ResourceTypeConstants.LOAD_BALANCER);

        InstanceSearchField isolation = new InstanceSearchField("是否被隔离", "isolation", InstanceFieldType.Enum,
                List.of(
                        new DefaultKeyValue<>("未被隔离", 0),
                        new DefaultKeyValue<>("被隔离", 1)
                ))
                .resetInstanceField(PlatformConstants.fit2cloud_tencent_platform, ResourceTypeConstants.LOAD_BALANCER);

        InstanceSearchField chargeType = new InstanceSearchField("负载均衡实例的计费类型", "chargeType", InstanceFieldType.Enum,
                List.of(
                        new DefaultKeyValue<>("包年包月", "PREPAID"),
                        new DefaultKeyValue<>("按量计费", "POSTPAID_BY_HOUR")
                ))
                .resetInstanceField(PlatformConstants.fit2cloud_tencent_platform, ResourceTypeConstants.LOAD_BALANCER);

        InstanceSearchField isDDos = new InstanceSearchField("是否可绑定高防包", "isDDos", InstanceFieldType.Enum,
                List.of(
                        new DefaultKeyValue<>("是", true),
                        new DefaultKeyValue<>("否", false)
                ))
                .resetInstanceField(PlatformConstants.fit2cloud_tencent_platform, ResourceTypeConstants.LOAD_BALANCER);

        InstanceSearchField snatPro = new InstanceSearchField("是否开启SnatPro", "snatPro", InstanceFieldType.Enum,
                List.of(
                        new DefaultKeyValue<>("是", true),
                        new DefaultKeyValue<>("否", false)
                ))
                .resetInstanceField(PlatformConstants.fit2cloud_tencent_platform, ResourceTypeConstants.LOAD_BALANCER);

        InstanceSearchField isBlock = new InstanceSearchField("vip是否被封堵", "isBlock", InstanceFieldType.Enum,
                List.of(
                        new DefaultKeyValue<>("是", true),
                        new DefaultKeyValue<>("否", false)
                ))
                .resetInstanceField(PlatformConstants.fit2cloud_tencent_platform, ResourceTypeConstants.LOAD_BALANCER);

        InstanceSearchField localBgp = new InstanceSearchField("IP类型是否是本地BGP", "localBgp", InstanceFieldType.Enum,
                List.of(
                        new DefaultKeyValue<>("是", true),
                        new DefaultKeyValue<>("否", false)
                ))
                .resetInstanceField(PlatformConstants.fit2cloud_tencent_platform, ResourceTypeConstants.LOAD_BALANCER);

        InstanceSearchField mixIpTarget = new InstanceSearchField("开启IPv6FullChain负载均衡7层监听器支持混绑IPv4/IPv6目标功能", "mixIpTarget", InstanceFieldType.Enum,
                List.of(
                        new DefaultKeyValue<>("是", true),
                        new DefaultKeyValue<>("否", false)
                ))
                .resetInstanceField(PlatformConstants.fit2cloud_tencent_platform, ResourceTypeConstants.LOAD_BALANCER);
        return List.of(status, chargeType, loadBalancerType, forward, openBgp, isolation, isDDos, snatPro, isBlock, localBgp,
                mixIpTarget);
    }
}

