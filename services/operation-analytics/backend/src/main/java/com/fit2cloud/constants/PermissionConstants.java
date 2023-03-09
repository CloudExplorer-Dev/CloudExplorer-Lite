package com.fit2cloud.constants;

import com.fit2cloud.common.constants.RoleConstants;
import com.fit2cloud.dto.permission.ModulePermission;
import com.fit2cloud.dto.permission.Permission;
import com.fit2cloud.dto.permission.PermissionGroup;
import com.fit2cloud.service.BasePermissionService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.DependsOn;
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
@DependsOn({"flyway", "flywayInitializer"})
public class PermissionConstants {

    public static class GROUP {
        public static final String RESOURCE_ANALYSIS = "RESOURCE_ANALYSIS";
        public static final String BASE_RESOURCE_ANALYSIS = "BASE_RESOURCE_ANALYSIS";
        public static final String SERVER_ANALYSIS = "SERVER_ANALYSIS";
        public static final String DISK_ANALYSIS = "DISK_ANALYSIS";
        public static final String RESOURCE_OPTIMIZATION = "RESOURCE_OPTIMIZATION";
        public static final String SERVER_OPTIMIZATION = "SERVER_OPTIMIZATION";
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
                    //总览
                    new PermissionGroup.Builder()
                            .id(GROUP.OVERVIEW)
                            .name("permission.operation.overview.base")
                            .permission(new Permission.Builder()
                                    .operate(OPERATE.READ)
                                    .name("permission.operation.overview.read")
                                    .role(RoleConstants.ROLE.ADMIN)
                                    .role(RoleConstants.ROLE.ORGADMIN)
                                    .role(RoleConstants.ROLE.USER))
            )
            .group(
                    //基础资源分析
                    new PermissionGroup.Builder()
                            .id(GROUP.BASE_RESOURCE_ANALYSIS)
                            .name("permission.operation.base_resource_analysis.base")
                            .permission(new Permission.Builder()
                                    .operate(OPERATE.READ)
                                    .name("permission.operation.base_resource_analysis.read")
                                    .role(RoleConstants.ROLE.ADMIN))
            )
            .group(
                    new PermissionGroup.Builder()
                            .id(GROUP.SERVER_ANALYSIS)
                            .name("permission.operation.server_analysis.base")
                            .permission(new Permission.Builder()
                                    .operate(OPERATE.READ)
                                    .name("permission.operation.server_analysis.read")
                                    .role(RoleConstants.ROLE.ADMIN)
                                    .role(RoleConstants.ROLE.ORGADMIN)
                                    .role(RoleConstants.ROLE.USER))
            )
            .group(
                    new PermissionGroup.Builder()
                            .id(GROUP.DISK_ANALYSIS)
                            .name("permission.operation.disk_analysis.base")
                            .permission(new Permission.Builder()
                                    .operate(OPERATE.READ)
                                    .name("permission.operation.disk_analysis.read")
                                    .role(RoleConstants.ROLE.ADMIN)
                                    .role(RoleConstants.ROLE.ORGADMIN)
                                    .role(RoleConstants.ROLE.USER))
            )
            .group(
                    new PermissionGroup.Builder()
                            .id(GROUP.SERVER_OPTIMIZATION)
                            .name("permission.operation.resource_optimization.base")
                            .permission(new Permission.Builder()
                                    .operate(OPERATE.READ)
                                    .name("permission.operation.resource_optimization.read")
                                    .role(RoleConstants.ROLE.ADMIN)
                                    .role(RoleConstants.ROLE.ORGADMIN)
                                    .role(RoleConstants.ROLE.USER))
            );


}
