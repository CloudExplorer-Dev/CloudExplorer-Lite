package com.fit2cloud.common.provider.impl.proxmox.client.entity;

import lombok.Getter;
import lombok.Setter;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2023/7/20  16:55}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
@Getter
@Setter
public class InstanceId {

    private String uuid;

    private String vmGenId;
}
