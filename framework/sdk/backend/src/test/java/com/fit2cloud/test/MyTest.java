package com.fit2cloud.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fit2cloud.common.constants.RoleConstants;
import com.fit2cloud.dto.permission.ModulePermission;
import com.fit2cloud.dto.permission.Permission;
import com.fit2cloud.dto.permission.PermissionGroup;
import org.junit.jupiter.api.Test;

public class MyTest {

    private ObjectMapper objectMapper = new ObjectMapper();

    private PermissionGroup group = new PermissionGroup.Builder()
            .id("USER")
            .name("用户管理")
            .permission(
                    //权限1
                    new Permission.Builder()
                            .operate("READ")
                            .name("查看")
                            .role(RoleConstants.ROLE.ADMIN)
            )
            .permission(
                    //权限1
                    new Permission.Builder()
                            .require("READ")
                            .operate("EDIT")
                            .name("查看")
                            .role(RoleConstants.ROLE.ADMIN)
                            .role(RoleConstants.ROLE.ORGADMIN)
            )
            .permission(
                    //权限1
                    new Permission.Builder()
                            .operate("READ")
                            .name("查看1")
                            .role(RoleConstants.ROLE.ORGADMIN)
            )
            .build();


    @Test
    public void myTest() throws Exception {
        System.out.println(objectMapper.writeValueAsString(group));
    }

    @Test
    public void myTest2() throws Exception {
        String str ="{\"module\":\"management-center\",\"groups\":[{\"module\":\"management-center\",\"id\":\"USER\",\"name\":\"i18n_permission_user\",\"permissions\":[{\"module\":\"management-center\",\"groupId\":\"USER\",\"require\":null,\"operate\":\"READ\",\"name\":\"i18n_permission_user_read\",\"roles\":[\"ADMIN\",\"ORGADMIN\"],\"id\":\"[management-center]USER:READ\"},{\"module\":\"management-center\",\"groupId\":\"USER\",\"require\":\"READ\",\"operate\":\"CREATE\",\"name\":\"i18n_permission_user_create\",\"roles\":[\"ADMIN\",\"ORGADMIN\"],\"id\":\"[management-center]USER:READ+CREATE\"}]},{\"module\":\"management-center\",\"id\":\"ROLE\",\"name\":\"i18n_permission_role\",\"permissions\":[{\"module\":\"management-center\",\"groupId\":\"ROLE\",\"require\":null,\"operate\":\"READ\",\"name\":\"i18n_permission_role_read\",\"roles\":[\"ADMIN\",\"ORGADMIN\"],\"id\":\"[management-center]ROLE:READ\"}]}]}";

        ModulePermission group1 = objectMapper.readValue(str, ModulePermission.class);

        System.out.println(objectMapper.writeValueAsString(group1));
    }
}
