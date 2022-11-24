package com.fit2cloud.provider.impl.aliyun.constants;

import lombok.Data;

/**
 * Author: LiuDi
 * Date: 2022/11/16 6:08 PM
 */
@Data
public class AliyunInstanceType {

    /**
     * 实例规格族
     */
    private String instanceTypeFamily;

    /**
     * 实例规格（例如：s1.small.1）
     */
    private String instanceType;

    /**
     * cpu和内存 （例如：1vCPU 1G）
     */
    private String cpuMemory;

}
