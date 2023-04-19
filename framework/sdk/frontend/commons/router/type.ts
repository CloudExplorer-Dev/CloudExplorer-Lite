import { useModuleStore } from "@commons/stores/modules/module";
import type { RouteRecordRaw } from "vue-router";
import { createRouter } from "vue-router";
import type {
  NavigationFailure,
  NavigationGuardNext,
  RouteLocationNormalized,
  RouteLocationNormalizedLoaded,
  Router,
  RouterHistory,
} from "vue-router";
import type { Menu, RequiredPermissions } from "@commons/api/menu/type";
import type { RolePermission } from "@commons/api/permission/type";
import type { App } from "vue";
import Layout from "@commons/business/app-layout/index.vue";
import NoPermissions from "@commons/business/error-page/NoPermissions.vue";
import Login from "@commons/business/login/index.vue";
import { getToken } from "@commons/utils/authStorage";
import { useUserStore } from "@commons/stores/modules/user";
import { useHomeStore } from "@commons/stores/modules/home";
import { store } from "@commons/stores";
import { usePermissionStore } from "@commons/stores/modules/permission";
import { hasRolePermission } from "@commons/base-directives/hasPermission";
import { flatMenu } from "@commons/router/index";
import _ from "lodash";

const rootRouteName = "rootRoute";

export interface RouteItem {
  /**
   *如果key是rootRoute,则是添加root级别路由,如果是其他,如home 则是给home路由添加子路由
   */
  [propName: string]: Array<RouteRecordRaw | any>;
}

export interface RouteComponents {
  /**
   *如果key是rootRoute,则是添加root级别路由,如果是其他,如home 则是给home路由添加子路由
   */
  [propName: string]: any;
}

export class RouteObj {
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

  setRouteComponent = (routeComponent: RouteComponents) => {
    this.routeComponent = routeComponent;
  };

  /**
   * 初始化基本路由
   * @param router
   * @param baseRoute
   * @param beforeEach
   * @param afterEach
   * @param onError
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
    this.resetRoute(router, { [rootRouteName]: baseRoute } as RouteItem);
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
   * @param routeItem     路由对象
   * @returns             是否重写了路由
   */
  resetRoute = (router: Router, routeItem: RouteItem) => {
    let isResetRoute = false;
    const routeItemKeys = Object.keys(routeItem);
    // 排序
    routeItemKeys.sort((key1: string) => {
      if (key1 === "home") {
        return -1;
      }
      if (key1 === rootRouteName) {
        return -1;
      }
      return 1;
    });
    // 添加路由
    routeItemKeys.forEach((key: string) => {
      const routeRecordRaws: Array<RouteRecordRaw | any> = routeItem[key];
      routeRecordRaws.forEach((routeRecordRaw) => {
        //console.log(routeRecordRaw);
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
            component: NoPermissions,
          },
        ],
      },
      {
        path: "/signin",
        name: "signin",
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
    console.debug(import.meta.env.VITE_APP_NAME, "to", to);

    if (!getToken()) {
      if (to.name !== "signin") {
        next({ name: "signin" });
      } else {
        next();
      }
      return;
    }
    if (import.meta.env.VITE_APP_NAME === "base" && to.name === "home") {
      const m = useModuleStore();
      m.updateCurrentModuleName("home");
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
      flatMenuData = flatMenu(dynamicRoute, []);
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
    //console.log(to.meta.requiredPermissions);
    const requiredPermissions: Array<RequiredPermissions> =
      to.meta.requiredPermissions instanceof Array<RequiredPermissions>
        ? to.meta.requiredPermissions
        : [];
    if (to.name === "home" && import.meta.env.VITE_APP_NAME !== "base") {
      // 路由到第一个有权限的菜单
      for (const index in dynamicRoute.home) {
        if (
          (dynamicRoute.home[index].component ||
            dynamicRoute.home[index].redirect) &&
          (await this.routeHasRolePermission(
            dynamicRoute.home[index].meta.requiredPermissions
          ))
        ) {
          next({ path: dynamicRoute.home[index].path });
          return;
        }
      }
    }
    const homeStore = useHomeStore(store);
    if (to.name === "home" && import.meta.env.VITE_APP_NAME === "base") {
      homeStore.setShow(true);
    } else {
      homeStore.setShow(false);
    }
    // 判断是否有权限
    if (await this.routeHasRolePermission(requiredPermissions)) {
      // 如果路由被重置则需要重制路由
      if (this.resetRoute(this.router, dynamicRoute as RouteItem)) {
        next({ ...to, replace: true });
      } else {
        next();
      }
    } else {
      // 没有权限 路由到没权限页面
      console.debug(import.meta.env.VITE_APP_NAME, "noPermission", to);
      if (window.__MICRO_APP_ENVIRONMENT__) {
        //发消息给基座，跳转首页
        console.debug("noPermission, jump to home");
        (window.microApp.router.getBaseAppRouter() as Router)?.push("/");
      } else {
        next({ name: "noPermission" });
      }
    }
  };

  /**
   * 判断路由是否有权限
   * @param requiredPermissions
   * @returns        是否有角色或者权限
   */
  routeHasRolePermission = async (
    requiredPermissions: Array<RequiredPermissions>
  ) => {
    const userStore = useUserStore(store);
    const permissionStore = usePermissionStore(store);
    if (!userStore.isLogin || !userStore.currentUser) {
      await userStore.getCurrentUser();
    }
    const hasPermission = hasRolePermission(
      userStore.currentRole,
      permissionStore.userPermissions,
      requiredPermissions
    );

    return hasPermission;
  };

  /**
   * 菜单转换为路由对象
   * @param menu 菜单对象
   * @returns    路由对象
   */
  menuToRouteItem: (menu: Menu) => RouteRecordRaw = (menu: Menu) => {
    if (menu.redirect) {
      return {
        name: menu.name,
        path: menu.path,
        component: this.routeComponent[menu.componentPath],
        redirect: menu.redirect,
        meta: {
          requiredPermissions: menu.requiredPermissions,
          title: menu.title,
          sourceMenu: menu.sourceMenu,
          saveRecent: menu.saveRecent,
        },
      };
    } else {
      return menu.sourceMenu
        ? {
            name: menu.name,
            path: menu.path,
            component: this.routeComponent[menu.componentPath],
            meta: {
              requiredPermissions: menu.requiredPermissions,
              title: menu.title,
              sourceMenu: menu.sourceMenu,
              saveRecent: menu.saveRecent,
            },
            props: true,
          }
        : {
            name: menu.name,
            path: menu.path,
            component: this.routeComponent[menu.componentPath],
            meta: {
              requiredPermissions: menu.requiredPermissions,
              title: menu.title,
              saveRecent: menu.saveRecent,
            },
            props: true,
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
    console.debug("defaultAfterEach", import.meta.env.VITE_APP_NAME, to);
    console.debug(
      "routeAfterEach failure",
      import.meta.env.VITE_APP_NAME,
      failure
    );

    if (
      import.meta.env.VITE_APP_NAME !== "base" &&
      to.name !== "signin" &&
      to.name !== "home" &&
      to.meta.saveRecent &&
      to.name
    ) {
      const menu: RecentAccessRoute = {
        module: import.meta.env.VITE_APP_NAME,
        name: to.name,
        fullPath: to.fullPath,
        time: new Date().getTime(),
      };
      const userStore = useUserStore(store);
      if (!userStore.isLogin || !userStore.currentUser) {
        userStore.getCurrentUser();
        return;
      }
      const key = "RecentAccess-" + userStore.currentUser.id;
      const str = localStorage.getItem(key);
      const recentAccessRoutes: Array<RecentAccessRoute> = str
        ? JSON.parse(str)
        : [];

      //去除原数组中同名route
      _.remove(recentAccessRoutes, (r) => r.name === menu.name);

      //限制长度为20
      const result = _.slice(recentAccessRoutes, 0, 20);

      result.unshift(menu);
      //保存到 local storage
      localStorage.setItem(key, JSON.stringify(result));
    }
  };
  /**
   * 默认的错误处理器
   */
  defaultOnError = (
    error: any,
    to: RouteLocationNormalized,
    from: RouteLocationNormalizedLoaded
  ) => {
    console.warn("defaultOnError", error, to, from);
  };
}

export interface RecentAccessRoute {
  module: string;
  name: string | symbol;
  fullPath: string;
  time: number;
}
