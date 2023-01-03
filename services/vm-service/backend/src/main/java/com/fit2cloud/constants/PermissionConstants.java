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

        public static final String JOBS = "JOBS";

        //...
    }

    public static class OPERATE {
        public static final String READ = "READ";
        public static final String EDIT = "EDIT";
        public static final String CREATE = "CREATE";
        public static final String DELETE = "DELETE";
        public static final String START = "START";
        public static final String STOP = "STOP";
        public static final String RESTART = "RESTART";
        public static final String ATTACH = "ATTACH";
        public static final String DETACH = "DETACH";
        public static final String RESIZE = "RESIZE";
        public static final String AUTH = "AUTH";
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
                                    .name("i18n_permission_cloud_server_read")
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
                            .permission(new Permission.Builder()
                                    .require(OPERATE.READ)
                                    .operate(OPERATE.EDIT)
                                    .name("i18n_permission_cloud_server_edit")
                                    .role(RoleConstants.ROLE.ADMIN)
                                    .role(RoleConstants.ROLE.ORGADMIN)
                                    .role(RoleConstants.ROLE.USER))
                            .permission(new Permission.Builder()
                                    .require(OPERATE.READ)
                                    .operate(OPERATE.DELETE)
                                    .name("i18n_permission_cloud_server_delete")
                                    .role(RoleConstants.ROLE.ADMIN)
                                    .role(RoleConstants.ROLE.ORGADMIN)
                                    .role(RoleConstants.ROLE.USER))
                            .permission(new Permission.Builder()
                                    .require(OPERATE.READ)
                                    .operate(OPERATE.START)
                                    .name("i18n_permission_cloud_server_start")
                                    .role(RoleConstants.ROLE.ADMIN)
                                    .role(RoleConstants.ROLE.ORGADMIN)
                                    .role(RoleConstants.ROLE.USER))
                            .permission(new Permission.Builder()
                                    .require(OPERATE.READ)
                                    .operate(OPERATE.STOP)
                                    .name("i18n_permission_cloud_server_stop")
                                    .role(RoleConstants.ROLE.ADMIN)
                                    .role(RoleConstants.ROLE.ORGADMIN)
                                    .role(RoleConstants.ROLE.USER))
                            .permission(new Permission.Builder()
                                    .require(OPERATE.READ)
                                    .operate(OPERATE.RESTART)
                                    .name("i18n_permission_cloud_server_restart")
                                    .role(RoleConstants.ROLE.ADMIN)
                                    .role(RoleConstants.ROLE.ORGADMIN)
                                    .role(RoleConstants.ROLE.USER))
                            .permission(new Permission.Builder()
                                    .require(OPERATE.READ)
                                    .operate(OPERATE.RESTART)
                                    .name("i18n_permission_cloud_server_restart")
                                    .role(RoleConstants.ROLE.ADMIN)
                                    .role(RoleConstants.ROLE.ORGADMIN)
                                    .role(RoleConstants.ROLE.USER))
                            .permission(new Permission.Builder()
                                    .require(OPERATE.READ)
                                    .operate(OPERATE.RESIZE)
                                    .name("i18n_permission_cloud_server_resize")
                                    .role(RoleConstants.ROLE.ADMIN)
                                    .role(RoleConstants.ROLE.ORGADMIN)
                                    .role(RoleConstants.ROLE.USER))
                            .permission(new Permission.Builder()
                                    .require(OPERATE.READ)
                                    .operate(OPERATE.AUTH)
                                    .name("i18n_permission_cloud_server_auth")
                                    .role(RoleConstants.ROLE.ADMIN)
                                    .role(RoleConstants.ROLE.ORGADMIN))

            )
            .group(
                    new PermissionGroup.Builder()
                            .id(GROUP.CLOUD_DISK)
                            .name("i18n_permission_cloud_disk")
                            .permission(new Permission.Builder()
                                    .operate(OPERATE.READ)
                                    .name("i18n_permission_cloud_disk_read")
                                    .role(RoleConstants.ROLE.ADMIN)
                                    .role(RoleConstants.ROLE.ORGADMIN)
                                    .role(RoleConstants.ROLE.USER))
                            .permission(new Permission.Builder()
                                    .require(OPERATE.READ)
                                    .operate(OPERATE.RESIZE)
                                    .name("i18n_permission_cloud_disk_resize")
                                    .role(RoleConstants.ROLE.ADMIN)
                                    .role(RoleConstants.ROLE.ORGADMIN)
                                    .role(RoleConstants.ROLE.USER))
                            .permission(new Permission.Builder()
                                    .require(OPERATE.READ)
                                    .operate(OPERATE.CREATE)
                                    .name("i18n_permission_cloud_disk_create")
                                    .role(RoleConstants.ROLE.ADMIN)
                                    .role(RoleConstants.ROLE.ORGADMIN)
                                    .role(RoleConstants.ROLE.USER))
                            .permission(new Permission.Builder()
                                    .require(OPERATE.READ)
                                    .operate(OPERATE.ATTACH)
                                    .name("i18n_permission_cloud_disk_attach")
                                    .role(RoleConstants.ROLE.ADMIN)
                                    .role(RoleConstants.ROLE.ORGADMIN)
                                    .role(RoleConstants.ROLE.USER))
                            .permission(new Permission.Builder()
                                    .require(OPERATE.READ)
                                    .operate(OPERATE.DETACH)
                                    .name("i18n_permission_cloud_disk_detach")
                                    .role(RoleConstants.ROLE.ADMIN)
                                    .role(RoleConstants.ROLE.ORGADMIN)
                                    .role(RoleConstants.ROLE.USER))
                            .permission(new Permission.Builder()
                                    .require(OPERATE.READ)
                                    .operate(OPERATE.DELETE)
                                    .name("i18n_permission_cloud_disk_delete")
                                    .role(RoleConstants.ROLE.ADMIN)
                                    .role(RoleConstants.ROLE.ORGADMIN)
                                    .role(RoleConstants.ROLE.USER))
                            .permission(new Permission.Builder()
                                    .require(OPERATE.READ)
                                    .operate(OPERATE.AUTH)
                                    .name("i18n_permission_cloud_disk_auth")
                                    .role(RoleConstants.ROLE.ADMIN)
                                    .role(RoleConstants.ROLE.ORGADMIN))
            )
            .group(
                    new PermissionGroup.Builder()
                            .id(GROUP.CLOUD_IMAGE)
                            .name("i18n_permission_cloud_images")
                            .permission(new Permission.Builder()
                                    .operate(OPERATE.READ)
                                    .name("i18n_permission_cloud_images_read")
                                    .role(RoleConstants.ROLE.ADMIN)
                                    .role(RoleConstants.ROLE.ORGADMIN)
                                    .role(RoleConstants.ROLE.USER))
            )
            .group(
                    new PermissionGroup.Builder()
                            .id(GROUP.JOBS)
                            .name("i18n_permission_jobs")
                            .permission(new Permission.Builder()
                                    .operate(OPERATE.READ)
                                    .name("i18n_permission_jobs_read")
                                    .role(RoleConstants.ROLE.ADMIN)
                                    .role(RoleConstants.ROLE.ORGADMIN)
                                    .role(RoleConstants.ROLE.USER))
            )
            //...
            ;


}
