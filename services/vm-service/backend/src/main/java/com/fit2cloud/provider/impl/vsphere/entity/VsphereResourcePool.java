package com.fit2cloud.provider.impl.vsphere.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.math.BigDecimal;


@Data
@Accessors(chain = true)
@NoArgsConstructor
public class VsphereResourcePool {
    private String mor;
    private String name;

    private BigDecimal totalCpu; //GHz
    private BigDecimal totalMemory; //GB

    private BigDecimal usedCpu;
    private BigDecimal usedMemory;


    public VsphereResourcePool(String mor, String name) {
        this.mor = mor;
        this.name = name;
    }
}
