package com.fit2cloud.provider.impl.vsphere.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@Accessors(chain = true)
public class VsphereDatastore {

    private String mor;
    private String name;
    private String description;
    private BigDecimal freeDisk; // unit : G
    private BigDecimal totalDisk; // unit : G
    private String info;

}
