package com.fit2cloud.provider.impl.huawei.entity;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author jianneng
 * @date 2022/11/15 11:50
 **/
@Data
@Accessors(chain = true)
public class InstanceSpecType {

    /**
     * 规格类型
     */
    private String specType;
    /**
     * 规格名称
     */
    private String specName;
    /**
     * 规格
     */
    private String instanceSpec;
    /**
     * 规格名称+CPU内存
     */
    private String instanceTypeDesc;
    /**
     * cpu
     */
    private String vcpus;
    /**
     * 内存 MB
     */
    private Integer ram;
    /**
     * 磁盘 GB
     */
    private String disk;

}
