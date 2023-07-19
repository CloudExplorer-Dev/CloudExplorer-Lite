package com.fit2cloud.provider.impl.proxmox.entity.request;

import lombok.Getter;
import lombok.Setter;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2023/7/16  22:49}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
@Getter
@Setter
public class ProxmoxUpdateConfigRequest extends ProxmoxBaseRequest {
    private String instanceUuid;
    private String annotation;
    private int cpu;
    private int mem;
    private int cpuSlot;
}
