package com.fit2cloud.provider.impl.aliyun.api;

import com.fit2cloud.provider.entity.InstanceFieldType;
import com.fit2cloud.provider.entity.InstanceSearchField;
import com.fit2cloud.provider.util.FieldUtil;
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
}
