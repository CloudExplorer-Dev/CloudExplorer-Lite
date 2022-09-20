package com.fit2cloud.controller.request.user;

import com.fit2cloud.base.mapper.BaseUserMapper;
import com.fit2cloud.common.validator.annnotaion.CustomValidated;
import com.fit2cloud.common.validator.group.ValidationGroup;
import com.fit2cloud.common.validator.handler.ExistHandler;
import com.fit2cloud.dto.RoleInfo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Author: LiuDi
 * Date: 2022/9/15 2:26 PM
 */
@Data
public class UpdateUserRequest {

    @ApiModelProperty(value = "主键ID", required = true)
    @NotNull(groups = {ValidationGroup.UPDATE.class}, message = "{i18n.user.id.cannot.be.null}")
    @CustomValidated(groups = {ValidationGroup.UPDATE.class}, mapper = BaseUserMapper.class, handler = ExistHandler.class, message = "{i18n.primary.key.not.exist}", exist = false)
    private String id;

    @ApiModelProperty("姓名")
    @Length(groups = {ValidationGroup.UPDATE.class}, min = 1, message = "{i18n.user.displayname.cannot.null}")
    private String name;

    @ApiModelProperty("邮箱")
    @Length(groups = {ValidationGroup.UPDATE.class}, min = 1, message = "{i18n.user.email.cannot.null}")
    @Email(groups = {ValidationGroup.UPDATE.class}, message = "{i18n.user.email.format.error}")
    private String email;

    @ApiModelProperty("手机号")
    private String phone;

    @ApiModelProperty("状态")
    private Boolean enabled;

    @ApiModelProperty(value = "角色信息列表", required = true)
    private List<RoleInfo> roleInfoList;

}
