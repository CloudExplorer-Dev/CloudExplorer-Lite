package com.fit2cloud.provider.impl.vsphere.api;

import com.fit2cloud.common.constants.PlatformConstants;
import com.fit2cloud.constants.ResourceTypeConstants;
import com.fit2cloud.provider.entity.InstanceFieldType;
import com.fit2cloud.provider.entity.InstanceSearchField;
import org.apache.commons.collections4.keyvalue.DefaultKeyValue;

import java.util.List;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2023/2/6  18:22}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
public class VsphereInstanceSearchApi {
    /**
     * 获取vsphere ecs云主机查询字段
     *
     * @return vsphere 云主机查询字段
     */
    public static List<InstanceSearchField> listEcsInstanceSearchField() {
        InstanceSearchField cpu = new InstanceSearchField("cpu", "cpu", InstanceFieldType.Number)
                .resetInstanceField(PlatformConstants.fit2cloud_vsphere_platform, ResourceTypeConstants.ECS);

        InstanceSearchField memory = new InstanceSearchField("内存", "memory", InstanceFieldType.Number);

        InstanceSearchField status = new InstanceSearchField("实例状态", "instanceStatus", InstanceFieldType.Enum,
                List.of(new DefaultKeyValue<>("运行中", "Running"),
                        new DefaultKeyValue<>("停止", "Stopped"),
                        new DefaultKeyValue<>("未知", "Unknown")));

        InstanceSearchField osInfo = new InstanceSearchField("操作系统", "osInfo", InstanceFieldType.String);

        return List.of(cpu, memory, status, osInfo);
    }

}
