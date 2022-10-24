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

        public static final String CLOUD_SERVER = "CLOUD_SERVER";

        public static final String CLOUD_DISK = "CLOUD_DISK";

        public static final String CLOUD_IMAGE = "CLOUD_IMAGE";

        public static final String INSTANCE_TYPE = "INSTANCE_TYPE";

        public static final String SYSTEM_SETTING = "SYSTEM_SETTING";

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

    private static final ModulePermission.Builder MODULE_PERMISSION_BUILDER = new ModulePermission.Builder()
            .group(
                    new PermissionGroup.Builder()
                            .id(GROUP.CLOUD_SERVER)
                            .name("i18n_permission_cloud_server")
                            .permission(new Permission.Builder()
                                    .operate(OPERATE.READ)
                                    .name("i18n_permission_cloud_server_red")
                                    .role(RoleConstants.ROLE.ADMIN)
                                    .role(RoleConstants.ROLE.ORGADMIN)
                                    .role(RoleConstants.ROLE.USER))
                            .permission(new Permission.Builder()
                                    .require(OPERATE.READ)
                                    .operate(OPERATE.CREATE)
                                    .name("i18n_permission_cloud_server_create")
                                    .role(RoleConstants.ROLE.ADMIN)
                                    .role(RoleConstants.ROLE.ORGADMIN)
                                    .role(RoleConstants.ROLE.USER))

            )
            .group(
                    new PermissionGroup.Builder()
                            .id(GROUP.CLOUD_DISK)
                            .name("i18n_permission_cloud_disk")
                            .permission(new Permission.Builder()
                                    .operate(OPERATE.READ)
                                    .name("i18n_permission_cloud_disk_red")
                                    .role(RoleConstants.ROLE.ADMIN)
                                    .role(RoleConstants.ROLE.ORGADMIN))
            )
            .group(
                    new PermissionGroup.Builder()
                            .id(GROUP.CLOUD_IMAGE)
                            .name("i18n_permission_cloud_images")
                            .permission(new Permission.Builder()
                                    .operate(OPERATE.READ)
                                    .name("i18n_permission_cloud_images_red")
                                    .role(RoleConstants.ROLE.ADMIN)
                                    .role(RoleConstants.ROLE.ORGADMIN))
            )
            .group(
                    new PermissionGroup.Builder()
                            .id(GROUP.INSTANCE_TYPE)
                            .name("i18n_permission_instance_type")
                            .permission(new Permission.Builder()
                                    .operate(OPERATE.READ)
                                    .name("i18n_permission_instance_type_red")
                                    .role(RoleConstants.ROLE.ADMIN)
                                    .role(RoleConstants.ROLE.ORGADMIN))
            ).group(
                    new PermissionGroup.Builder()
                            .id(GROUP.SYSTEM_SETTING)
                            .name("i18n_permission_system_setting")
                            .permission(new Permission.Builder()
                                    .operate(OPERATE.READ)
                                    .name("i18n_permission_system_setting_red")
                                    .role(RoleConstants.ROLE.ADMIN)
                                    .role(RoleConstants.ROLE.ORGADMIN))
            )
            //...
            ;


}
