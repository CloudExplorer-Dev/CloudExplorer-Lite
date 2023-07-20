package com.fit2cloud.provider.impl.proxmox.entity.request;

import com.fit2cloud.common.provider.impl.proxmox.client.entity.InstanceId;
import com.fit2cloud.common.utils.JsonUtil;
import lombok.Getter;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2023/7/16  22:49}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
@Getter
public class ProxmoxUpdateConfigRequest extends ProxmoxBaseRequest {
    private InstanceId instanceUuid;
    private String annotation;
    private int cpu;
    private int mem;
    private int cpuSlot;

    public void setInstanceUuid(String instanceUuid) {
        this.instanceUuid = JsonUtil.parseObject(instanceUuid, InstanceId.class);
    }

    public void setAnnotation(String annotation) {
        this.annotation = annotation;
    }


    public void setMem(int mem) {
        this.mem = mem;
    }

    public void setCpu(int cpu) {
        this.cpu = cpu;
    }

    public void setCpuSlot(int cpuSlot) {
        this.cpuSlot = cpuSlot;
    }
}
