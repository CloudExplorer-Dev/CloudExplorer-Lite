package com.fit2cloud.provider.impl.vsphere.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class F2CVsphereCluster {
    private String name;
    private String description;
    private String info;
}
