package com.fit2cloud.provider.impl.tencent.constants;

public enum TencentChargeType {
    POSTPAID("POSTPAID_BY_HOUR", "按量付费"),
    PREPAID("PREPAID", "包年包月");

    private String id;
    private String name;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public static String getName(String id) {
        for (TencentChargeType chargeType : TencentChargeType.values()) {
            if (id.equalsIgnoreCase(chargeType.id)) {
                return chargeType.name;
            }
        }
        return id;
    }

    TencentChargeType(String id, String name) {
        this.id = id;
        this.name = name;
    }
}
