import { createWebHashHistory, createWebHistory } from "vue-router";
import Login from "@commons/business/login/index.vue";
import type { UnwrapRef } from "vue";
import type { Menu } from "@commons/api/menu";
import { useUserStore } from "@commons/stores/modules/user";
import { store } from "@commons/stores";
import BaseLayout from "@commons/business/base-layout/index.vue";
import { useModuleStore } from "@commons/stores/modules/module";
import type { Module } from "@commons/api/module";
import type { RouteModule } from "@commons/api/module/type";
import type { RouteItem } from "@commons/router/type";
import { RouteObj } from "@commons/router/type";
import AppContent from "@commons/components/layout/app-content/index.vue";
import { usePermissionStore } from "@commons/stores/modules/permission";

declare global {
  interface Window {
    eventCenterForAppNameVite: any;
    __MICRO_APP_NAME__: string;
    __MICRO_APP_ENVIRONMENT__: string;
    __MICRO_APP_BASE_APPLICATION__: string;
  }
}

/**
 * 扁平化菜单
 * @param menus
 * @param newMenus
 * @param prentPath
 * @returns 扁平化后的菜单
 */
export const flatMenu = (
  menus: Array<Menu> | undefined,
  newMenus: Array<Menu>
) => {
  menus?.forEach((item) => {
    const newMenu: Menu = {
      ...item,
      children: [],
    };
    newMenus.push(newMenu);
    if (item.children != null && item.children.length > 0) {
      flatMenu(item.children, newMenus);
    }
    if (item.operations != null && item.operations.length > 0) {
      flatMenu(item.operations, newMenus);
    }
  });
  return newMenus;
};

let route: RouteObj;

if (import.meta.env.VITE_APP_NAME === "base") {
  /**
   * base route
   */
  route = new RouteObj(
    createWebHistory(),
    {},
    [
      {
        path: "/",
        name: "home",
        component: BaseLayout,
      },
      {
        path: "/signin",
        name: "signin",
        component: Login,
      },
    ],
    () => {
      const moduleStore = useModuleStore(store);
      const userStore = useUserStore(store);

      console.log(userStore.isLogin);
      console.log(moduleStore.runningModules);

      const routers = moduleStore.runningModules.map(
        (module: UnwrapRef<Module>) => {
          const routeModule: RouteModule = {
            path: "/" + module.id,
            name: module.id,
            componentPath: "/src/views/MicroAppRouteView/index.vue",
            props: {
              moduleName: module.id,
              name: module.id,
              url: (module.basePath ? module.basePath : "") + "/",
              baseRoute: "/",
            },
          };
          return routeModule;
        }
      );
      console.log(routers);
      const route: RouteItem = { home: routers };
      return route;
    },
    undefined,
    async () => {
      // 处理新模块上来后,对模块的import路径进行重写,去掉项目名称
      await window.rootMicroApp.updateModule();
    }
  );
} else {
  /**
   * service route
   *
   */

  const baseRoute = window.__MICRO_APP_BASE_APPLICATION__
    ? [
        {
          path: "/",
          name: "home",
          component: AppContent,
        },
        {
          path: "/signin",
          name: "signin",
          component: Login,
        },
      ]
    : undefined;

  route = new RouteObj(
    createWebHashHistory(),
    {},
    baseRoute ? baseRoute : undefined,
    async () => {
      const moduleStore = useModuleStore(store);

      await moduleStore.refreshModules();
      console.log(moduleStore.currentModuleMenu);

      return moduleStore.currentModuleMenu;
    },
    async () => {
      const userStore = useUserStore(store);
      const permissionStore = usePermissionStore(store);
      await permissionStore.refreshPermissions();
      console.log(userStore.isLogin);
      console.log(permissionStore.userPermissions);
      return {
        permissions: permissionStore.userPermissions,
        role: userStore.currentRole,
      };
    }
  );
}

export default route;
