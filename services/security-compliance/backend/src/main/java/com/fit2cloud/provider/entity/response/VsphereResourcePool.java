package com.fit2cloud.provider.entity.response;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.math.BigDecimal;


@Data
@Accessors(chain = true)
@NoArgsConstructor
public class VsphereResourcePool {
    /**
     * mor
     */
    private String mor;
    /**
     * 名称
     */
    private String name;
    /**
     * CPU总量(单位: GHz)
     */
    private BigDecimal totalCpu;
    /**
     * 内存总量(单位: GB)
     */
    private BigDecimal totalMemory;
    /**
     * 已使用CPU
     */
    private BigDecimal usedCpu;
    /**
     * 已使用内存
     */
    private BigDecimal usedMemory;


    public VsphereResourcePool(String mor, String name) {
        this.mor = mor;
        this.name = name;
    }
}
