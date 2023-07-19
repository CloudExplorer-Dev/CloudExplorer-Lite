package com.fit2cloud.common.provider.impl.proxmox.client.entity.config;

import lombok.Getter;
import lombok.Setter;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2023/7/14  16:32}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
@Getter
@Setter
public class Description {
    public static boolean current(String key) {
        return "description".equals(key);
    }

    private String key;
    private String description;

    public Description(String key, String v) {
        this.key = key;
        this.description = v;
    }

    public Description(String v) {
        this.key = "description";
        this.description = v;
    }

    public Description() {

    }
}
