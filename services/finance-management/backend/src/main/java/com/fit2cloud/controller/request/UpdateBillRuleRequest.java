package com.fit2cloud.controller.request;

import com.fit2cloud.common.validator.annnotaion.CustomQueryWrapperValidated;
import com.fit2cloud.common.validator.annnotaion.CustomValidated;
import com.fit2cloud.common.validator.group.ValidationGroup;
import com.fit2cloud.common.validator.handler.ExistHandler;
import com.fit2cloud.common.validator.handler.ExistQueryWrapperValidatedHandler;
import com.fit2cloud.dao.mapper.BillRuleMapper;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2022/11/20  8:32 PM}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
@Data
@CustomQueryWrapperValidated(groups = ValidationGroup.UPDATE.class, handler = ExistQueryWrapperValidatedHandler.class, mapper = BillRuleMapper.class
        , message = "账单规则名称不能重复", exist = true, el = "#getQueryWrapper().ne(\"id\",#this.id).eq(\"name\",#this.name)")
public class UpdateBillRuleRequest extends AddBillRuleRequest {
    /**
     * 主键id
     */
    @Schema(title = "主键id", description = "主键id")
    @CustomValidated(mapper = BillRuleMapper.class, handler = ExistHandler.class, message = "账单规则id必须存在", exist = false)
    private String id;
}
