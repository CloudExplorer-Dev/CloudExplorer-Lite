package com.fit2cloud.provider.impl.openstack.util;

/**
 * 监控相关常量
 *
 * @author jianneng
 * @date 2022/10/28 12:07
 **/
public class OpenStackPerfMetricConstants {

    public enum CloudServerPerfMetricEnum {
        CPU_USED_UTILIZATION("cpu_util", "CPU使用率", "%", 100L),
        MEMORY_USED_UTILIZATION("memory.usage", "内存使用率", "%", 100L),
        DATASTORE_USED_UTILIZATION("datastore.usage", "存储器使用率", "%", 100L),

        INTERNET_IN_RATE("network.incoming.bytes.rate", "流入带宽", "Byte/s", 1L),
        INTERNET_OUT_RATE("network.outgoing.bytes.rate", "流出带宽", "Byte/s", 1L),

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

        public Long getDivisor() {
            return divisor;
        }

    }
}
