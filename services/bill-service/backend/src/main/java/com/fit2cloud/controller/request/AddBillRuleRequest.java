package com.fit2cloud.controller.request;

import com.fit2cloud.dao.jentity.Group;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2022/11/20  8:27 PM}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
@Data
public class AddBillRuleRequest {
    /**
     * 账单规则名称
     */
    @ApiModelProperty(value = "账单规则名称", notes = "账单规则名称", required = true)
    private String name;
    /**
     * 账单组
     */
    @ApiModelProperty(value = "账单规则组", notes = "账单规则组", required = true)
    private List<Group> groups;
}
