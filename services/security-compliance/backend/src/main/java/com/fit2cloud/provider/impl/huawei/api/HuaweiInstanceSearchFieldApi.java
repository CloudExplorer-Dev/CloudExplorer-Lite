package com.fit2cloud.provider.impl.huawei.api;

import com.fit2cloud.common.constants.PlatformConstants;
import com.fit2cloud.constants.ResourceTypeConstants;
import com.fit2cloud.provider.entity.InstanceFieldType;
import com.fit2cloud.provider.entity.InstanceSearchField;
import org.apache.commons.collections4.keyvalue.DefaultKeyValue;

import java.util.List;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2023/1/4  11:53}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
public class HuaweiInstanceSearchFieldApi {
    /**
     * 获取华为云 ECS云服务可查询字段
     *
     * @return ecs实例可查询字段
     */
    public static List<InstanceSearchField> listEcsInstanceSearchField() {
        InstanceSearchField status = new InstanceSearchField("云服务器状态", "status", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("创建实例后，在实例状态进入运行中之前的状态", "BUILD"),
                        new DefaultKeyValue<>("实例正在进行重启操作", "REBOOT"),
                        new DefaultKeyValue<>("实例正在进行强制重启操作", "HARD_REBOOT"),
                        new DefaultKeyValue<>("实例正在重建中", "REBUILD"),
                        new DefaultKeyValue<>("实例正在热迁移中", "MIGRATING"),
                        new DefaultKeyValue<>("实例接收变更请求，开始进行变更操作", "RESIZE"),
                        new DefaultKeyValue<>("实例正常运行状态", "ACTIVE"),
                        new DefaultKeyValue<>("实例被正常停止", "SHUTOFF"),
                        new DefaultKeyValue<>("实例正在回退变更规格的配置", "REVERT_RESIZE"),
                        new DefaultKeyValue<>("实例处于异常状态", "ERROR"),
                        new DefaultKeyValue<>("实例已被正常删除", "DELETED"),
                        new DefaultKeyValue<>("镜像启动的实例处于搁置状态", "SHELVED"),
                        new DefaultKeyValue<>("卷启动的实例处于搁置状态", "SHELVED_OFFLOADED"),
                        new DefaultKeyValue<>("实例处于未知状态", "UNKNOWN")
                ))
                .resetInstanceField(PlatformConstants.fit2cloud_huawei_platform, ResourceTypeConstants.ECS);

        InstanceSearchField disk = new InstanceSearchField("云服务器规格对应要求系统盘大小,0为不限制", "disk", InstanceFieldType.Number)
                .resetInstanceField(PlatformConstants.fit2cloud_huawei_platform, ResourceTypeConstants.ECS);

        InstanceSearchField vcpus = new InstanceSearchField("云服务器规格对应的CPU核数", "vcpus", InstanceFieldType.Number)
                .resetInstanceField(PlatformConstants.fit2cloud_huawei_platform, ResourceTypeConstants.ECS);

        InstanceSearchField ram = new InstanceSearchField("云服务器规格对应的内存大小,单位为MB", "ram", InstanceFieldType.Number)
                .resetInstanceField(PlatformConstants.fit2cloud_huawei_platform, ResourceTypeConstants.ECS);

        InstanceSearchField diskConfig = new InstanceSearchField("diskConfig的类型", "OS-DCF:diskConfig", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("镜像空间不会扩展", "OS-DCF:diskConfig"),
                        new DefaultKeyValue<>("系统盘镜像空间会自动扩展为与flavor大小一致", "AUTO")

                ))
                .resetInstanceField(PlatformConstants.fit2cloud_huawei_platform, ResourceTypeConstants.ECS);

        InstanceSearchField charging_mode = new InstanceSearchField("服务器的计费类型", "metadata.charging_mode", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("按需计费", "0"),
                        new DefaultKeyValue<>("包年包月", "1")

                ))
                .resetInstanceField(PlatformConstants.fit2cloud_huawei_platform, ResourceTypeConstants.ECS);


        InstanceSearchField imagetype = new InstanceSearchField("镜像类型", "metadata.metering.imagetype", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("公共镜像", "gold"),
                        new DefaultKeyValue<>("私有镜像", "private"),
                        new DefaultKeyValue<>("共享镜像", "shared")

                ))
                .resetInstanceField(PlatformConstants.fit2cloud_huawei_platform, ResourceTypeConstants.ECS);

        InstanceSearchField os_bit = new InstanceSearchField("操作系统位数", "metadata.os_bit", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("32位", "32"),
                        new DefaultKeyValue<>("64位", "64")

                ))
                .resetInstanceField(PlatformConstants.fit2cloud_huawei_platform, ResourceTypeConstants.ECS);

        InstanceSearchField os_type = new InstanceSearchField("操作系统类型", "metadata.os_type", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("Linux", "Linux"),
                        new DefaultKeyValue<>("Windows", "Windows")

                ))
                .resetInstanceField(PlatformConstants.fit2cloud_huawei_platform, ResourceTypeConstants.ECS);

        InstanceSearchField __support_agent_list = new InstanceSearchField("操作系统类型", "metadata.os_type", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("企业主机安全", "hss"),
                        new DefaultKeyValue<>("主机监控", "ces"),
                        new DefaultKeyValue<>("委托的名称", "agency_name")
                ))
                .resetInstanceField(PlatformConstants.fit2cloud_huawei_platform, ResourceTypeConstants.ECS);

        InstanceSearchField delete_on_termination = new InstanceSearchField("是否随实例删除", "os-extended-volumes:volumes_attached.delete_on_termination", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("是", "true"),
                        new DefaultKeyValue<>("否", "false")
                ))
                .resetInstanceField(PlatformConstants.fit2cloud_huawei_platform, ResourceTypeConstants.ECS);

        return List.of(status, disk, vcpus, ram, diskConfig, charging_mode, imagetype, os_bit, os_type, __support_agent_list, delete_on_termination);
    }

    /**
     * 获取华为云oss 实例查询字段
     *
     * @return 华为云oss实例查询字段
     */
    public static List<InstanceSearchField> listOssInstanceSearchField() {
        InstanceSearchField encryption = new InstanceSearchField("加密方式", "encryption.sseAlgorithm", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("KMS", "KMS"),
                        new DefaultKeyValue<>("未开启", null)))
                .resetInstanceField(PlatformConstants.fit2cloud_huawei_platform, ResourceTypeConstants.OSS);

        InstanceSearchField accessIdentifier = new InstanceSearchField("acl拥有者", "acl.grants.grantee.identifier", InstanceFieldType.ArrayString)
                .resetInstanceField(PlatformConstants.fit2cloud_huawei_platform, ResourceTypeConstants.OSS);
        return List.of(encryption, accessIdentifier);
    }

    /**
     * 获取华为云 云磁盘 oss 实例查询字段
     *
     * @return 华为云 云磁盘 实例查询字段
     */
    public static List<InstanceSearchField> listDiskInstanceSearchField() {
        InstanceSearchField status = new InstanceSearchField("云硬盘状态", "status", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("正在创建", "creating"),
                        new DefaultKeyValue<>("可用", "available"),
                        new DefaultKeyValue<>("正在使用", "in-use"),
                        new DefaultKeyValue<>("错误", "error"),
                        new DefaultKeyValue<>("正在挂载", "attaching"),
                        new DefaultKeyValue<>("正在卸载", "detaching"),
                        new DefaultKeyValue<>("正在恢复", "restoring-backup"),
                        new DefaultKeyValue<>("正在创建备份", "backing-up"),
                        new DefaultKeyValue<>("正在恢复", "error_restoring"),
                        new DefaultKeyValue<>("正在上传", "uploading"),
                        new DefaultKeyValue<>("正在下载", "downloading"),
                        new DefaultKeyValue<>("正在扩容", "extending"),
                        new DefaultKeyValue<>("扩容失败", "error_extending"),
                        new DefaultKeyValue<>("正在删除", "deleting"),
                        new DefaultKeyValue<>("删除失败", "error_deleting"),
                        new DefaultKeyValue<>("正在回滚", "rollbacking"),
                        new DefaultKeyValue<>("回滚数据失败", "error_rollbacking"),
                        new DefaultKeyValue<>("等待过户", "awaiting-transfer")
                )).resetInstanceField(PlatformConstants.fit2cloud_huawei_platform, ResourceTypeConstants.DISK);

        InstanceSearchField volume_type = new InstanceSearchField("云硬盘类型", "volume_type", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("普通IO云硬盘", "SATA"),
                        new DefaultKeyValue<>("高IO云硬盘", "SAS"),
                        new DefaultKeyValue<>("通用型SSD云硬盘", "GPSSD"),
                        new DefaultKeyValue<>("超高IO云硬盘", "SSD"),
                        new DefaultKeyValue<>("极速IO云硬盘", "ESSD"),
                        new DefaultKeyValue<>("通用型SSD V2云硬盘", "GPSSD2"),
                        new DefaultKeyValue<>("极速型SSD V2云硬盘", "ESSD2")
                ))
                .resetInstanceField(PlatformConstants.fit2cloud_huawei_platform, ResourceTypeConstants.DISK);

        InstanceSearchField size = new InstanceSearchField("云硬盘类型", "size", InstanceFieldType.Number)
                .resetInstanceField(PlatformConstants.fit2cloud_huawei_platform, ResourceTypeConstants.DISK);

        InstanceSearchField bootable = new InstanceSearchField("云硬盘类型", "bootable", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("启动云硬盘", "true"),
                        new DefaultKeyValue<>("非启动云硬盘", "false")
                )).resetInstanceField(PlatformConstants.fit2cloud_huawei_platform, ResourceTypeConstants.DISK);

        InstanceSearchField encrypted = new InstanceSearchField("是否加密", "bootable", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("加密", true),
                        new DefaultKeyValue<>("不加密", false)
                )).resetInstanceField(PlatformConstants.fit2cloud_huawei_platform, ResourceTypeConstants.DISK);

        return List.of(status, volume_type, size, bootable, encrypted);
    }

    /**
     * 华为云 vpc实例查询字段
     *
     * @return 华为云 vpc 实例查询字段
     */
    public static List<InstanceSearchField> listVpcInstanceSearchField() {

        InstanceSearchField cidr = new InstanceSearchField("私有云下可用子网的范围", "cidr", InstanceFieldType.String).resetInstanceField(PlatformConstants.fit2cloud_huawei_platform, ResourceTypeConstants.VPC);

        InstanceSearchField status = new InstanceSearchField("私有云下可用子网的范围", "cidr", InstanceFieldType.Enum,
                List.of(
                        new DefaultKeyValue<>("创建中", "CREATING"),
                        new DefaultKeyValue<>("创建成功", "OK")
                )).resetInstanceField(PlatformConstants.fit2cloud_huawei_platform, ResourceTypeConstants.VPC);
        return List.of(status, cidr);
    }

    /**
     * 华为云 RAM实例查询字段
     *
     * @return 华为云 ram实例查询字段
     */
    public static List<InstanceSearchField> listRamInstanceSearchField() {
        InstanceSearchField enabled = new InstanceSearchField("IAM用户是否启用", "enabled", InstanceFieldType.Enum,
                List.of(
                        new DefaultKeyValue<>("启用", true),
                        new DefaultKeyValue<>("停用", false)
                )).resetInstanceField(PlatformConstants.fit2cloud_huawei_platform, ResourceTypeConstants.RAM);

        InstanceSearchField pwd_status = new InstanceSearchField("IAM用户密码状态", "pwd_status", InstanceFieldType.Enum,
                List.of(
                        new DefaultKeyValue<>("需要修改密码", true),
                        new DefaultKeyValue<>("正常", false)
                )).resetInstanceField(PlatformConstants.fit2cloud_huawei_platform, ResourceTypeConstants.RAM);

        InstanceSearchField pwd_strength = new InstanceSearchField("IAM用户的密码强度", "pwd_strength", InstanceFieldType.Enum,
                List.of(
                        new DefaultKeyValue<>("密码强度高", "high"),
                        new DefaultKeyValue<>("密码强度中等", "mid"),
                        new DefaultKeyValue<>("密码强度低", "low")
                )).resetInstanceField(PlatformConstants.fit2cloud_huawei_platform, ResourceTypeConstants.RAM);

        InstanceSearchField loginProfileEnabled = new InstanceSearchField("IAM用户的登录保护状态", "loginProfile.enabled", InstanceFieldType.Enum,
                List.of(
                        new DefaultKeyValue<>("开启", true),
                        new DefaultKeyValue<>("未开启", false)
                )).resetInstanceField(PlatformConstants.fit2cloud_huawei_platform, ResourceTypeConstants.RAM);

        return List.of(enabled, pwd_status, pwd_strength, loginProfileEnabled);
    }


    /**
     * 获取redis实例查询字段
     *
     * @return redis实例查询字段
     */
    public static List<InstanceSearchField> listRedisInstanceSearchField() {
        InstanceSearchField charging_mode = new InstanceSearchField("计费模式", "charging_mode", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("按需计费", 0),
                        new DefaultKeyValue<>("包年/包月计费", 1)))
                .resetInstanceField(PlatformConstants.fit2cloud_huawei_platform, ResourceTypeConstants.REDIS);

        InstanceSearchField enable_ssl = new InstanceSearchField("是否支持ssl", "enable_ssl", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("开启", true),
                        new DefaultKeyValue<>("不开启", false)))
                .resetInstanceField(PlatformConstants.fit2cloud_huawei_platform, ResourceTypeConstants.REDIS);

        InstanceSearchField max_memory = new InstanceSearchField("总内存，单位：MB", "max_memory", InstanceFieldType.Number)
                .resetInstanceField(PlatformConstants.fit2cloud_huawei_platform, ResourceTypeConstants.REDIS);

        InstanceSearchField used_memory = new InstanceSearchField("已使用的内存，单位：MB", "used_memory", InstanceFieldType.Number)
                .resetInstanceField(PlatformConstants.fit2cloud_huawei_platform, ResourceTypeConstants.REDIS);

        InstanceSearchField no_password_access = new InstanceSearchField("是否允许免密码访问缓存实例", "no_password_access", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("无需密码即可访问", true),
                        new DefaultKeyValue<>("密码认证才能访问", false)))
                .resetInstanceField(PlatformConstants.fit2cloud_huawei_platform, ResourceTypeConstants.REDIS);

        InstanceSearchField enable_publicip = new InstanceSearchField("是否开启公网访问功能", "enable_publicip", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("开启", true),
                        new DefaultKeyValue<>("开启", false)))
                .resetInstanceField(PlatformConstants.fit2cloud_huawei_platform, ResourceTypeConstants.REDIS);
        return List.of(charging_mode, enable_ssl, max_memory, used_memory, no_password_access, enable_publicip);
    }

    /**
     * mongodb 实例查询字段
     *
     * @return mongodb实例查询字段
     */
    public static List<InstanceSearchField> listMongodbInstanceSearchField() {

        InstanceSearchField pay_mode = new InstanceSearchField("计费方式", "pay_mode", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("按需计费", "0"),
                        new DefaultKeyValue<>("包年/包月计费", "1")))
                .resetInstanceField(PlatformConstants.fit2cloud_huawei_platform, ResourceTypeConstants.MONGO_DB);

        InstanceSearchField status = new InstanceSearchField("实例状态", "status", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("实例创建中", "normal"),
                        new DefaultKeyValue<>("实例创建中", "abnormal"),
                        new DefaultKeyValue<>("实例创建中", "creating"),
                        new DefaultKeyValue<>("实例被冻结", "frozen"),
                        new DefaultKeyValue<>("实例磁盘已满", "data_disk_full"),
                        new DefaultKeyValue<>("实例创建失败", "createfail"),
                        new DefaultKeyValue<>("实例扩容节点个数失败", "enlargefail")))
                .resetInstanceField(PlatformConstants.fit2cloud_huawei_platform, ResourceTypeConstants.MONGO_DB);

        InstanceSearchField ssl = new InstanceSearchField("是否开启SSL安全连接", "ssl", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("开启", 1),
                        new DefaultKeyValue<>("不开启", 0)))
                .resetInstanceField(PlatformConstants.fit2cloud_huawei_platform, ResourceTypeConstants.MONGO_DB);

        InstanceSearchField groupsType = new InstanceSearchField("节点类型", "groups.type", InstanceFieldType.ArrayEnum,
                List.of(new DefaultKeyValue<>("shard", "shard"),
                        new DefaultKeyValue<>("config", "config"),
                        new DefaultKeyValue<>("mongos", "mongos"),
                        new DefaultKeyValue<>("replica", "replica"),
                        new DefaultKeyValue<>("single", "single")))
                .resetInstanceField(PlatformConstants.fit2cloud_huawei_platform, ResourceTypeConstants.MONGO_DB);

        InstanceSearchField groupsVolumeSize = new InstanceSearchField("磁盘大小。单位:GB", "groups.volume.size", InstanceFieldType.ArrayNumber)
                .resetInstanceField(PlatformConstants.fit2cloud_huawei_platform, ResourceTypeConstants.MONGO_DB);

        InstanceSearchField groupsVolumeUsed = new InstanceSearchField("磁盘使用量。单位:GB", "groups.volume.used", InstanceFieldType.ArrayNumber)
                .resetInstanceField(PlatformConstants.fit2cloud_huawei_platform, ResourceTypeConstants.MONGO_DB);

        InstanceSearchField backupStrategyKeepDays = new InstanceSearchField("已生成备份文件可以保存的天数", "backup_strategy.keep_days", InstanceFieldType.Number)
                .resetInstanceField(PlatformConstants.fit2cloud_huawei_platform, ResourceTypeConstants.MONGO_DB);

        InstanceSearchField public_ip = new InstanceSearchField("节点绑定的外网IP", "groups.nodes.public_ip", InstanceFieldType.ArrayString)
                .resetInstanceField(PlatformConstants.fit2cloud_huawei_platform, ResourceTypeConstants.MONGO_DB);

        return List.of(pay_mode, status, ssl, groupsType, groupsVolumeSize, groupsVolumeUsed, backupStrategyKeepDays, public_ip);
    }

    public static List<InstanceSearchField> listMysqlInstanceSearchField() {
        return listRdsInstanceSearchField(ResourceTypeConstants.MYSQL);
    }

    public static List<InstanceSearchField> listSqlServerInstanceSearchField() {
        return listRdsInstanceSearchField(ResourceTypeConstants.SQL_SERVER);
    }

    public static List<InstanceSearchField> listPostGreSqlInstanceSearchField() {
        return listRdsInstanceSearchField(ResourceTypeConstants.POST_GRE_SQL);
    }

    public static List<InstanceSearchField> listRdsInstanceSearchField(ResourceTypeConstants resourceType) {
        InstanceSearchField status = new InstanceSearchField("实例状态", "status", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("正在创建", "BUILD"),
                        new DefaultKeyValue<>("正常", "ACTIVE"),
                        new DefaultKeyValue<>("异常", "FAILED"),
                        new DefaultKeyValue<>("冻结", "FROZEN"),
                        new DefaultKeyValue<>("正在扩容", "MODIFYING"),
                        new DefaultKeyValue<>("正在重启", "REBOOTING"),
                        new DefaultKeyValue<>("正在恢复", "RESTORING"),
                        new DefaultKeyValue<>("正在转主备", "MODIFYING INSTANCE TYPE"),
                        new DefaultKeyValue<>("正在主备切换", "SWITCHOVER"),
                        new DefaultKeyValue<>("正在迁移", "MIGRATING"),
                        new DefaultKeyValue<>("正在进行备份", "BACKING UP"),
                        new DefaultKeyValue<>("正在修改数据库端口", "MODIFYING DATABASE PORT"),
                        new DefaultKeyValue<>("磁盘空间满", "STORAGE FULL")))
                .resetInstanceField(PlatformConstants.fit2cloud_huawei_platform, resourceType);

        InstanceSearchField enable_ssl = new InstanceSearchField("是否开启SSL", "enable_ssl", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("开启了SSL", true),
                        new DefaultKeyValue<>("未开启了SSL", false)
                ))
                .resetInstanceField(PlatformConstants.fit2cloud_huawei_platform, resourceType);

        InstanceSearchField type = new InstanceSearchField("实例类型", "type", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("单机实例", "Single"),
                        new DefaultKeyValue<>("主备实例", "Ha"),
                        new DefaultKeyValue<>("只读实例", "Replica")
                ))
                .resetInstanceField(PlatformConstants.fit2cloud_huawei_platform, resourceType);

        InstanceSearchField switch_strategy = new InstanceSearchField("数据库切换策略", "switch_strategy", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("可靠性优先", "reliability"),
                        new DefaultKeyValue<>("可用性优先", "availability")
                ))
                .resetInstanceField(PlatformConstants.fit2cloud_huawei_platform, resourceType);

        InstanceSearchField volumeType = new InstanceSearchField("磁盘类型", "volume.type", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("SATA", "COMMON"),
                        new DefaultKeyValue<>("SAS", "HIGH"),
                        new DefaultKeyValue<>("SSD", "ULTRAHIGH"),
                        new DefaultKeyValue<>("SSD尊享版", "ULTRAHIGHPRO"),
                        new DefaultKeyValue<>("SSD云盘", "CLOUDSSD"),
                        new DefaultKeyValue<>("本地SSD", "LOCALSSD"),
                        new DefaultKeyValue<>("极速型SSD", "ESSD")
                ))
                .resetInstanceField(PlatformConstants.fit2cloud_huawei_platform, resourceType);

        InstanceSearchField volumeSize = new InstanceSearchField("磁盘大小,单位为GB", "volume.size", InstanceFieldType.Number)
                .resetInstanceField(PlatformConstants.fit2cloud_huawei_platform, resourceType);

        InstanceSearchField charge_mode = new InstanceSearchField("计费模式", "charge_info.charge_mode", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("包年/包月", "prePaid"),
                        new DefaultKeyValue<>("按需付费", "postPaid")
                ))
                .resetInstanceField(PlatformConstants.fit2cloud_huawei_platform, resourceType);


        InstanceSearchField backupStrategyKeepDays = new InstanceSearchField("备份文件的可保存天数", "backup_strategy.keep_days", InstanceFieldType.Number)
                .resetInstanceField(PlatformConstants.fit2cloud_huawei_platform, resourceType);

        InstanceSearchField cpu = new InstanceSearchField("CPU大小", "cpu", InstanceFieldType.Number)
                .resetInstanceField(PlatformConstants.fit2cloud_huawei_platform, resourceType);

        InstanceSearchField mem = new InstanceSearchField("内存大小(单位:GB)", "mem", InstanceFieldType.Number)
                .resetInstanceField(PlatformConstants.fit2cloud_huawei_platform, resourceType);

        InstanceSearchField time_zone = new InstanceSearchField("时区", "time_zone", InstanceFieldType.String)
                .resetInstanceField(PlatformConstants.fit2cloud_huawei_platform, resourceType);
        return List.of(status, enable_ssl, type, switch_strategy, volumeType, volumeSize, charge_mode, backupStrategyKeepDays, cpu, mem, time_zone);
    }


    public static List<InstanceSearchField> listElasticSearchInstanceSearchField() {

        InstanceSearchField status = new InstanceSearchField("实例状态", "status", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("创建中", "100"),
                        new DefaultKeyValue<>("可用", "200"),
                        new DefaultKeyValue<>("不可用", "303")))
                .resetInstanceField(PlatformConstants.fit2cloud_huawei_platform, ResourceTypeConstants.ELASTIC_SEARCH);

        InstanceSearchField bandwidthSize = new InstanceSearchField("公网带宽大小", "bandwidthSize", InstanceFieldType.Number)
                .resetInstanceField(PlatformConstants.fit2cloud_huawei_platform, ResourceTypeConstants.ELASTIC_SEARCH);

        InstanceSearchField httpsEnable = new InstanceSearchField("通信加密状态", "httpsEnable", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("未设置通信加密", false),
                        new DefaultKeyValue<>("已设置通信加密", true)))
                .resetInstanceField(PlatformConstants.fit2cloud_huawei_platform, ResourceTypeConstants.ELASTIC_SEARCH);

        InstanceSearchField authorityEnable = new InstanceSearchField("是否开启认证", "authorityEnable", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("集群开启认证", true),
                        new DefaultKeyValue<>("集群不开启认证", false)))
                .resetInstanceField(PlatformConstants.fit2cloud_huawei_platform, ResourceTypeConstants.ELASTIC_SEARCH);

        InstanceSearchField diskEncrypted = new InstanceSearchField("磁盘是否加密", "diskEncrypted", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("磁盘已加密", true),
                        new DefaultKeyValue<>("磁盘未加密", false)))
                .resetInstanceField(PlatformConstants.fit2cloud_huawei_platform, ResourceTypeConstants.ELASTIC_SEARCH);

        InstanceSearchField backupAvailable = new InstanceSearchField("快照是否开启", "backupAvailable", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("快照开启", true),
                        new DefaultKeyValue<>("快照关闭", false)))
                .resetInstanceField(PlatformConstants.fit2cloud_huawei_platform, ResourceTypeConstants.ELASTIC_SEARCH);

        InstanceSearchField period = new InstanceSearchField("计费模式", "period", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("包周期计费的集群", true),
                        new DefaultKeyValue<>("按需计费的集群", false)))
                .resetInstanceField(PlatformConstants.fit2cloud_huawei_platform, ResourceTypeConstants.ELASTIC_SEARCH);

        InstanceSearchField elbWhiteListEnableWhiteList = new InstanceSearchField("是否开启公网访问控制", "elbWhiteList.enableWhiteList", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("开启公网访问控制", true),
                        new DefaultKeyValue<>("关闭公网访问控制", false)))
                .resetInstanceField(PlatformConstants.fit2cloud_huawei_platform, ResourceTypeConstants.ELASTIC_SEARCH);

        InstanceSearchField elbWhiteList = new InstanceSearchField("公网访问白名单", "elbWhiteList.whiteList", InstanceFieldType.ArrayString)
                .resetInstanceField(PlatformConstants.fit2cloud_huawei_platform, ResourceTypeConstants.ELASTIC_SEARCH);

        InstanceSearchField instancesStatus = new InstanceSearchField("节点状态", "instances.status", InstanceFieldType.ArrayEnum,
                List.of(new DefaultKeyValue<>("创建中", "100"),
                        new DefaultKeyValue<>("可用", "200"),
                        new DefaultKeyValue<>("不可用", "303")))
                .resetInstanceField(PlatformConstants.fit2cloud_huawei_platform, ResourceTypeConstants.ELASTIC_SEARCH);


        InstanceSearchField instancesVolumeType = new InstanceSearchField("节点磁盘类型", "instances.volume.type", InstanceFieldType.ArrayEnum,
                List.of(new DefaultKeyValue<>("SATA", "COMMON"),
                        new DefaultKeyValue<>("SAS", "HIGH"),
                        new DefaultKeyValue<>("SSD", "ULTRAHIGH"),
                        new DefaultKeyValue<>("SSD尊享版", "ULTRAHIGHPRO"),
                        new DefaultKeyValue<>("SSD云盘", "CLOUDSSD"),
                        new DefaultKeyValue<>("本地SSD", "LOCALSSD"),
                        new DefaultKeyValue<>("极速型SSD", "ESSD")
                ))
                .resetInstanceField(PlatformConstants.fit2cloud_huawei_platform, ResourceTypeConstants.ELASTIC_SEARCH);

        InstanceSearchField instancesVolumeSize = new InstanceSearchField("节点磁盘大小", "instances.volume.size", InstanceFieldType.ArrayNumber)
                .resetInstanceField(PlatformConstants.fit2cloud_huawei_platform, ResourceTypeConstants.ELASTIC_SEARCH);

        return List.of(status, bandwidthSize, httpsEnable, authorityEnable, diskEncrypted, backupAvailable, period, elbWhiteListEnableWhiteList, elbWhiteList, instancesStatus, instancesVolumeType
                , instancesVolumeSize);
    }

    public static List<InstanceSearchField> listLoadBalancerInstanceSearchField() {
        InstanceSearchField status = new InstanceSearchField("实例状态", "provisioning_status", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("使用中", "ACTIVE"),
                        new DefaultKeyValue<>("删除中", "PENDING_DELETE")))
                .resetInstanceField(PlatformConstants.fit2cloud_huawei_platform, ResourceTypeConstants.LOAD_BALANCER);

        InstanceSearchField operating_status = new InstanceSearchField("负载均衡器的操作状态", "operating_status", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("正常运行", "ONLINE"),
                        new DefaultKeyValue<>("已冻结", "FROZEN")))
                .resetInstanceField(PlatformConstants.fit2cloud_huawei_platform, ResourceTypeConstants.LOAD_BALANCER);

        InstanceSearchField guaranteed = new InstanceSearchField("是否独享型", "guaranteed", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("共享型", false),
                        new DefaultKeyValue<>("独享型", true)))
                .resetInstanceField(PlatformConstants.fit2cloud_huawei_platform, ResourceTypeConstants.LOAD_BALANCER);

        InstanceSearchField ip_target_enable = new InstanceSearchField("是否启用跨VPC后端转发", "ip_target_enable", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("不开启", false),
                        new DefaultKeyValue<>("开启", true)))
                .resetInstanceField(PlatformConstants.fit2cloud_huawei_platform, ResourceTypeConstants.LOAD_BALANCER);

        InstanceSearchField deletion_protection_enable = new InstanceSearchField("是否开启删除保护", "deletion_protection_enable", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("不开启", false),
                        new DefaultKeyValue<>("开启", true)))
                .resetInstanceField(PlatformConstants.fit2cloud_huawei_platform, ResourceTypeConstants.LOAD_BALANCER);

        InstanceSearchField billing_info = new InstanceSearchField("计费模式", "billing_info", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("按需计费", null)))
                .resetInstanceField(PlatformConstants.fit2cloud_huawei_platform, ResourceTypeConstants.LOAD_BALANCER);
        return List.of(status, operating_status, guaranteed, ip_target_enable, deletion_protection_enable, billing_info);
    }

    public static List<InstanceSearchField> listPublicIpInstanceSearchField() {
        InstanceSearchField status = new InstanceSearchField("实例状态", "status", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("冻结", "FREEZED"),
                        new DefaultKeyValue<>("BIND_ERROR", "BIND_ERROR"),
                        new DefaultKeyValue<>("绑定中", "BINDING"),
                        new DefaultKeyValue<>("即将释放", "PENDING_DELETE"),
                        new DefaultKeyValue<>("即将创建", "PENDING_CREATE"),
                        new DefaultKeyValue<>("创建中", "NOTIFYING"),
                        new DefaultKeyValue<>("释放中", "NOTIFY_DELETE"),
                        new DefaultKeyValue<>("更新中", "PENDING_UPDATE"),
                        new DefaultKeyValue<>("未绑定", "DOWN"),
                        new DefaultKeyValue<>("绑定", "ACTIVE"),
                        new DefaultKeyValue<>("绑定ELB", "ELB"),
                        new DefaultKeyValue<>("绑定VPN", "VPN"),
                        new DefaultKeyValue<>("失败", "ERROR")))
                .resetInstanceField(PlatformConstants.fit2cloud_huawei_platform, ResourceTypeConstants.PUBLIC_IP);

        InstanceSearchField type = new InstanceSearchField("弹性公网IP类型", "type", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("EIP", "EIP"),
                        new DefaultKeyValue<>("DUALSTACK", "DUALSTACK"),
                        new DefaultKeyValue<>("DUALSTACK_SUBNET", "DUALSTACK_SUBNET")))
                .resetInstanceField(PlatformConstants.fit2cloud_huawei_platform, ResourceTypeConstants.PUBLIC_IP);

        InstanceSearchField associate_instance_type = new InstanceSearchField("公网IP绑定的实例类型", "associate_instance_type", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("PORT", "PORT"),
                        new DefaultKeyValue<>("NATGW", "NATGW"),
                        new DefaultKeyValue<>("ELB", "ELB"),
                        new DefaultKeyValue<>("ELBV1", "ELBV1"),
                        new DefaultKeyValue<>("VPN", "VPN"),
                        new DefaultKeyValue<>("未知", null)))
                .resetInstanceField(PlatformConstants.fit2cloud_huawei_platform, ResourceTypeConstants.PUBLIC_IP);

        InstanceSearchField cascade_delete_by_instance = new InstanceSearchField("eip是否支持与实例同步删除", "cascade_delete_by_instance", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("是", true),
                        new DefaultKeyValue<>("否", false),
                        new DefaultKeyValue<>("未知", null)))
                .resetInstanceField(PlatformConstants.fit2cloud_huawei_platform, ResourceTypeConstants.PUBLIC_IP);

        InstanceSearchField bandwidth_charge_mode = new InstanceSearchField("带宽计费方式", "bandwidth.charge_mode", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("按带宽计费", "bandwidth"),
                        new DefaultKeyValue<>("按流量计费", "traffic"),
                        new DefaultKeyValue<>("按增强型95计费", "95peak_plus")))
                .resetInstanceField(PlatformConstants.fit2cloud_huawei_platform, ResourceTypeConstants.PUBLIC_IP);

        InstanceSearchField bandwidth_share_type = new InstanceSearchField("带宽类型", "bandwidth.share_type", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("独享带宽", "PER"),
                        new DefaultKeyValue<>("共享带宽", "WHOLE")))
                .resetInstanceField(PlatformConstants.fit2cloud_huawei_platform, ResourceTypeConstants.PUBLIC_IP);

        InstanceSearchField bandwidth_size = new InstanceSearchField("带宽类型", "bandwidth.size", InstanceFieldType.Number)
                .resetInstanceField(PlatformConstants.fit2cloud_huawei_platform, ResourceTypeConstants.PUBLIC_IP);


        return List.of(status, type, associate_instance_type, cascade_delete_by_instance, bandwidth_charge_mode, bandwidth_share_type, bandwidth_size);
    }

    public static List<InstanceSearchField> listSecurityGroupInstanceSearchField() {
        InstanceSearchField direction = new InstanceSearchField("安全组规则的出入控制方向", "direction", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("入方向", "ingress"),
                        new DefaultKeyValue<>("出方向", "egress")))
                .resetFilterArrayField(PlatformConstants.fit2cloud_huawei_platform, ResourceTypeConstants.SECURITY_GROUP, "group_rule");

        InstanceSearchField protocol = new InstanceSearchField("网络协议", "protocol", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("icmp", "icmp"),
                        new DefaultKeyValue<>("tcp", "tcp"),
                        new DefaultKeyValue<>("udp", "udp")))
                .resetFilterArrayField(PlatformConstants.fit2cloud_huawei_platform, ResourceTypeConstants.SECURITY_GROUP, "group_rule");

        InstanceSearchField ethertype = new InstanceSearchField("IP地址协议类型", "ethertype", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("IPv4", "IPv4"),
                        new DefaultKeyValue<>("IPv6", "IPv6")))
                .resetFilterArrayField(PlatformConstants.fit2cloud_huawei_platform, ResourceTypeConstants.SECURITY_GROUP, "group_rule");

        InstanceSearchField multiport = new InstanceSearchField("端口取值范围", "multiport", InstanceFieldType.String)
                .resetFilterArrayField(PlatformConstants.fit2cloud_huawei_platform, ResourceTypeConstants.SECURITY_GROUP, "group_rule");

        InstanceSearchField action = new InstanceSearchField("安全组规则生效策略", "action", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("允许", "allow"),
                        new DefaultKeyValue<>("拒绝", "deny")))
                .resetFilterArrayField(PlatformConstants.fit2cloud_huawei_platform, ResourceTypeConstants.SECURITY_GROUP, "group_rule");

        InstanceSearchField remote_ip_prefix = new InstanceSearchField("远端IP地址", "remoteIpPrefix", InstanceFieldType.String)
                .resetFilterArrayField(PlatformConstants.fit2cloud_huawei_platform, ResourceTypeConstants.SECURITY_GROUP, "group_rule");

        return List.of(direction, protocol, ethertype, multiport, action, remote_ip_prefix);
    }
}
