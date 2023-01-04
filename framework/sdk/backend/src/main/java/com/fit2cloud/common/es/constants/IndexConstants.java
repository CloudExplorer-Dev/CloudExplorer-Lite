package com.fit2cloud.common.es.constants;

/**
 * @author jianneng
 * @date 2022/12/28 15:43
 **/
public enum IndexConstants {

    CE_PERF_METRIC_MONITOR_DATA("ce-perf-metric-monitor-data", "监控索引");
    /**
     * 描述
     */
    private final String describe;
    /**
     * 索引CODE
     */
    private final String code;

    IndexConstants(String code, String describe) {
        this.code = code;
        this.describe = describe;
    }

    /**
     * 获取错误code
     *
     * @return 错误code
     */
    public String getCode() {
        return code;
    }
}
