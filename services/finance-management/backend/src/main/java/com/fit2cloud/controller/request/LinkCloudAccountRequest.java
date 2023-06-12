package com.fit2cloud.controller.request;

import com.fit2cloud.base.mapper.BaseBillPolicyMapper;
import com.fit2cloud.common.validator.annnotaion.CustomValidated;
import com.fit2cloud.common.validator.handler.ExistHandler;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.NotNull;
import java.util.List;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2023/6/6  11:13}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
@Data
public class LinkCloudAccountRequest {

    @Schema(title = "策略主键")
    @NotNull(message = "计费策略id不能为空")
    @CustomValidated(mapper = BaseBillPolicyMapper.class, handler = ExistHandler.class, field = "id", message = "计费策略id不存在", exist = false)
    private String billingPolicyId;

    @Schema(title = "关联云账号列表")
    private List<String> cloudAccountIdList;
}
