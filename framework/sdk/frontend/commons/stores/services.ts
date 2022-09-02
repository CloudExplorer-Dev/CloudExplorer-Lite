import type { Module } from "@commons/api/module";
import { listRuningModules } from "@commons/api/module";
import { defineStore } from "pinia";
import type { ModuleMenu } from "@commons/api/menu";
import { getRuningModuleMenu } from "@commons/api/menu";
import type { ModulePermission } from "@commons/api/permission";
import { getRuningPermissions } from "@commons/api/permission";
import type { RouteItem } from "@commons/router";
import { getCurrentRole } from "@commons/api/role";

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
            url: module.basePath + module.name,
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
