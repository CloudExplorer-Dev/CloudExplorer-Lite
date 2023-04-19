import { createWebHistory } from "vue-router";
import Login from "@commons/business/login/index.vue";
import type { Menu } from "@commons/api/menu/type";
import { useUserStore } from "@commons/stores/modules/user";
import { store } from "@commons/stores";
import BaseLayout from "@commons/business/base-layout/index.vue";
import { useModuleStore } from "@commons/stores/modules/module";
import type { RouteItem } from "@commons/router/type";
import { RouteObj } from "@commons/router/type";
import { usePermissionStore } from "@commons/stores/modules/permission";
import microApp from "@micro-zoe/micro-app";
microApp.router.afterEach((module) => {
  if (import.meta.env.VITE_APP_NAME === "base") {
    const m = useModuleStore();
    m.updateCurrentModuleName(module.name);
  }
});
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
  autoOperations = true,
  sourceMenu?: string,
  childrenOperations?: boolean,
  parentTitle?: string
) => {
  menus?.forEach((item) => {
    const newMenu: Menu = {
      ...item,
      sourceMenu: sourceMenu ? sourceMenu : item.path,
      children: [],
      parentTitle: parentTitle,
    };
    newMenus.push(newMenu);
    if (item.children != null && item.children.length > 0) {
      flatMenu(
        item.children,
        newMenus,
        autoOperations,
        undefined,
        childrenOperations,
        item.title
      );
    }
    if (
      item.operations != null &&
      item.operations.length > 0 &&
      autoOperations
    ) {
      flatMenu(
        item.operations,
        newMenus,
        autoOperations,
        item.path,
        childrenOperations,
        item.title
      );
    }
    if (
      item.childOperations != null &&
      item.childOperations.length > 0 &&
      childrenOperations
    ) {
      flatMenu(
        item.childOperations,
        newMenus,
        autoOperations,
        item.path,
        childrenOperations,
        item.title
      );
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
      async () => {
        const moduleStore = useModuleStore(store);
        if (!moduleStore.modules) {
          await moduleStore.refreshModules("route base");
        }
        const route: RouteItem = { home: moduleStore.routeModules };
        return route;
      },
      async () => {
        const userStore = useUserStore(store);
        const permissionStore = usePermissionStore(store);
        if (!permissionStore.permissions) {
          await permissionStore.refreshPermissions();
        }
        return {
          permissions: permissionStore.userPermissions,
          role: userStore.currentRole,
        };
      },
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
      createWebHistory(import.meta.env.VITE_APP_NAME),
      {},
      window.__MICRO_APP_ENVIRONMENT__
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
        if (!moduleStore.modules) {
          await moduleStore.refreshModules("route");
        }

        return moduleStore.currentModuleMenu;
      },
      async () => {
        const userStore = useUserStore(store);
        const permissionStore = usePermissionStore(store);
        if (!permissionStore.permissions) {
          await permissionStore.refreshPermissions();
        }
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
