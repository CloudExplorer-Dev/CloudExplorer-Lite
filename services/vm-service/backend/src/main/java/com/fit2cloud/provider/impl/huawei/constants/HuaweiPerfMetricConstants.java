package com.fit2cloud.provider.impl.huawei.constants;

/**
 * @author jianneng
 * @date 2022/11/4 11:47
 **/
public class HuaweiPerfMetricConstants {

    /**
     * 云主机性能指标枚举
     * 监控数据粒度300秒
     * 网络统一为基础指标，Agnet没有
     * 华为云无法判断云主机agent状态
     * 处理方式：查询Agent指标无数据，转查询基础指标监控
     * 除了CPU,内存，磁盘与网络都是基础指标的数据，因为API无法获取
     * 官网有指标，但是无法读取数据，缺少参数（mount_pint）
     */
    public enum CloudServerPerfMetricEnum {
        /**
         * CPU使用率
         */
        CPU_USED_UTILIZATION("cpu_util", "cpu_usage", "CPU使用率", "%"),
        /**
         * 内存使用率
         */
        MEMORY_USED_UTILIZATION("mem_util", "mem_usedPercent", "内存使用率", "%"),
        /**
         * 所有磁盘读取BPS
         */
        DISK_READ_BPS("disk_read_bytes_rate", "mountPointPrefix_disk_agt_read_bytes_rate", "所有磁盘读取BPS", "byte/s"),
        /**
         * 所有磁盘写入BPS
         */
        DISK_WRITE_BPS("disk_write_bytes_rate", "mountPointPrefix_disk_agt_write_bytes_rate", "所有磁盘写入BPS", "byte/s"),
        /**
         * 所有磁盘每秒读取次数
         */
        DISK_READ_IOPS("disk_read_requests_rate", "mountPointPrefix_disk_agt_read_requests_rate", "所有磁盘每秒读取次数", "Count/Second"),
        /**
         * 所有磁盘每秒写入次数
         */
        DISK_WRITE_IOPS("disk_write_requests_rate", "mountPointPrefix_disk_agt_write_requests_rate", "所有磁盘每秒写入次数", "Count/Second"),
        /**
         * 公网流入带宽
         */
        INTERNET_IN_RATE("network_incoming_bytes_aggregate_rate", "network_incoming_bytes_aggregate_rate", "公网流入带宽", "Byte/s"),
        /**
         * 公网流出带宽
         */
        INTERNET_OUT_RATE("network_outgoing_bytes_aggregate_rate", "network_outgoing_bytes_aggregate_rate", "公网流出带宽", "Byte/s"),
        /**
         * 内网流入带宽
         */
        INTRANET_IN_RATE("network_vm_bandwidth_in", "network_vm_bandwidth_in", "内网流入带宽", "KB/s"),
        /**
         * 内网流出带宽
         */
        INTRANET_OUT_RATE("network_vm_bandwidth_out", "network_vm_bandwidth_out", "内网流出带宽", "KB/s"),
//        /**
//         * 磁盘使用率
//         * 暂无法使用该指标
//         */
//        DISK_USED_UTILIZATION("disk_util_inband", "mountPointPrefix_disk_usedPercent","磁盘使用率", "%"),
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
