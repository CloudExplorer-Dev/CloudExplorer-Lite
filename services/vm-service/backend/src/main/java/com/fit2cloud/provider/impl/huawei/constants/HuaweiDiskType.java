package com.fit2cloud.provider.impl.huawei.constants;

/**
 * Author: LiuDi
 * Date: 2022/11/3 12:21 PM
 */
public enum HuaweiDiskType {
    SAS("SAS", "高IO"),
    SSD("SSD", "超高IO"),
    GPSSD("GPSSD", "通用型SSD"),
    ESSD("ESSD", "极速型SSD");

    private String id;
    private String name;

    public String getId() {
        return id;
    }

    HuaweiDiskType(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public static String getName(String id) {
        for (HuaweiDiskType aliyunDiskType : HuaweiDiskType.values()) {
            if (aliyunDiskType.getId().equals(id)) {
                return aliyunDiskType.name;
            }
        }
        return id;
    }
}


