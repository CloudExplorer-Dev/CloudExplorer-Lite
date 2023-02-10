package com.fit2cloud.controller.response.view;

import com.fit2cloud.constants.GroupTypeConstants;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2023/2/10  9:30}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
@Data
public class ComplianceViewGroupResponse {
    @ApiModelProperty(value = "分组名称", notes = "分组名称")
    private String groupName;
    @ApiModelProperty(value = "分组类型", notes = "分组类型")
    private GroupTypeConstants groupType;
    @ApiModelProperty(value = "合规数量", notes = "合规数量")
    private int complianceCount;
    @ApiModelProperty(value = "总数", notes = "总数")
    private int total;
    @ApiModelProperty(value = "不合规数量", notes = "不合规数量")
    private int notComplianceCount;
}
