package com.fit2cloud.provider.impl.tencent.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

/**
 * @author : LiuDi
 * @date : 2022/11/25 17:23
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TencentInstanceType {
    /**
     * 实例规格族
     */
    private String instanceTypeFamily;

    /**
     * 实例规格族中文名称
     */
    private String instanceTypeFamilyName;

    /**
     * 实例规格（例如：s1.small.1）
     */
    private String instanceType;
    /**
     * 实例规格和 Cpu 内存
     */
    private String instanceTypeDesc;

    /**
     * cpu 和内存 （例如：1vCPU 1G）
     */
    private String cpuMemory;

    private Long cpu;

    private Long memory;

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        TencentInstanceType tencentInstanceType = (TencentInstanceType) o;
        return Objects.equals(instanceType, tencentInstanceType.instanceType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(instanceType);
    }

}
