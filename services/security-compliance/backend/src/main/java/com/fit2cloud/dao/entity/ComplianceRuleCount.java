package com.fit2cloud.dao.entity;

import com.fit2cloud.dao.constants.RiskLevel;
import lombok.Data;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2023/3/22  14:52}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
@Data
public class ComplianceRuleCount {

    /**
     * 风险等级
     */
    private RiskLevel riskLevel;
    /**
     * 合规数量
     */
    private Long complianceCount;

    /**
     * 不合规数量
     */
    private Long notComplianceCount;
    /**
     * 总数
     */
    private Long total;
}
