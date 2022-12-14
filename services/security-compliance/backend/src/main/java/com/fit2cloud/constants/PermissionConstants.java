package com.fit2cloud.constants;

import com.fit2cloud.common.constants.RoleConstants;
import com.fit2cloud.dto.permission.ModulePermission;
import com.fit2cloud.dto.permission.Permission;
import com.fit2cloud.dto.permission.PermissionGroup;
import com.fit2cloud.service.BasePermissionService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 模块内权限
 * 需要实现 MODULE_PERMISSION_BUILDER
 * 当前模块内判断主要是 groupId 和 operate
 * 也可以通过判断id，但是id需要带上模块名与require组合，如："[management-center]USER:READ"
 */
@Component
public class PermissionConstants {

    public static class GROUP {
        public static final String OVERVIEW = "OVERVIEW";
        public static final String SCAN = "SCAN";
        public static final String RULE = "RULE";
        public static final String INSURANCE = "INSURANCE";

        //...
    }

    public static class OPERATE {
        public static final String READ = "READ";
        public static final String EDIT = "EDIT";
        public static final String CREATE = "CREATE";
        public static final String DELETE = "DELETE";
        //...
    }

    public static ModulePermission MODULE_PERMISSION;

    /**
     * 可以通过id找到对应权限
     */
    public static Map<String, Permission> PERMISSION_MAP = null;

    @Resource
    private BasePermissionService basePermissionService;

    @Value("${spring.application.name}")
    public void setModule(String module) {
        PermissionConstants.MODULE_PERMISSION = MODULE_PERMISSION_BUILDER
                .module(module)
                .build();

        PERMISSION_MAP = PermissionConstants.MODULE_PERMISSION.getGroups().stream()
                .map(PermissionGroup::getPermissions)
                .flatMap(Collection::stream)
                .collect(Collectors.toMap(Permission::getId, permission -> permission));

        //推送到redis
        basePermissionService.init(module, PermissionConstants.MODULE_PERMISSION);
    }

    private static final ModulePermission.Builder MODULE_PERMISSION_BUILDER = new ModulePermission.Builder().group()
            .group(new PermissionGroup.Builder().id(GROUP.OVERVIEW)
                    .permission(new Permission.Builder()
                            .operate(OPERATE.READ)
                            .name("i18n_permission_overview_red")
                            .role(RoleConstants.ROLE.ADMIN)
                            .role(RoleConstants.ROLE.ORGADMIN)
                            .role(RoleConstants.ROLE.USER)))
            .group(new PermissionGroup.Builder().id(GROUP.SCAN)
                    .permission(new Permission.Builder()
                            .operate(OPERATE.READ)
                            .name("i18n_permission_scan_red")
                            .role(RoleConstants.ROLE.ADMIN)
                            .role(RoleConstants.ROLE.ORGADMIN)
                            .role(RoleConstants.ROLE.USER)))
            .group(new PermissionGroup.Builder().id(GROUP.RULE)
                    .permission(new Permission.Builder()
                            .operate(OPERATE.READ)
                            .name("i18n_permission_rule_red")
                            .role(RoleConstants.ROLE.ADMIN)
                            .role(RoleConstants.ROLE.ORGADMIN)
                            .role(RoleConstants.ROLE.USER)))
            .group(new PermissionGroup.Builder().id(GROUP.INSURANCE)
                    .permission(new Permission.Builder()
                            .operate(OPERATE.READ)
                            .name("i18n_permission_insurance_red")
                            .role(RoleConstants.ROLE.ADMIN)
                            .role(RoleConstants.ROLE.ORGADMIN)
                            .role(RoleConstants.ROLE.USER)));
    ;


}
