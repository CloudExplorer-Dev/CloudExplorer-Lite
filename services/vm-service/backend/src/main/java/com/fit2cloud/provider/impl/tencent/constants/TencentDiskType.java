package com.fit2cloud.provider.impl.tencent.constants;

/**
 * Author: LiuDi
 * Date: 2022/11/3 12:21 PM
 */
public enum TencentDiskType {
    CLOUD_PREMIUM("CLOUD_PREMIUM", "高性能云硬盘"),
    CLOUD_SSD("CLOUD_SSD", "SSD云硬盘"),
    CLOUD_HSSD("CLOUD_HSSD", "增强型SSD云硬盘"),
    CLOUD_BSSD("CLOUD_BSSD", "通用型SSD云硬盘");

    //CLOUD_TSSD("CLOUD_TSSD", "极速型SSD云硬盘"); //速型 SSD 云硬盘仅支持随存储增强型云服务器 S5se 一起购买。其他类型云服务器不支持极速型 SSD 云硬盘。

    private String id;
    private String name;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    TencentDiskType(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public static String getName(String id) {
        for (TencentDiskType aliyunDiskType : TencentDiskType.values()) {
            if (aliyunDiskType.getId().equals(id)) {
                return aliyunDiskType.name;
            }
        }
        return id;
    }
}


