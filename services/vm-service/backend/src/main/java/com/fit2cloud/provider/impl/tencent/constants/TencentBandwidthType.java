package com.fit2cloud.provider.impl.tencent.constants;

/**
 * Author: LiuDi
 * Date: 2022/11/16 6:08 PM
 */
public enum TencentBandwidthType {

    BANDWIDTH_PREPAID("BANDWIDTH_PREPAID", "按固定带宽"),
    BANDWIDTH_POSTPAID_BY_HOUR("BANDWIDTH_POSTPAID_BY_HOUR", "按固定带宽"),
    TRAFFIC("TRAFFIC_POSTPAID_BY_HOUR", "按使用流量");

    private String id;
    private String name;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    TencentBandwidthType(String id, String name) {
        this.id = id;
        this.name = name;
    }
}
