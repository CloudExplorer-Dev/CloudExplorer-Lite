package com.fit2cloud.common.provider.impl.proxmox.client.entity.config;

import lombok.Getter;
import lombok.Setter;

import java.util.regex.Pattern;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2023/7/15  23:35}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
@Getter
@Setter
public class UnUsed {
    private final static Pattern diskPattern = Pattern.compile("^unused\\d+$");

    public static boolean current(String key) {
        return diskPattern.matcher(key).find();
    }

    private String key;

    private String value;

    public UnUsed(String key, String value) {
        this.key = key;
        this.value = value;

    }

    public UnUsed() {

    }
}
