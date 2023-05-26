package com.fit2cloud.provider.impl.vsphere.entity;

import com.fit2cloud.common.provider.entity.F2CEntityType;
import lombok.Data;

/**
 * 将监控数据转换为平台监控数据对象所需要的字段
 *
 * @author jianneng
 * @date 2023/5/25 11:26
 **/
@Data
public class ConvertToPerfMetricDataVO {


    /**
     * 对象类型
     */
    private F2CEntityType entityType;
    /**
     * 监控对象ID或UUID
     */
    private String instanceId;
    /**
     * 宿主机ID
     * 云主机监控数据，需要返回云主机的宿主机ID
     */
    private String hostId;
    /**
     * 集群名称
     * 宿主机监控数据, 需要返回集群名称
     */
    private String clusterName;
    /**
     * 指标值，对应指标枚举中的metricName
     */
    private String metricName;
    /**
     * 指标名称，对应指标枚举中的枚举名称
     */
    private String name;
    /**
     * 数据单位，对应指标枚举中的unit
     */
    private String unit;
    /**
     * 除数，对应指标枚举中的divisor
     * 对于指标需要做数据转换时的除数
     */
    private Long divisor;

    /**
     * 数据间隔
     */
    private int period;

}
