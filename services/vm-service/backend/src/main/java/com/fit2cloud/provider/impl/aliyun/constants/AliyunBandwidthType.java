package com.fit2cloud.provider.impl.aliyun.constants;

/**
 * Author: LiuDi
 * Date: 2022/11/16 6:08 PM
 */
public enum AliyunBandwidthType {

    PayByBandwidth("PayByBandwidth", "按固定带宽"),
    PayByTraffic("PayByTraffic", "按使用流量");

    private String id;
    private String name;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    AliyunBandwidthType(String id, String name) {
        this.id = id;
        this.name = name;
    }
}
