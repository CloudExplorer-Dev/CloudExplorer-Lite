package com.fit2cloud.constants;

import com.fit2cloud.common.constants.RoleConstants;
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
                    .name("overview")
                    .title("总览")
                    .path("/overview")
                    .componentPath("/src/views/overview/index.vue")
                    .icon("yunyingzonglan")
                    .order(1)
                    .redirect("/overview/list")
                    .requiredPermission(new MenuPermission.Builder()
                            .role(RoleConstants.ROLE.ADMIN)
                            .permission(PermissionConstants.GROUP.OVERVIEW, PermissionConstants.OPERATE.READ)
                    )
                    .requiredPermission(new MenuPermission.Builder()
                            .role(RoleConstants.ROLE.ORGADMIN)
                            .permission(PermissionConstants.GROUP.OVERVIEW, PermissionConstants.OPERATE.READ)
                    )
                    .requiredPermission(new MenuPermission.Builder()
                            .role(RoleConstants.ROLE.USER)
                            .permission(PermissionConstants.GROUP.OVERVIEW, PermissionConstants.OPERATE.READ)
                    )
                    .childOperationRoute(new Menu.Builder()
                            .name("overview_list")
                            .path("/list")
                            .title("列表")
                            .componentPath("/src/views/overview/list.vue")
                            .requiredPermission(new MenuPermission.Builder()
                                    .role(RoleConstants.ROLE.ADMIN)
                                    .permission(PermissionConstants.GROUP.OVERVIEW, PermissionConstants.OPERATE.READ)
                            )
                    )

            )
            .menu(new Menu.Builder()
                    .name("resource_analysis")
                    .title("资源分析")
                    .path("/resource_analysis")
                    .icon("fenxi3")
                    .order(2)
                    .requiredPermission(new MenuPermission.Builder()
                            .role(RoleConstants.ROLE.ADMIN)
                            .permission(PermissionConstants.GROUP.RESOURCE_ANALYSIS, PermissionConstants.OPERATE.READ)
                    )
                    .requiredPermission(new MenuPermission.Builder()
                            .role(RoleConstants.ROLE.ORGADMIN)
                            .permission(PermissionConstants.GROUP.RESOURCE_ANALYSIS, PermissionConstants.OPERATE.READ)
                    )
                    .requiredPermission(new MenuPermission.Builder()
                            .role(RoleConstants.ROLE.USER)
                            .permission(PermissionConstants.GROUP.OVERVIEW, PermissionConstants.OPERATE.READ)
                    )
                    .childMenu(new Menu.Builder()
                            .name("base_resource_analysis")
                            .title("基础资源分析")
                            .path("/base_resource_analysis")
                            .icon("")
                            .componentPath("/src/views/base_resource_analysis/index.vue")
                            .redirect("/resource_analysis/base_resource_analysis/list")
                            .order(1)
                            .requiredPermission(new MenuPermission.Builder()
                                    .role(RoleConstants.ROLE.ADMIN)
                                    .permission(PermissionConstants.GROUP.RESOURCE_ANALYSIS, PermissionConstants.OPERATE.READ)
                            )
                            .childOperationRoute(new Menu.Builder()
                                    .name("list")
                                    .title("列表")
                                    .path("/list")
                                    .componentPath("/src/views/base_resource_analysis/list.vue")
                                    .requiredPermission(new MenuPermission.Builder()
                                            .role(RoleConstants.ROLE.ADMIN)
                                            .permission(PermissionConstants.GROUP.RESOURCE_ANALYSIS, PermissionConstants.OPERATE.READ)
                                    )
                            )

                    )

            );


}
