package com.fit2cloud.provider.impl.huawei.constants;

/**
 * @author jianneng
 * @date 2022/11/4 11:47
 **/
public class HuaweiPerfMetricConstants {

    /**
     * 云主机性能指标枚举
     */
    public enum CloudServerPerfMetricEnum {
        CPU_USED_UTILIZATION("cpu_util", "CPU使用率","%"),
        MEMORY_USED_UTILIZATION("mem_util", "内存使用率","%"),
        DISK_READ_BPS("disk_read_bytes_rate", "所有磁盘读取BPS","Byte/s"),
        DISK_WRITE_BPS("disk_write_bytes_rate", "所有磁盘写入BPS","Byte/s"),
        DISK_READ_IOPS("disk_read_requests_rate", "所有磁盘每秒读取次数","Count/Second"),
        DISK_WRITE_IOPS("disk_write_requests_rate", "所有磁盘每秒写入次数","Count/Second"),
        INTERNET_IN_RATE("network_incoming_bytes_aggregate_rate", "公网流入带宽","Byte/s"),
        INTERNET_OUT_RATE("network_outgoing_bytes_aggregate_rate", "公网流出带宽","Byte/s"),
        INTRANET_IN_RATE("network_incoming_bytes_rate_inband", "内网流入带宽","Byte/s"),
        INTRANET_OUT_RATE("network_outgoing_bytes_rate_inband", "内网流出带宽","Byte/s"),
        DISK_USED_UTILIZATION("disk_util_inband","磁盘使用率","%"),
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
     * 磁盘的无法获取
     * 磁盘枚举
     */
    public enum CloudDiskPerfMetricEnum {
        DISK_READ_BPS("DiskReadBytes", "磁盘读取BPS","Bytes/s"),
        DISK_WRITE_BPS("DiskWriteBytes", "磁盘写入BPS","Bytes/s"),
        DISK_READ_IOPS("DiskIOPSRead", "磁盘每秒读取次数","Count/Second"),
        DISK_WRITE_IOPS("DiskIOPSWrite", "磁盘每秒写入次数","Count/Second"),
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
