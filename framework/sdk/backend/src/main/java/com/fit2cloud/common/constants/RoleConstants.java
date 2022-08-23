package com.fit2cloud.common.constants;

public class RoleConstants {

    public static final String ROLE_TOKEN = "CE-ROLE";

    public enum ROLE {
        SYSTEM, ADMIN, ORGADMIN, USER, ANONYMOUS;

    }

    public enum Type {
        origin, inherit
    }

}
