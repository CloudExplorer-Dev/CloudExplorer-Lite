package com.fit2cloud.controller.request.user;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
public class UserBatchAddRoleObjectV3 {

    @NotEmpty(message = "用户ID列表不能为空")
    @ApiModelProperty(value = "用户ID列表")
    private List<String> userIds;

    @NotEmpty(message = "角色ID列表不能为空")
    @ApiModelProperty(value = "角色ID列表")
    private List<String> roleIds;
}
