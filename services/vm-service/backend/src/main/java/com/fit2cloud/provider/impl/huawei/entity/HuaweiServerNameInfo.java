package com.fit2cloud.provider.impl.huawei.entity;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author jianneng
 * @date 2022/11/19 13:01
 **/
@Data
@Accessors(chain = true)
public class HuaweiServerNameInfo {

    private String name;
    private String hostName;
}
