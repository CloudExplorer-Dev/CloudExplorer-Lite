import { defineStore } from "pinia";
import { useUserStore } from "./user";
import type { Module } from "@commons/api/module/type";
import { listModules } from "@commons/api/module";
import type { Menu } from "@commons/api/menu/type";
import { listMenus } from "@commons/api/menu";
import type { SimpleMap } from "@commons/api/base/type";
import type { RouteModule } from "@commons/api/module/type";
import type { UnwrapRef } from "vue";
import _ from "lodash";

interface ModuleStoreObject {
  modules?: Array<Module>;
  menus: SimpleMap<Array<Menu>>;
}

export const useModuleStore = defineStore({
  id: "module",
  state: (): ModuleStoreObject => ({
    modules: undefined,
    menus: <SimpleMap<Array<Menu>>>{},
  }),
  getters: {
    runningModules(state: any): Array<Module> {
      return _.map(state.modules, (module: Module) => {
        return {
          ...module,
          requiredPermissions: _.flatMap(
            _.get(state.menus, module.id, []),
            (menu: Menu) => {
              return menu.requiredPermissions;
            }
          ),
        };
      });
      //return state.modules;
    },
    runningMenus(state: any): SimpleMap<Array<Menu>> {
      return state.menus;
    },
    routeModules(state: any): Array<RouteModule> {
      return _.map(state.runningModules, (module: UnwrapRef<Module>) => {
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
          requiredPermissions: module.requiredPermissions,
        };
        return routeModule;
      });
    },
    currentModule(state: any): Module | undefined {
      return _.find(
        state.modules,
        (module: Module) => module.id === import.meta.env.VITE_APP_NAME
      );
    },
    currentModuleMenu(state: any): Array<Menu> {
      return _.get(state.menus, import.meta.env.VITE_APP_NAME, []);
    },
  },
  actions: {
    async refreshModules() {
      const userStore = useUserStore();
      await userStore.getCurrentUser();

      if (!userStore.isLogin) {
        return;
      }
      if (this.modules) {
        console.log("ok");
        return;
      } else {
        console.log("init");
      }
      const modules = (await listModules()).data;
      if (modules) {
        this.modules = modules;
      }
      await this.refreshMenus();
    },
    async refreshMenus() {
      const userStore = useUserStore();
      if (!userStore.isLogin) {
        return;
      }
      const menus = (await listMenus()).data;
      if (menus) {
        this.menus = menus;
      }
    },
  },
});
