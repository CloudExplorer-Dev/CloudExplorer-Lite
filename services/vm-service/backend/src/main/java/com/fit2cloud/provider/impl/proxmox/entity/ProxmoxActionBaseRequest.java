package com.fit2cloud.provider.impl.proxmox.entity;

import com.fit2cloud.provider.impl.proxmox.entity.request.ProxmoxBaseRequest;
import lombok.Getter;
import lombok.Setter;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2023/7/16  16:46}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
@Getter
@Setter
public class ProxmoxActionBaseRequest extends ProxmoxBaseRequest {
    private String uuid;

    public void setInstanceUuid(String uuid) {
        this.uuid = uuid;
    }
}
