package com.fit2cloud.provider.impl.proxmox.constants;

import com.fit2cloud.common.provider.entity.F2CBasePerfMetricMonitorData;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.function.Function;

/**
 * @author jianneng
 * @date 2022/11/4 11:47
 **/
public class ProxmoxPerfMetricConstants {
    public interface PerfMetricConstantsParent {
        Function<JSONObject, F2CBasePerfMetricMonitorData> getF2CBasePerfMetricMonitorData();

        String getDescription();

        String getUnit();

        String name();
    }


    /**
     * 云主机性能指标枚举
     */
    public enum CloudServerPerfMetricEnum implements PerfMetricConstantsParent {
        CPU_USED_UTILIZATION((data) -> {
            BigDecimal bigDecimal = data.isNull("cpu") ? null : BigDecimal.valueOf(data.getDouble("cpu"))
                    .multiply(new BigDecimal(100))
                    .setScale(2, RoundingMode.CEILING);
            return new F2CBasePerfMetricMonitorData(bigDecimal, bigDecimal, bigDecimal);
        }, "CPU使用率", "%"),
        MEMORY_USED_UTILIZATION(data -> {
            if (!data.isNull("maxmem") && !data.isNull("mem")) {
                BigDecimal divide = BigDecimal.valueOf(data.getDouble("mem"))
                        .divide(BigDecimal.valueOf(data.getDouble("maxmem")), MathContext.DECIMAL64)
                        .multiply(new BigDecimal(100)).setScale(2, RoundingMode.CEILING);
                return new F2CBasePerfMetricMonitorData(divide, divide, divide);
            }
            return new F2CBasePerfMetricMonitorData();
        }, "内存使用率", "%"),
        INTRANET_IN_RATE(data -> {
            BigDecimal bigDecimal = data.isNull("netin") ? null : BigDecimal.valueOf(data.getDouble("netin")).divide(new BigDecimal(1024 * 1024), MathContext.DECIMAL64).setScale(2, RoundingMode.CEILING);
            return new F2CBasePerfMetricMonitorData(bigDecimal, bigDecimal, bigDecimal);
        }, "内网流入带宽", "Mbps"),
        INTRANET_OUT_RATE(data -> {
            BigDecimal bigDecimal = data.isNull("netout") ? null : BigDecimal.valueOf(data.getDouble("netout")).divide(new BigDecimal(1024 * 1024), MathContext.DECIMAL64).setScale(2, RoundingMode.CEILING);
            return new F2CBasePerfMetricMonitorData(bigDecimal, bigDecimal, bigDecimal);
        }, "内网流出带宽", "Mbps"),
        DISK_READ_BPS(data -> {
            BigDecimal bigDecimal = data.isNull("diskread") ? null : BigDecimal.valueOf(data.getDouble("diskread")).divide(new BigDecimal(1024 * 1024), MathContext.DECIMAL64).setScale(2, RoundingMode.CEILING);
            return new F2CBasePerfMetricMonitorData(bigDecimal, bigDecimal, bigDecimal);
        }, "所有磁盘读取BPS", "MB/s"),
        DISK_WRITE_BPS(data -> {
            BigDecimal bigDecimal = data.isNull("diskwrite") ? null : BigDecimal.valueOf(data.getDouble("diskwrite")).divide(new BigDecimal(1024 * 1024), MathContext.DECIMAL64).setScale(2, RoundingMode.CEILING);
            return new F2CBasePerfMetricMonitorData(bigDecimal, bigDecimal, bigDecimal);
        }, "所有磁盘写入BPS", "MB/s");
        /**
         * 名称
         */
        private final Function<JSONObject, F2CBasePerfMetricMonitorData> f2CBasePerfMetricMonitorData;

        /**
         * 描述
         */
        private final String description;
        /**
         * 单位
         */
        private final String unit;

        CloudServerPerfMetricEnum(Function<JSONObject, F2CBasePerfMetricMonitorData> f2CBasePerfMetricMonitorData, String description, String unit) {
            this.f2CBasePerfMetricMonitorData = f2CBasePerfMetricMonitorData;
            this.description = description;
            this.unit = unit;
        }

        @Override
        public Function<JSONObject, F2CBasePerfMetricMonitorData> getF2CBasePerfMetricMonitorData() {
            return f2CBasePerfMetricMonitorData;
        }

        @Override
        public String getDescription() {
            return description;
        }

        @Override
        public String getUnit() {
            return unit;
        }
    }

    /**
     * 云主机性能指标枚举
     */
    public enum CloudHostPerfMetricEnum implements PerfMetricConstantsParent {
        CPU_USED_UTILIZATION((data) -> {
            BigDecimal bigDecimal = data.isNull("cpu") ? null : BigDecimal.valueOf(data.getDouble("cpu"))
                    .multiply(new BigDecimal(100))
                    .setScale(2, RoundingMode.CEILING);
            return new F2CBasePerfMetricMonitorData(bigDecimal, bigDecimal, bigDecimal);
        }, "CPU使用率", "%"),
        MEMORY_USED_UTILIZATION(data -> {
            if (!data.isNull("memtotal") && !data.isNull("memused")) {
                BigDecimal divide = BigDecimal.valueOf(data.getDouble("memused"))
                        .divide(BigDecimal.valueOf(data.getDouble("memtotal")), MathContext.DECIMAL64)
                        .multiply(new BigDecimal(100)).setScale(2, RoundingMode.CEILING);
                return new F2CBasePerfMetricMonitorData(divide, divide, divide);
            }
            return new F2CBasePerfMetricMonitorData();
        }, "内存使用率", "%"),
        INTRANET_IN_RATE(data -> {
            BigDecimal bigDecimal = data.isNull("netin") ? null : BigDecimal.valueOf(data.getDouble("netin")).divide(new BigDecimal(1024 * 1024), MathContext.DECIMAL64).setScale(2, RoundingMode.CEILING);
            return new F2CBasePerfMetricMonitorData(bigDecimal, bigDecimal, bigDecimal);
        }, "内网流入带宽", "Mbps"),
        INTRANET_OUT_RATE(data -> {
            BigDecimal bigDecimal = data.isNull("netout") ? null : BigDecimal.valueOf(data.getDouble("netout")).divide(new BigDecimal(1024 * 1024), MathContext.DECIMAL64).setScale(2, RoundingMode.CEILING);
            return new F2CBasePerfMetricMonitorData(bigDecimal, bigDecimal, bigDecimal);
        }, "内网流出带宽", "Mbps"),
        DISK_READ_BPS(data -> {
            BigDecimal bigDecimal = data.isNull("diskread") ? null : BigDecimal.valueOf(data.getDouble("diskread")).divide(new BigDecimal(1024 * 1024), MathContext.DECIMAL64).setScale(2, RoundingMode.CEILING);
            return new F2CBasePerfMetricMonitorData(bigDecimal, bigDecimal, bigDecimal);
        }, "所有磁盘读取BPS", "MB/s"),
        DISK_WRITE_BPS(data -> {
            BigDecimal bigDecimal = data.isNull("diskwrite") ? null : BigDecimal.valueOf(data.getDouble("diskwrite")).divide(new BigDecimal(1024 * 1024), MathContext.DECIMAL64).setScale(2, RoundingMode.CEILING);
            return new F2CBasePerfMetricMonitorData(bigDecimal, bigDecimal, bigDecimal);
        }, "所有磁盘写入BPS", "MB/s");
        /**
         * 名称
         */
        private final Function<JSONObject, F2CBasePerfMetricMonitorData> f2CBasePerfMetricMonitorData;

        /**
         * 描述
         */
        private final String description;
        /**
         * 单位
         */
        private final String unit;

        CloudHostPerfMetricEnum(Function<JSONObject, F2CBasePerfMetricMonitorData> f2CBasePerfMetricMonitorData, String description, String unit) {
            this.f2CBasePerfMetricMonitorData = f2CBasePerfMetricMonitorData;
            this.description = description;
            this.unit = unit;
        }

        @Override
        public Function<JSONObject, F2CBasePerfMetricMonitorData> getF2CBasePerfMetricMonitorData() {
            return f2CBasePerfMetricMonitorData;
        }

        @Override
        public String getDescription() {
            return description;
        }

        @Override
        public String getUnit() {
            return unit;
        }
    }

    public enum CloudDatastorePerfMetricEnum implements PerfMetricConstantsParent {
        DATASTORE_USED_UTILIZATION((data) -> {
            if (!data.isNull("total") && !data.isNull("used")) {
                BigDecimal divide = BigDecimal.valueOf(data.getDouble("used"))
                        .divide(BigDecimal.valueOf(data.getDouble("total")), MathContext.DECIMAL64)
                        .multiply(new BigDecimal(100)).setScale(2, RoundingMode.CEILING);
                return new F2CBasePerfMetricMonitorData(divide, divide, divide);
            }
            return new F2CBasePerfMetricMonitorData();
        }, "存储器使用率", "%");
        /**
         * 名称
         */
        private final Function<JSONObject, F2CBasePerfMetricMonitorData> f2CBasePerfMetricMonitorData;

        /**
         * 描述
         */
        private final String description;
        /**
         * 单位
         */
        private final String unit;

        CloudDatastorePerfMetricEnum(Function<JSONObject, F2CBasePerfMetricMonitorData> f2CBasePerfMetricMonitorData, String description, String unit) {
            this.f2CBasePerfMetricMonitorData = f2CBasePerfMetricMonitorData;
            this.description = description;
            this.unit = unit;
        }

        @Override
        public Function<JSONObject, F2CBasePerfMetricMonitorData> getF2CBasePerfMetricMonitorData() {
            return f2CBasePerfMetricMonitorData;
        }

        @Override
        public String getDescription() {
            return description;
        }

        @Override
        public String getUnit() {
            return unit;
        }
    }

}
