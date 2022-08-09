import { defineStore } from "pinia";
import packageJson from "@/../package.json";
import { listRuningModules, Module } from "../api/module";
import { getMenuByModule, Menu } from "../api/menu";
interface RouteItem {
  /**
   *路由名称
   */
  name: string;
  /**
   * 理由路径
   */
  path: string;
  /**
   * 组建路径
   */
  componentPath: string;
}

interface Route {
  [propName: string]: Array<RouteItem>;
}
/**
 * 扁平化菜单转换为路由
 * @param menus 菜单
 * @param newArr 空数组
 * @param prentPath 父path
 * @returns 扁平化后的数组
 */
const flatMenuToRoute = (
  menus: Array<Menu>,
  newArr: Array<RouteItem>,
  prentPath: string
) => {
  return flatMenu(menus, [], prentPath).map((menu) => {
    const newRoute: RouteItem = {
      path: menu.path,
      name: menu.name,
      componentPath: menu.componentPath,
    };
    return newRoute;
  });
};
/**
 * 扁平化菜单
 * @param menus
 * @param newMenus
 * @param prentPath
 * @returns
 */
const flatMenu = (
  menus: Array<Menu>,
  newMenus: Array<Menu>,
  prentPath: string
) => {
  menus.forEach((item) => {
    const newMenu: Menu = { ...item, path: prentPath + item.path };
    newMenus.push(newMenu);
    if (item.children != null && item.children.length > 0) {
      flatMenu(item.children, newMenus, newMenu.path);
    }
  });
  return newMenus;
};

const resetMenuPath = (menus: Array<Menu>, prentPath: string) => {
  menus.forEach((menu) => {
    menu.path = prentPath + menu.path;
    if (menu.children != null && menu.children.length > 0) {
      resetMenuPath(menu.children, menu.path);
    }
  });
  return menus;
};
export const moduleStore = defineStore({
  id: "module",
  state: () => ({
    /**
     *当前模块信息
     */
    currentModule: <Module | undefined>{},
    /**
     *运行的所有模块
     */
    runingModules: <Array<Module>>[],
    /**
     *当前模块菜单
     */
    currentMenus: <Array<Menu>>[],
  }),
  getters: {
    route(state: any) {
      if (!this.currentMenus || this.currentMenus.length === 0) {
        state.initModule();
        return [];
      } else {
        const route: Route = {
          home: flatMenuToRoute(this.currentMenus, [], ""),
        };
        return route;
      }
    },
    menus(state: any) {
      if (!this.currentMenus || this.currentMenus.length === 0) {
        state.initModule();
        return [];
      } else {
        const a = resetMenuPath(this.currentMenus, "");
        console.log(a);
        return a;
      }
    },
  },
  actions: {
    async initModule() {
      // 运行模块
      this.runingModules = (await listRuningModules()).data;
      // 当前模块
      this.currentModule = this.runingModules.find((m: Module) => {
        return packageJson.name === m.name;
      });
      // 获取当前模块菜单
      this.currentMenus = (await getMenuByModule()).data;
    },
    async getRoute() {
      if (!this.currentMenus || this.currentMenus.length === 0) {
        await this.initModule();
      }
      if (this.currentMenus) {
        const route: Route = {
          home: flatMenuToRoute(this.currentMenus, [], ""),
        };
        return route;
      }
      return [];
    },
    async getMenu() {
      if (!this.currentMenus || this.currentMenus.length === 0) {
        await this.initModule();
      }
      const a = flatMenu(this.currentMenus, [], "");
      console.log(a);
      return a;
    },
  },
});
