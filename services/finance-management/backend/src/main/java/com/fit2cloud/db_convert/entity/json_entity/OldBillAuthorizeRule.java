package com.fit2cloud.db_convert.entity.json_entity;

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
public class OldBillAuthorizeRule {
    /**
     * 账单授权条件关系 并且 或者
     */
    private BillAuthorizeConditionTypeConstants conditionType;

    /**
     * 规则组
     */
    private List<OldBillAuthorizeRuleGroup> billAuthorizeRuleSettingGroups;

    @Data
    public static class BillAuthorizeRuleCondition {
        /**
         * 条件key
         */
        private String field;
        /**
         * 比较器
         */
        private String compare;
        /**
         * 条件值
         */
        private List<String> value;
    }

    @Data
    public static class OldBillAuthorizeRuleGroup {
        /**
         * 账单授权条件
         */
        private List<BillAuthorizeRuleCondition> billAuthorizeRules;

        /**
         * 账单授权条件关系 并且 或者
         */
        private BillAuthorizeConditionTypeConstants conditionType;
    }

}
