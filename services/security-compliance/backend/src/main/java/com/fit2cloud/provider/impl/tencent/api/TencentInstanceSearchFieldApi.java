package com.fit2cloud.provider.impl.tencent.api;

import com.fit2cloud.provider.entity.InstanceFieldType;
import com.fit2cloud.provider.entity.InstanceSearchField;
import com.fit2cloud.provider.util.FieldUtil;
import org.apache.commons.collections4.keyvalue.DefaultKeyValue;

import java.util.List;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2023/1/4  14:14}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
public class TencentInstanceSearchFieldApi {
    public static final String ECS_INSTANCE_PREFIX = "instance.fit2cloud_tencent_platform_ECS.";

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
}
