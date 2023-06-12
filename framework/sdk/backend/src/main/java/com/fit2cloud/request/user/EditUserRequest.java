package com.fit2cloud.request.user;

import com.fit2cloud.common.validator.group.ValidationGroup;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

/**
 * @author : LiuDi
 * @date : 2023/2/13 17:43
 */
@Data
public class EditUserRequest {

    private String id;

    @Schema(title = "姓名")
    @NotNull(message = "用户名不能为空")
    @Length(groups = {ValidationGroup.UPDATE.class}, min = 1, message = "用户名不能为空字符串")
    private String name;

    @Schema(title = "邮箱")
    @NotNull(message = "邮箱不能为空")
    @Length(min = 1, message = "邮箱不能为空字符串")
    @Email(message = "邮箱格式错误")
    private String email;

    @Schema(title = "手机号")
    private String phone;
}
