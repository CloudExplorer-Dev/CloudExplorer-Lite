package com.fit2cloud.provider.impl.tencent.constants;

/**
 * @author jianneng
 * @date 2022/11/4 11:47
 **/
public class TencentPerfMetricConstants {

    /**
     * 云主机性能指标枚举
     */
    public enum CloudServerPerfMetricEnum {
        CPU_USED_UTILIZATION("CPUUsage", "CPU使用率","%"),
        MEMORY_USED_UTILIZATION("MemUsage", "内存使用率","%"),
        INTERNET_IN_RATE("WanIntraffic", "公网流入带宽","Mbps"),
        INTERNET_OUT_RATE("WanOuttraffic", "公网流出带宽","Mbps"),
        INTRANET_IN_RATE("LanIntraffic", "内网流入带宽","Mbps"),
        INTRANET_OUT_RATE("LanOuttraffic", "内网流出带宽","Mbps"),
        ;
        /**
         * 名称
         */
        private final String metricName;

        /**
         * 描述
         */
        private final String description;
        /**
         * 单位
         */
        private final String unit;

        CloudServerPerfMetricEnum(String metricName, String description, String unit) {
            this.metricName = metricName;
            this.description = description;
            this.unit = unit;
        }
        public String getMetricName() {
            return metricName;
        }
        public String getDescription() {
            return description;
        }
        public String getUnit() {
            return unit;
        }

    }

    /**
     * 磁盘枚举
     */
    public enum CloudDiskPerfMetricEnum {
        DISK_READ_BPS("DiskReadTraffic", "所有磁盘读取BPS","KB/s"),
        DISK_WRITE_BPS("DiskWriteTraffic", "所有磁盘写入BPS","KB/s"),
        DISK_READ_IOPS("DiskReadIops", "所有磁盘每秒读取次数","Count/Second"),
        DISK_WRITE_IOPS("DiskWriteIops", "所有磁盘每秒写入次数","Count/Second"),
        ;
        /**
         * 名称
         */
        private final String metricName;

        /**
         * 描述
         */
        private final String description;
        /**
         * 单位
         */
        private final String unit;

        CloudDiskPerfMetricEnum(String metricName, String description, String unit) {
            this.metricName = metricName;
            this.description = description;
            this.unit = unit;
        }
        public String getMetricName() {
            return metricName;
        }
        public String getDescription() {
            return description;
        }
        public String getUnit() {
            return unit;
        }

    }
}
