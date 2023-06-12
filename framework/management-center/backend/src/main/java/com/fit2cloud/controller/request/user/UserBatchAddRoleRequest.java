package com.fit2cloud.controller.request.user;

import com.fit2cloud.dto.RoleInfo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.NotEmpty;
import java.util.List;

/**
 * Author: LiuDi
 * Date: 2022/9/7 10:21 PM
 */
@Data
public class UserBatchAddRoleRequest {

    @NotEmpty(message = "{i18n.user.id.list.cannot.null}")
    @Schema(title = "用户ID列表")
    private List<String> userIdList;

    @NotEmpty(message = "{i18n.user.role.list.cannot.null}")
    @Schema(title = "角色信息列表")
    private List<RoleInfo> roleInfoList;
}
