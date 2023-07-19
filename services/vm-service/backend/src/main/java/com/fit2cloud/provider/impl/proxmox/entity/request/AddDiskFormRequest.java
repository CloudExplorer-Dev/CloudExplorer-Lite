package com.fit2cloud.provider.impl.proxmox.entity.request;

import lombok.Getter;
import lombok.Setter;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2023/7/17  18:12}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
@Getter
@Setter
public class AddDiskFormRequest extends AddDiskForm {

    private String instanceUuid;
}
