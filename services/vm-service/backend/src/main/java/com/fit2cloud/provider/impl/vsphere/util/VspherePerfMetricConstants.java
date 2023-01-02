package com.fit2cloud.provider.impl.vsphere.util;

/**
 * 监控相关常量
 * @author jianneng
 * @date 2022/10/28 12:07
 **/
public class VspherePerfMetricConstants {

    /**
     * 云主机性能指标枚举
     * 目前VM监控只获取CPU内存这些，其他监控后面再弄，api获取数据有问题，暂停
     */
    public enum CloudServerPerfMetricEnum {
        CPU_USED_UTILIZATION("2", "CPU使用率","%",100L),
        MEMORY_USED_UTILIZATION("24", "内存使用率","%",100L),
        /**
         * 虚拟磁盘读取速度
         */
        DISK_READ_BPS("180", "虚拟磁盘读取速度","KBps",1L),
        /**
         * 虚拟磁盘写入速度
         */
        DISK_WRITE_BPS("181", "虚拟磁盘写入速度","KBps",1L),
        /**
         * 平均每秒读取请求数
         */
        DISK_READ_IOPS("178", "平均每秒读取请求数","Count/Second",1L),
        /**
         * 平均每秒写入请求数
         */
        DISK_WRITE_IOPS("179", "平均每秒写入请求数","Count/Second",1L),
        /**
         * 这个指标是查询存储器实际使用量
         * disk.used.latest 这个指标返回的是kb
         * 一个小时只会返回两个时间点，25分钟、55分钟两个
         */
        DATASTORE_USED_UTILIZATION("275", "存储器使用率","%",1L),


//        DISK_READ_BPS("180", "虚拟磁盘读取速度","KBps",1L),
//        DISK_WRITE_BPS("181", "虚拟磁盘写入速度","KBps",1L),
//        DISK_READ_IOPS("143", "所有磁盘每秒读取次数","Count/Second",1L),
//        DISK_WRITE_IOPS("144", "所有磁盘每秒写入次数","Count/Second",1L),
//        INTERNET_IN_RATE("VPC_PublicIP_InternetInRate", "公网流入带宽","bit/s",1L),
//        INTERNET_OUT_RATE("VPC_PublicIP_InternetOutRate", "公网流出带宽","bit/s",1L),
//        INTRANET_IN_RATE("IntranetInRate", "内网流入带宽","bit/s",1L),
//        INTRANET_OUT_RATE("IntranetOutRate", "内网流出带宽","bit/s",1L),
//        DISK_USED_UTILIZATION("diskusage_utilization","磁盘使用率","%",1L),
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

        /**
         * 单位要换算的除数
         */
        private final Long divisor;

        CloudServerPerfMetricEnum(String metricName, String description, String unit, Long divisor) {
            this.metricName = metricName;
            this.description = description;
            this.unit = unit;
            this.divisor = divisor;
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

        public Long getDivisor() { return divisor; }

    }
}
