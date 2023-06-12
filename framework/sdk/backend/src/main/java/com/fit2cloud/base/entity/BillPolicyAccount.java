package com.fit2cloud.base.entity;

import lombok.Getter;
import lombok.Setter;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2023/5/31  10:59}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
@Getter
@Setter
public class BillPolicyAccount extends BillPolicy {
    /**
     * 云账号Id
     */
    private String cloudAccountId;
    /**
     * 云账号名称
     */
    private String cloudAccountName;
    /**
     * 云平台
     */
    private String platform;

}
