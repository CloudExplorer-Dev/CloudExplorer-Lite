package com.fit2cloud.provider.impl.proxmox.entity.constants;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2023/7/17  20:24}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
public enum DiskType {
    raw("原始磁盘映像 (raw)"),
    qcow2("QEMU映像格式 (qcow2)"),
    vmdk("VMware映像格式 (vmdk)");
    private final String message;

    DiskType(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
