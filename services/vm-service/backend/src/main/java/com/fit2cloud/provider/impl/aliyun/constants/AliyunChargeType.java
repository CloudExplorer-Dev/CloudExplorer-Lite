package com.fit2cloud.provider.impl.aliyun.constants;

public enum AliyunChargeType {
    POSTPAID("PostPaid", "按量付费"),
    PREPAID("PrePaid", "包年包月");

    private String id;
    private String name;

    AliyunChargeType(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public static String getName(String id) {
        for (AliyunChargeType chargeType : AliyunChargeType.values()) {
            if (id.equalsIgnoreCase(chargeType.id)) {
                return chargeType.name;
            }
        }
        return id;
    }
}
