package com.fit2cloud.controller.request.user;

import com.fit2cloud.base.mapper.BaseUserMapper;
import com.fit2cloud.common.validator.annnotaion.CustomValidated;
import com.fit2cloud.common.validator.group.ValidationGroup;
import com.fit2cloud.common.validator.handler.ExistHandler;
import com.fit2cloud.dto.RoleInfo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Author: LiuDi
 * Date: 2022/8/30 4:26 PM
 */
@Data
public class CreateUserRequest {

    @ApiModelProperty(value = "主键ID")
    @CustomValidated(groups = {ValidationGroup.SAVE.class}, field = "id", mapper = BaseUserMapper.class, handler = ExistHandler.class, message = "{i18n.user.id.warn.duplicated}", exist = true, ifNullPass = true)
    private String id;

    @ApiModelProperty(value = "用户ID", required = true)
    @NotNull(groups = ValidationGroup.SAVE.class, message = "{i18n.user.name.cannot.be.null}")
    @CustomValidated(groups = {ValidationGroup.SAVE.class}, field = "username", mapper = BaseUserMapper.class, handler = ExistHandler.class, message = "{i18n.user.name.warn.duplicated}", exist = true)
    private String username;

    @ApiModelProperty(value = "姓名", required = true)
    @NotEmpty(message = "{i18n.user.displayname.cannot.null}")
    private String name;

    @ApiModelProperty(value = "状态")
    private Boolean enabled;

    @ApiModelProperty(value = "邮箱", required = true)
    @Email(groups = ValidationGroup.SAVE.class, message = "{i18n.user.email.format.error}")
    @NotNull(groups = ValidationGroup.SAVE.class, message = "{i18n.user.email.cannot.null}")
    @CustomValidated(groups = {ValidationGroup.SAVE.class}, field = "email", mapper = BaseUserMapper.class, handler = ExistHandler.class, message = "{i18n.user.email.warn.duplicated}", exist = true)
    private String email;

    @ApiModelProperty(value = "手机号码")
    private String phone;

    @ApiModelProperty(value = "密码")
    //@NotEmpty(message = "{i18n.user.pwd.cannot.null}")
    private String password;

    @ApiModelProperty(value = "用户来源", required = true)
    @NotNull(groups = ValidationGroup.SAVE.class, message = "{i18n.user.source.cannot.null}")
    private String source;

    @ApiModelProperty(value = "角色信息列表", hidden = true)
    private List<RoleInfo> roleInfoList;
}
