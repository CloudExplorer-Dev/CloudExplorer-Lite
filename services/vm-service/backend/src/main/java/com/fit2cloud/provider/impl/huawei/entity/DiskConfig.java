package com.fit2cloud.provider.impl.huawei.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * @author jianneng
 * @date 2022/11/17 11:22
 **/
@Data
@Accessors(chain = true)
public class DiskConfig {

    private String name;

    private Integer size;

    private String diskType;

    private BigDecimal amount;

    private String amountText;

    private boolean deleteWithInstance;
}
