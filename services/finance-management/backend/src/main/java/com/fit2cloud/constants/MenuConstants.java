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
            .menu(
                    new Menu.Builder()
                            .name("bill_view")
                            .title("账单总览")
                            .path("/bill_view")
                            .componentPath("/src/views/bill_view/index.vue")
                            .icon("zichanzonglan")
                            .order(1)
                            .requiredPermission(new MenuPermission.Builder()
                                    .role(RoleConstants.ROLE.ADMIN)
                                    .permission(GROUP.BILL_ViEW, OPERATE.READ))
                            .requiredPermission(new MenuPermission.Builder()
                                    .role(RoleConstants.ROLE.ORGADMIN)
                                    .permission(GROUP.BILL_ViEW, OPERATE.READ))
                            .requiredPermission(new MenuPermission.Builder()
                                    .role(RoleConstants.ROLE.USER)
                                    .permission(GROUP.BILL_ViEW, OPERATE.READ))

            )
            .menu(
                    new Menu.Builder()
                            .name("bill_detailed")
                            .title("账单明细")
                            .componentPath("/src/views/bill_detailed/index.vue")
                            .path("/bill_detailed")
                            .icon("wj-zd")
                            .order(2)
                            .requiredPermission(new MenuPermission.Builder()
                                    .role(RoleConstants.ROLE.ADMIN)
                                    .permission(GROUP.BILL_DETAILED, OPERATE.READ))
                            .requiredPermission(new MenuPermission.Builder()
                                    .role(RoleConstants.ROLE.ORGADMIN)
                                    .permission(GROUP.BILL_DETAILED, OPERATE.READ))
                            .requiredPermission(new MenuPermission.Builder()
                                    .role(RoleConstants.ROLE.USER)
                                    .permission(GROUP.BILL_DETAILED, OPERATE.READ))

            )
            .menu(
                    new Menu.Builder()
                            .name("custom_bill")
                            .title("自定义账单")
                            .componentPath("/src/views/custom_bill/index.vue")
                            .path("/custom_bill")
                            .icon("zidingyibaobiao")
                            .order(3)
                            .requiredPermission(new MenuPermission.Builder()
                                    .role(RoleConstants.ROLE.ADMIN)
                                    .permission(GROUP.CUSTOM_BILL, OPERATE.READ))
            )
            .menu(
                    new Menu.Builder()
                            .name("dimension_setting")
                            .title("分账设置")
                            .componentPath("/src/views/dimension_setting/index.vue")
                            .path("/dimension_setting")
                            .icon("zidingyipeizhi")
                            .order(4)
                            .requiredPermission(new MenuPermission.Builder()
                                    .role(RoleConstants.ROLE.ADMIN)
                                    .permission(GROUP.DIMENSION_SETTING, OPERATE.READ))
            )
            //...
            ;


}
