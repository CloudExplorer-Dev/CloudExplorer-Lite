package com.fit2cloud.provider.impl.aliyun.api;

import com.fit2cloud.common.constants.PlatformConstants;
import com.fit2cloud.constants.ResourceTypeConstants;
import com.fit2cloud.provider.entity.InstanceFieldType;
import com.fit2cloud.provider.entity.InstanceSearchField;
import org.apache.commons.collections4.keyvalue.DefaultKeyValue;

import java.util.List;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2022/12/7  18:23}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
public class AliInstanceSearchFieldApi {

    /**
     * 获取阿里云 云服务器 ecs 查询字段
     *
     * @return 阿里云云服务器ecs查询字段
     */
    public static List<InstanceSearchField> listEcsInstanceSearchField() {
        InstanceSearchField cpu = new InstanceSearchField("cpu", "cpu", InstanceFieldType.Number)
                .resetInstanceField(PlatformConstants.fit2cloud_ali_platform, ResourceTypeConstants.ECS);

        InstanceSearchField memory = new InstanceSearchField("内存大小,单位为MiB", "memory", InstanceFieldType.Number)
                .resetInstanceField(PlatformConstants.fit2cloud_ali_platform, ResourceTypeConstants.ECS);

        InstanceSearchField instanceStatus = new InstanceSearchField("实例状态", "status", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("创建中", "Pending"),
                        new DefaultKeyValue<>("运行中", "Running"),
                        new DefaultKeyValue<>("启动中", "Starting"),
                        new DefaultKeyValue<>("停止中", "Stopping"),
                        new DefaultKeyValue<>("已停止", "Stopped")))
                .resetInstanceField(PlatformConstants.fit2cloud_ali_platform, ResourceTypeConstants.ECS);

        InstanceSearchField lockReason = new InstanceSearchField("实例的锁定原因", "operationLocks.lockReason", InstanceFieldType.ArrayEnum,
                List.of(new DefaultKeyValue<>("因欠费被锁定", "financial"),
                        new DefaultKeyValue<>("因安全原因被锁定", "security"),
                        new DefaultKeyValue<>("抢占式实例的待释放锁定状态", "Recycling"),
                        new DefaultKeyValue<>("因为专有宿主机欠费导致ECS实例被锁定", "dedicatedhostfinancial"),
                        new DefaultKeyValue<>("因退款被锁定", "refunded")))
                .resetInstanceField(PlatformConstants.fit2cloud_ali_platform, ResourceTypeConstants.ECS);

        InstanceSearchField osType = new InstanceSearchField("操作系统类型", "OSType", InstanceFieldType.String)
                .resetInstanceField(PlatformConstants.fit2cloud_ali_platform, ResourceTypeConstants.ECS);

        InstanceSearchField instanceType = new InstanceSearchField("实例规格", "instanceType", InstanceFieldType.String)
                .resetInstanceField(PlatformConstants.fit2cloud_ali_platform, ResourceTypeConstants.ECS);

        InstanceSearchField gpu = new InstanceSearchField("GPU", "GPUAmount", InstanceFieldType.Number)
                .resetInstanceField(PlatformConstants.fit2cloud_ali_platform, ResourceTypeConstants.ECS);


        InstanceSearchField publicIpAddress = new InstanceSearchField("公网地址", "publicIpAddress.ipAddress", InstanceFieldType.ArrayString)
                .resetInstanceField(PlatformConstants.fit2cloud_ali_platform, ResourceTypeConstants.ECS);

        InstanceSearchField imageId = new InstanceSearchField("镜像Id", "imageId", InstanceFieldType.String)
                .resetInstanceField(PlatformConstants.fit2cloud_ali_platform, ResourceTypeConstants.ECS);

        InstanceSearchField securityGroupName = new InstanceSearchField("安全组名称", "securityGroupName", InstanceFieldType.String)
                .resetFilterArrayField(PlatformConstants.fit2cloud_ali_platform, ResourceTypeConstants.ECS, "securityGroupRules");

        InstanceSearchField securityGroupDescription = new InstanceSearchField("安全组描述信息", "description", InstanceFieldType.String)
                .resetFilterArrayField(PlatformConstants.fit2cloud_ali_platform, ResourceTypeConstants.ECS, "securityGroupRules");

        InstanceSearchField securityGroupDirection = new InstanceSearchField("安全组授权方向", "securityGroupName.rule.permissions.permission.direction", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("出方向", "egress"),
                        new DefaultKeyValue<>("入方向", "ingress")))
                .resetFilterArrayField(PlatformConstants.fit2cloud_ali_platform, ResourceTypeConstants.ECS, "securityGroupRules");

        InstanceSearchField securityGroupIpProtocol = new InstanceSearchField("安全组IP协议", "securityGroupName.rule.permissions.permission.ipProtocol", InstanceFieldType.String)
                .resetFilterArrayField(PlatformConstants.fit2cloud_ali_platform, ResourceTypeConstants.ECS, "securityGroupRules");

        InstanceSearchField securityGroupIpPolicy = new InstanceSearchField("安全组授权策略", "securityGroupName.rule.permissions.permission.policy", InstanceFieldType.String)
                .resetFilterArrayField(PlatformConstants.fit2cloud_ali_platform, ResourceTypeConstants.ECS, "securityGroupRules");

        InstanceSearchField securityGroupPortRange = new InstanceSearchField("安全组端口区间", "securityGroupName.rule.permissions.permission.portRange", InstanceFieldType.String)
                .resetFilterArrayField(PlatformConstants.fit2cloud_ali_platform, ResourceTypeConstants.ECS, "securityGroupRules");

        InstanceSearchField securityGroupSourceCidrIp = new InstanceSearchField("安全组源端IP地址段", "securityGroupName.rule.permissions.permission.sourceCidrIp", InstanceFieldType.String)
                .resetFilterArrayField(PlatformConstants.fit2cloud_ali_platform, ResourceTypeConstants.ECS, "securityGroupRules");

        InstanceSearchField securityGroupDestCidrIp = new InstanceSearchField("安全组源目的端IP地址段", "securityGroupName.rule.permissions.permission.destCidrIp", InstanceFieldType.String)
                .resetFilterArrayField(PlatformConstants.fit2cloud_ali_platform, ResourceTypeConstants.ECS, "securityGroupRules");

        InstanceSearchField diskLockReason = new InstanceSearchField("磁盘锁定原因", "lockReason", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("因欠费被锁定", "financial"),
                        new DefaultKeyValue<>("因安全原因被锁定", "security"),
                        new DefaultKeyValue<>("抢占式实例的待释放锁定状态", "recycling"),
                        new DefaultKeyValue<>("因为专有宿主机欠费导致ECS实例被锁定", "dedicatedhostfinancial")))
                .resetFilterArrayField(PlatformConstants.fit2cloud_ali_platform, ResourceTypeConstants.ECS, "disks");

        InstanceSearchField diskSize = new InstanceSearchField("磁盘大小,单位GiB", "size", InstanceFieldType.Number)
                .resetFilterArrayField(PlatformConstants.fit2cloud_ali_platform, ResourceTypeConstants.ECS, "disks", false);

        InstanceSearchField diskType = new InstanceSearchField("磁盘类型", "type", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("系统盘", "system"),
                        new DefaultKeyValue<>("数据盘", "data")))
                .resetFilterArrayField(PlatformConstants.fit2cloud_ali_platform, ResourceTypeConstants.ECS, "disks");

        InstanceSearchField autoRenewEnabled = new InstanceSearchField("是否开启自动续费", "autoRenew.autoRenewEnabled", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("开启", true),
                        new DefaultKeyValue<>("未开启", false)))
                .resetInstanceField(PlatformConstants.fit2cloud_ali_platform, ResourceTypeConstants.ECS);


        InstanceSearchField spotStrategy = new InstanceSearchField("竞价策略", "spotStrategy", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("正常按量付费实例", "NoSpot"),
                        new DefaultKeyValue<>("设置上限价格的抢占式实例", "SpotWithPriceLimit"),
                        new DefaultKeyValue<>("系统自动出价", "SpotAsPriceGo")))
                .resetInstanceField(PlatformConstants.fit2cloud_ali_platform, ResourceTypeConstants.ECS);

        InstanceSearchField deviceAvailable = new InstanceSearchField("是否可挂载数据盘", "deviceAvailable", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("可挂载数据盘", true),
                        new DefaultKeyValue<>("不可挂载数据盘", false)))
                .resetInstanceField(PlatformConstants.fit2cloud_ali_platform, ResourceTypeConstants.ECS);

        InstanceSearchField instanceNetworkType = new InstanceSearchField("网络类型", "instanceNetworkType", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("经典网络", "classic"),
                        new DefaultKeyValue<>("专有网络VPC", "vpc")))
                .resetInstanceField(PlatformConstants.fit2cloud_ali_platform, ResourceTypeConstants.ECS);


        InstanceSearchField internetMaxBandwidthOut = new InstanceSearchField("公网出带宽最大值,单位为Mbit/s", "internetMaxBandwidthOut", InstanceFieldType.Number)
                .resetInstanceField(PlatformConstants.fit2cloud_ali_platform, ResourceTypeConstants.ECS);

        InstanceSearchField internetMaxBandwidthIn = new InstanceSearchField("公网入带宽最大值,单位为Mbit/s", "internetMaxBandwidthIn", InstanceFieldType.Number)
                .resetInstanceField(PlatformConstants.fit2cloud_ali_platform, ResourceTypeConstants.ECS);

        InstanceSearchField instanceChargeType = new InstanceSearchField("计费方式", "instanceChargeType", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("包年包月", "PrePaid"),
                        new DefaultKeyValue<>("按量付费", "PostPaid")))
                .resetInstanceField(PlatformConstants.fit2cloud_ali_platform, ResourceTypeConstants.ECS);

        InstanceSearchField ioOptimized = new InstanceSearchField("是否为I/O优化型实例", "ioOptimized", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("是", true),
                        new DefaultKeyValue<>("否", false)))
                .resetInstanceField(PlatformConstants.fit2cloud_ali_platform, ResourceTypeConstants.ECS);

        InstanceSearchField internetChargeType = new InstanceSearchField("网络计费类型", "internetChargeType", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("按固定带宽计费", "PayByBandwidth"),
                        new DefaultKeyValue<>("按使用流量计费", "PayByTraffic")))
                .resetInstanceField(PlatformConstants.fit2cloud_ali_platform, ResourceTypeConstants.ECS);

        InstanceSearchField recyclable = new InstanceSearchField("是否可以回收", "recyclable", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("是", true),
                        new DefaultKeyValue<>("否", false)))
                .resetInstanceField(PlatformConstants.fit2cloud_ali_platform, ResourceTypeConstants.ECS);

        InstanceSearchField deletionProtection = new InstanceSearchField("实例释放保护", "deletionProtection", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("已开启实例释放保护", true),
                        new DefaultKeyValue<>("未开启实例释放保护", false)))
                .resetInstanceField(PlatformConstants.fit2cloud_ali_platform, ResourceTypeConstants.ECS);

        InstanceSearchField stoppedMode = new InstanceSearchField("实例停机后是否继续收费", "stoppedMode", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("停机后继续收费,继续保留库存资源", "KeepCharging"),
                        new DefaultKeyValue<>("停机后不收费,释放实例对应的资源", "StopCharging"),
                        new DefaultKeyValue<>("不支持停机不收费功能", "Not-applicable")))
                .resetInstanceField(PlatformConstants.fit2cloud_ali_platform, ResourceTypeConstants.ECS);

        InstanceSearchField tagKey = new InstanceSearchField("标签键", "tagKey", InstanceFieldType.NestedArrayString)
                .resetFilterArrayField(PlatformConstants.fit2cloud_ali_platform, ResourceTypeConstants.ECS, "tags");

        InstanceSearchField tagValue = new InstanceSearchField("标签值", "tagValue", InstanceFieldType.NestedArrayString)
                .resetFilterArrayField(PlatformConstants.fit2cloud_ali_platform, ResourceTypeConstants.ECS, "tags");

        return List.of(cpu, memory, instanceStatus, spotStrategy, deviceAvailable, instanceNetworkType, internetMaxBandwidthOut,
                internetMaxBandwidthIn, instanceChargeType, ioOptimized, internetChargeType, recyclable, deletionProtection,
                stoppedMode, instanceType, gpu, publicIpAddress, lockReason, imageId, securityGroupName, securityGroupDirection, securityGroupIpProtocol
                , securityGroupIpPolicy, securityGroupPortRange, securityGroupSourceCidrIp, securityGroupDestCidrIp, diskLockReason, autoRenewEnabled,
                diskSize, diskType, securityGroupDescription, osType,
                tagKey, tagValue
        );
    }

    /**
     * 对象存储
     *
     * @return 对象存储查询对象
     */
    public static List<InstanceSearchField> listOSSInstanceSearchField() {
        InstanceSearchField encryption = new InstanceSearchField("加密方式", "encryption.applyServerSideEncryptionByDefault.ssealgorithm", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("KMS", "KMS"),
                        new DefaultKeyValue<>("AES256", "AES256"),
                        new DefaultKeyValue<>("未加密", null)
                ))
                .resetInstanceField(PlatformConstants.fit2cloud_ali_platform, ResourceTypeConstants.OSS);

        InstanceSearchField referer = new InstanceSearchField("防盗链", "referer.refererList.referer", InstanceFieldType.ArrayString)
                .resetInstanceField(PlatformConstants.fit2cloud_ali_platform, ResourceTypeConstants.OSS);

        InstanceSearchField refererAllowEmptyReferer = new InstanceSearchField("空Referer", "referer.allowEmptyReferer", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("允许", true),
                        new DefaultKeyValue<>("不允许", false)))
                .resetInstanceField(PlatformConstants.fit2cloud_ali_platform, ResourceTypeConstants.OSS);

        InstanceSearchField instanceSearchField = new InstanceSearchField("访问控制", "acl.accessControlList.grant", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("私有", "private"),
                        new DefaultKeyValue<>("公共读", "public-read"),
                        new DefaultKeyValue<>("公共读写", "public-read-write")
                ))
                .resetInstanceField(PlatformConstants.fit2cloud_ali_platform, ResourceTypeConstants.OSS);
        return List.of(encryption, instanceSearchField, referer, refererAllowEmptyReferer);
    }

    /**
     * RAM用户查询字段
     *
     * @return ram 用户查询字段
     */
    public static List<InstanceSearchField> listRAMInstanceSearchField() {

        InstanceSearchField loginProfile = new InstanceSearchField("用户是否配置登录配置", "loginProfile.userName", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("未配置", null)))
                .resetInstanceField(PlatformConstants.fit2cloud_ali_platform, ResourceTypeConstants.RAM);

        InstanceSearchField loginProfileMfabindRequired = new InstanceSearchField("要求绑定多因素认证设备", "loginProfile.mfabindRequired", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("未开启", false),
                        new DefaultKeyValue<>("开启", true)))
                .resetInstanceField(PlatformConstants.fit2cloud_ali_platform, ResourceTypeConstants.RAM);

        InstanceSearchField loginProfilePasswordResetRequired = new InstanceSearchField("要求下次登录时重设密码", "loginProfile.passwordResetRequired", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("重设", true),
                        new DefaultKeyValue<>("不重设", false)))
                .resetInstanceField(PlatformConstants.fit2cloud_ali_platform, ResourceTypeConstants.RAM);


        return List.of(loginProfileMfabindRequired, loginProfilePasswordResetRequired, loginProfile);
    }

    /**
     * vpc 实例查询字段
     *
     * @return vpc实例查询字段
     */
    public static List<InstanceSearchField> listVPCInstanceSearchField() {
        InstanceSearchField status = new InstanceSearchField("VPC状态", "status", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("配置中", "Pending"), new DefaultKeyValue<>("可用", "Available")))
                .resetInstanceField(PlatformConstants.fit2cloud_ali_platform, ResourceTypeConstants.VPC);

        InstanceSearchField isDefault = new InstanceSearchField("默认VPC", "isDefault", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("是", true),
                        new DefaultKeyValue<>("不是", false)))
                .resetInstanceField(PlatformConstants.fit2cloud_ali_platform, ResourceTypeConstants.VPC);

        InstanceSearchField cidrBlock = new InstanceSearchField("ipv4网段", "cidrBlock", InstanceFieldType.String)
                .resetInstanceField(PlatformConstants.fit2cloud_ali_platform, ResourceTypeConstants.VPC);
        InstanceSearchField flowLog = new InstanceSearchField("流日志状态", "flowLog.status", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("启动状态", "Active"),
                        new DefaultKeyValue<>("创建中", "Activating"),
                        new DefaultKeyValue<>("未启动状态", "Inactive")
                ))
                .resetInstanceField(PlatformConstants.fit2cloud_ali_platform, ResourceTypeConstants.VPC);

        InstanceSearchField switchesAvailableIpAddressCount = new InstanceSearchField("交换机可用IP数量", "availableIpAddressCount", InstanceFieldType.NestedArrayNumber)
                .resetFilterArrayField(PlatformConstants.fit2cloud_ali_platform, ResourceTypeConstants.VPC, "switchesList", false);

        InstanceSearchField tagKey = new InstanceSearchField("标签键", "key", InstanceFieldType.NestedArrayString)
                .resetFilterArrayField(PlatformConstants.fit2cloud_ali_platform, ResourceTypeConstants.VPC, "tags");
        InstanceSearchField tagValue = new InstanceSearchField("标签值", "value", InstanceFieldType.NestedArrayString)
                .resetFilterArrayField(PlatformConstants.fit2cloud_ali_platform, ResourceTypeConstants.VPC, "tags");

        return List.of(status, isDefault, cidrBlock, flowLog, switchesAvailableIpAddressCount, tagKey, tagValue);
    }

    /**
     * 公网ip实例查询字段
     *
     * @return 公网ip实例查询字段
     */
    public static List<InstanceSearchField> listPublicIpInstanceSearchField() {
        InstanceSearchField status = new InstanceSearchField("弹性ip状态", "status", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("绑定中", "Associating"),
                        new DefaultKeyValue<>("解绑中", "Unassociating"),
                        new DefaultKeyValue<>("已分配", "InUse"),
                        new DefaultKeyValue<>("可用", "Available"),
                        new DefaultKeyValue<>("释放中", "Releasing")))
                .resetInstanceField(PlatformConstants.fit2cloud_ali_platform, ResourceTypeConstants.PUBLIC_IP);

        InstanceSearchField isDefault = new InstanceSearchField("付费类型", "chargeType", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("按量计费", "PostPaid"),
                        new DefaultKeyValue<>("包年包月", "PrePaid")))
                .resetInstanceField(PlatformConstants.fit2cloud_ali_platform, ResourceTypeConstants.PUBLIC_IP);

        InstanceSearchField deletionProtection = new InstanceSearchField("删除保护功能", "deletionProtection", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("是", true),
                        new DefaultKeyValue<>("否", false)))
                .resetInstanceField(PlatformConstants.fit2cloud_ali_platform, ResourceTypeConstants.PUBLIC_IP);

        InstanceSearchField secondLimited = new InstanceSearchField("是否开启二级限速", "secondLimited", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("是", true),
                        new DefaultKeyValue<>("否", false)))
                .resetInstanceField(PlatformConstants.fit2cloud_ali_platform, ResourceTypeConstants.PUBLIC_IP);

        InstanceSearchField bandwidth = new InstanceSearchField("带宽峰值,单位：Mbps", "bandwidth", InstanceFieldType.Number)
                .resetInstanceField(PlatformConstants.fit2cloud_ali_platform, ResourceTypeConstants.PUBLIC_IP);

        InstanceSearchField tagKey = new InstanceSearchField("标签键", "key", InstanceFieldType.NestedArrayString)
                .resetFilterArrayField(PlatformConstants.fit2cloud_ali_platform, ResourceTypeConstants.PUBLIC_IP, "tags");
        InstanceSearchField tagValue = new InstanceSearchField("标签值", "value", InstanceFieldType.NestedArrayString)
                .resetFilterArrayField(PlatformConstants.fit2cloud_ali_platform, ResourceTypeConstants.PUBLIC_IP, "tags");

        return List.of(status, isDefault, deletionProtection, secondLimited, bandwidth, tagKey, tagValue);
    }

    /**
     * 云磁盘实例查询字段
     *
     * @return 云磁盘实例查询字段
     */
    public static List<InstanceSearchField> listDiskInstanceSearchField() {
        InstanceSearchField status = new InstanceSearchField("磁盘状态", "status", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("使用中", "In_use"),
                        new DefaultKeyValue<>("待挂载", "Available"),
                        new DefaultKeyValue<>("已分配", "Attaching"),
                        new DefaultKeyValue<>("卸载中", "Detaching"),
                        new DefaultKeyValue<>("创建中", "Creating"),
                        new DefaultKeyValue<>("初始化中", "ReIniting"),
                        new DefaultKeyValue<>("已删除", "Deleted")))
                .resetInstanceField(PlatformConstants.fit2cloud_ali_platform, ResourceTypeConstants.DISK);

        InstanceSearchField type = new InstanceSearchField("磁盘类型", "type", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("系统盘", "system"),
                        new DefaultKeyValue<>("数据盘", "data")))
                .resetInstanceField(PlatformConstants.fit2cloud_ali_platform, ResourceTypeConstants.DISK);

        InstanceSearchField performanceLevel = new InstanceSearchField("ESSD云盘的性能等级", "performanceLevel", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("单盘最高随机读写IOPS 1万", "PL0"),
                        new DefaultKeyValue<>("单盘最高随机读写IOPS 5万", "PL1"),
                        new DefaultKeyValue<>("单盘最高随机读写IOPS 10万", "PL2"),
                        new DefaultKeyValue<>("单盘最高随机读写IOPS 100万", "PL3"),
                        new DefaultKeyValue<>("其他", "")))
                .resetInstanceField(PlatformConstants.fit2cloud_ali_platform, ResourceTypeConstants.DISK);

        InstanceSearchField deleteAutoSnapshot = new InstanceSearchField("是否同时删除自动快照", "deleteAutoSnapshot", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("删除云盘上的快照", true),
                        new DefaultKeyValue<>("保留云盘上的快照", false)))
                .resetInstanceField(PlatformConstants.fit2cloud_ali_platform, ResourceTypeConstants.DISK);

        InstanceSearchField encrypted = new InstanceSearchField("是否为加密云盘", "encrypted", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("是", true),
                        new DefaultKeyValue<>("否", false)))
                .resetInstanceField(PlatformConstants.fit2cloud_ali_platform, ResourceTypeConstants.DISK);

        InstanceSearchField iops = new InstanceSearchField("每秒读写（I/O）操作的次数上限,单位：次/s", "iops", InstanceFieldType.Number)
                .resetInstanceField(PlatformConstants.fit2cloud_ali_platform, ResourceTypeConstants.DISK);

        InstanceSearchField size = new InstanceSearchField("磁盘大小,单位GiB", "size", InstanceFieldType.Number)
                .resetInstanceField(PlatformConstants.fit2cloud_ali_platform, ResourceTypeConstants.DISK);

        InstanceSearchField diskChargeType = new InstanceSearchField("计费方式", "diskChargeType", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("包年包月", "PrePaid"),
                        new DefaultKeyValue<>("按量付费", "PostPaid")))
                .resetInstanceField(PlatformConstants.fit2cloud_ali_platform, ResourceTypeConstants.DISK);

        InstanceSearchField category = new InstanceSearchField("磁盘种类", "category", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("普通云盘", "cloud"),
                        new DefaultKeyValue<>("高效云盘", "cloud_efficiency"),
                        new DefaultKeyValue<>("SSD盘", "cloud_ssd"),
                        new DefaultKeyValue<>("ESSD云盘", "cloud_essd"),
                        new DefaultKeyValue<>("ESSD AutoPL云盘", "cloud_auto"),
                        new DefaultKeyValue<>("I/O密集型本地盘", "local_ssd_pro"),
                        new DefaultKeyValue<>("吞吐密集型本地盘", "local_hdd_pro"),
                        new DefaultKeyValue<>("本地盘", "ephemeral"),
                        new DefaultKeyValue<>("本地SSD盘", "ephemeral_ssd")))
                .resetInstanceField(PlatformConstants.fit2cloud_ali_platform, ResourceTypeConstants.DISK);

        InstanceSearchField burstingEnabled = new InstanceSearchField("是否开启Burst(性能突发)", "burstingEnabled", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("是", true),
                        new DefaultKeyValue<>("否", false)))
                .resetInstanceField(PlatformConstants.fit2cloud_ali_platform, ResourceTypeConstants.DISK);

        InstanceSearchField enableAutomatedSnapshotPolicy = new InstanceSearchField("是否开启自动备份策略", "enableAutomatedSnapshotPolicy", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("开启", true),
                        new DefaultKeyValue<>("未开启", false)))
                .resetInstanceField(PlatformConstants.fit2cloud_ali_platform, ResourceTypeConstants.DISK);

        InstanceSearchField tagKey = new InstanceSearchField("标签键", "tagKey", InstanceFieldType.NestedArrayString)
                .resetFilterArrayField(PlatformConstants.fit2cloud_ali_platform, ResourceTypeConstants.DISK, "tags");

        InstanceSearchField tagValue = new InstanceSearchField("标签值", "tagValue", InstanceFieldType.NestedArrayString)
                .resetFilterArrayField(PlatformConstants.fit2cloud_ali_platform, ResourceTypeConstants.DISK, "tags");

        return List.of(status, type, performanceLevel, deleteAutoSnapshot, encrypted, iops, size, diskChargeType, category,
                burstingEnabled
                , enableAutomatedSnapshotPolicy,
                tagKey, tagValue);
    }

    /**
     * 获取redis 实例查询字段
     *
     * @return redis实例查询字段
     */
    public static List<InstanceSearchField> listRedisInstanceSearchField() {
        InstanceSearchField status = new InstanceSearchField("实例状态", "instanceStatus", InstanceFieldType.Enum,
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
                        new DefaultKeyValue<>("大版本升级中，可正常访问", "MajorVersionUpgrading")))
                .resetInstanceField(PlatformConstants.fit2cloud_ali_platform, ResourceTypeConstants.REDIS);


        InstanceSearchField chargeType = new InstanceSearchField("计费方式", "chargeType", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("包年包月", "PrePaid"),
                        new DefaultKeyValue<>("按量付费", "PostPaid")))
                .resetInstanceField(PlatformConstants.fit2cloud_ali_platform, ResourceTypeConstants.REDIS);

        InstanceSearchField networkType = new InstanceSearchField("网络类型", "networkType", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("经典网络", "CLASSIC"),
                        new DefaultKeyValue<>("专有网络", "VPC")))
                .resetInstanceField(PlatformConstants.fit2cloud_ali_platform, ResourceTypeConstants.REDIS);

        InstanceSearchField nodeType = new InstanceSearchField("节点类型", "nodeType", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("双副本", "double"),
                        new DefaultKeyValue<>("单副本", "single")))
                .resetInstanceField(PlatformConstants.fit2cloud_ali_platform, ResourceTypeConstants.REDIS);

        InstanceSearchField editionType = new InstanceSearchField("版本类型", "editionType", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("社区版", "Community"),
                        new DefaultKeyValue<>("企业版", "Enterprise")))
                .resetInstanceField(PlatformConstants.fit2cloud_ali_platform, ResourceTypeConstants.REDIS);

        InstanceSearchField engineVersion = new InstanceSearchField("实例类型", "engineVersion", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("2.8", "2.8"),
                        new DefaultKeyValue<>("4.0", "4.0"),
                        new DefaultKeyValue<>("5.0", "5.0"),
                        new DefaultKeyValue<>("6.0", "6.0")))
                .resetInstanceField(PlatformConstants.fit2cloud_ali_platform, ResourceTypeConstants.REDIS);

        InstanceSearchField bandwidth = new InstanceSearchField("实例带宽,单位：MB/s", "bandwidth", InstanceFieldType.Number)
                .resetInstanceField(PlatformConstants.fit2cloud_ali_platform, ResourceTypeConstants.REDIS);

        InstanceSearchField capacity = new InstanceSearchField("实例容量,单位：MB", "capacity", InstanceFieldType.Number)
                .resetInstanceField(PlatformConstants.fit2cloud_ali_platform, ResourceTypeConstants.REDIS);

        InstanceSearchField DBInstanceNetType = new InstanceSearchField("网络信息所属的网络类型", "network.netInfoItems.instanceNetInfo.dbinstanceNetType", InstanceFieldType.ArrayEnum,
                List.of(new DefaultKeyValue<>("公网", "0"),
                        new DefaultKeyValue<>("经典网络", "1"),
                        new DefaultKeyValue<>("专有网络", "2")))
                .resetInstanceField(PlatformConstants.fit2cloud_ali_platform, ResourceTypeConstants.REDIS);

        InstanceSearchField tagKey = new InstanceSearchField("标签键", "key", InstanceFieldType.NestedArrayString)
                .resetFilterArrayField(PlatformConstants.fit2cloud_ali_platform, ResourceTypeConstants.REDIS, "tags");
        InstanceSearchField tagValue = new InstanceSearchField("标签值", "value", InstanceFieldType.NestedArrayString)
                .resetFilterArrayField(PlatformConstants.fit2cloud_ali_platform, ResourceTypeConstants.REDIS, "tags");

        return List.of(status, chargeType, networkType, nodeType, editionType, engineVersion, bandwidth, capacity, DBInstanceNetType, tagKey, tagValue);
    }


    /**
     * 获取 mongodb 实例查询字段
     *
     * @return mongodb实例查询字段
     */
    public static List<InstanceSearchField> listMongodbInstanceSearchField() {
        InstanceSearchField chargeType = new InstanceSearchField("付费类型", "chargeType", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("包年包月", "PrePaid"),
                        new DefaultKeyValue<>("按量付费", "PostPaid")))
                .resetInstanceField(PlatformConstants.fit2cloud_ali_platform, ResourceTypeConstants.MONGO_DB);

        InstanceSearchField networkType = new InstanceSearchField("实例网络类型", "networkType", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("经典网络", "Classic"),
                        new DefaultKeyValue<>("专有网络", "VPC")))
                .resetInstanceField(PlatformConstants.fit2cloud_ali_platform, ResourceTypeConstants.MONGO_DB);

        InstanceSearchField lockMode = new InstanceSearchField("实例的锁定状态", "lockMode", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("正常", "Unlock"),
                        new DefaultKeyValue<>("手动触发锁定", "ManualLock"),
                        new DefaultKeyValue<>("实例过期自动锁定", "LockByExpiration"),
                        new DefaultKeyValue<>("实例回滚前自动锁定", "LockByRestoration"),
                        new DefaultKeyValue<>("实例空间满自动锁定", "LockByDiskQuota"),
                        new DefaultKeyValue<>("实例已释放", "Released")))
                .resetInstanceField(PlatformConstants.fit2cloud_ali_platform, ResourceTypeConstants.MONGO_DB);

        InstanceSearchField engineVersion = new InstanceSearchField("数据库版本号", "engineVersion", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("5.0", "5.0"),
                        new DefaultKeyValue<>("4.4", "4.4"),
                        new DefaultKeyValue<>("4.2", "4.2"),
                        new DefaultKeyValue<>("4.0", "4.0"),
                        new DefaultKeyValue<>("3.4", "3.4")))
                .resetInstanceField(PlatformConstants.fit2cloud_ali_platform, ResourceTypeConstants.MONGO_DB);

        InstanceSearchField dbinstanceType = new InstanceSearchField("实例类型", "dbinstanceType", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("分片集群实例", "sharding"),
                        new DefaultKeyValue<>("副本集实例和单节点实例", "replicate"),
                        new DefaultKeyValue<>("Serverless实例", "serverless")
                ))
                .resetInstanceField(PlatformConstants.fit2cloud_ali_platform, ResourceTypeConstants.MONGO_DB);

        InstanceSearchField dbinstanceStatus = new InstanceSearchField("实例状态", "dbinstanceStatus", InstanceFieldType.Enum,
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
                ))
                .resetInstanceField(PlatformConstants.fit2cloud_ali_platform, ResourceTypeConstants.MONGO_DB);

        InstanceSearchField dbinstanceStorage = new InstanceSearchField("实例存储空间(GB)", "dbinstanceStorage", InstanceFieldType.Number)
                .resetInstanceField(PlatformConstants.fit2cloud_ali_platform, ResourceTypeConstants.MONGO_DB);

        InstanceSearchField engine = new InstanceSearchField("数据库引擎", "engine", InstanceFieldType.String)
                .resetInstanceField(PlatformConstants.fit2cloud_ali_platform, ResourceTypeConstants.MONGO_DB);

        InstanceSearchField replicationFactor = new InstanceSearchField("实例中节点的个数", "replicationFactor", InstanceFieldType.Number)
                .resetInstanceField(PlatformConstants.fit2cloud_ali_platform, ResourceTypeConstants.MONGO_DB);

        InstanceSearchField kindCode = new InstanceSearchField("实例的类型", "kindCode", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("物理机", "0"),
                        new DefaultKeyValue<>("ECS", "1"),
                        new DefaultKeyValue<>("DOCKER", "2"),
                        new DefaultKeyValue<>("k8s新架构实例", "18")
                ))
                .resetInstanceField(PlatformConstants.fit2cloud_ali_platform, ResourceTypeConstants.MONGO_DB);

        InstanceSearchField storageType = new InstanceSearchField("存储类型", "storageType", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("ESSD云盘", "cloud_essd"),
                        new DefaultKeyValue<>("SSD本地盘", "local_ssd")
                ))
                .resetInstanceField(PlatformConstants.fit2cloud_ali_platform, ResourceTypeConstants.MONGO_DB);

        InstanceSearchField networkAddressNetworkType = new InstanceSearchField("实例连接网络类型", "networkObj.networkAddresses.networkAddress.networkType", InstanceFieldType.ArrayEnum,
                List.of(new DefaultKeyValue<>("专有网络", "VPC"),
                        new DefaultKeyValue<>("经典网络", "Classic"),
                        new DefaultKeyValue<>("公网", "Public")
                ))
                .resetInstanceField(PlatformConstants.fit2cloud_ali_platform, ResourceTypeConstants.MONGO_DB);

        return List.of(chargeType, lockMode, engineVersion, networkType, dbinstanceType, dbinstanceStatus, dbinstanceStorage, engine, replicationFactor, kindCode, storageType, networkAddressNetworkType);
    }

    public static List<InstanceSearchField> listMysqlInstanceSearchField() {
        return listRDSInstanceSearchField(ResourceTypeConstants.MYSQL);
    }

    public static List<InstanceSearchField> listPostGreSqlInstanceSearchField() {
        return listRDSInstanceSearchField(ResourceTypeConstants.POST_GRE_SQL);
    }

    public static List<InstanceSearchField> listSqlServerInstanceSearchField() {
        return listRDSInstanceSearchField(ResourceTypeConstants.SQL_SERVER);
    }

    public static List<InstanceSearchField> listMariadbInstanceSearchField() {
        return listRDSInstanceSearchField(ResourceTypeConstants.MARIA_DB);
    }

    /**
     * 获取Rds实例查询字段
     *
     * @param resourceType 资源类型
     * @return 实例
     */
    private static List<InstanceSearchField> listRDSInstanceSearchField(ResourceTypeConstants resourceType) {
        InstanceSearchField payType = new InstanceSearchField("付费类型", "payType", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("按量付费", "Postpaid"),
                        new DefaultKeyValue<>("包年包月", "Prepaid")
                ))
                .resetInstanceField(PlatformConstants.fit2cloud_ali_platform, resourceType);

        InstanceSearchField mutriORsignle = new InstanceSearchField("是否是组合可用区", "mutriORsignle", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("是", true),
                        new DefaultKeyValue<>("否", false)
                ))
                .resetInstanceField(PlatformConstants.fit2cloud_ali_platform, resourceType);

        InstanceSearchField engineVersion = new InstanceSearchField("数据库版本", "engineVersion", InstanceFieldType.String)
                .resetInstanceField(PlatformConstants.fit2cloud_ali_platform, resourceType);

        InstanceSearchField instanceNetworkType = new InstanceSearchField("实例网络类型", "instanceNetworkType", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("经典网络", "Classic"),
                        new DefaultKeyValue<>("VPC网络", "VPC")
                ))
                .resetInstanceField(PlatformConstants.fit2cloud_ali_platform, resourceType);

        InstanceSearchField tipsLevel = new InstanceSearchField("异常提示等级", "tipsLevel", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("正常", 1),
                        new DefaultKeyValue<>("只读实例和主实例规格不对齐", 2)
                ))
                .resetInstanceField(PlatformConstants.fit2cloud_ali_platform, resourceType);

        InstanceSearchField connectionMode = new InstanceSearchField("实例的访问模式", "connectionMode", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("标准访问模式", "Standard"),
                        new DefaultKeyValue<>("数据库代理模式", "Safe")
                ))
                .resetInstanceField(PlatformConstants.fit2cloud_ali_platform, resourceType);

        InstanceSearchField lockMode = new InstanceSearchField("实例的锁定状态", "lockMode", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("正常", "Unlock"),
                        new DefaultKeyValue<>("手动触发锁定", "ManualLock"),
                        new DefaultKeyValue<>("实例回滚前自动锁定", "LockByRestoration"),
                        new DefaultKeyValue<>("实例过期自动锁定", "LockByExpiration"),
                        new DefaultKeyValue<>("实例空间满自动锁定", "LockByDiskQuota"),
                        new DefaultKeyValue<>("实例已释放", "Released")
                ))
                .resetInstanceField(PlatformConstants.fit2cloud_ali_platform, resourceType);

        InstanceSearchField dbinstanceType = new InstanceSearchField("实例类型", "dbinstanceType", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("主实例", "Primary"),
                        new DefaultKeyValue<>("只读实例", "Readonly"),
                        new DefaultKeyValue<>("灾备实例", "Guard"),
                        new DefaultKeyValue<>("临时实例", "Temp")
                ))
                .resetInstanceField(PlatformConstants.fit2cloud_ali_platform, resourceType);

        InstanceSearchField deletionProtection = new InstanceSearchField("是否已开启释放保护功能", "deletionProtection", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("已开启", true),
                        new DefaultKeyValue<>("未开启", false)
                ))
                .resetInstanceField(PlatformConstants.fit2cloud_ali_platform, resourceType);

        InstanceSearchField dbinstanceNetInfosIpType = new InstanceSearchField("实例网络连接类型", "networkObj.dbinstanceNetInfos.dbinstanceNetInfo.iptype", InstanceFieldType.Enum,
                List.of(
                        new DefaultKeyValue<>("外网", "Public"),
                        new DefaultKeyValue<>("VPC类型内网", "Private"),
                        new DefaultKeyValue<>("经典网络类型内网", "Inner")
                ))
                .resetInstanceField(PlatformConstants.fit2cloud_ali_platform, resourceType);

        InstanceSearchField dedicatedHostZoneIdForSlave = new InstanceSearchField("Slave节点所在主机的可用区ID", "dedicatedHostZoneIdForSlave", InstanceFieldType.String)
                .resetInstanceField(PlatformConstants.fit2cloud_ali_platform, resourceType);

        InstanceSearchField tagKey = new InstanceSearchField("标签键", "tagKey", InstanceFieldType.NestedArrayString)
                .resetFilterArrayField(PlatformConstants.fit2cloud_ali_platform, resourceType, "tags");
        InstanceSearchField tagValue = new InstanceSearchField("标签值", "tagValue", InstanceFieldType.NestedArrayString)
                .resetFilterArrayField(PlatformConstants.fit2cloud_ali_platform, resourceType, "tags");
        return List.of(payType, mutriORsignle, engineVersion, instanceNetworkType, dbinstanceType, tipsLevel, connectionMode,
                lockMode, deletionProtection, dbinstanceNetInfosIpType, dedicatedHostZoneIdForSlave, tagKey, tagValue);

    }

    /**
     * 获取 安全组实例查询字段
     *
     * @return 安全组实例查询字段
     */
    public static List<InstanceSearchField> listSecurityGroupInstanceSearchField() {
        InstanceSearchField securityGroupType = new InstanceSearchField("安全组类型", "securityGroupType", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("普通安全组", "normal"),
                        new DefaultKeyValue<>("企业安全组", "enterprise")
                )).resetInstanceField(PlatformConstants.fit2cloud_ali_platform, ResourceTypeConstants.SECURITY_GROUP);

        InstanceSearchField innerAccessPolicy = new InstanceSearchField("安全组内网络连通策略", "rule.innerAccessPolicy", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("内网互通", "Accept"),
                        new DefaultKeyValue<>("内网隔离", "Drop")
                )).resetInstanceField(PlatformConstants.fit2cloud_ali_platform, ResourceTypeConstants.SECURITY_GROUP);

        InstanceSearchField ruleSourceCidrIp = new InstanceSearchField("源端IP地址段,用于入方向授权", "rule.permissions.permission.sourceCidrIp", InstanceFieldType.String)
                .resetInstanceField(PlatformConstants.fit2cloud_ali_platform, ResourceTypeConstants.SECURITY_GROUP);

        InstanceSearchField tagKey = new InstanceSearchField("标签键", "tagKey", InstanceFieldType.NestedArrayString)
                .resetFilterArrayField(PlatformConstants.fit2cloud_ali_platform, ResourceTypeConstants.SECURITY_GROUP, "tags");
        InstanceSearchField tagValue = new InstanceSearchField("标签值", "tagValue", InstanceFieldType.NestedArrayString)
                .resetFilterArrayField(PlatformConstants.fit2cloud_ali_platform, ResourceTypeConstants.SECURITY_GROUP, "tags");
        return List.of(securityGroupType, innerAccessPolicy, ruleSourceCidrIp, tagKey, tagValue);

    }

    /**
     * 获取elasticsearch 查询字段
     *
     * @return elasticsearch 查询字段
     */
    public static List<InstanceSearchField> listElasticSearchInstanceSearchField() {

        InstanceSearchField advancedDedicateMaster = new InstanceSearchField("是否包含专有主节点", "advancedDedicateMaster", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("包含", true),
                        new DefaultKeyValue<>("不包含", false)
                ))
                .resetInstanceField(PlatformConstants.fit2cloud_ali_platform, ResourceTypeConstants.ELASTIC_SEARCH);

        InstanceSearchField nodeAmount = new InstanceSearchField("实例的数据节点数量", "nodeAmount", InstanceFieldType.Number)
                .resetInstanceField(PlatformConstants.fit2cloud_ali_platform, ResourceTypeConstants.ELASTIC_SEARCH);

        InstanceSearchField status = new InstanceSearchField("实例的状态", "status", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("正常", "active"),
                        new DefaultKeyValue<>("生效中", "activating"),
                        new DefaultKeyValue<>("冻结", "inactive"),
                        new DefaultKeyValue<>("失效", "invalid")
                ))
                .resetInstanceField(PlatformConstants.fit2cloud_ali_platform, ResourceTypeConstants.ELASTIC_SEARCH);

        InstanceSearchField serviceVpc = new InstanceSearchField("是否为服务VPC", "serviceVpc", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("是", true),
                        new DefaultKeyValue<>("否", false)
                ))
                .resetInstanceField(PlatformConstants.fit2cloud_ali_platform, ResourceTypeConstants.ELASTIC_SEARCH);

        InstanceSearchField paymentType = new InstanceSearchField("付费类型", "paymentType", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("包年包月", "prepaid"),
                        new DefaultKeyValue<>("按量付费", "postpaid")
                ))
                .resetInstanceField(PlatformConstants.fit2cloud_ali_platform, ResourceTypeConstants.ELASTIC_SEARCH);

        InstanceSearchField postpaidServiceStatus = new InstanceSearchField("包年包月实例叠加的后付费服务状态", "postpaidServiceStatus", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("正常", "active"),
                        new DefaultKeyValue<>("关闭", "closed"),
                        new DefaultKeyValue<>("欠费冻结中", "indebt")
                ))
                .resetInstanceField(PlatformConstants.fit2cloud_ali_platform, ResourceTypeConstants.ELASTIC_SEARCH);

        InstanceSearchField esVersion = new InstanceSearchField("包年包月实例叠加的后付费服务状态", "esVersion", InstanceFieldType.String)
                .resetInstanceField(PlatformConstants.fit2cloud_ali_platform, ResourceTypeConstants.ELASTIC_SEARCH);

        InstanceSearchField nodeSpecDisk = new InstanceSearchField("节点的存储空间大小,单位为GB", "nodeSpec.disk", InstanceFieldType.Number)
                .resetInstanceField(PlatformConstants.fit2cloud_ali_platform, ResourceTypeConstants.ELASTIC_SEARCH);

        InstanceSearchField nodeSpecDiskEncryption = new InstanceSearchField("节点是否使用磁盘加密", "nodeSpec.diskEncryption", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("是", true),
                        new DefaultKeyValue<>("否", false)

                ))
                .resetInstanceField(PlatformConstants.fit2cloud_ali_platform, ResourceTypeConstants.ELASTIC_SEARCH);

        InstanceSearchField nodeSpecDiskType = new InstanceSearchField("节点的存储类型", "nodeSpec.diskType", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("SSD云盘", "cloud_ssd"),
                        new DefaultKeyValue<>("高效云盘", "cloud_efficiency")

                ))
                .resetInstanceField(PlatformConstants.fit2cloud_ali_platform, ResourceTypeConstants.ELASTIC_SEARCH);

        InstanceSearchField kibanaConfigurationAmount = new InstanceSearchField("Kibana节点数量", "kibanaConfiguration.amount", InstanceFieldType.Number)
                .resetInstanceField(PlatformConstants.fit2cloud_ali_platform, ResourceTypeConstants.ELASTIC_SEARCH);

        InstanceSearchField kibanaConfigurationSpec = new InstanceSearchField("Kibana节点规格", "kibanaConfiguration.spec", InstanceFieldType.String)
                .resetInstanceField(PlatformConstants.fit2cloud_ali_platform, ResourceTypeConstants.ELASTIC_SEARCH);

        InstanceSearchField kibanaConfigurationDisk = new InstanceSearchField("Kibana节点存储空间大小,单位为GB", "kibanaConfiguration.disk", InstanceFieldType.Number)
                .resetInstanceField(PlatformConstants.fit2cloud_ali_platform, ResourceTypeConstants.ELASTIC_SEARCH);

        InstanceSearchField tagKey = new InstanceSearchField("标签键", "tagKey", InstanceFieldType.NestedArrayString)
                .resetFilterArrayField(PlatformConstants.fit2cloud_ali_platform, ResourceTypeConstants.ELASTIC_SEARCH, "tags");
        InstanceSearchField tagValue = new InstanceSearchField("标签值", "tagValue", InstanceFieldType.NestedArrayString)
                .resetFilterArrayField(PlatformConstants.fit2cloud_ali_platform, ResourceTypeConstants.ELASTIC_SEARCH, "tags");


        return List.of(advancedDedicateMaster, nodeAmount, status, serviceVpc, paymentType, postpaidServiceStatus, esVersion, nodeSpecDisk, nodeSpecDiskEncryption
                , nodeSpecDiskType, kibanaConfigurationAmount, kibanaConfigurationSpec, kibanaConfigurationDisk,
                tagKey, tagValue);
    }

    /**
     * 获取负载均衡查询字段
     *
     * @return 负载均衡查询字段
     */
    public static List<InstanceSearchField> listLoadBalancerInstanceSearchField() {
        InstanceSearchField payType = new InstanceSearchField("负载均衡实例付费模式", "payType", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("按量付费", "PayOnDemand"),
                        new DefaultKeyValue<>("包年包月", "PrePay")

                ))
                .resetInstanceField(PlatformConstants.fit2cloud_ali_platform, ResourceTypeConstants.LOAD_BALANCER);
        InstanceSearchField addressType = new InstanceSearchField("实例的网络类型", "addressType", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("公网负载均衡实例", "internet"),
                        new DefaultKeyValue<>("内网负载均衡实例", "intranet")

                ))
                .resetInstanceField(PlatformConstants.fit2cloud_ali_platform, ResourceTypeConstants.LOAD_BALANCER);

        InstanceSearchField networkType = new InstanceSearchField("私网负载均衡实例的网络类型", "networkType", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("专有网络实例", "vpc"),
                        new DefaultKeyValue<>("经典网络实例", "classic")

                ))
                .resetInstanceField(PlatformConstants.fit2cloud_ali_platform, ResourceTypeConstants.LOAD_BALANCER);

        InstanceSearchField bandwidth = new InstanceSearchField("监听的带宽峰值,单位：Mbps,-1不限制带宽峰值", "bandwidth", InstanceFieldType.Number)
                .resetInstanceField(PlatformConstants.fit2cloud_ali_platform, ResourceTypeConstants.LOAD_BALANCER);

        InstanceSearchField internetChargeTypeAlias = new InstanceSearchField("公网计费方式", "internetChargeTypeAlias", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("按带宽计费", "paybybandwidth"),
                        new DefaultKeyValue<>("按流量计费", "paybytraffic")

                ))
                .resetInstanceField(PlatformConstants.fit2cloud_ali_platform, ResourceTypeConstants.LOAD_BALANCER);

        InstanceSearchField loadBalancerStatus = new InstanceSearchField("负载均衡实例状态", "loadBalancerStatus", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("实例已停止", "inactive"),
                        new DefaultKeyValue<>("实例运行中", "active"),
                        new DefaultKeyValue<>("实例已锁定", "locked")

                ))
                .resetInstanceField(PlatformConstants.fit2cloud_ali_platform, ResourceTypeConstants.LOAD_BALANCER);

        InstanceSearchField internetChargeType = new InstanceSearchField("公网类型实例付费方式", "internetChargeType", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("按带宽计费", "3"),
                        new DefaultKeyValue<>("按流量计费", "4")
                )).resetInstanceField(PlatformConstants.fit2cloud_ali_platform, ResourceTypeConstants.LOAD_BALANCER);

        InstanceSearchField deleteProtection = new InstanceSearchField("负载均衡删除保护状态", "deleteProtection", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("开启", "on"),
                        new DefaultKeyValue<>("关闭", "off")
                ))
                .resetInstanceField(PlatformConstants.fit2cloud_ali_platform, ResourceTypeConstants.LOAD_BALANCER);

        InstanceSearchField instanceChargeType = new InstanceSearchField("实例计费方式", "instanceChargeType", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("按规格计费", "PayBySpec"),
                        new DefaultKeyValue<>("按使用量计费", "PayByCLCU")
                ))
                .resetInstanceField(PlatformConstants.fit2cloud_ali_platform, ResourceTypeConstants.LOAD_BALANCER);

        InstanceSearchField tagKey = new InstanceSearchField("标签键", "tagKey", InstanceFieldType.NestedArrayString)
                .resetFilterArrayField(PlatformConstants.fit2cloud_ali_platform, ResourceTypeConstants.LOAD_BALANCER, "tags");
        InstanceSearchField tagValue = new InstanceSearchField("标签值", "tagValue", InstanceFieldType.NestedArrayString)
                .resetFilterArrayField(PlatformConstants.fit2cloud_ali_platform, ResourceTypeConstants.LOAD_BALANCER, "tags");

        return List.of(payType, addressType, networkType, bandwidth, internetChargeTypeAlias, loadBalancerStatus, internetChargeType, deleteProtection
                , instanceChargeType, tagKey, tagValue);


    }
}
