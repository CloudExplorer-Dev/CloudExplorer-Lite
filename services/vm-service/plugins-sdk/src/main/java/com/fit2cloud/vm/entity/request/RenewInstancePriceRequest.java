package com.fit2cloud.vm.entity.request;

import lombok.Getter;
import lombok.Setter;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2023/8/11  11:30}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
@Getter
@Setter
public class RenewInstancePriceRequest extends RenewInstanceRequest {
    /**
     * 云账号id
     */
    private String cloudAccountId;
    /**
     * cpu
     */
    public int cpu;

    /**
     * 内存
     */
    private int mem;

}
