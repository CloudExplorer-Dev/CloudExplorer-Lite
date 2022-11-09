package com.fit2cloud.provider.impl.aliyun.constants;

/**
 * Author: LiuDi
 * Date: 2022/11/3 12:21 PM
 */
public enum AliyunDiskType {
    CLOUD_AUTO("cloud_auto", "ESSD AutoPL云盘"),
    CLOUD_EFFICIENCY("cloud_efficiency", "高效云盘"),
    CLOUD_ESSD("cloud_essd", "ESSD云盘"),
    CLOUD_SSD("cloud_ssd", "SSD云盘"),
    CLOUD("cloud", "普通云盘");

    private String id;
    private String name;

    public String getId() {
        return id;
    }

    AliyunDiskType(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public static String getName(String id) {
        for (AliyunDiskType aliyunDiskType : AliyunDiskType.values()) {
            if (aliyunDiskType.getId().equals(id)) {
                return aliyunDiskType.name;
            }
        }
        return id;
    }
}


