package com.fit2cloud.provider.impl.huawei.entity;

import lombok.Data;

/**
 * @author jianneng
 * @date 2022/11/18 18:25
 **/
@Data
public class F2CHuaweiSecurityGroups {

    private String uuid;
    private String name;
    private String vpc_id;
    private String description;
}
