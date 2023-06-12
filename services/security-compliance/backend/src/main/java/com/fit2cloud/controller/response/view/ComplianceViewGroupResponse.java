package com.fit2cloud.controller.response.view;

import com.fit2cloud.constants.GroupTypeConstants;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2023/2/10  9:30}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
@Data
public class ComplianceViewGroupResponse {

    @Schema(title = "分组值", description = "分组值")
    private String groupValue;

    @Schema(title = "分组名称", description = "分组名称")
    private String groupName;

    @Schema(title = "分组类型", description = "分组类型")
    private GroupTypeConstants groupType;

    @Schema(title = "合规数量", description = "合规数量")
    private int complianceCount;

    @Schema(title = "总数", description = "总数")
    private int total;

    @Schema(title = "不合规数量", description = "不合规数量")
    private int notComplianceCount;

}
