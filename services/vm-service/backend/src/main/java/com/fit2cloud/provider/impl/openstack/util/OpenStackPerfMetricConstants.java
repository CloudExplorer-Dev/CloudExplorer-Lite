package com.fit2cloud.provider.impl.openstack.util;

/**
 * 监控相关常量
 *
 * @author jianneng
 * @date 2022/10/28 12:07
 **/
public class OpenStackPerfMetricConstants {

    public enum CloudServerPerfMetricEnum {
        CPU_USED_UTILIZATION("2", "CPU使用率", "%", 100L),
        MEMORY_USED_UTILIZATION("24", "内存使用率", "%", 100L),
        DATASTORE_USED_UTILIZATION("275", "存储器使用率", "%", 100L),

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
