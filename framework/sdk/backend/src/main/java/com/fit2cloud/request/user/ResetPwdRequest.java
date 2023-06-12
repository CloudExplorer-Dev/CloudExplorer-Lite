package com.fit2cloud.request.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.NotNull;

/**
 * @author : LiuDi
 * @date : 2023/2/13 16:39
 */
@Data
public class ResetPwdRequest {

    @Schema(title = "原密码")
    @NotNull(message = "原密码不能为空")
    @Length(min = 1, message = "原密码不能为空字符串")
    private String oldPassword;

    @Schema(title = "新密码")
    @NotNull(message = "新密码不能为空")
    @Length(min = 1, message = "新密码不能为空字符串")
    private String newPassword;
}
