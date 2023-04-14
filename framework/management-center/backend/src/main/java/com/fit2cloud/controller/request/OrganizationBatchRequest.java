package com.fit2cloud.controller.request;

import com.fit2cloud.base.mapper.BaseOrganizationMapper;
import com.fit2cloud.common.validator.annnotaion.CustomValidated;
import com.fit2cloud.common.validator.group.ValidationGroup;
import com.fit2cloud.common.validator.handler.ExistHandler;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Size;
import java.util.List;

/**
 * @Author:张少虎
 * @Date: 2022/8/29  12:10 PM
 * @Version 1.0
 * @注释:
 */
@Data
public class OrganizationBatchRequest {
    @ApiModelProperty("所属组织id")
    @CustomValidated(groups = {ValidationGroup.UPDATE.class}, mapper = BaseOrganizationMapper.class, handler = ExistHandler.class, message = "{i18n.organization.id.is.not.existent}", exist = false)
    private String pid;

    @Size(min = 1, message = "{i18m.organization.is.not.empty}")
    @ApiModelProperty("组织数据")
    private List<OriginDetails> orgDetails;

    @Data
    public static class OriginDetails {
        @ApiModelProperty(value = "组织名称", notes = "组织名称")
        private String name;
        @ApiModelProperty(value = "组织描述", notes = "组织描述")
        private String description;
    }

}

