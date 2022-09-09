package com.fit2cloud.constants;

import com.fit2cloud.common.constants.RoleConstants;
import com.fit2cloud.constants.PermissionConstants.GROUP;
import com.fit2cloud.constants.PermissionConstants.OPERATE;
import com.fit2cloud.dto.module.Menu;
import com.fit2cloud.dto.module.MenuPermission;
import com.fit2cloud.dto.module.Menus;
import com.fit2cloud.service.MenuService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Component
public class MenuConstants {

    public static List<Menu> MENUS;

    @Resource
    private MenuService menuService;

    @Value("${spring.application.name}")
    public void setModule(String module) {

        MENUS = MENUS_BUILDER.module(module).build().getMenus();

        //推送到redis
        menuService.init(module, MENUS);

    }

    private static final Menus.Builder MENUS_BUILDER = new Menus.Builder()
            .menu(new Menu.Builder()
                    .name("user_tenant")
                    .title("用户与租户")
                    .path("/user_tenant")
                    .icon("yonghuguanli_huaban")
                    .order(1)
                    .childMenu(new Menu.Builder()
                            .name("user")
                            .title("用户管理")
                            .path("/user")
                            .icon("yonghuguanli_huaban")
                            .componentPath("/src/views/UserManage/index.vue")
                            .redirect("/user_tenant/user/list")
                            .order(1)
                            .requiredPermission(new MenuPermission.Builder()
                                    .role(RoleConstants.ROLE.ADMIN)
                                    .permission(GROUP.USER, OPERATE.READ)
                            )
                            .requiredPermission(new MenuPermission.Builder()
                                    .role(RoleConstants.ROLE.ORGADMIN)
                                    .permission(GROUP.USER, OPERATE.READ)
                            )
                            .childOperationRoute(new Menu.Builder()
                                    .name("list")
                                    .title("列表")
                                    .path("/list")
                                    .componentPath("/src/views/UserManage/list.vue")
                                    .requiredPermission(new MenuPermission.Builder()
                                            .role(RoleConstants.ROLE.ADMIN)
                                            .permission(GROUP.USER, OPERATE.CREATE)
                                    )
                            )
                            .childOperationRoute(new Menu.Builder()
                                    .name("create")
                                    .title("创建")
                                    .path("/create")
                                    .componentPath("/src/views/UserManage/create.vue")
                                    .requiredPermission(new MenuPermission.Builder()
                                            .role(RoleConstants.ROLE.ADMIN)
                                            .permission(GROUP.USER, OPERATE.CREATE)
                                    )
                            )
                            .childOperationRoute(new Menu.Builder()
                                    .name("update")
                                    .title("修改")
                                    .path("/update")
                                    .componentPath("/src/views/UserManage/create.vue")
                                    .requiredPermission(new MenuPermission.Builder()
                                            .role(RoleConstants.ROLE.ADMIN)
                                            .permission(GROUP.USER, OPERATE.CREATE)
                                    )
                            )
                    )
                    .childMenu(new Menu.Builder()
                                    .name("role")
                                    .title("角色管理")
                                    .path("/role")
                                    .icon("jurassic_user")
                                    .componentPath("/src/views/RoleManage/index.vue")
                                    .redirect("/user_tenant/role/list")
                                    .order(2)
                                    .requiredPermission(new MenuPermission.Builder()
                                            .role(RoleConstants.ROLE.ADMIN)
                                            .permission(GROUP.ROLE, OPERATE.READ)
                                    ).childOperationRoute(new Menu.Builder()
                                            .name("role_list")
                                            .title("列表")
                                            .path("/list")
                                            .componentPath("/src/views/RoleManage/list.vue")
                                            .requiredPermission(new MenuPermission.Builder()
                                                    .role(RoleConstants.ROLE.ADMIN)
                                                    .permission(GROUP.ROLE, OPERATE.READ)

                                            )
                                    ).childOperationRoute(new Menu.Builder()
                                            .name("role_create")
                                            .title("创建")
                                            .path("/create")
                                            .componentPath("/src/views/RoleManage/create.vue")
                                            .requiredPermission(new MenuPermission.Builder()
                                                    .role(RoleConstants.ROLE.ADMIN)
                                                    .permission(GROUP.ROLE, OPERATE.CREATE)
                                            )
                                    ).childOperationRoute(
                                            new Menu.Builder()
                                                    .name("role_update")
                                                    .title("修改")
                                                    .path("/update")
                                                    .componentPath("/src/views/RoleManage/update.vue")
                                                    .requiredPermission(new MenuPermission.Builder()
                                                            .role(RoleConstants.ROLE.ADMIN)
                                                            .permission(GROUP.ROLE, OPERATE.EDIT)
                                                    )
                                    )
                            // 工作空间相关菜单
                    ).childMenu(new Menu.Builder()
                                    .name("workspace")
                                    .title("工作空间")
                                    .path("/workspace")
                                    .icon("project_space")
                                    .componentPath("/src/views/WorkspaceManage/index.vue")
                                    .redirect("/user_tenant/workspace/list")
                                    .order(2)
                                    .requiredPermission(new MenuPermission.Builder()
                                            .role(RoleConstants.ROLE.ADMIN)
                                            .permission(GROUP.WORKSPACE, OPERATE.READ)
                                    ).childOperationRoute(new Menu.Builder()
                                            .name("workspace_list")
                                            .title("列表")
                                            .path("/list")
                                            .componentPath("/src/views/WorkspaceManage/list.vue")
                                            .requiredPermission(new MenuPermission.Builder()
                                                    .role(RoleConstants.ROLE.ADMIN)
                                                    .permission(GROUP.WORKSPACE, OPERATE.READ)

                                            )
                                    ).childOperationRoute(new Menu.Builder()
                                            .name("workspace_create")
                                            .title("创建")
                                            .path("/create")
                                            .componentPath("/src/views/WorkspaceManage/create.vue")
                                            .requiredPermission(new MenuPermission.Builder()
                                                    .role(RoleConstants.ROLE.ADMIN)
                                                    .permission(GROUP.WORKSPACE, OPERATE.CREATE)
                                            )
                                    ).childOperationRoute(new Menu.Builder()
                                            .name("workspace_update")
                                            .title("编辑")
                                            .path("/update")
                                            .componentPath("/src/views/WorkspaceManage/create.vue")
                                            .requiredPermission(new MenuPermission.Builder()
                                                    .role(RoleConstants.ROLE.ADMIN)
                                                    .permission(GROUP.WORKSPACE, OPERATE.EDIT)
                                            )
                                    )

                            // 组织相关菜单
                    ).childMenu(new Menu.Builder()
                                    .name("org")
                                    .title("组织管理")
                                    .path("/org")
                                    .icon("zuzhijiagou")
                                    .componentPath("/src/views/OrgManage/index.vue")
                                    .redirect("/user_tenant/org/list")
                                    .order(3)
                                    .requiredPermission(new MenuPermission.Builder()
                                            .role(RoleConstants.ROLE.ADMIN)
                                            .permission(GROUP.ORGANIZATION, OPERATE.READ)
                                    ).childOperationRoute(new Menu.Builder()
                                            .name("org_list")
                                            .title("列表")
                                            .path("/list")
                                            .componentPath("/src/views/OrgManage/list.vue")
                                            .requiredPermission(new MenuPermission.Builder()
                                                    .role(RoleConstants.ROLE.ADMIN)
                                                    .permission(GROUP.ORGANIZATION, OPERATE.READ)

                                            )
                                    ).childOperationRoute(new Menu.Builder()
                                            .name("org_create")
                                            .title("创建")
                                            .path("/create")
                                            .componentPath("/src/views/OrgManage/create.vue")
                                            .requiredPermission(new MenuPermission.Builder()
                                                    .role(RoleConstants.ROLE.ADMIN)
                                                    .permission(GROUP.ORGANIZATION, OPERATE.CREATE)
                                            )
                                    ).childOperationRoute(
                                            new Menu.Builder()
                                                    .name("org_update")
                                                    .title("修改")
                                                    .path("/update")
                                                    .componentPath("/src/views/OrgManage/update.vue")
                                                    .requiredPermission(new MenuPermission.Builder()
                                                            .role(RoleConstants.ROLE.ADMIN)
                                                            .permission(GROUP.ORGANIZATION, OPERATE.EDIT)
                                                    )
                                    )
                            //...
                    )

            )
            // 云账号相关菜单
            .menu(
                    new Menu.Builder()
                            .name("cloud_account")
                            .title("云账号")
                            .path("/cloud_account")
                            .componentPath("/src/views/CloudAccount/index.vue").
                            icon("Cloudy")
                            .order(2).redirect("/cloud_account/list")
                            .childOperationRoute(new Menu.Builder()
                                    .name("cloud_account_list")
                                    .path("/list")
                                    .title("列表")
                                    .componentPath("/src/views/CloudAccount/list.vue")
                                    .requiredPermission(new MenuPermission.Builder()
                                            .role(RoleConstants.ROLE.ADMIN)
                                            .permission(GROUP.CLOUD_ACCOUNT, OPERATE.CREATE)
                                    )
                            ).childOperationRoute(new Menu.Builder()
                                    .name("cloud_account_create")
                                    .path("/create")
                                    .title("创建")
                                    .componentPath("/src/views/CloudAccount/create.vue")
                                    .requiredPermission(new MenuPermission.Builder()
                                            .role(RoleConstants.ROLE.ADMIN)
                                            .permission(GROUP.CLOUD_ACCOUNT, OPERATE.CREATE)
                                    ))
                            .childOperationRoute(new Menu.Builder()
                                    .name("cloud_account_update")
                                    .path("/update")
                                    .title("修改")
                                    .componentPath("/src/views/CloudAccount/update.vue")
                                    .requiredPermission(new MenuPermission.Builder()
                                            .role(RoleConstants.ROLE.ADMIN)
                                            .permission(GROUP.CLOUD_ACCOUNT, OPERATE.EDIT)
                                    ))
            )

            .menu(new Menu.Builder()
                    .name("system_setting")
                    .title("系统设置")
                    .path("/system_setting")
                    .icon("xitongshezhi")
                    .order(2)
                    .requiredPermission(new MenuPermission.Builder()
                            .role(RoleConstants.ROLE.ADMIN)
                            .permission(GROUP.SYSTEM_SETTING, OPERATE.READ)
                    )
                    .childMenu(
                            new Menu.Builder()
                                    .name("setting")
                                    .title("系统设置")
                                    .path("/setting")
                                    .icon("shezhi")
                                    .componentPath("/src/views/SystemModule/index.vue")
                                    .order(2)
                                    .requiredPermission(new MenuPermission.Builder()
                                            .role(RoleConstants.ROLE.ADMIN)
                                            .permission(GROUP.SYSTEM_SETTING, OPERATE.READ)
                                    )
                    )

            )
            //...
            ;


}
