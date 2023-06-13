package com.fit2cloud.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class LoginRequest {

    @Schema(title = "用户名", example = "user")
    private String username;

    @Schema(title = "密码", example = "user_password")
    private String password;

    public LoginRequest() {
    }
}
