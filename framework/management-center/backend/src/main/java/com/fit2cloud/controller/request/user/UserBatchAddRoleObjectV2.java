package com.fit2cloud.controller.request.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.NotEmpty;
import java.util.List;

@Data
public class UserBatchAddRoleObjectV2 {

    @NotEmpty(message = "用户ID列表不能为空")
    @Schema(title = "用户ID列表")
    private List<String> userIds;

    @Schema(title = "组织/工作空间ID列表", description = "系统管理员及继承系统管理员时，该字段不用传")
    private List<String> sourceIds;
}
