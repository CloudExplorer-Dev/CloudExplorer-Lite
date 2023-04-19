package com.fit2cloud.dao.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2023/3/23  15:12}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
@Getter
@Setter
public class ComplianceRuleGroupCount extends ComplianceRuleGroup {
    /**
     * 低风险数量
     */
    private Long low;

    /**
     * 中风险数量
     */
    private Long middle;
    /**
     * 高风险数量
     */
    private Long high;
    /**
     * 总数
     */
    private Long total;
    /**
     * 资源类型
     */
    private List<String> resourceType;
    /**
     * 云平台
     */
    private List<String> platform;
}
