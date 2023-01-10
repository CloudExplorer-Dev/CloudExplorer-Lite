package com.fit2cloud.provider.impl.aliyun.constants;

/**
 * 监控相关常量
 * @author jianneng
 * @date 2022/10/28 12:07
 **/
public class AliyunPerfMetricConstants {

    /**
     * 云主机性能指标枚举
     */
    public enum CloudServerPerfMetricEnum {
        CPU_USED_UTILIZATION("CPUUtilization", "CPU使用率","%"),
        MEMORY_USED_UTILIZATION("memory_usedutilization", "内存使用率","%"),
        DISK_READ_BPS("DiskReadBPS", "所有磁盘读取BPS","Byte/s"),
        DISK_WRITE_BPS("DiskWriteBPS", "所有磁盘写入BPS","Byte/s"),
        DISK_READ_IOPS("DiskReadIOPS", "所有磁盘每秒读取次数","Count/Second"),
        DISK_WRITE_IOPS("DiskWriteIOPS", "所有磁盘每秒写入次数","Count/Second"),
        INTERNET_IN_RATE("VPC_PublicIP_InternetInRate", "公网流入带宽","bit/s"),
        INTERNET_OUT_RATE("VPC_PublicIP_InternetOutRate", "公网流出带宽","bit/s"),
        INTRANET_IN_RATE("IntranetInRate", "内网流入带宽","bit/s"),
        INTRANET_OUT_RATE("IntranetOutRate", "内网流出带宽","bit/s"),
        DISK_USED_UTILIZATION("diskusage_utilization","磁盘使用率","%"),
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
