package com.fit2cloud.common.provider.impl.proxmox.client.entity.config;

import jodd.util.StringUtil;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2023/7/11  11:53}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
@Getter
@Setter
public class ConfigBoot {

    public static boolean current(String key) {
        return List.of("boot", "bootdisk").contains(key);
    }

    private String key;
    private String order;

    public ConfigBoot(String key, String boot) {
        this.key = key;
        if (boot.contains("=")) {
            String[] split = boot.split("=");
            if (split.length == 2) {
                this.order = split[1];
            }
        }
        if (StringUtil.equals("bootdisk", key)) {
            this.order = boot;
        }
    }

    public ConfigBoot() {

    }
}
