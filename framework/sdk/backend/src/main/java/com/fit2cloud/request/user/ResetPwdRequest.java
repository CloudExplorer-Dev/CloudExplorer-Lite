package com.fit2cloud.request.user;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

/**
 * @author : LiuDi
 * @date : 2023/2/13 16:39
 */
@Data
public class ResetPwdRequest {
    @ApiModelProperty("原密码")
    @NotNull(message = "原密码不能为空")
    @Length( min = 1, message = "原密码不能为空字符串")
    private String oldPassword;
    @ApiModelProperty("新密码")
    @NotNull(message = "新密码不能为空")
    @Length( min = 1, message = "新密码不能为空字符串")
    private String newPassword;
}
