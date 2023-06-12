package com.fit2cloud.controller.response.view;

import com.fit2cloud.controller.response.rule_group.ComplianceRuleGroupResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2023/3/23  15:14}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
@Getter
@Setter
public class ComplianceRuleGroupCountResponse extends ComplianceRuleGroupResponse {

    @Schema(title = "低风险数量", description = "低风险数量")
    private Long low;

    @Schema(title = "中风险数量", description = "中风险数量")
    private Long middle;

    @Schema(title = "高风险数量", description = "高风险数量")
    private Long high;

    @Schema(title = "总数", description = "总数")
    private Long total;

    @Schema(title = "资源类型", description = "资源类型")
    private List<String> resourceType;

    @Schema(title = "云平台", description = "云平台")
    private List<String> platform;

}
