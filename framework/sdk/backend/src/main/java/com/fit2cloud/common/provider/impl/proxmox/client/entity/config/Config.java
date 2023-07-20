package com.fit2cloud.common.provider.impl.proxmox.client.entity.config;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2023/7/11  10:33}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
@Getter
@Setter
public class Config {

    /**
     * 磁盘数据
     */
    private List<Disk> disks;

    /**
     * 未使用磁盘信息
     */
    private List<UnUsed> unuseds;

    private ConfigBoot boot;

    /**
     * 描述信息
     */
    private Description description;

    private String osType;

    /**
     * 元数据
     */
    private MetaConfig meta;

    public Config(Map<String, Object> config) {
        disks = new ArrayList<>();
        unuseds = new ArrayList<>();
        for (String key : config.keySet()) {
            if (ConfigBoot.current(key) && Objects.isNull(boot)) {
                ConfigBoot configBoot = new ConfigBoot(key, config.get(key).toString());
                if (StringUtils.isNotEmpty(configBoot.getOrder())) {
                    this.boot = configBoot;
                }
            }

            if (Disk.current(key)) {
                disks.add(new Disk(key, config.get(key).toString()));
            }

            if (Description.current(key)) {
                description = new Description(key, config.get(key).toString());
            }
            if (UnUsed.current(key)) {
                unuseds.add(new UnUsed(key, config.get(key).toString()));
            }
            if (StringUtils.equals(key, "ostype")) {
                this.osType = config.get(key).toString();
            }
            if (MetaConfig.current(key)) {
                this.meta = new MetaConfig(key, config.get(key).toString());
            }
        }
        if (Objects.nonNull(boot)) {
            String order = boot.getOrder();
            disks.stream().filter(disk -> StringUtils.equals(order, disk.getKey()))
                    .forEach(disk -> disk.setBoot(true));
        }
    }
}
