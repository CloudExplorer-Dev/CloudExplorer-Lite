package com.fit2cloud.provider.impl.openstack.api;

import com.fit2cloud.constants.ResourceTypeConstants;
import com.fit2cloud.provider.entity.InstanceFieldType;
import com.fit2cloud.provider.entity.InstanceSearchField;
import com.fit2cloud.provider.impl.openstack.OpenstackProvider;
import org.apache.commons.collections4.keyvalue.DefaultKeyValue;

import java.util.List;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2023/2/7  10:09}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
public class OpenstackSearchFieldApi {

    /**
     * 获取vsphere ecs云主机查询字段
     *
     * @return vsphere 云主机查询字段
     */
    public static List<InstanceSearchField> listEcsInstanceSearchField() {
        InstanceSearchField cpu = new InstanceSearchField("cpu", "cpu", InstanceFieldType.Number)
                .resetInstanceField(OpenstackProvider.openStackBaseCloudProvider.getCloudAccountMeta().platform, ResourceTypeConstants.ECS);
        InstanceSearchField memory = new InstanceSearchField("内存(单位: GB)", "memory", InstanceFieldType.Number)
                .resetInstanceField(OpenstackProvider.openStackBaseCloudProvider.getCloudAccountMeta().platform, ResourceTypeConstants.ECS);

        InstanceSearchField status = new InstanceSearchField("实例状态", "instanceStatus", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("运行中", "Running"),
                        new DefaultKeyValue<>("停止", "Stopped"),
                        new DefaultKeyValue<>("创建中", "Creating"),
                        new DefaultKeyValue<>("变更中", "Resize"),
                        new DefaultKeyValue<>("重启中", "Rebooting"),
                        new DefaultKeyValue<>("已删除", "Deleted"),
                        new DefaultKeyValue<>("未知", "Unknown")))
                .resetInstanceField(OpenstackProvider.openStackBaseCloudProvider.getCloudAccountMeta().platform, ResourceTypeConstants.ECS);

        InstanceSearchField osInfo = new InstanceSearchField("操作系统", "osInfo", InstanceFieldType.String);

        return List.of(cpu, memory, status, osInfo);
    }

    public static List<InstanceSearchField> listSecurityInstanceSearchField() {
        InstanceSearchField direction = new InstanceSearchField("方向", "direction", InstanceFieldType.NestedArrayEnum,
                List.of(new DefaultKeyValue<>("入方向", "ingress"),
                        new DefaultKeyValue<>("出方向", "egress")))
                .resetFilterArrayField(OpenstackProvider.openStackBaseCloudProvider.getCloudAccountMeta().platform, ResourceTypeConstants.SECURITY_GROUP, "group_rule");

        InstanceSearchField etherType = new InstanceSearchField("ip类型", "ethertype", InstanceFieldType.NestedArrayEnum,
                List.of(new DefaultKeyValue<>("IPv4", "IPv4"),
                        new DefaultKeyValue<>("IPv6", "IPv6")))
                .resetFilterArrayField(OpenstackProvider.openStackBaseCloudProvider.getCloudAccountMeta().platform, ResourceTypeConstants.SECURITY_GROUP, "group_rule");

        InstanceSearchField protocol = new InstanceSearchField("协议", "protocol", InstanceFieldType.NestedArrayEnum,
                List.of(new DefaultKeyValue<>("TCP", "tcp"),
                        new DefaultKeyValue<>("UDP", "udp"),
                        new DefaultKeyValue<>("ICMP", "icmp")))
                .resetFilterArrayField(OpenstackProvider.openStackBaseCloudProvider.getCloudAccountMeta().platform, ResourceTypeConstants.SECURITY_GROUP, "group_rule");


        InstanceSearchField port_range_max = new InstanceSearchField("结束端口", "port_range_max", InstanceFieldType.NestedArrayNumber)
                .resetFilterArrayField(OpenstackProvider.openStackBaseCloudProvider.getCloudAccountMeta().platform, ResourceTypeConstants.SECURITY_GROUP, "group_rule", false);

        InstanceSearchField port_range_min = new InstanceSearchField("开始端口", "port_range_min", InstanceFieldType.NestedArrayNumber)
                .resetFilterArrayField(OpenstackProvider.openStackBaseCloudProvider.getCloudAccountMeta().platform, ResourceTypeConstants.SECURITY_GROUP, "group_rule", false);

        InstanceSearchField remote_ip_prefix = new InstanceSearchField("CIDR", "remote_ip_prefix", InstanceFieldType.NestedArrayString)
                .resetFilterArrayField(OpenstackProvider.openStackBaseCloudProvider.getCloudAccountMeta().platform, ResourceTypeConstants.SECURITY_GROUP, "group_rule");
        return List.of(direction, etherType, protocol, port_range_min, port_range_max, remote_ip_prefix);
    }
}
