package com.fit2cloud.provider.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author:张少虎
 * @Date: 2022/9/20  10:39 AM
 * @Version 1.0
 * @注释:
 */
@Data
@NoArgsConstructor
public class F2CImage {
    /**
     * 镜像id
     */
    private String id;
    /**
     * 镜像名称
     */
    private String name;
    /**
     * 镜像描述
     */
    private String description;
    /**
     * 镜像系统
     */
    private String os;
    /**
     * 镜像区域
     */
    private String region;
    /**
     * 镜像区域名称
     */
    private String regionName;
    /**
     * 创建时间
     */
    private Long created;
    /**
     * 磁盘大小
     */
    private Long diskSize;

    public F2CImage(String id, String name, String description, String os, String region, Long created) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.os = os;
        this.region = region;
        this.regionName = region;
        this.created = created;
    }

}
