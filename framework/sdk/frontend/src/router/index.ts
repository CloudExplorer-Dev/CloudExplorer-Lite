import { Route } from "@commons/index";
import { createWebHistory } from "vue-router";
import baseLayout from "@commons/business/base-layout/index.vue";
import Login from "@commons/business/login/index.vue";
import { useModuleStore } from "@commons/stores/modules/module";
import { useUserStore } from "@commons/stores/modules/user";
import { store } from "@commons/stores";

import type { RouteItem } from "@commons/router";
import type { Module, RouteModule } from "@commons/api/module/type";
import type { UnwrapRef } from "vue";

const route = new Route(
  createWebHistory(),
  import.meta.glob("@/views/*/*.vue"),
  [
    {
      path: "/",
      name: "home",
      component: baseLayout,
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
export default route;
