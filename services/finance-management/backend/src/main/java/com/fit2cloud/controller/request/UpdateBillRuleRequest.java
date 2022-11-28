package com.fit2cloud.controller.request;

import com.fit2cloud.common.validator.annnotaion.CustomValidated;
import com.fit2cloud.common.validator.handler.ExistHandler;
import com.fit2cloud.dao.mapper.BillRuleMapper;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2022/11/20  8:32 PM}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
@Data
public class UpdateBillRuleRequest extends AddBillRuleRequest {
    /**
     * 主键id
     */
    @ApiModelProperty(value = "主键id", notes = "主键id")
    @CustomValidated(mapper = BillRuleMapper.class, handler = ExistHandler.class, message = "账单规则id必须存在", exist = false)
    private String id;
}
