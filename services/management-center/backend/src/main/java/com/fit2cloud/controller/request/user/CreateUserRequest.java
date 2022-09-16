package com.fit2cloud.controller.request.user;

import com.fit2cloud.dto.RoleInfo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * Author: LiuDi
 * Date: 2022/8/30 4:26 PM
 */
@Data
public class CreateUserRequest{

    @ApiModelProperty(value = "主键ID")
    private String id;

    @ApiModelProperty(value = "用户ID",required = true)
    @NotEmpty(message = "{i18n.user.id.cannot.be.null}")
    private String username;

    @ApiModelProperty(value = "姓名",required = true)
    @NotEmpty(message = "{i18n.user.name.cannot.null}")
    private String name;

    @ApiModelProperty(value = "状态")
    private Boolean enabled;

    @ApiModelProperty(value = "邮箱",required = true)
    @NotEmpty(message = "{i18n.user.email.cannot.null}")
    @Email(message = "{i18n.user.email.format.error}")
    private String email;

    @ApiModelProperty(value = "手机号码")
    private String phone;

    @ApiModelProperty(value = "密码",required = true)
    @NotEmpty(message = "{i18n.user.pwd.cannot.null}")
    private String password;

    @ApiModelProperty(value = "用户来源")
    private String source;

    @ApiModelProperty(value = "角色信息列表",hidden = true)
    private List<RoleInfo> roleInfoList;
}
