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

        public static final String CLOUD_SERVER = "CLOUD_SERVER";

        public static final String CLOUD_DISK = "CLOUD_DISK";

        public static final String CLOUD_IMAGE = "CLOUD_IMAGE";

        public static final String INSTANCE_TYPE = "INSTANCE_TYPE";

        public static final String JOBS = "JOBS";

        public static final String RECYCLE_BIN = "RECYCLE_BIN";

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
        public static final String RECOVER = "RECOVER";
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
                            .name("permission.vm.cloud_server.base")
                            .permission(new Permission.Builder()
                                    .operate(OPERATE.READ)
                                    .name("permission.vm.cloud_server.read")
                                    .role(RoleConstants.ROLE.ADMIN)
                                    .role(RoleConstants.ROLE.ORGADMIN)
                                    .role(RoleConstants.ROLE.USER))
                            .permission(new Permission.Builder()
                                    .require(OPERATE.READ)
                                    .operate(OPERATE.CREATE)
                                    .name("permission.vm.cloud_server.create")
                                    .role(RoleConstants.ROLE.ADMIN)
                                    .role(RoleConstants.ROLE.ORGADMIN)
                                    .role(RoleConstants.ROLE.USER))
                            .permission(new Permission.Builder()
                                    .require(OPERATE.READ)
                                    .operate(OPERATE.EDIT)
                                    .name("permission.vm.cloud_server.edit")
                                    .role(RoleConstants.ROLE.ADMIN)
                                    .role(RoleConstants.ROLE.ORGADMIN)
                                    .role(RoleConstants.ROLE.USER))
                            .permission(new Permission.Builder()
                                    .require(OPERATE.READ)
                                    .operate(OPERATE.DELETE)
                                    .name("permission.vm.cloud_server.delete")
                                    .role(RoleConstants.ROLE.ADMIN)
                                    .role(RoleConstants.ROLE.ORGADMIN)
                                    .role(RoleConstants.ROLE.USER))
                            .permission(new Permission.Builder()
                                    .require(OPERATE.READ)
                                    .operate(OPERATE.START)
                                    .name("permission.vm.cloud_server.start")
                                    .role(RoleConstants.ROLE.ADMIN)
                                    .role(RoleConstants.ROLE.ORGADMIN)
                                    .role(RoleConstants.ROLE.USER))
                            .permission(new Permission.Builder()
                                    .require(OPERATE.READ)
                                    .operate(OPERATE.STOP)
                                    .name("permission.vm.cloud_server.stop")
                                    .role(RoleConstants.ROLE.ADMIN)
                                    .role(RoleConstants.ROLE.ORGADMIN)
                                    .role(RoleConstants.ROLE.USER))
                            .permission(new Permission.Builder()
                                    .require(OPERATE.READ)
                                    .operate(OPERATE.RESTART)
                                    .name("permission.vm.cloud_server.restart")
                                    .role(RoleConstants.ROLE.ADMIN)
                                    .role(RoleConstants.ROLE.ORGADMIN)
                                    .role(RoleConstants.ROLE.USER))
                            .permission(new Permission.Builder()
                                    .require(OPERATE.READ)
                                    .operate(OPERATE.RESIZE)
                                    .name("permission.vm.cloud_server.resize")
                                    .role(RoleConstants.ROLE.ADMIN)
                                    .role(RoleConstants.ROLE.ORGADMIN)
                                    .role(RoleConstants.ROLE.USER))
                            .permission(new Permission.Builder()
                                    .require(OPERATE.READ)
                                    .operate(OPERATE.AUTH)
                                    .name("permission.vm.cloud_server.auth")
                                    .role(RoleConstants.ROLE.ADMIN)
                                    .role(RoleConstants.ROLE.ORGADMIN))

            )
            .group(
                    new PermissionGroup.Builder()
                            .id(GROUP.CLOUD_DISK)
                            .name("permission.vm.cloud_disk.base")
                            .permission(new Permission.Builder()
                                    .operate(OPERATE.READ)
                                    .name("permission.vm.cloud_disk.read")
                                    .role(RoleConstants.ROLE.ADMIN)
                                    .role(RoleConstants.ROLE.ORGADMIN)
                                    .role(RoleConstants.ROLE.USER))
                            .permission(new Permission.Builder()
                                    .require(OPERATE.READ)
                                    .operate(OPERATE.CREATE)
                                    .name("permission.vm.cloud_disk.create")
                                    .role(RoleConstants.ROLE.ADMIN)
                                    .role(RoleConstants.ROLE.ORGADMIN)
                                    .role(RoleConstants.ROLE.USER))
                            .permission(new Permission.Builder()
                                    .require(OPERATE.READ)
                                    .operate(OPERATE.EDIT)
                                    .name("permission.vm.cloud_disk.edit")
                                    .role(RoleConstants.ROLE.ADMIN)
                                    .role(RoleConstants.ROLE.ORGADMIN)
                                    .role(RoleConstants.ROLE.USER))
                            .permission(new Permission.Builder()
                                    .require(OPERATE.READ)
                                    .operate(OPERATE.DELETE)
                                    .name("permission.vm.cloud_disk.delete")
                                    .role(RoleConstants.ROLE.ADMIN)
                                    .role(RoleConstants.ROLE.ORGADMIN)
                                    .role(RoleConstants.ROLE.USER))
                            .permission(new Permission.Builder()
                                    .require(OPERATE.READ)
                                    .operate(OPERATE.RESIZE)
                                    .name("permission.vm.cloud_disk.resize")
                                    .role(RoleConstants.ROLE.ADMIN)
                                    .role(RoleConstants.ROLE.ORGADMIN)
                                    .role(RoleConstants.ROLE.USER))
                            .permission(new Permission.Builder()
                                    .require(OPERATE.READ)
                                    .operate(OPERATE.ATTACH)
                                    .name("permission.vm.cloud_disk.attach")
                                    .role(RoleConstants.ROLE.ADMIN)
                                    .role(RoleConstants.ROLE.ORGADMIN)
                                    .role(RoleConstants.ROLE.USER))
                            .permission(new Permission.Builder()
                                    .require(OPERATE.READ)
                                    .operate(OPERATE.DETACH)
                                    .name("permission.vm.cloud_disk.detach")
                                    .role(RoleConstants.ROLE.ADMIN)
                                    .role(RoleConstants.ROLE.ORGADMIN)
                                    .role(RoleConstants.ROLE.USER))
                            .permission(new Permission.Builder()
                                    .require(OPERATE.READ)
                                    .operate(OPERATE.AUTH)
                                    .name("permission.vm.cloud_disk.auth")
                                    .role(RoleConstants.ROLE.ADMIN)
                                    .role(RoleConstants.ROLE.ORGADMIN))
            )
// 目前不展示镜像列表，暂时屏蔽其权限展示
//            .group(
//                    new PermissionGroup.Builder()
//                            .id(GROUP.CLOUD_IMAGE)
//                            .name("permission.vm.cloud_image.base")
//                            .permission(new Permission.Builder()
//                                    .operate(OPERATE.READ)
//                                    .name("permission.vm.cloud_image.read")
//                                    .role(RoleConstants.ROLE.ADMIN)
//                                    .role(RoleConstants.ROLE.ORGADMIN)
//                                    .role(RoleConstants.ROLE.USER))
//            )
            .group(
                    new PermissionGroup.Builder()
                            .id(GROUP.JOBS)
                            .name("permission.vm.jobs.base")
                            .permission(new Permission.Builder()
                                    .operate(OPERATE.READ)
                                    .name("permission.vm.jobs.read")
                                    .role(RoleConstants.ROLE.ADMIN)
                                    .role(RoleConstants.ROLE.ORGADMIN)
                                    .role(RoleConstants.ROLE.USER))
            )
            .group(
                    new PermissionGroup.Builder()
                            .id(GROUP.RECYCLE_BIN)
                            .name("permission.vm.recycle_bin.base")
                            .permission(new Permission.Builder()
                                    .operate(OPERATE.READ)
                                    .name("permission.vm.recycle_bin.read")
                                    .role(RoleConstants.ROLE.ADMIN)
                                    .role(RoleConstants.ROLE.ORGADMIN)
                                    .role(RoleConstants.ROLE.USER))
                            .permission(new Permission.Builder()
                                    .require(OPERATE.READ)
                                    .operate(OPERATE.DELETE)
                                    .name("permission.vm.recycle_bin.delete")
                                    .role(RoleConstants.ROLE.ADMIN)
                                    .role(RoleConstants.ROLE.ORGADMIN)
                                    .role(RoleConstants.ROLE.USER))
                            .permission(new Permission.Builder()
                                    .require(OPERATE.READ)
                                    .operate(OPERATE.RECOVER)
                                    .name("permission.vm.recycle_bin.recover")
                                    .role(RoleConstants.ROLE.ADMIN)
                                    .role(RoleConstants.ROLE.ORGADMIN)
                                    .role(RoleConstants.ROLE.USER))
            )
            //...
            ;


}
