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


    /**
     * 云主机性能指标枚举
     */
    public enum CloudServerPerfMetricEnum {
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

        public Function<JSONObject, F2CBasePerfMetricMonitorData> getF2CBasePerfMetricMonitorData() {
            return f2CBasePerfMetricMonitorData;
        }

        public String getDescription() {
            return description;
        }

        public String getUnit() {
            return unit;
        }

    }
}
