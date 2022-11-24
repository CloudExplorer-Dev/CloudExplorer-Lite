package com.fit2cloud.dao.jentity;

import com.fit2cloud.constants.BillAuthorizeConditionTypeConstants;
import lombok.Data;

import java.util.List;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2022/11/6  5:33 PM}
 * {@code @Version 1.0}
 * {@code @注释:  账单授权规则组}
 */
@Data
public class BillAuthorizeRuleGroup {
    /**
     * 唯一id
     */
    private String id;
    /**
     * 账单授权条件
     */
    private List<BillAuthorizeRuleCondition> billAuthorizeRules;

    /**
     * 账单授权条件关系 并且 或者
     */
    private BillAuthorizeConditionTypeConstants conditionType;
}
