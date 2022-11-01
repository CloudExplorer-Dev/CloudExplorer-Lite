package com.fit2cloud.provider.impl.vsphere.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;


@Data
@Accessors(chain = true)
@NoArgsConstructor
public class VsphereHost {
    private String mor;
    private String name;

    private long totalCpu;
    private long totalMemory;

    private long usedCpu;
    private long usedMemory;

    public VsphereHost(String mor, String name) {
        this.mor = mor;
        this.name = name;
    }
}
