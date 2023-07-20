package com.fit2cloud.common.provider.impl.proxmox.client.entity.config;

import com.fit2cloud.common.provider.impl.proxmox.client.entity.Qemu;
import lombok.Getter;
import lombok.Setter;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2023/7/16  00:19}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
@Getter
@Setter
public class DiskStatusInfo {
    private String volId;

    private boolean boot;

    private boolean used;

    private Qemu qemu;

    private String device;

    private long ctime;

    public DiskStatusInfo(String volId, boolean boot, boolean used, String device, Qemu qemu, long ctime) {
        this.volId = volId;
        this.boot = boot;
        this.used = used;
        this.qemu = qemu;
        this.device = device;
        this.ctime = ctime;
    }
}
