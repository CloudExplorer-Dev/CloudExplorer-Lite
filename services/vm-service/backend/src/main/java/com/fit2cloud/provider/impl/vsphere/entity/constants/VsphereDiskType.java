package com.fit2cloud.provider.impl.vsphere.entity.constants;

/**
 * Author: LiuDi
 * Date: 2022/11/10 4:50 PM
 */
public enum VsphereDiskType {
    THIN("THIN", "精简置备"),

    THICK_EAGER_ZEROED("THICK_EAGER_ZEROED", "厚置备置零"),

    THICK_LAZY_ZEROED("THICK_LAZY_ZEROED", "厚置备延迟置零"),

    SPARSE("SPARSE", "稀疏型"),// 用于同步磁盘时展示类型，申请时不可设置

    NA("NA", "未知");// 用于同步磁盘时展示类型，申请时不可设置

    private String id;
    private String name;

    public String getId() {
        return id;
    }

    VsphereDiskType(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public static String getName(String id) {
        for (VsphereDiskType vsphereDiskType : VsphereDiskType.values()) {
            if (vsphereDiskType.getId().equals(id)) {
                return vsphereDiskType.name;
            }
        }
        return id;
    }
}
