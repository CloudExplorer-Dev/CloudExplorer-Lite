package com.fit2cloud.common.constants;

import com.fit2cloud.base.entity.User;
import org.apache.commons.lang3.StringUtils;

public class SystemUserConstants {

    public static final User user = new User()
            .setUsername("system")
            .setName("SYSTEM");

    public static String getUserName() {
        return user.getUsername();
    }

    public static boolean isSystemUser(String username) {
        return StringUtils.equals(user.getUsername(), username);
    }

    public static boolean isSystemUser(User user) {
        return isSystemUser(user.getUsername());
    }
}
