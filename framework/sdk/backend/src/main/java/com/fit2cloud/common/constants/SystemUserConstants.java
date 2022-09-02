package com.fit2cloud.common.constants;

import com.fit2cloud.base.entity.User;
public class SystemUserConstants extends User {

    private static User user = new User();

    static {
        user.setUsername("system");
        user.setName("SYSTEM");
    }

    public static String getUserName() {
        return user.getUsername();
    }
}
