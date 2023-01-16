package com.fit2cloud.provider.impl.huawei.api;

import com.fit2cloud.constants.ResourceTypeConstants;
import com.fit2cloud.provider.entity.InstanceFieldType;
import com.fit2cloud.provider.entity.InstanceSearchField;
import com.fit2cloud.provider.util.FieldUtil;
import io.swagger.models.auth.In;
import org.apache.commons.collections4.keyvalue.DefaultKeyValue;
import org.springframework.data.keyvalue.core.event.KeyValueEvent;

import java.util.List;
import java.util.Map;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2023/1/4  11:53}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
public class HuaweiInstanceSearchFieldApi {
    /**
     * 云服务器实例前缀
     */
    public static final String ECS_INSTANCE_PREFIX = "instance.fit2cloud_huawei_platform_ECS.";

    /**
     * 对象存储实例前缀
     */
    public static final String OSS_INSTANCE_PREFIX = "instance.fit2cloud_huawei_platform_OSS.";

    public static final String DISK_INSTANCE_PREFIX = "instance.fit2cloud_huawei_platform_DISK.";

    public static final String VPC_INSTANCE_PREFIX = "instance.fit2cloud_huawei_platform_VPC.";

    public static final String PLATFORM = "fit2cloud_huawei_platform";


    public static List<InstanceSearchField> listEcsInstanceSearchField() {
        InstanceSearchField status = new InstanceSearchField("云服务器状态", "status.keyword", InstanceFieldType.Enum,
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
                ));

        InstanceSearchField disk = new InstanceSearchField("云服务器规格对应要求系统盘大小,0为不限制", "disk", InstanceFieldType.Number);

        InstanceSearchField vcpus = new InstanceSearchField("云服务器规格对应的CPU核数", "vcpus", InstanceFieldType.Number);

        InstanceSearchField ram = new InstanceSearchField("云服务器规格对应的内存大小,单位为MB", "ram", InstanceFieldType.Number);

        InstanceSearchField diskConfig = new InstanceSearchField("diskConfig的类型", "OS-DCF:diskConfig", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("镜像空间不会扩展", "OS-DCF:diskConfig"),
                        new DefaultKeyValue<>("系统盘镜像空间会自动扩展为与flavor大小一致", "AUTO")

                ));

        InstanceSearchField charging_mode = new InstanceSearchField("服务器的计费类型", "metadata.charging_mode", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("按需计费", "0"),
                        new DefaultKeyValue<>("包年包月", "1")

                ));


        InstanceSearchField imagetype = new InstanceSearchField("镜像类型", "metadata.metering.imagetype", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("公共镜像", "gold"),
                        new DefaultKeyValue<>("私有镜像", "private"),
                        new DefaultKeyValue<>("共享镜像", "shared")

                ));

        InstanceSearchField os_bit = new InstanceSearchField("操作系统位数", "metadata.os_bit", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("32位", "32"),
                        new DefaultKeyValue<>("64位", "64")

                ));
        InstanceSearchField os_type = new InstanceSearchField("操作系统类型", "metadata.os_type", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("Linux", "Linux"),
                        new DefaultKeyValue<>("Windows", "Windows")

                ));

        InstanceSearchField __support_agent_list = new InstanceSearchField("操作系统类型", "metadata.os_type", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("企业主机安全", "hss"),
                        new DefaultKeyValue<>("主机监控", "ces"),
                        new DefaultKeyValue<>("委托的名称", "agency_name")
                ));

        InstanceSearchField delete_on_termination = new InstanceSearchField("是否随实例删除", "os-extended-volumes:volumes_attached.delete_on_termination.keyword", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("是", "true"),
                        new DefaultKeyValue<>("否", "false")
                ));

        return FieldUtil.appendPrefixField(ECS_INSTANCE_PREFIX, List.of(status, disk,
                vcpus, ram, diskConfig, charging_mode, imagetype, os_bit, os_type, __support_agent_list, delete_on_termination));
    }

    public static List<InstanceSearchField> listOssInstanceSearchField() {
        InstanceSearchField encryption = new InstanceSearchField("加密方式", "encryption.sseAlgorithm", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("KMS", "KMS"),
                        new DefaultKeyValue<>("未开启", null)
                ));
        InstanceSearchField accessIdentifier = new InstanceSearchField("acl拥有者", "acl.grants.grantee.identifier", InstanceFieldType.ArrayString);
        return FieldUtil.appendPrefixField(OSS_INSTANCE_PREFIX, List.of(encryption, accessIdentifier));
    }

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
                ));

        InstanceSearchField volume_type = new InstanceSearchField("云硬盘类型", "volume_type", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("普通IO云硬盘", "SATA"),
                        new DefaultKeyValue<>("高IO云硬盘", "SAS"),
                        new DefaultKeyValue<>("通用型SSD云硬盘", "GPSSD"),
                        new DefaultKeyValue<>("超高IO云硬盘", "SSD"),
                        new DefaultKeyValue<>("极速IO云硬盘", "ESSD"),
                        new DefaultKeyValue<>("通用型SSD V2云硬盘", "GPSSD2"),
                        new DefaultKeyValue<>("极速型SSD V2云硬盘", "ESSD2")
                ));

        InstanceSearchField size = new InstanceSearchField("云硬盘类型", "size", InstanceFieldType.Number);

        InstanceSearchField bootable = new InstanceSearchField("云硬盘类型", "bootable", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("启动云硬盘", "true"),
                        new DefaultKeyValue<>("非启动云硬盘", "false")
                ));

        InstanceSearchField encrypted = new InstanceSearchField("是否加密", "bootable", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("加密", true),
                        new DefaultKeyValue<>("不加密", false)
                ));

        return FieldUtil.appendPrefixField(DISK_INSTANCE_PREFIX, List.of(status, volume_type,
                size, bootable, encrypted));
    }

    public static List<InstanceSearchField> listVpcInstanceSearchField() {

        InstanceSearchField cidr = new InstanceSearchField("私有云下可用子网的范围", "cidr", InstanceFieldType.String);

        InstanceSearchField status = new InstanceSearchField("私有云下可用子网的范围", "cidr", InstanceFieldType.String,
                List.of(
                        new DefaultKeyValue<>("创建中", "CREATING"),
                        new DefaultKeyValue<>("创建成功", "OK")
                ));
        return FieldUtil.appendPrefixField(VPC_INSTANCE_PREFIX, List.of(status, cidr));
    }

    public static List<InstanceSearchField> listRamInstanceSearchField() {
        InstanceSearchField enabled = new InstanceSearchField("IAM用户是否启用", "enabled", InstanceFieldType.String,
                List.of(
                        new DefaultKeyValue<>("启用", true),
                        new DefaultKeyValue<>("停用", false)
                ));
        InstanceSearchField pwd_status = new InstanceSearchField("IAM用户密码状态", "pwd_status", InstanceFieldType.String,
                List.of(
                        new DefaultKeyValue<>("需要修改密码", true),
                        new DefaultKeyValue<>("正常", false)
                ));

        InstanceSearchField pwd_strength = new InstanceSearchField("IAM用户的密码强度", "pwd_strength", InstanceFieldType.String,
                List.of(
                        new DefaultKeyValue<>("密码强度高", "high"),
                        new DefaultKeyValue<>("密码强度中等", "mid"),
                        new DefaultKeyValue<>("密码强度低", "low")
                ));

        InstanceSearchField loginProfileEnabled = new InstanceSearchField("IAM用户的登录保护状态", "loginProfile.enabled", InstanceFieldType.String,
                List.of(
                        new DefaultKeyValue<>("开启", true),
                        new DefaultKeyValue<>("未开启", false)
                ));
        return FieldUtil.appendPrefixField(PLATFORM, ResourceTypeConstants.RAM, List.of(enabled, pwd_status, pwd_strength, loginProfileEnabled));
    }

    public static List<InstanceSearchField> listEipInstanceSearchField() {
        InstanceSearchField bandwidth_share_type = new InstanceSearchField("带宽类型", "bandwidth.share_type", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("共享带宽", "WHOLE"),
                        new DefaultKeyValue<>("独享带宽", "PER")));
        InstanceSearchField bandwidth_size = new InstanceSearchField("带宽大小", "bandwidth.size", InstanceFieldType.Number);

        InstanceSearchField charge_mode = new InstanceSearchField("计费方式", "bandwidth.charge_mode", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("按带宽计费", "bandwidth"),
                        new DefaultKeyValue<>("按流量计费", "traffic"),
                        new DefaultKeyValue<>("按增强型95计费", "95peak_plus")));

        InstanceSearchField status = new InstanceSearchField("弹性公网IP的状态", "status", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("冻结", "FREEZED"),
                        new DefaultKeyValue<>("绑定失败", "BIND_ERROR"),
                        new DefaultKeyValue<>("绑定中", "BINDING"),
                        new DefaultKeyValue<>("释放中", "PENDING_DELETE"),
                        new DefaultKeyValue<>("创建中", "PENDING_CREATE"),
                        new DefaultKeyValue<>("创建中(通知)", "NOTIFYING"),
                        new DefaultKeyValue<>("释放中(通知)", "NOTIFY_DELETE"),
                        new DefaultKeyValue<>("更新中", "PENDING_UPDATE"),
                        new DefaultKeyValue<>("未绑定", "DOWN"),
                        new DefaultKeyValue<>("绑定", "ACTIVE"),
                        new DefaultKeyValue<>("绑定ELB", "ELB"),
                        new DefaultKeyValue<>("绑定VPN", "VPN"),
                        new DefaultKeyValue<>("失败", "ERROR")
                ));
        return FieldUtil.appendPrefixField(PLATFORM, ResourceTypeConstants.PUBLIC_IP, List.of(bandwidth_share_type, bandwidth_size
                , charge_mode, status));


    }
}
