package com.fit2cloud.controller.request;

import com.fit2cloud.base.mapper.BaseBillPolicyMapper;
import com.fit2cloud.common.validator.annnotaion.CustomValidated;
import com.fit2cloud.common.validator.handler.ExistHandler;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2023/6/5  18:06}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
@Data
public class AddBillingPolicyRequest {

    @ApiModelProperty(value = "策略名称")
    @CustomValidated(mapper = BaseBillPolicyMapper.class,
            handler = ExistHandler.class,
            message = "策略名称不能重复", exist = true)
    @NotNull(message = "计费策略名称不能为空")
    @Length(min = 1, max = 255, message = "计费策略名称长度必须在1-255之间")
    private String name;

    @ApiModelProperty(value = "关联云账号id集合")
    private List<String> linkCloudAccountIds;
}
