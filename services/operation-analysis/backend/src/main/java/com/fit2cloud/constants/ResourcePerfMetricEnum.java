package com.fit2cloud.constants;

/**
 * @author jianneng
 * @date 2023/3/15 15:03
 **/
public enum ResourcePerfMetricEnum {
    /**
     * 资源监控指标
     */
    CPU_USED_UTILIZATION("CPU_USED_UTILIZATION", "CPU使用率"),
    MEMORY_USED_UTILIZATION("MEMORY_USED_UTILIZATION", "内存使用率"),
    DISK_READ_BPS("DISK_READ_BPS", "所有磁盘读取BPS"),
    DISK_WRITE_BPS("DISK_WRITE_BPS", "所有磁盘写入BPS"),
    DISK_READ_IOPS("DISK_READ_IOPS", "所有磁盘每秒读取次数"),
    DISK_WRITE_IOPS("DISK_WRITE_IOPS", "所有磁盘每秒写入次数"),
    INTERNET_IN_RATE("INTERNET_IN_RATE", "公网流入带宽"),
    INTERNET_OUT_RATE("INTERNET_OUT_RATE", "公网流出带宽"),
    INTRANET_IN_RATE("INTRANET_IN_RATE", "内网流入带宽"),
    INTRANET_OUT_RATE("INTRANET_OUT_RATE", "内网流出带宽"),
    DISK_USED_UTILIZATION("DISK_USED_UTILIZATION", "磁盘使用率"),
    ;

    /**
     * 名称
     */
    private final String metricName;

    /**
     * 描述
     */
    private final String description;
    ResourcePerfMetricEnum(String metricName, String description) {
        this.metricName = metricName;
        this.description = description;
    }

}
