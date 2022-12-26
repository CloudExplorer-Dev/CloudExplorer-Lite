package com.fit2cloud.provider.impl.vsphere.entity.request;

import lombok.Data;

/**
 * @author : LiuDi
 * @date : 2022/12/20 11:12
 */
@Data
public class VsphereUpdateConfigRequest extends VsphereVmBaseRequest {
    private String instanceUuid;
    private String annotation;
    private int cpu;
    private int memory;
}
