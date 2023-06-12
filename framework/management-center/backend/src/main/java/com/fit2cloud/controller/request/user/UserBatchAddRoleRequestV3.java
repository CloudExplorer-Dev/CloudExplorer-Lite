package com.fit2cloud.controller.request.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.NotEmpty;
import java.util.List;


@Data
public class UserBatchAddRoleRequestV3 {

    @Schema(title = "组织/工作空间ID")
    private String sourceId;

    @Schema(title = "组织/工作空间类型", description = "ORGANIZATION | WORKSPACE , 其他默认为没有类型")
    private String type;

    @NotEmpty(message = "关联关系不能为空")
    @Schema(title = "用户与角色ID关联列表")
    private List<UserBatchAddRoleObjectV3> userRoleMappings;

}
