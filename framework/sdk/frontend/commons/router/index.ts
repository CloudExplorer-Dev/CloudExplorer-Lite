import type {
  NavigationFailure,
  NavigationGuardNext,
  RouteLocationNormalized,
  RouteLocationNormalizedLoaded,
  Router,
  RouteRecordRaw,
  RouterHistory,
} from "vue-router";
import { createRouter } from "vue-router";
import Layout from "@commons/business/app-layout/index.vue";
import Login from "@commons/business/login/index.vue";
import { getToken } from "@commons/utils/auth";
import type { App } from "vue";
import type { Menu, RequiredPermissions } from "@commons/api/menu";
import type { Permission, RolePermission } from "@commons/api/permission/type";
import noPermissions from "@commons/business/err-page/noPermissions.vue";

declare global {
  interface Window {
    eventCenterForAppNameVite: any;
    __MICRO_APP_NAME__: string;
    __MICRO_APP_ENVIRONMENT__: string;
    __MICRO_APP_BASE_APPLICATION__: string;
  }
}

interface RouteItem {
  /**
   *如果key是rootRoute,则是添加root级别路由,如果是其他,如home 则是给home路由添加子路由
   */
  [propName: string]: Array<RouteRecordRaw | any>;
}

interface RouteComponents {
  /**
   *如果key是rootRoute,则是添加root级别路由,如果是其他,如home 则是给home路由添加子路由
   */
  [propName: string]: any;
}
const rootRouteName = "rootRoute";
/**
 * 扁平化菜单
 * @param menus
 * @param newMenus
 * @param prentPath
 * @returns 扁平化后的菜单
 */
export const flatMenu = (
  menus: Array<Menu>,
  newMenus: Array<Menu>,
  prentPath?: string
) => {
  menus.forEach((item) => {
    const newMenu: Menu = {
      ...item,
      children: [],
      path: prentPath ? prentPath + item.path : item.path,
    };
    newMenus.push(newMenu);
    if (item.children != null && item.children.length > 0) {
      flatMenu(item.children, newMenus, prentPath ? item.path : prentPath);
    }
    if (item.operations != null && item.operations.length > 0) {
      flatMenu(item.operations, newMenus, prentPath ? item.path : prentPath);
    }
  });
  return newMenus;
};
/**
 * 判断是否有角色或者权限
 * @param menu         菜单
 * @param role         角色
 * @param permissions  权限
 * @returns
 */
export const hasRolePermission = (
  menu: Menu,
  role: string,
  permissions: Array<Permission>
) => {
  const requiredPermissions: Array<RequiredPermissions> =
    menu.requiredPermissions;
  if (!menu.requiredPermissions || requiredPermissions.length === 0) {
    return true;
  }
  for (let i = 0; i < requiredPermissions.length; i++) {
    const roleOk = requiredPermissions[i].role === role;
    const permissionOk = permissions.some((item) => {
      if (
        requiredPermissions[i].permissions ||
        requiredPermissions[i].permissions.length > 0
      ) {
        return requiredPermissions[i].permissions.includes(item.id);
      }
      return true;
    });
    if (requiredPermissions[i].logical === "OR") {
      return roleOk || permissionOk;
    } else {
      return roleOk && permissionOk;
    }
  }
};
class RouteObj {
  /**
   * 路由模式
   */
  history: RouterHistory;
  /**
   *路由对象
   */
  router: Router;
  /**
   * 获取动态路由函数
   */
  dynamicRoute: () =>
    | RouteItem
    | Promise<RouteItem>
    | Array<Menu>
    | Promise<Array<Menu>>;
  getPermissions?: () => RolePermission | Promise<RolePermission>;
  beforeEachAppend?: (
    to: RouteLocationNormalized,
    from: RouteLocationNormalized,
    next: NavigationGuardNext
  ) => void;
  install: (app: App) => void;
  routeComponent: RouteComponents;
  afterEachAppend?: (
    to: RouteLocationNormalized,
    from: RouteLocationNormalized,
    failure?: NavigationFailure | void
  ) => void;
  constructor(
    history: RouterHistory,
    routeComponent: RouteComponents,
    baseRoute?: Array<RouteRecordRaw>,
    dynamicRoute?: () =>
      | RouteItem
      | Promise<RouteItem>
      | Array<Menu>
      | Promise<Array<Menu>>,
    getPermissions?: () => RolePermission | Promise<RolePermission>,
    beforeEachAppend?: (
      to: RouteLocationNormalized,
      from: RouteLocationNormalized,
      next: NavigationGuardNext
    ) => void,
    afterEachAppend?: (
      to: RouteLocationNormalized,
      from: RouteLocationNormalized,
      failure?: NavigationFailure | void
    ) => void,
    beforeEach?: (
      to: RouteLocationNormalized,
      from: RouteLocationNormalized,
      next: NavigationGuardNext
    ) => void,
    afterEach?: (
      to: RouteLocationNormalized,
      from: RouteLocationNormalized,
      failure?: NavigationFailure | void
    ) => void,
    onError?: (
      error: any,
      to: RouteLocationNormalized,
      from: RouteLocationNormalizedLoaded
    ) => void
  ) {
    this.history = history;
    this.dynamicRoute = dynamicRoute ? dynamicRoute : this.defaultDynamicRoute;
    this.router = createRouter({ history, routes: [] });
    this.beforeEachAppend = beforeEachAppend;
    this.afterEachAppend = afterEachAppend;
    this.getPermissions = getPermissions;
    this.initBaseRoute(
      this.router,
      baseRoute ? baseRoute : this.defaultBaseRoute(),
      beforeEach ? beforeEach.bind(this) : this.defaultBeforeEach,
      afterEach ? afterEach : this.defaultAfterEach,
      onError ? onError : this.defaultOnError
    );
    this.install = this.router.install;
    this.routeComponent = routeComponent;
  }
  /**
   * 初始化基本路由
   * @param router
   * @param baseRoute
   */
  initBaseRoute = (
    router: Router,
    baseRoute: Array<RouteRecordRaw>,
    beforeEach: (
      to: RouteLocationNormalized,
      from: RouteLocationNormalized,
      next: NavigationGuardNext
    ) => void,
    afterEach: (
      to: RouteLocationNormalized,
      from: RouteLocationNormalized,
      failure?: NavigationFailure | void
    ) => void,
    onError: (
      error: any,
      to: RouteLocationNormalized,
      from: RouteLocationNormalizedLoaded
    ) => void
  ) => {
    // 初始化基本路由
    this.resetRoute(router, { [rootRouteName]: baseRoute });
    // 初始化前置守卫
    this.router.beforeEach(beforeEach);
    // 初始化后置守卫
    this.router.afterEach(afterEach);
    // 初始化错误
    this.router.onError(onError);
  };

  /**
   *
   * @param router        vue 路由对象
   * @param getRouteItem  获取路由对象的函数
   * @returns             是否重写了路由
   */
  resetRoute = (router: Router, routeItem: RouteItem) => {
    let isResetRoute = false;
    const routeItemKeys = Object.keys(routeItem);
    // 排序
    routeItemKeys.sort((key1: string) => {
      if (key1 === rootRouteName) {
        return -1;
      }
      return 1;
    });
    // 添加路由
    routeItemKeys.forEach((key: string) => {
      const routeRecordRaws: Array<RouteRecordRaw | any> = routeItem[key];
      routeRecordRaws.forEach((routeRecordRaw) => {
        if (routeRecordRaw.name && !router.hasRoute(routeRecordRaw.name)) {
          isResetRoute = true;
          if (!routeRecordRaw.component) {
            routeRecordRaw.component =
              this.routeComponent[routeRecordRaw.componentPath];
          }
          if (key === rootRouteName) {
            router.addRoute(routeRecordRaw);
          } else {
            router.addRoute(key, routeRecordRaw);
          }
        }
      });
    });
    return isResetRoute;
  };

  defaultDynamicRoute: () => RouteItem | any | Promise<RouteItem | any> =
    () => {
      return {
        home: [
          {
            name: "test",
            path: "/test",
            componentPath: "/src/views/Test/index.vue",
          },
        ],
      };
    };
  /**
   * 默认路由
   * @returns
   */
  defaultBaseRoute: () => Array<RouteRecordRaw> = () => {
    return [
      {
        path: "/",
        name: "home",
        component: Layout,
        children: [
          {
            path: "/noPermission",
            name: "noPermission",
            component: noPermissions,
          },
        ],
      },
      {
        path: "/login",
        name: "login",
        component: Login,
      },
    ];
  };

  /**
   *默认的路由前置守卫
   */
  defaultBeforeEach = async (
    to: RouteLocationNormalized,
    from: RouteLocationNormalized,
    next: NavigationGuardNext
  ) => {
    if (!getToken()) {
      if (to.name !== "login") {
        next({ name: "login" });
      } else {
        next();
      }
      return;
    }
    if (this.beforeEachAppend) {
      await this.beforeEachAppend(to, from, next);
    }
    /**
     * 获取动态路由
     */
    let dynamicRoute = await this.dynamicRoute();
    /**
     * 菜单信息,主要用于判断路由权限
     */
    let flatMenuData: Array<Menu> = [];
    if (dynamicRoute instanceof Array) {
      flatMenuData = flatMenu(JSON.parse(JSON.stringify(dynamicRoute)), [], "");
      const childrenRoute = flatMenuData
        .map((item) => {
          if (item.childOperations) {
            return {
              [item.name]: item.childOperations.map(this.menuToRouteItem),
            };
          }
          return {};
        })
        .reduce((pre, next) => {
          return { ...pre, ...next };
        }, {});
      dynamicRoute = {
        home: flatMenuData.map(this.menuToRouteItem),
        ...childrenRoute,
      };
    }
    // 判断是否有权限
    if (await this.routeHasRolePermission(flatMenuData, to.path)) {
      // 如果路由被重置则需要重制路由
      if (this.resetRoute(this.router, dynamicRoute as RouteItem)) {
        next({ ...to, replace: true });
      } else {
        next();
      }
    } else {
      // 没有权限 路由到没权限页面
      next({ name: "noPermission" });
    }
  };

  /**
   * 判断路由是否有权限
   * @param flatMenu 菜单
   * @param to       路由next
   * @returns        是否有角色或者权限
   */
  routeHasRolePermission = async (flatMenu: Array<Menu>, toPath: string) => {
    if (this.getPermissions) {
      const rolePermission: RolePermission = await this.getPermissions();
      const menu = flatMenu.find((menu) => {
        return menu.path === toPath;
      });
      if (menu) {
        return hasRolePermission(
          menu,
          rolePermission.role,
          rolePermission.permissions
        );
      } else {
        return true;
      }
    }
    return true;
  };

  /**
   * 菜单转换为路由对象
   * @param menu 菜单对象
   * @returns    路由对象
   */
  menuToRouteItem: (menu: Menu) => RouteRecordRaw = (menu: Menu) => {
    if (menu.redirect) {
      return { name: menu.name, path: menu.path, redirect: menu.redirect };
    } else {
      return {
        name: menu.name,
        path: menu.path,
        component: this.routeComponent[menu.componentPath],
      };
    }
  };

  /**
   * 默认的路由后置守卫
   */
  defaultAfterEach = (
    to: RouteLocationNormalized,
    from: RouteLocationNormalized,
    failure?: NavigationFailure | void
  ) => {
    console.log(to, from);
  };
  /**
   * 默认的错误处理器
   */
  defaultOnError = (
    error: any,
    to: RouteLocationNormalized,
    from: RouteLocationNormalizedLoaded
  ) => {
    console.log("defaultOnError", error, to, from);
  };
}
export default RouteObj;
export type { RouteItem };
