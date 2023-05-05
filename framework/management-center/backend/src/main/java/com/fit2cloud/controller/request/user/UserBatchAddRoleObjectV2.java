package com.fit2cloud.controller.request.user;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
public class UserBatchAddRoleObjectV2 {

    @NotEmpty(message = "")
    @ApiModelProperty(value = "用户ID列表")
    private List<String> userIds;

    @ApiModelProperty(value = "组织/工作空间ID列表", notes = "系统管理员及继承系统管理员时，该字段不用传")
    private List<String> sourceIds;
}
