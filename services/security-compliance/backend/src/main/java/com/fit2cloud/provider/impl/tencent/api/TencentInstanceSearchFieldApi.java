package com.fit2cloud.provider.impl.tencent.api;

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
     * 云服务器实例前缀
     */
    public static final String ECS_INSTANCE_PREFIX = "instance.fit2cloud_tencent_platform_ECS.";

    /**
     * 对象存储实例前缀
     */
    public static final String OSS_INSTANCE_PREFIX = "instance.fit2cloud_tencent_platform_OSS.";

    public static final String DISK_INSTANCE_PREFIX = "instance.fit2cloud_tencent_platform_DISK.";

    public static final String VPC_INSTANCE_PREFIX = "instance.fit2cloud_tencent_platform_VPC.";

    public static final String PUBLIC_IP_INSTANCE_PREFIX = "instance.fit2cloud_tencent_platform_PUBLIC_IP.";
    public static final String SECURITY_INSTANCE_PREFIX = "instance.fit2cloud_tencent_platform_SECURITY_GROUP.";

    public static List<InstanceSearchField> listEcsInstanceSearchField() {
        InstanceSearchField instanceChargeType = new InstanceSearchField("计费模式", "instanceChargeType.keyword", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("包年包月", "PREPAID"),
                        new DefaultKeyValue<>("按量计费", "POSTPAID_BY_HOUR"),
                        new DefaultKeyValue<>("专用宿主机付费", "CDHPAID"),
                        new DefaultKeyValue<>("竞价实例付费", "SPOTPAID")
                ));

        InstanceSearchField instanceState = new InstanceSearchField("实例状态", "instanceState.keyword", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("创建中", "PENDING"),
                        new DefaultKeyValue<>("创建失败", "LAUNCH_FAILED"),
                        new DefaultKeyValue<>("运行中", "RUNNING"),
                        new DefaultKeyValue<>("关机", "STOPPED"),
                        new DefaultKeyValue<>("开机中", "STARTING"),
                        new DefaultKeyValue<>("关机中", "STOPPING"),
                        new DefaultKeyValue<>("重启中", "REBOOTING"),
                        new DefaultKeyValue<>("停止待销毁", "SHUTDOWN"),
                        new DefaultKeyValue<>("销毁中", "TERMINATING")));

        InstanceSearchField cpu = new InstanceSearchField("CPU", "cpu", InstanceFieldType.Number);

        InstanceSearchField memory = new InstanceSearchField("内存", "memory", InstanceFieldType.Number);

        InstanceSearchField restrictState = new InstanceSearchField("实例业务状态", "RestrictState.keyword", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("正常状态的实例", "NORMAL"),
                        new DefaultKeyValue<>("过期的实例", "EXPIRED"),
                        new DefaultKeyValue<>("被安全隔离的实例", "PROTECTIVELY_ISOLATED")));

        InstanceSearchField renewFlag = new InstanceSearchField("自动续费标识", "renewFlag.keyword", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("通知即将过期，但不自动续费", "NOTIFY_AND_MANUAL_RENEW"),
                        new DefaultKeyValue<>("通知即将过期，而且自动续费", "NOTIFY_AND_AUTO_RENEW"),
                        new DefaultKeyValue<>("不通知即将过期，也不自动续费", "DISABLE_NOTIFY_AND_MANUAL_RENEW")));

        InstanceSearchField stopChargingMode = new InstanceSearchField("关机计费模式", "stopChargingMode.keyword", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("关机继续收费", "KEEP_CHARGING"),
                        new DefaultKeyValue<>("关机停止收费", "STOP_CHARGING"),
                        new DefaultKeyValue<>("实例处于非关机状态或者不适用关机停止计费的条件", "NOT_APPLICABLE")));

        InstanceSearchField isolatedSource = new InstanceSearchField("实例隔离类型", "isolatedSource.keyword", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("欠费隔离", "ARREAR"),
                        new DefaultKeyValue<>("到期隔离", "EXPIRE"),
                        new DefaultKeyValue<>("主动退还隔离", "MANMADE"),
                        new DefaultKeyValue<>("未隔离", "NOTISOLATED")));

        InstanceSearchField defaultLoginUser = new InstanceSearchField("默认登录用户", "defaultLoginUser.keyword", InstanceFieldType.String);

        InstanceSearchField defaultLoginPort = new InstanceSearchField("默认登录端口", "defaultLoginPort.keyword", InstanceFieldType.String);

        InstanceSearchField systemDiskType = new InstanceSearchField("系统盘类型", "systemDisk.diskType.keyword", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("本地硬盘", "LOCAL_BASIC"),
                        new DefaultKeyValue<>("本地SSD硬盘", "LOCAL_SSD"),
                        new DefaultKeyValue<>("普通云硬盘", "CLOUD_BASIC"),
                        new DefaultKeyValue<>("SSD云硬盘", "CLOUD_SSD"),
                        new DefaultKeyValue<>("高性能云硬盘", "CLOUD_PREMIUM"),
                        new DefaultKeyValue<>("通用性SSD云硬盘", "CLOUD_BSSD")));

        InstanceSearchField systemDiskSize = new InstanceSearchField("系统盘大小", "systemDisk.diskSize", InstanceFieldType.Number);

        InstanceSearchField dataDiskSize = new InstanceSearchField("数据盘大小", "dataDisks.diskSize", InstanceFieldType.ArrayNumber);

        InstanceSearchField dataDiskDeleteWithInstance = new InstanceSearchField("数据盘是否随子机销毁", "dataDisks.deleteWithInstance", InstanceFieldType.ArrayEnum,
                List.of(new DefaultKeyValue<>("销毁数据盘", true),
                        new DefaultKeyValue<>("保留数据盘", false)));

        InstanceSearchField dataDiskEncrypt = new InstanceSearchField("数据盘是否加密", "dataDisks.encrypt", InstanceFieldType.ArrayEnum,
                List.of(new DefaultKeyValue<>("加密", true),
                        new DefaultKeyValue<>("不加密", false)));

        InstanceSearchField dataDiskType = new InstanceSearchField("数据盘类型", "dataDisks.diskType.keyword", InstanceFieldType.ArrayEnum,
                List.of(new DefaultKeyValue<>("本地硬盘", "LOCAL_BASIC"),
                        new DefaultKeyValue<>("本地SSD硬盘", "LOCAL_SSD"),
                        new DefaultKeyValue<>("本地NVME硬盘", "LOCAL_NVME"),
                        new DefaultKeyValue<>("本地HDD硬盘", "LOCAL_PRO"),
                        new DefaultKeyValue<>("普通云硬盘", "CLOUD_BASIC"),
                        new DefaultKeyValue<>("高性能云硬盘", "CLOUD_PREMIUM"),
                        new DefaultKeyValue<>("SSD云硬盘", "CLOUD_SSD"),
                        new DefaultKeyValue<>("增强型SSD云硬盘", "CLOUD_HSSD"),
                        new DefaultKeyValue<>("极速型SSD云硬盘", "CLOUD_TSSD"),
                        new DefaultKeyValue<>("通用型SSD云硬盘", "CLOUD_BSSD")));

        InstanceSearchField internetAccessibleInternetChargeType = new InstanceSearchField("网络计费类型", "internetAccessible.internetChargeType.keyword", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("预付费按带宽结算", "BANDWIDTH_PREPAID"),
                        new DefaultKeyValue<>("流量按小时后付费", "TRAFFIC_POSTPAID_BY_HOUR"),
                        new DefaultKeyValue<>("带宽按小时后付费", "BANDWIDTH_POSTPAID_BY_HOUR"),
                        new DefaultKeyValue<>("带宽包用户", "BANDWIDTH_PACKAGE")));

        InstanceSearchField internetAccessiblePublicIpAssigned = new InstanceSearchField("是否分配公网IP", "internetAccessible.publicIpAssigned", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("分配公网IP", true),
                        new DefaultKeyValue<>("不分配公网IP", false)
                ));

        InstanceSearchField internetAccessibleInternetAccessible = new InstanceSearchField("是否分配公网IP", "internetAccessible.internetMaxBandwidthOut", InstanceFieldType.Number);
        return FieldUtil.appendPrefixField(ECS_INSTANCE_PREFIX, List.of(instanceChargeType, instanceState, cpu, memory, renewFlag, restrictState, stopChargingMode, isolatedSource,
                defaultLoginUser, defaultLoginPort, systemDiskType, systemDiskSize, dataDiskSize, dataDiskDeleteWithInstance, dataDiskEncrypt, dataDiskType
                , internetAccessibleInternetChargeType, internetAccessiblePublicIpAssigned, internetAccessibleInternetAccessible));
    }

    public static List<InstanceSearchField> listOSSInstanceSearchField() {
        InstanceSearchField refererStatus = new InstanceSearchField("是否开启防盗链", "referer.status.keyword", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("开启", "Enabled"),
                        new DefaultKeyValue<>("关闭", null)
                ));

        InstanceSearchField refererType = new InstanceSearchField("防盗链类型", "referer.refererType.keyword", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("黑名单", "Black-List"),
                        new DefaultKeyValue<>("白名单", "White-List")
                ));
        InstanceSearchField refererDomainList = new InstanceSearchField("生效域名列表", "referer.domainList", InstanceFieldType.ArrayString);

        InstanceSearchField refererEmptyReferConfiguration = new InstanceSearchField("是否允许空Referer访问", "referer.emptyReferConfiguration.keyword", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("允许", "Allow"),
                        new DefaultKeyValue<>("不允许", "Deny")
                ));

        InstanceSearchField accessCannedAccessControl = new InstanceSearchField("公共权限", "access.cannedAccessControl.keyword", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("允许", "Private"),
                        new DefaultKeyValue<>("公有读私有写", "PublicRead"),
                        new DefaultKeyValue<>("公有读写", "PublicReadWrite")
                ));
        InstanceSearchField encryptionSseAlgorithm = new InstanceSearchField("加密类型", "encryption.rule.applyServerSideEncryptionByDefault.sseAlgorithm.keyword", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("未开启", null),
                        new DefaultKeyValue<>("KMS", "KMS"),
                        new DefaultKeyValue<>("AES256", "AES256")));

        return FieldUtil.appendPrefixField(OSS_INSTANCE_PREFIX, List.of(refererStatus, refererType, refererDomainList, refererEmptyReferConfiguration
                , accessCannedAccessControl, encryptionSseAlgorithm));

    }

    public static List<InstanceSearchField> listDISKInstanceSearchField() {
        InstanceSearchField deleteWithInstance = new InstanceSearchField("云盘是否与挂载的实例一起销毁", "deleteWithInstance", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("销毁实例时会同时销毁云盘", true),
                        new DefaultKeyValue<>("销毁实例时不销毁云盘", false)));

        InstanceSearchField renewFlag = new InstanceSearchField("自动续费标识", "renewFlag.keyword", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("通知过期且自动续费", "NOTIFY_AND_AUTO_RENEW"),
                        new DefaultKeyValue<>("通知过期不自动续费", "NOTIFY_AND_MANUAL_RENEW"),
                        new DefaultKeyValue<>("不通知过期不自动续费", "DISABLE_NOTIFY_AND_MANUAL_RENEW"),
                        new DefaultKeyValue<>("取不到有效值", null)));

        InstanceSearchField diskType = new InstanceSearchField("硬盘介质类型", "diskType.keyword", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("普通云硬盘", "CLOUD_BASIC"),
                        new DefaultKeyValue<>("高性能云硬盘", "CLOUD_PREMIUM"),
                        new DefaultKeyValue<>("通用型SSD云硬盘", "CLOUD_BSSD"),
                        new DefaultKeyValue<>("SSD云硬盘", "CLOUD_SSD"),
                        new DefaultKeyValue<>("增强型SSD云硬盘", "CLOUD_HSSD"),
                        new DefaultKeyValue<>("极速型SSD云硬盘", "CLOUD_TSSD")));

        InstanceSearchField diskState = new InstanceSearchField("云盘状态", "diskState.keyword", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("未挂载", "UNATTACHED"),
                        new DefaultKeyValue<>("挂载中", "ATTACHING"),
                        new DefaultKeyValue<>("已挂载", "ATTACHED"),
                        new DefaultKeyValue<>("解挂中", "DETACHING"),
                        new DefaultKeyValue<>("扩容中", "EXPANDING"),
                        new DefaultKeyValue<>("回滚中", "ROLLBACKING"),
                        new DefaultKeyValue<>("待回收", "TORECYCLE"),
                        new DefaultKeyValue<>("拷贝硬盘中", "DUMPING")));

        InstanceSearchField snapshotCount = new InstanceSearchField("云盘拥有的快照总数", "snapshotCount", InstanceFieldType.Number);

        InstanceSearchField autoRenewFlagError = new InstanceSearchField("云盘已挂载到子机", "autoRenewFlagError", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("子机设置了自动续费标识，但云盘未设置", true),
                        new DefaultKeyValue<>("云盘自动续费标识正常", false)));

        InstanceSearchField rollbacking = new InstanceSearchField("云盘是否处于快照回滚状态", "rollbacking", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("不处于快照回滚状态。", false),
                        new DefaultKeyValue<>("处于快照回滚状态", true)));
        InstanceSearchField encrypt = new InstanceSearchField("云盘是否为加密盘", "encrypt", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("非加密盘。", false),
                        new DefaultKeyValue<>("加密盘", true)));

        InstanceSearchField backupDisk = new InstanceSearchField("云硬盘因欠费销毁或者到期销毁时,是否使用快照备份数据的标识", "backupDisk", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("表示直接销毁", false),
                        new DefaultKeyValue<>("销毁时创建快照进行数据备份", true)));

        InstanceSearchField throughputPerformance = new InstanceSearchField("云硬盘额外性能值,单位MB/s", "throughputPerformance", InstanceFieldType.Number);
        InstanceSearchField migrating = new InstanceSearchField("云盘是否处于类型变更中", "migrating", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("云盘不处于类型变更中。", false),
                        new DefaultKeyValue<>("云盘已发起类型变更，正处于迁移中", true)));

        InstanceSearchField snapshotSize = new InstanceSearchField("云盘拥有的快照总容量,单位为MB", "snapshotSize", InstanceFieldType.Number);

        InstanceSearchField isReturnable = new InstanceSearchField("预付费的云盘是否支持主动退还", "isReturnable", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("不支持主动退还", false),
                        new DefaultKeyValue<>("支持主动退还", true)));

        InstanceSearchField attached = new InstanceSearchField("云盘是否挂载到云主机上", "attached", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("未挂载", false),
                        new DefaultKeyValue<>("已挂载", true)));
        InstanceSearchField diskSize = new InstanceSearchField("云硬盘大小,单位GB", "diskSize", InstanceFieldType.Number);

        InstanceSearchField diskUsage = new InstanceSearchField("云盘类型", "diskUsage.keyword", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("系统盘", "SYSTEM_DISK"),
                        new DefaultKeyValue<>("数据盘", "DATA_DISK")));

        InstanceSearchField diskChargeType = new InstanceSearchField("付费模式", "diskChargeType.keyword", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("包年包月", "PREPAID"),
                        new DefaultKeyValue<>("按量计费", "POSTPAID_BY_HOUR")));

        InstanceSearchField portable = new InstanceSearchField("是否为弹性云盘", "portable", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("非弹性云盘", false),
                        new DefaultKeyValue<>("弹性云盘", true)));

        InstanceSearchField snapshotAbility = new InstanceSearchField("云盘是否具备创建快照的能力", "snapshotAbility", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("不具备", false),
                        new DefaultKeyValue<>("具备", true)));

        InstanceSearchField deadlineError = new InstanceSearchField("云盘到期时间与实例到期时间状态", "deadlineError", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("云盘到期时间早于实例", true),
                        new DefaultKeyValue<>("云盘到期时间晚于实例", false)));
        InstanceSearchField differDaysOfDeadline = new InstanceSearchField("当前时间距离盘到期的天数(仅对包年包月盘有意义)", "differDaysOfDeadline", InstanceFieldType.Number);

        InstanceSearchField shareable = new InstanceSearchField("云盘是否为共享型云盘", "deadlineError", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("是", true),
                        new DefaultKeyValue<>("否", false)));

        InstanceSearchField deleteSnapshot = new InstanceSearchField("销毁云盘时删除关联的非永久保留快照", "deleteSnapshot", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("非永久快照不随云盘销毁而销毁", 0),
                        new DefaultKeyValue<>("非永久快照随云盘销毁而销毁", 1)));

        InstanceSearchField diskBackupCount = new InstanceSearchField("云硬盘备份点已使用的数量", "deleteSnapshot", InstanceFieldType.Number);

        InstanceSearchField instanceType = new InstanceSearchField("云硬盘挂载实例的类型", "instanceType", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("CVM", "CVM"),
                        new DefaultKeyValue<>("EKS", "EKS")));

        return FieldUtil.appendPrefixField(DISK_INSTANCE_PREFIX, List.of(
                deleteWithInstance, renewFlag, diskType, diskState
                , snapshotCount, autoRenewFlagError, rollbacking, encrypt, backupDisk, throughputPerformance, migrating, snapshotSize, isReturnable,
                attached, diskSize, diskUsage, diskChargeType, portable, snapshotAbility, deadlineError, differDaysOfDeadline, shareable, deleteSnapshot, diskBackupCount,
                instanceType));
    }

    public static List<InstanceSearchField> listVpcInstanceSearchField() {
        InstanceSearchField isDefault = new InstanceSearchField("是否默认VPC", "isDefault", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("是", true),
                        new DefaultKeyValue<>("否", false)));

        InstanceSearchField enableMulticast = new InstanceSearchField("是否开启组播", "enableMulticast", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("是", true),
                        new DefaultKeyValue<>("否", false)));
        InstanceSearchField cidrBlock = new InstanceSearchField("VPC的IPv4 CIDR", "cidrBlock.keyword", InstanceFieldType.String);

        InstanceSearchField enableDhcp = new InstanceSearchField("是否开启DHCP", "enableDhcp", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("是", true),
                        new DefaultKeyValue<>("否", false)));

        return FieldUtil.appendPrefixField(VPC_INSTANCE_PREFIX, List.of(
                isDefault, enableMulticast, cidrBlock, enableDhcp
        ));
    }

    public static List<InstanceSearchField> listPublicIpInstanceSearchField() {

        InstanceSearchField addressStatus = new InstanceSearchField("EIP状态", "addressStatus.keyword", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("创建中", "CREATING"),
                        new DefaultKeyValue<>("绑定中", "BINDING"),
                        new DefaultKeyValue<>("已绑定", "BIND"),
                        new DefaultKeyValue<>("解绑中", "UNBINDING"),
                        new DefaultKeyValue<>("已解绑", "UNBIND"),
                        new DefaultKeyValue<>("释放中", "OFFLINING"),
                        new DefaultKeyValue<>("绑定悬空弹性网卡", "BIND_ENI")));

        InstanceSearchField isArrears = new InstanceSearchField("资源隔离状态", "isArrears", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("隔离状态", true),
                        new DefaultKeyValue<>("未隔离状态", false)));

        InstanceSearchField isBlocked = new InstanceSearchField("资源封堵状态", "isBlocked", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("封堵状态", true),
                        new DefaultKeyValue<>("未封堵状态", false)));

        InstanceSearchField isEipDirectConnection = new InstanceSearchField("支持直通模式", "isEipDirectConnection", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("支持直通模式", true),
                        new DefaultKeyValue<>("不支持直通模式", false)));

        InstanceSearchField addressType = new InstanceSearchField("资源类型", "addressType.keyword", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("设备 IP", "CalcIP"),
                        new DefaultKeyValue<>("普通公网 IP", "WanIP"),
                        new DefaultKeyValue<>("弹性公网 IP", "EIP"),
                        new DefaultKeyValue<>("加速 EIP", "AnycastEip"),
                        new DefaultKeyValue<>("高防EIP", "AntiDDoSEIP")));


        InstanceSearchField cascadeRelease = new InstanceSearchField("eip是否在解绑后自动释放", "cascadeRelease", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("解绑后自动释放", true),
                        new DefaultKeyValue<>("解绑后不会自动释放", false)));

        InstanceSearchField bandwidth = new InstanceSearchField("eip是否在解绑后自动释放", "bandwidth", InstanceFieldType.Number);

        InstanceSearchField internetChargeType = new InstanceSearchField("网络计费模式", "internetChargeType.keyword", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("包月带宽预付费", "BANDWIDTH_PREPAID_BY_MONTH"),
                        new DefaultKeyValue<>("按小时流量后付费", "TRAFFIC_POSTPAID_BY_HOUR"),
                        new DefaultKeyValue<>("按小时带宽后付费", "BANDWIDTH_POSTPAID_BY_HOUR"),
                        new DefaultKeyValue<>("共享带宽包", "BANDWIDTH_PACKAGE"),
                        new DefaultKeyValue<>("未知", null)));

        return FieldUtil.appendPrefixField(PUBLIC_IP_INSTANCE_PREFIX, List.of(
                addressStatus, isArrears, isBlocked, isEipDirectConnection, addressType, cascadeRelease, bandwidth,
                internetChargeType
        ));

    }


    public static List<InstanceSearchField> listSecurityGroupInstanceSearchField() {
        InstanceSearchField isDefault = new InstanceSearchField("是否是默认安全组", "isDefault", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("是", true),
                        new DefaultKeyValue<>("否", false)));

        InstanceSearchField ruleIngressProtocol = new InstanceSearchField("安全组入站协议", "rule.ingress.protocol.keyword", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("TCP", "TCP"),
                        new DefaultKeyValue<>("UDP", "UDP"),
                        new DefaultKeyValue<>("ICMP", "ICMP"),
                        new DefaultKeyValue<>("ICMPv6", "ICMPv6"),
                        new DefaultKeyValue<>("ALL", "ALL")
                ));

        InstanceSearchField ruleIngressCidrBlock = new InstanceSearchField("安全组入站网段", "rule.ingress.cidrBlock.keyword", InstanceFieldType.String);

        InstanceSearchField ruleEgressProtocol = new InstanceSearchField("安全组出站协议", "rule.egress.protocol.keyword", InstanceFieldType.ArrayEnum,
                List.of(new DefaultKeyValue<>("TCP", "TCP"),
                        new DefaultKeyValue<>("UDP", "UDP"),
                        new DefaultKeyValue<>("ICMP", "ICMP"),
                        new DefaultKeyValue<>("ICMPv6", "ICMPv6"),
                        new DefaultKeyValue<>("ALL", "ALL")
                ));

        InstanceSearchField ruleEgressCidrBlock = new InstanceSearchField("安全组出站网段", "rule.egress.cidrBlock.keyword", InstanceFieldType.ArrayString);
        return FieldUtil.appendPrefixField(SECURITY_INSTANCE_PREFIX, List.of(
                isDefault, ruleIngressProtocol, ruleIngressCidrBlock, ruleEgressProtocol, ruleEgressCidrBlock
        ));
    }

    public static List<InstanceSearchField> listUserInstanceSearchField() {
        return List.of();
    }
}
