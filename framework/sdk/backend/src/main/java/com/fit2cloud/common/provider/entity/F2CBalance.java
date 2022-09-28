package com.fit2cloud.common.provider.entity;

import lombok.Data;

/**
 * Author: LiuDi
 * Date: 2022/9/27 6:36 PM
 */
@Data
public class F2CBalance {

    /**
     * 余额
     */
    Double amount;

    /**
     * 欠款
     */
    Double debtAmount;
    /**
     * 币种
     */
    String currency;

    public F2CBalance() {
    }

    public F2CBalance(Double amount, String currency, Double debtAmount) {
        this.amount = amount;
        this.debtAmount = debtAmount;
        this.currency = currency;
    }
}
