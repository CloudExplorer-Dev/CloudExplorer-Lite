package com.fit2cloud.provider.impl.openstack.entity;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class VolumeType {

    private String id;
    private String name;

}
