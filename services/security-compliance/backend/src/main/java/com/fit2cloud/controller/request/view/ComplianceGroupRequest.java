package com.fit2cloud.controller.request.view;

import com.fit2cloud.common.query.annotaion.Query;
import com.fit2cloud.common.utils.QueryUtil;
import com.fit2cloud.constants.GroupTypeConstants;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2023/2/10  12:14}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
@Data
public class ComplianceGroupRequest {

    @Query(field = "cloudAccountId", compareType = QueryUtil.CompareType.EQ)
    @Schema(title = "云账号id", description = "云账号id")
    private String cloudAccountId;

    @Schema(title = "分组类型", description = "分组类型")
    private GroupTypeConstants groupType;

}
