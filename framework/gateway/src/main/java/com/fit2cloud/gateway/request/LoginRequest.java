package com.fit2cloud.gateway.request;

import lombok.Data;

@Data
public class LoginRequest {

    private String username;
    private String password;

    public LoginRequest() {
    }
}
