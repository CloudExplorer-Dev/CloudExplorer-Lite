package com.fit2cloud.request.user;

import com.fit2cloud.common.validator.group.ValidationGroup;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

/**
 * @author : LiuDi
 * @date : 2023/2/13 17:43
 */
@Data
public class EditUserRequest {
    private String id;

    @ApiModelProperty("姓名")
    @NotNull(message = "用户名不能为空")
    @Length(groups = {ValidationGroup.UPDATE.class}, min = 1, message = "用户名不能为空字符串")
    private String name;

    @ApiModelProperty("邮箱")
    @NotNull(message = "邮箱不能为空")
    @Length(min = 1, message = "邮箱不能为空字符串")
    @Email(message = "邮箱格式错误")
    private String email;

    @ApiModelProperty("手机号")
    private String phone;
}
