package com.fit2cloud.provider.impl.vsphere.entity.constants;

/**
 * Author: LiuDi
 * Date: 2022/11/10 4:50 PM
 */
public enum VsphereDiskMode {
    independent_persistent("independent_persistent", "独立持久"),

    independent_nonpersistent("independent_nonpersistent", "独立非持久"),

    persistent("persistent", "从属");

    private String id;
    private String name;

    public String getId() {
        return id;
    }

    VsphereDiskMode(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public static String getName(String id) {
        for (VsphereDiskMode vsphereDiskMode : VsphereDiskMode.values()) {
            if (vsphereDiskMode.getId().equals(id)) {
                return vsphereDiskMode.name;
            }
        }
        return id;
    }
}
