package com.fit2cloud.controller.request.user;

import com.fit2cloud.base.mapper.BaseUserMapper;
import com.fit2cloud.common.validator.annnotaion.CustomValidated;
import com.fit2cloud.common.validator.group.ValidationGroup;
import com.fit2cloud.common.validator.handler.ExistHandler;
import com.fit2cloud.dto.RoleInfo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import java.util.List;

/**
 * Author: LiuDi
 * Date: 2022/9/15 2:26 PM
 */
@Data
public class UpdateUserRequest {

    @Schema(title = "主键ID", required = true)
    @NotNull(groups = {ValidationGroup.UPDATE.class}, message = "{i18n.user.id.cannot.be.null}")
    @CustomValidated(groups = {ValidationGroup.UPDATE.class}, mapper = BaseUserMapper.class, handler = ExistHandler.class, message = "{i18n.primary.key.not.exist}", exist = false)
    private String id;

    @Schema(title = "姓名")
    @Length(groups = {ValidationGroup.UPDATE.class}, min = 1, message = "{i18n.user.displayname.cannot.null}")
    private String name;

    @Schema(title = "邮箱")
    @Length(groups = {ValidationGroup.UPDATE.class}, min = 1, message = "{i18n.user.email.cannot.null}")
    @Email(groups = {ValidationGroup.UPDATE.class}, message = "{i18n.user.email.format.error}")
    private String email;

    @Schema(title = "手机号")
    private String phone;

    @Schema(title = "状态")
    private Boolean enabled;

    @Schema(title = "角色信息列表", required = true)
    private List<RoleInfo> roleInfoList;

}
