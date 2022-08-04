import { createRouter } from "vue-router";
import type {
  Router,
  RouterHistory,
  RouteRecordRaw,
  RouteLocationNormalized,
  RouteLocationNormalizedLoaded,
  NavigationGuardNext,
  NavigationFailure,
} from "vue-router";
import Layout from "../business/app-layout/index.vue";
import Login from "../business/login/index.vue";
import type { App } from "vue";
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
  [propName: string]: Array<RouteRecordRaw>;
}

interface RouteComponents {
  /**
   *如果key是rootRoute,则是添加root级别路由,如果是其他,如home 则是给home路由添加子路由
   */
  [propName: string]: any;
}
const rootRouteName = "rootRoute";

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
  moveRouteItem: () => RouteItem | Promise<RouteItem>;
  install: (app: App) => void;
  routeComponent: RouteComponents;
  constructor(
    history: RouterHistory,
    routeComponent: RouteComponents,
    baseRoute?: Array<RouteRecordRaw>,
    moveRouteItem?: () => RouteItem | Promise<RouteItem>,
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
    this.moveRouteItem = moveRouteItem
      ? moveRouteItem
      : this.defaultMoveRouteItem;
    this.router = createRouter({ history, routes: [] });
    this.initBaseRoute(
      this.router,
      baseRoute ? baseRoute : this.defaultBaseRoute(),
      beforeEach ? beforeEach : this.defaultBeforeEach,
      afterEach ? afterEach : this.defaultAfterEach,
      onError ? onError : this.defaultOnError
    );
    this.install = this.router.install;
    console.log(routeComponent);
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

  defaultMoveRouteItem: () => RouteItem | any | Promise<RouteItem | any> =
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
    const routeItem = await this.moveRouteItem();
    // 如果路由被重置则需要重制路由
    if (this.resetRoute(this.router, routeItem)) {
      next({ ...to, replace: true });
    } else {
      next();
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
    console.log("defaultAfterEach", to, from, failure);
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
