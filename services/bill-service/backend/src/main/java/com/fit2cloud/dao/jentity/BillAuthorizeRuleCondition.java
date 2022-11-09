package com.fit2cloud.dao.jentity;

import lombok.Data;

import java.util.List;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2022/11/6  5:33 PM}
 * {@code @Version 1.0}
 * {@code @注释: 账单授权规则}
 */
@Data
public class BillAuthorizeRuleCondition {
    private String id;
    /**
     * 条件key
     */
    private String field;
    /**
     * 条件值
     */
    private List<String> value;
}
