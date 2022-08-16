import { listRuningModules, Module } from "../api/module";
import { defineStore } from "pinia";
import { ModuleMenu, getRuningModuleMenu } from "../api/menu";
import { ModulePermission, getRuningPermissions } from "../api/permission";
import { RouteItem } from "../router";
import { getCurrentRole } from "../api/role";
interface RouteModule {
  /**
   *路由名称
   */
  name: string;
  /**
   *路由path
   */
  path: string;
  /**
   * 组建path
   */
  componentPath: string;
  props: {
    /**
     * 模块名称
     */
    moduleName: string;
    /**
     * 模块名称
     */
    name: string;
    /**
     * 模块地址
     */
    url: string;
    /**
     * 模块路由地址
     */
    baseRoute: string;
  };
}

export const servicesStore = defineStore({
  id: "services",
  state: () => ({
    /**
     * 运行的所有模块
     */
    runingModules: <Array<Module>>[],
    /**
     * 运行中的所有模块的菜单
     */
    runingModuleMenus: <ModuleMenu>{},
    /**
     * 运行中的所有权限
     */
    runingModulePermissions: <ModulePermission>{},
  }),
  getters: {},
  actions: {
    async init() {
      await getCurrentRole();
      this.runingModules = (await listRuningModules()).data;
      this.runingModuleMenus = (await getRuningModuleMenu()).data;
      this.runingModulePermissions = (await getRuningPermissions()).data;
    },
    async getRuningModule() {
      return (await listRuningModules()).data;
    },
    getRouters() {
      const routers = this.runingModules.map((module) => {
        const routeModule: RouteModule = {
          path: "/" + module.name,
          name: module.name,
          componentPath: "/src/views/MicroAppRouteView/index.vue",
          props: {
            moduleName: module.name,
            name: module.name,
            url: module.url + module.name,
            baseRoute: "/",
          },
        };
        return routeModule;
      });
      const route: RouteItem = { home: routers };
      return route;
    },
  },
});
