import { defineStore } from "pinia";
import { useUserStore } from "./user";
import type { Module } from "@commons/api/module/type";
import { listModules } from "@commons/api/module";
import type { Menu } from "@commons/api/menu/type";
import { listMenus } from "@commons/api/menu";
import type { SimpleMap } from "@commons/api/base/type";

interface ModuleStoreObject {
  modules: Array<Module>;
  menus: SimpleMap<Array<Menu>>;
}

export const useModuleStore = defineStore({
  id: "module",
  state: (): ModuleStoreObject => ({
    modules: [],
    menus: <SimpleMap<Array<Menu>>>{},
  }),
  getters: {
    runningModules(state: any): Array<Module> {
      return state.modules;
    },
    runningMenus(state: any): SimpleMap<Array<Menu>> {
      return state.menus;
    },
    currentModule(state: any): Module | undefined {
      return state.modules.find(
        (module: Module) => module.id === import.meta.env.VITE_APP_NAME
      );
    },
    currentModuleMenu(state: any): Array<Menu> {
      return state.menus[import.meta.env.VITE_APP_NAME]
        ? state.menus[import.meta.env.VITE_APP_NAME]
        : [];
    },
  },
  actions: {
    async refreshModules() {
      const userStore = useUserStore();
      await userStore.getCurrentUser();

      if (!userStore.isLogin) {
        return;
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
