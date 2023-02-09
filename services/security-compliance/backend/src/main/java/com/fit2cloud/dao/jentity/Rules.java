package com.fit2cloud.dao.jentity;

import com.fit2cloud.constants.ConditionTypeConstants;
import com.fit2cloud.constants.ScanRuleConstants;
import lombok.Data;

import java.util.List;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2023/2/9  15:44}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
@Data
public class Rules {
    /**
     * 规则
     */
    private List<Rule> rules;

    /**
     * 条件 并 条件 或
     */
    private ConditionTypeConstants conditionType = ConditionTypeConstants.AND;
    /**
     * 视为合规 还是视为不合规
     */
    private ScanRuleConstants scanRule = ScanRuleConstants.COMPLIANCE;
}
