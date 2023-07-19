package com.fit2cloud.common.provider.impl.proxmox.client.entity.config;

import lombok.Getter;
import lombok.Setter;

import java.util.regex.Pattern;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2023/7/11  10:33}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
@Getter
@Setter
public class Disk {
    private final static Pattern diskPattern = Pattern.compile("^(scsi|virtio|ide|sata)\\d+$");

    public static boolean current(String key) {
        return diskPattern.matcher(key).find();
    }

    private String key;
    /**
     * 存储位置
     */
    private String storage;


    private String media;

    /**
     * 磁盘名称
     */
    private String name;

    /**
     * 磁盘大小
     */
    private Long size;

    /**
     * 磁盘设备
     */
    private String device;

    /**
     * 单位
     */
    private String unit;

    /**
     * 是否是系统盘
     */
    private boolean boot;

    public Disk(String key, String disk) {
        this.key = key;
        if (key.startsWith("scsi")) {
            this.device = "scsi";
        }
        if (key.startsWith("virtio")) {
            this.device = "virtio";
        }
        if (key.startsWith("ide")) {
            this.device = "ide";
        }
        if (key.startsWith("sata")) {
            this.device = "sata";
        }
        if (disk.contains(",")) {
            String[] split = disk.split(",");
            for (String splitValue : split) {
                if (isSize(splitValue)) {
                    String replace = splitValue.replace("size=", "");
                    if (replace.endsWith("M")) {
                        this.size = Long.parseLong(replace.replace("M", ""));
                        this.unit = "MB";
                    }
                    if (replace.endsWith("G")) {
                        this.size = Long.parseLong(replace.replace("G", ""));
                        this.unit = "GB";
                    }
                }
                if (isVolId(splitValue)) {
                    String[] volIdSplit = splitValue.split(":");
                    this.storage = volIdSplit[0];
                    this.name = volIdSplit[1];
                }
                if (isMedia(splitValue)) {
                    media = splitValue.replace("media=", "");
                }
            }
        }

    }

    private boolean isVolId(String splitValue) {
        return splitValue.contains(":");
    }

    private boolean isSize(String splitValue) {
        return splitValue.contains("size=");
    }

    private boolean isMedia(String splitValue) {
        return splitValue.contains("media=");
    }

    public Disk() {

    }

    public String getVolId() {
        return this.storage + ":" + this.name;
    }

}

