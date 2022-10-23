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
                    .name("resource_manage")
                    .title("资源管理")
                    .path("/resource_manage")
                    .icon("yunzhuji2")
                    .order(1)
                    .requiredPermission(new MenuPermission.Builder()
                            .role(RoleConstants.ROLE.ADMIN)
                            .permission(GROUP.CLOUD_SERVER, OPERATE.READ)
                    )
                    .childMenu(new Menu.Builder()
                            .name("vm_cloud_server")
                            .title("云主机")
                            .path("/vm_cloud_server")
                            .icon("xuniji1")
                            .componentPath("/src/views/vm_cloud_server/index.vue")
                            .redirect("/resource_manage/vm_cloud_server/list")
                            .order(1)
                            .requiredPermission(new MenuPermission.Builder()
                                    .role(RoleConstants.ROLE.ADMIN)
                                    .permission(GROUP.CLOUD_SERVER, OPERATE.READ)
                            )
                            .requiredPermission(new MenuPermission.Builder()
                                    .role(RoleConstants.ROLE.ORGADMIN)
                                    .permission(GROUP.CLOUD_SERVER, OPERATE.READ)
                            )
                            .childOperationRoute(new Menu.Builder()
                                    .name("server_list")
                                    .title("列表")
                                    .path("/list")
                                    .componentPath("/src/views/vm_cloud_server/list.vue")
                                    .requiredPermission(new MenuPermission.Builder()
                                            .role(RoleConstants.ROLE.ADMIN)
                                            .permission(GROUP.CLOUD_SERVER, OPERATE.READ)
                                    )
                            )
                            .childOperationRoute(new Menu.Builder()
                                    .name("detail")
                                    .title("详情")
                                    .path("/detail")
                                    .componentPath("/src/views/vm_cloud_server/detail.vue")
                                    .requiredPermission(new MenuPermission.Builder()
                                            .role(RoleConstants.ROLE.ADMIN)
                                            .permission(GROUP.CLOUD_SERVER, OPERATE.READ)
                                    )
                            ).childOperationRoute(
                                    new Menu.Builder()
                                            .name("add_disk")
                                            .title("添加磁盘")
                                            .path("/add_disk:id")
                                            .componentPath("/src/views/VmCloudServer/AddDisk.vue")
                                            .requiredPermission(new MenuPermission.Builder()
                                                    .role(RoleConstants.ROLE.ADMIN)
                                                    .permission(GROUP.CLOUD_DISK, OPERATE.READ)
                                                    .permission(GROUP.CLOUD_DISK, OPERATE.EDIT)
                                            )
                            )
                    )
                    .childMenu(new Menu.Builder()
                            .name("vm_cloud_disk")
                            .title("磁盘")
                            .path("/vm_cloud_disk")
                            .icon("yuncunchu")
                            .componentPath("/src/views/vm_cloud_disk/index.vue")
                            .redirect("/resource_manage/vm_cloud_disk/list")
                            .order(2)
                            .requiredPermission(new MenuPermission.Builder()
                                    .role(RoleConstants.ROLE.ADMIN)
                                    .permission(GROUP.CLOUD_DISK, OPERATE.READ)
                            )
                            .requiredPermission(new MenuPermission.Builder()
                                    .role(RoleConstants.ROLE.ORGADMIN)
                                    .permission(GROUP.CLOUD_DISK, OPERATE.READ)
                            )
                            .childOperationRoute(new Menu.Builder()
                                    .name("disk_list")
                                    .title("磁盘列表")
                                    .path("/list")
                                    .componentPath("/src/views/vm_cloud_disk/list.vue")
                                    .requiredPermission(new MenuPermission.Builder()
                                            .role(RoleConstants.ROLE.ADMIN)
                                            .permission(GROUP.CLOUD_DISK, OPERATE.READ)

                                    )
                            ).childOperationRoute(
                                    new Menu.Builder()
                                            .name("detail")
                                            .title("详情")
                                            .path("/detail")
                                            .componentPath("/src/views/vm_cloud_disk/detail.vue")
                                            .requiredPermission(new MenuPermission.Builder()
                                                    .role(RoleConstants.ROLE.ADMIN)
                                                    .permission(GROUP.CLOUD_DISK, OPERATE.READ)
                                                    .permission(GROUP.CLOUD_DISK, OPERATE.EDIT)
                                            )
                            ).childOperationRoute(
                                    new Menu.Builder()
                                            .name("enlarge")
                                            .title("扩容")
                                            .path("/enlarge/:id")
                                            .componentPath("/src/views/VmCloudDisk/enlarge.vue")
                                            .requiredPermission(new MenuPermission.Builder()
                                                    .role(RoleConstants.ROLE.ADMIN)
                                                    .permission(GROUP.CLOUD_DISK, OPERATE.READ)
                                                    .permission(GROUP.CLOUD_DISK, OPERATE.EDIT)
                                            )
                            )
                    )
                    .childMenu(new Menu.Builder()
                            .name("vm_cloud_images")
                            .title("镜像")
                            .path("/vm_cloud_images")
                            .icon("jingxiang")
                            .componentPath("/src/views/vm_cloud_images/index.vue")
                            .redirect("/resource_manage/vm_cloud_images/list")
                            .order(2)
                            .requiredPermission(new MenuPermission.Builder()
                                    .role(RoleConstants.ROLE.ADMIN)
                                    .permission(GROUP.CLOUD_IMAGE, OPERATE.READ)
                            )
                            .requiredPermission(new MenuPermission.Builder()
                                    .role(RoleConstants.ROLE.ORGADMIN)
                                    .permission(GROUP.CLOUD_IMAGE, OPERATE.READ)
                            )
                            .childOperationRoute(new Menu.Builder()
                                    .name("image_list")
                                    .title("镜像列表")
                                    .path("/list")
                                    .componentPath("/src/views/vm_cloud_images/list.vue")
                                    .requiredPermission(new MenuPermission.Builder()
                                            .role(RoleConstants.ROLE.ADMIN)
                                            .permission(GROUP.CLOUD_IMAGE, OPERATE.READ)

                                    )
                            )
                            .childOperationRoute(
                                    new Menu.Builder()
                                            .name("detail")
                                            .title("详情")
                                            .path("/detail")
                                            .componentPath("/src/views/vm_cloud_images/detail.vue")
                                            .requiredPermission(new MenuPermission.Builder()
                                                    .role(RoleConstants.ROLE.ADMIN)
                                                    .permission(GROUP.CLOUD_IMAGE, OPERATE.READ)
                                                    .permission(GROUP.CLOUD_IMAGE, OPERATE.EDIT)
                                            )
                            )
                    )

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
                    .childMenu(new Menu.Builder()
                            .name("instance_type")
                            .title("实例规格")
                            .path("/instance_type")
                            .icon("")
                            .componentPath("/src/views/InstanceType/index.vue")
                            .redirect("/system_setting/instance_type/list")
                            .order(2)
                            .requiredPermission(new MenuPermission.Builder()
                                    .role(RoleConstants.ROLE.ADMIN)
                                    .permission(GROUP.INSTANCE_TYPE, OPERATE.READ)
                            )
                            .childOperationRoute(new Menu.Builder()
                                    .name("instance_type_list")
                                    .title("列表")
                                    .path("/list")
                                    .componentPath("/src/views/InstanceType/list.vue")
                                    .requiredPermission(new MenuPermission.Builder()
                                            .role(RoleConstants.ROLE.ADMIN)
                                            .permission(GROUP.INSTANCE_TYPE, OPERATE.READ)

                                    )
                            )
                    )

            )
            //...
            ;


}
