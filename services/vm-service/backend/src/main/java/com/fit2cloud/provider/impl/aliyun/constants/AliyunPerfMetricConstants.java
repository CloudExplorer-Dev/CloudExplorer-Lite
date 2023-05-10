package com.fit2cloud.provider.impl.aliyun.constants;

/**
 * 监控相关常量
 *
 * @author jianneng
 * @date 2022/10/28 12:07
 **/
public class AliyunPerfMetricConstants {

    /**
     * 云主机性能监控指标指标
     * 监控数据粒度60秒
     */
    public enum CloudServerPerfMetricEnum {
        /**
         * CPU使用率
         */
        CPU_USED_UTILIZATION("CPUUtilization", "cpu_total", "CPU使用率", "%"),
        /**
         * 内存使用率
         */
        MEMORY_USED_UTILIZATION("", "memory_usedutilization", "内存使用率", "%"),
        /**
         * 磁盘读取BPS
         * 如果是基础指标，表示针对所有磁盘，否则单个磁盘
         */
        DISK_READ_BPS("DiskReadBPS", "disk_readbytes", "磁盘读取BPS", "bytes/s"),
        /**
         * 磁盘写入BPS
         * 如果是基础指标，表示针对所有磁盘，否则单个磁盘
         */
        DISK_WRITE_BPS("DiskWriteBPS", "disk_writebytes", "磁盘写入BPS", "bytes/s"),
        /**
         * 磁盘每秒读取次数
         * 如果是基础指标，表示针对所有磁盘，否则单个磁盘
         */
        DISK_READ_IOPS("DiskReadIOPS", "disk_readiops", "磁盘每秒读取次数", "Count/Second"),
        /**
         * 磁盘每秒写入次数
         * 如果是基础指标，表示针对所有磁盘，否则单个磁盘
         */
        DISK_WRITE_IOPS("DiskWriteIOPS", "disk_writeiops", "磁盘每秒写入次数", "Count/Second"),
        /**
         * 公网流入带宽
         * 不区分基础跟Agent，因为Agent没找到对应的指标,直接使用基础的指标
         */
        INTERNET_IN_RATE("VPC_PublicIP_InternetInRate", "VPC_PublicIP_InternetInRate", "公网流入带宽", "bit/s"),
        /**
         * 公网流出带宽
         * 不区分基础跟Agent，因为Agent没找到对应的指标,直接使用基础的指标
         */
        INTERNET_OUT_RATE("VPC_PublicIP_InternetOutRate", "VPC_PublicIP_InternetOutRate", "公网流出带宽", "bit/s"),
        /**
         * 内网流入带宽
         * 不区分基础跟Agent，因为Agent没找到对应的指标,直接使用基础的指标
         */
        INTRANET_IN_RATE("IntranetInRate", "IntranetInRate", "内网流入带宽", "bit/s"),
        /**
         * 内网流出带宽
         * 不区分基础跟Agent，因为Agent没找到对应的指标,直接使用基础的指标
         */
        INTRANET_OUT_RATE("IntranetOutRate", "IntranetOutRate", "内网流出带宽", "bit/s"),
        /**
         * 磁盘使用率
         */
        DISK_USED_UTILIZATION("", "diskusage_utilization", "磁盘使用率", "%"),
        ;

        /**
         * 基础监控指标名称
         */
        private final String baseMetricName;
        /**
         * Agent监控指标名称
         */
        private final String agentMetricName;
        /**
         * 描述
         */
        private final String description;
        /**
         * 单位
         */
        private final String unit;

        CloudServerPerfMetricEnum(String baseMetricName, String agentMetricName, String description, String unit) {
            this.baseMetricName = baseMetricName;
            this.agentMetricName = agentMetricName;
            this.description = description;
            this.unit = unit;
        }

        public String getBaseMetricName() {
            return baseMetricName;
        }

        public String getAgentMetricName() {
            return agentMetricName;
        }

        public String getDescription() {
            return description;
        }

        public String getUnit() {
            return unit;
        }

    }
}
