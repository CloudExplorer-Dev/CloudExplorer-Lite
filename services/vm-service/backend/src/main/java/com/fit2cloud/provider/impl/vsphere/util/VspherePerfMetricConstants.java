package com.fit2cloud.provider.impl.vsphere.util;

/**
 * 监控相关常量
 * vSphere 7.x 8.x 与 6.x 的指标有一些区别，直接分多个枚举
 * TODO 目前发现的磁盘读写速度不一样，其他的后面再说
 *
 * @author jianneng
 * @date 2022/10/28 12:07
 **/
public interface VspherePerfMetricConstants {


    /**
     * 性能指标枚举池
     * 默认为7.x 8.x的指标
     * 如果有特殊的版本指标，需要特殊指定
     */
    enum PerfMetricEnum {
        /**
         * CPU使用率
         */
        CPU_USED_UTILIZATION("2", "CPU使用率", "%", 100L),
        /**
         * 内存使用率
         */
        MEMORY_USED_UTILIZATION("24", "内存使用率", "%", 100L),
        /**
         * 虚拟磁盘读取速度
         */
        DISK_READ_BPS("180", "虚拟磁盘读取速度", "KBps", 1L),
        /**
         * 虚拟磁盘写入速度
         */
        DISK_WRITE_BPS("181", "虚拟磁盘写入速度", "KBps", 1L),
        /**
         * 平均每秒读取请求数
         */
        DISK_READ_IOPS("178", "平均每秒读取请求数", "Count/Second", 1L),
        /**
         * 平均每秒写入请求数
         */
        DISK_WRITE_IOPS("179", "平均每秒写入请求数", "Count/Second", 1L),

        /**
         * 平均每秒接收的数据量
         */
        INTRANET_IN_RATE("494", "内网流入带宽", "KBps", 1L),
        /**
         * 平均每秒传输的数据量
         */
        INTRANET_OUT_RATE("495", "内网流出带宽", "KBps", 1L),

        /**
         * 存储器没有使用率监控指标
         * 这个指标是查询存储器实际使用量，然后跟总容量计算
         * disk.used.latest 这个指标返回的是kb
         * 一个小时只会返回两个时间点，25分钟、55分钟两个
         */
        DATASTORE_USED_UTILIZATION("275", "存储器使用率", "%", 1L),

        /**
         * 虚拟磁盘读取速度 针对vSphere6.x的
         */
        DISK_READ_BPS_FOR_SIX("173", "虚拟磁盘读取速度", "KBps", 1L),
        /**
         * 虚拟磁盘写入速度 针对vSphere6.x的
         */
        DISK_WRITE_BPS_FOR_SIX("174", "虚拟磁盘写入速度", "KBps", 1L),
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

        PerfMetricEnum(String metricName, String description, String unit, Long divisor) {
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

        public Long getDivisor() {
            return divisor;
        }
    }

    /**
     * vSphere 版本7.x 8.x 的性能指标枚举
     */
    enum PerfMetricForSevenAndEightEnum {
        /**
         * CPU使用率
         */
        CPU_USED_UTILIZATION(PerfMetricEnum.CPU_USED_UTILIZATION),
        MEMORY_USED_UTILIZATION(PerfMetricEnum.MEMORY_USED_UTILIZATION),
        DISK_READ_BPS(PerfMetricEnum.DISK_READ_BPS),
        DISK_WRITE_BPS(PerfMetricEnum.DISK_WRITE_BPS),
        DISK_READ_IOPS(PerfMetricEnum.DISK_READ_IOPS),
        DISK_WRITE_IOPS(PerfMetricEnum.DISK_WRITE_IOPS),
        INTRANET_IN_RATE(PerfMetricEnum.INTRANET_IN_RATE),
        INTRANET_OUT_RATE(PerfMetricEnum.INTRANET_OUT_RATE),
        DATASTORE_USED_UTILIZATION(PerfMetricEnum.DATASTORE_USED_UTILIZATION);

        private final PerfMetricEnum metric;

        PerfMetricForSevenAndEightEnum(PerfMetricEnum metric) {
            this.metric = metric;
        }

        public String getMetricName() {
            return metric.getMetricName();
        }

        public String getDescription() {
            return metric.getDescription();
        }

        public String getUnit() {
            return metric.getUnit();
        }

        public Long getDivisor() {
            return metric.getDivisor();
        }

    }

    /**
     * vSphere 版本6.x 的性能指标枚举
     */
    enum PerfMetricForSixEnum {
        /**
         * CPU使用率
         */
        CPU_USED_UTILIZATION(PerfMetricEnum.CPU_USED_UTILIZATION),
        /**
         * 内存使用率
         */
        MEMORY_USED_UTILIZATION(PerfMetricEnum.MEMORY_USED_UTILIZATION),
        /**
         * 虚拟磁盘读取速度
         */
        DISK_READ_BPS(PerfMetricEnum.DISK_READ_BPS_FOR_SIX),
        /**
         * 虚拟磁盘写入速度
         */
        DISK_WRITE_BPS(PerfMetricEnum.DISK_WRITE_BPS_FOR_SIX),
        /**
         * 平均每秒读取请求数
         */
        DISK_READ_IOPS(PerfMetricEnum.DISK_READ_IOPS),
        /**
         * 平均每秒写入请求数
         */
        DISK_WRITE_IOPS(PerfMetricEnum.DISK_WRITE_IOPS),
        /**
         * 平均每秒接收的数据量
         */
        INTRANET_IN_RATE(PerfMetricEnum.INTRANET_IN_RATE),
        /**
         * 平均每秒传输的数据量
         */
        INTRANET_OUT_RATE(PerfMetricEnum.INTRANET_OUT_RATE),
        /**
         * 这个指标是查询存储器实际使用量
         * disk.used.latest 这个指标返回的是kb
         * 一个小时只会返回两个时间点，25分钟、55分钟两个
         */
        DATASTORE_USED_UTILIZATION(PerfMetricEnum.DATASTORE_USED_UTILIZATION),

        ;

        private final PerfMetricEnum metric;

        PerfMetricForSixEnum(PerfMetricEnum metric) {
            this.metric = metric;
        }

        public String getMetricName() {
            return metric.getMetricName();
        }

        public String getDescription() {
            return metric.getDescription();
        }

        public String getUnit() {
            return metric.getUnit();
        }

        public Long getDivisor() {
            return metric.getDivisor();
        }

    }


}
