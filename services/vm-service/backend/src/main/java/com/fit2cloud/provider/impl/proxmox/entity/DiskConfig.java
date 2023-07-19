package com.fit2cloud.provider.impl.proxmox.entity;

import lombok.Getter;
import lombok.Setter;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.function.Function;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2023/7/11  14:48}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
@Getter
@Setter
public class DiskConfig {
    /**
     * 磁盘大小
     */
    private int size;
    /**
     * 单位
     */
    private String unit;
    /**
     * 是否是系统盘
     */
    private boolean boot;
    /**
     * 是否是模板自带
     */
    private boolean template;

    public String toString(String dataStore) {
        Function<Integer, Integer> formatSize = (s) -> s;
        if ("MB".equals(unit)) {
            formatSize = (s) -> Math.round(s / 1024.0f);
        }
        return URLEncoder.encode(dataStore + ":" + formatSize.apply(size), StandardCharsets.UTF_8);
    }
}
