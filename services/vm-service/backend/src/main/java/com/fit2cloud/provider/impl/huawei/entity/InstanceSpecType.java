package com.fit2cloud.provider.impl.huawei.entity;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author jianneng
 * @date 2022/11/15 11:50
 **/
@Data
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
     * 金额
     */
    private BigDecimal amount;

    /**
     * 金额显示
     */
    private String amountText;

}
