package com.fit2cloud.common.provider.impl.proxmox.client.entity.config;

import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2023/7/20  16:15}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
@Getter
public class MetaConfig {
    public static final Pattern ctimePattern = Pattern.compile("\\d+");
    //meta -> creation-qemu=7.2.0,ctime=1689326214
    private String key;

    private long ctime;

    private String creationQemu;

    public static boolean current(String key) {
        return StringUtils.equals("meta", key);
    }

    public MetaConfig(String key, String value) {
        this.key = key;
        if (StringUtils.isNotEmpty(value) && value.contains(",")) {
            String[] split = value.split(",");
            for (String s : split) {
                if (s.contains("ctime")) {
                    Matcher matcher = ctimePattern.matcher(s);
                    if (matcher.find()) {
                        this.ctime = Long.parseLong(matcher.group()) * 1000;
                    }
                }
            }
        }
    }


    public MetaConfig() {

    }

}
