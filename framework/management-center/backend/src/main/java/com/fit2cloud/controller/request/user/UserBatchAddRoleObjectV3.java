package com.fit2cloud.controller.request.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.NotEmpty;
import java.util.List;

@Data
public class UserBatchAddRoleObjectV3 {

    @NotEmpty(message = "用户ID列表不能为空")
    @Schema(title = "用户ID列表")
    private List<String> userIds;

    @NotEmpty(message = "角色ID列表不能为空")
    @Schema(title = "角色ID列表")
    private List<String> roleIds;
}
