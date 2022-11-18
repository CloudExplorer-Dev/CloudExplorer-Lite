package com.fit2cloud.provider.impl.huawei.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author jianneng
 * @date 2022/11/15 12:16
 **/
@Data
@Accessors(chain = true)
public class InstanceSpecConfig {
    //表数据
    private List<InstanceSpecType> tableData;

}
