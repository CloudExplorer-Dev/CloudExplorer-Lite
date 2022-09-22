import { createWebHashHistory, createWebHistory } from "vue-router";
import Login from "@commons/business/login/index.vue";
import type { UnwrapRef } from "vue";
import type { Menu } from "@commons/api/menu/type";
import { useUserStore } from "@commons/stores/modules/user";
import { store } from "@commons/stores";
import BaseLayout from "@commons/business/base-layout/index.vue";
import { useModuleStore } from "@commons/stores/modules/module";
import type { Module } from "@commons/api/module/type";
import type { RouteModule } from "@commons/api/module/type";
import type { RouteItem } from "@commons/router/type";
import { RouteObj } from "@commons/router/type";
//import AppContent from "@commons/components/layout/app-content/index.vue";
import { usePermissionStore } from "@commons/stores/modules/permission";

/**
 * 扁平化菜单
 * @param menus
 * @param newMenus
 * @param autoOperations
 * @returns 扁平化后的菜单
 */
export const flatMenu = (
  menus: Array<Menu> | undefined,
  newMenus: Array<Menu>,
  autoOperations = true
) => {
  menus?.forEach((item) => {
    const newMenu: Menu = {
      ...item,
      children: [],
    };
    newMenus.push(newMenu);
    if (item.children != null && item.children.length > 0) {
      flatMenu(item.children, newMenus, autoOperations);
    }
    if (
      item.operations != null &&
      item.operations.length > 0 &&
      autoOperations
    ) {
      flatMenu(item.operations, newMenus, autoOperations);
    }
  });
  return newMenus;
};

let route: RouteObj | null = null;

export async function initRouteObj(): Promise<RouteObj> {
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

        const routers = moduleStore.runningModules.map(
          (module: UnwrapRef<Module>) => {
            const routeModule: RouteModule = {
              path: "/" + module.id,
              name: module.id,
              componentPath: "/src/views/MicroAppRouteView/index.vue",
              props: {
                moduleName: module.id,
                name: module.id,
                url:
                  window.location.protocol +
                  "//" +
                  window.location.host +
                  (module.basePath ? module.basePath : ""),
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

    //需要按需加载，不然会找不到
    const AppContent = () =>
      import("@commons/components/layout/app-content/index.vue");

    route = new RouteObj(
      createWebHashHistory(),
      {},
      window.__MICRO_APP_BASE_APPLICATION__
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
        : undefined,
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
  return route;
}

export function clearRoute() {
  route = null;
}

export function getRoute() {
  return route;
}

const routeObj = {
  initRouteObj,
  clearRoute,
  getRoute,
};

export default routeObj;
