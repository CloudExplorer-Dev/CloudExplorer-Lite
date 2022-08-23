import { defineStore } from "pinia";
import { getCurrentModule, Module } from "../api/module";
import { getCurrentMenus, Menu, RequiredPermissions } from "../api/menu";
import { getPermission } from "../api/permission";
import { getCurrentRole } from "../api/role";
import type { Role } from "../api/role";
import type { Permission } from "../api/permission";

/**
 * 重写菜单Path
 * @param menus     菜单
 * @param prentPath 父菜单path
 * @returns         将子菜单path与父亲菜单拼接
 */
const resetMenuPath = (menus: Array<Menu>, prentPath: string) => {
  menus.forEach((menu) => {
    menu.path = prentPath + menu.path;
    if (menu.children != null && menu.children.length > 0) {
      resetMenuPath(menu.children, menu.path);
    }
    if (menu.operations != null && menu.operations.length > 0) {
      resetMenuPath(menu.operations, menu.path);
    }
    if (menu.childOperations != null && menu.childOperations.length > 0) {
      resetMenuPath(menu.childOperations, menu.path);
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
     *当前模块菜单
     */
    currentMenus: <Array<Menu>>[],
    /**
     *当前角色
     */
    currentRole: <Role>{},
    /**
     *权限
     */
    permissions: <Array<Permission>>[],
    /**
     *是否初始化过状态
     */
    initState: <boolean>false,
  }),
  getters: {
    modules(state: any) {
      if (!this.initModule) {
        state.initModule();
        return [];
      }
      return state.runingModules;
    },
    menus(state: any) {
      if (!this.currentMenus || this.currentMenus.length === 0) {
        state.initModule();
        return [];
      } else {
        return this.currentMenus;
      }
    },
  },
  actions: {
    async initModule() {
      // 获取当前角色
      this.currentRole = (await getCurrentRole()).data;
      // 运行模块
      this.currentModule = (await getCurrentModule()).data;
      // 权限
      this.permissions = (await getPermission()).data;
      // 获取当前模块菜单
      this.currentMenus = resetMenuPath((await getCurrentMenus()).data, "");
      // 根据权限过滤菜单
      this.initState = true;
    },
    /**
     * 获取菜单
     * @returns
     */
    async getMenu() {
      if (!this.initState) {
        await this.initModule();
      }
      return this.currentMenus;
    },
    async getCurrentRole() {
      if (!this.initState) {
        this.currentRole = (await getCurrentRole()).data;
      }
      return this.currentRole;
    },
    async getPermission() {
      if (!this.initState) {
        await this.initModule();
      }
      return this.permissions;
    },
  },
});
