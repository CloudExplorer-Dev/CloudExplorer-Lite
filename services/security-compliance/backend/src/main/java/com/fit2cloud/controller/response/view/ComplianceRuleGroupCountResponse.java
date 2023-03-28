package com.fit2cloud.controller.response.view;

import com.fit2cloud.controller.response.rule_group.ComplianceRuleGroupResponse;
import io.swagger.annotations.ApiModelProperty;
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

    @ApiModelProperty(value = "低风险数量", notes = "低风险数量")
    private Long low;

    @ApiModelProperty(value = "中风险数量", notes = "中风险数量")
    private Long middle;

    @ApiModelProperty(value = "高风险数量", notes = "高风险数量")
    private Long high;

    @ApiModelProperty(value = "总数", notes = "总数")
    private Long total;

    @ApiModelProperty(value = "资源类型", notes = "资源类型")
    private List<String> resourceType;

    @ApiModelProperty(value = "云平台", notes = "云平台")
    private List<String> platform;

}
