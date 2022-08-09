package com.fit2cloud.dto;


import lombok.Data;

@Data
public class User {

    private String id;

    private String username;

    private String email;

    private String phone;

    private String password;

    public User() {
    }
}

