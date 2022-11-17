package com.fit2cloud.common.provider.impl.openstack.entity;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class Zone {

    private String id;

    private String name;

}
