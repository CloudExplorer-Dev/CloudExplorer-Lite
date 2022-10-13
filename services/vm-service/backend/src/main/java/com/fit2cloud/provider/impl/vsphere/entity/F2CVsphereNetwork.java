package com.fit2cloud.provider.impl.vsphere.entity;

import lombok.Data;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Data
public class F2CVsphereNetwork {

    private String region;

    private String name;

    private String id;

    private String description;

}
