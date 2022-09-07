package com.fit2cloud.common.constants;

import com.baomidou.mybatisplus.annotation.EnumValue;

public class RoleConstants {

    public static final String ROLE_TOKEN = "CE-ROLE";

    public enum ROLE {
        SYSTEM, ADMIN, ORGADMIN, USER, ANONYMOUS;

    }

    public enum Type {
        origin(0), inherit(1);

        Type(int index) {
            this.index = index;
        }

        @EnumValue
        private final int index;
    }

}
