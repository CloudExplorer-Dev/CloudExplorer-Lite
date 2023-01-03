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
        public static final String RESOURCE_ANALYSIS = "RESOURCE_ANALYSIS";
        public static final String SERVER_ANALYSIS = "SERVER_ANALYSIS";
        public static final String DISK_ANALYSIS = "DISK_ANALYSIS";
        public static final String RESOURCE_OPTIMIZATION = "RESOURCE_OPTIMIZATION";
        public static final String OVERVIEW = "OVERVIEW";

        //...
    }

    public static class OPERATE {
        public static final String READ = "READ";
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
            .group(
                    new PermissionGroup.Builder()
                            .id(GROUP.OVERVIEW)
                            .name("i18n_permission_overview_view")
                            .permission(new Permission.Builder()
                                    .operate(OPERATE.READ)
                                    .name("i18n_permission_overview_view_read")
                                    .role(RoleConstants.ROLE.ADMIN)
                                    .role(RoleConstants.ROLE.ORGADMIN)
                                    .role(RoleConstants.ROLE.USER))
            )
            .group(
                    new PermissionGroup.Builder()
                            .id(GROUP.RESOURCE_ANALYSIS)
                            .name("i18n_permission_resource_analysis_view")
                            .permission(new Permission.Builder()
                                    .operate(OPERATE.READ)
                                    .name("i18n_permission_resource_analysis_read")
                                    .role(RoleConstants.ROLE.ADMIN)
                                    .role(RoleConstants.ROLE.ORGADMIN)
                                    .role(RoleConstants.ROLE.USER))
            )
            .group(
                    new PermissionGroup.Builder()
                            .id(GROUP.SERVER_ANALYSIS)
                            .name("i18n_permission_server_analysis_view")
                            .permission(new Permission.Builder()
                                    .operate(OPERATE.READ)
                                    .name("i18n_permission_server_analysis_red")
                                    .role(RoleConstants.ROLE.ADMIN)
                                    .role(RoleConstants.ROLE.ORGADMIN)
                                    .role(RoleConstants.ROLE.USER))
            )
            .group(
                    new PermissionGroup.Builder()
                            .id(GROUP.DISK_ANALYSIS)
                            .name("i18n_permission_disk_analysis_view")
                            .permission(new Permission.Builder()
                                    .operate(OPERATE.READ)
                                    .name("i18n_permission_disk_analysis_red")
                                    .role(RoleConstants.ROLE.ADMIN)
                                    .role(RoleConstants.ROLE.ORGADMIN)
                                    .role(RoleConstants.ROLE.USER))
            )
            .group(
                    new PermissionGroup.Builder()
                            .id(GROUP.RESOURCE_OPTIMIZATION)
                            .name("i18n_permission_resource_optimization_view")
                            .permission(new Permission.Builder()
                                    .operate(OPERATE.READ)
                                    .name("i18n_permission_resource_optimization_red")
                                    .role(RoleConstants.ROLE.ADMIN)
                                    .role(RoleConstants.ROLE.ORGADMIN)
                                    .role(RoleConstants.ROLE.USER))
            );


}
