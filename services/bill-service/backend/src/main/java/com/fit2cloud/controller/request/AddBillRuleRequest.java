package com.fit2cloud.controller.request;

import com.fit2cloud.common.validator.annnotaion.CustomValidated;
import com.fit2cloud.common.validator.group.ValidationGroup;
import com.fit2cloud.common.validator.handler.ExistHandler;
import com.fit2cloud.dao.jentity.Group;
import com.fit2cloud.dao.mapper.BillRuleMapper;
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
    @CustomValidated(mapper = BillRuleMapper.class, handler = ExistHandler.class, message = "账单规则名称不能重复", exist = true, groups = ValidationGroup.SAVE.class)
    private String name;
    /**
     * 账单组
     */
    @ApiModelProperty(value = "账单规则组", notes = "账单规则组", required = true)
    private List<Group> groups;
}
