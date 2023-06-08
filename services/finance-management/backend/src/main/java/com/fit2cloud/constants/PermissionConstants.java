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
        public static final String BILL_ViEW = "BILL_ViEW";
        public static final String BILL_DETAILED = "BILL_DETAILED";

        public static final String CUSTOM_BILL = "CUSTOM_BILL";

        public static final String DIMENSION_SETTING = "DIMENSION_SETTING";

        public static final String BILLING_POLICY = "BILLING_POLICY";
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
                            .id(GROUP.BILL_ViEW)
                            .name("permission.bill.view.base")
                            .permission(
                                    new Permission.Builder()
                                            .operate(OPERATE.READ)
                                            .name("permission.bill.view.read")
                                            .role(RoleConstants.ROLE.ADMIN,
                                                    RoleConstants.ROLE.ORGADMIN,
                                                    RoleConstants.ROLE.USER)
                            )
            )
            .group(
                    new PermissionGroup.Builder()
                            .id(GROUP.BILL_DETAILED)
                            .name("permission.bill.detailed.base")
                            .permission(
                                    new Permission.Builder()
                                            .operate(OPERATE.READ)
                                            .name("permission.bill.detailed.read")
                                            .role(RoleConstants.ROLE.ADMIN,
                                                    RoleConstants.ROLE.ORGADMIN,
                                                    RoleConstants.ROLE.USER)
                            )
            )
            .group(
                    new PermissionGroup.Builder()
                            .id(GROUP.DIMENSION_SETTING)
                            .name("permission.bill.dimension_setting.base")
                            .permission(
                                    new Permission.Builder()
                                            .operate(OPERATE.READ)
                                            .name("permission.bill.dimension_setting.read")
                                            .role(RoleConstants.ROLE.ADMIN)
                            ).permission(new Permission.Builder()
                                    .operate(OPERATE.CREATE)
                                    .name("permission.bill.dimension_setting.create")
                                    .role(RoleConstants.ROLE.ADMIN))
                            .permission(new Permission.Builder()
                                    .operate(OPERATE.EDIT)
                                    .name("permission.bill.dimension_setting.edit")
                                    .role(RoleConstants.ROLE.ADMIN))
                            .permission(new Permission.Builder()
                                    .operate(OPERATE.DELETE)
                                    .name("permission.bill.dimension_setting.delete")
                                    .role(RoleConstants.ROLE.ADMIN))
            )
            .group(
                    new PermissionGroup.Builder()
                            .id(GROUP.CUSTOM_BILL)
                            .name("permission.bill.custom_bill.base")
                            .permission(
                                    new Permission.Builder()
                                            .operate(OPERATE.READ)
                                            .name("permission.bill.custom_bill.read")
                                            .role(RoleConstants.ROLE.ADMIN))
                            .permission(new Permission.Builder()
                                    .operate(OPERATE.CREATE)
                                    .name("permission.bill.dimension_setting.create")
                                    .role(RoleConstants.ROLE.ADMIN))
                            .permission(new Permission.Builder()
                                    .operate(OPERATE.EDIT)
                                    .name("permission.bill.dimension_setting.edit")
                                    .role(RoleConstants.ROLE.ADMIN))
                            .permission(new Permission.Builder()
                                    .operate(OPERATE.DELETE)
                                    .name("permission.bill.dimension_setting.delete")
                                    .role(RoleConstants.ROLE.ADMIN))
            )
            .group(
                    new PermissionGroup.Builder()
                            .id(GROUP.BILLING_POLICY)
                            .name("permission.bill.billing_policy.base")
                            .permission(
                                    new Permission.Builder()
                                            .operate(OPERATE.READ)
                                            .name("permission.bill.billing_policy.read")
                                            .role(RoleConstants.ROLE.ADMIN))
                            .permission(new Permission.Builder()
                                    .operate(OPERATE.CREATE)
                                    .name("permission.bill.billing_policy.create")
                                    .role(RoleConstants.ROLE.ADMIN))
                            .permission(new Permission.Builder()
                                    .operate(OPERATE.EDIT)
                                    .name("permission.bill.billing_policy.edit")
                                    .role(RoleConstants.ROLE.ADMIN))
                            .permission(new Permission.Builder()
                                    .operate(OPERATE.DELETE)
                                    .name("permission.bill.billing_policy.delete")
                                    .role(RoleConstants.ROLE.ADMIN))
            );


}
