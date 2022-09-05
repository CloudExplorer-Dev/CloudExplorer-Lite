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
                            .name("user_manage")
                            .title("用户与租户")
                            .path("/user_manage")
                            .icon("yonghuguanli_huaban")
                            .order(1)
                            .childMenu(new Menu.Builder()
                                    .name("user")
                                    .title("用户管理")
                                    .path("/user")
                                    .icon("yonghuguanli_huaban")
                                    .componentPath("/src/views/UserManage/index.vue")
                                    .order(1)
                                    .requiredPermission(new MenuPermission.Builder()
                                            .role(RoleConstants.ROLE.ADMIN)
                                            .permission(GROUP.USER, OPERATE.READ)
                                    )
                                    .requiredPermission(new MenuPermission.Builder()
                                            .role(RoleConstants.ROLE.ORGADMIN)
                                            .permission(GROUP.USER, OPERATE.READ)
                                    )
                                    .operationRoute(new Menu.Builder()
                                            .name("create")
                                            .title("创建")
                                            .path("/create")
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
                                    .order(2)
                                    .requiredPermission(new MenuPermission.Builder()
                                            .role(RoleConstants.ROLE.ADMIN)
                                            .permission(GROUP.ROLE, OPERATE.READ)
                                    )
                            )
                    //...
            )
            .menu(new Menu.Builder()
                    .name("system_setting")
                    .title("系统设置")
                    .path("/system_setting")
                    .icon("xitongshezhi")
                    .order(2)
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
