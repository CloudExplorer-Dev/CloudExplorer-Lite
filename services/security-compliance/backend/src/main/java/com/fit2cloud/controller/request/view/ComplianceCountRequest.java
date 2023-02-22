package com.fit2cloud.controller.request.view;

import com.fit2cloud.common.query.annotaion.Query;
import com.fit2cloud.common.utils.QueryUtil;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2023/2/10  12:07}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
@Data
public class ComplianceCountRequest {
    @ApiModelProperty(value = "云账号id", notes = "云账号id")
    @Query(compareType = QueryUtil.CompareType.EQ, field = "cloudAccountId")
    private String cloudAccountId;
}
