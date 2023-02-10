package com.fit2cloud.controller.request.view;

import com.fit2cloud.constants.GroupTypeConstants;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2023/2/10  12:14}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
@Data
public class ComplianceGroupRequest {
    @ApiModelProperty(value = "云账号id", notes = "云账号id")
    private String cloudAccountId;
    @ApiModelProperty(value = "分组类型", notes = "分组类型")
    private GroupTypeConstants groupType;
}
