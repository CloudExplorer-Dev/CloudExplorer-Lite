package com.fit2cloud.common.provider.impl.proxmox.client.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2023/7/15  18:17}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
@Getter
@Setter
public class Disk {
    private Long size;

    @JsonProperty("vmid")
    private Integer vmId;

    private String format;

    private Long ctime;

    private String type;

    private String volid;

    private String content;

    public String getStorage() {
        if (volid.contains(":")) {
            String[] split = this.volid.split(":");
            if (split.length == 2) {
                return split[0];
            }

        }
        return null;
    }

    public String getDiskName() {
        if (volid.contains(":")) {
            String[] split = this.volid.split(":");
            if (split.length == 2) {
                return split[1];
            }

        }
        return null;
    }

}
