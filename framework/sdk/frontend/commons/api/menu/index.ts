import { get } from "../../request";
import type {
  Menu,
  MenuRequest,
  ModuleMenu,
  RequiredPermissions,
} from "./type";
import type { Result } from "../../request/Result";
import type { Ref } from "vue";
import type { SimpleMap } from "@commons/api/base/type";

export const listMenus = (loading?: Ref<boolean>) => {
  const modules: Promise<Result<SimpleMap<Array<Menu>>>> = get(
    "/api/menus",
    {},
    loading
  );
  return modules;
};

/**
 * 获取当前模块的菜单
 * @returns
 */
export const getCurrentMenus: () => Promise<Result<Array<Menu>>> = () => {
  const moduleRequest: MenuRequest = {
    moduleName: import.meta.env.VITE_APP_NAME, //todo 这里其实不需要，url中basePath有名字了
  };
  return get("/api/menu", moduleRequest);
};

/**
 * 获取正在运行的所有模块
 */
export const getRuningModuleMenu: () => Promise<Result<ModuleMenu>> = () => {
  return get("/api/runingModulesMenu");
};

export type { Menu, RequiredPermissions, ModuleMenu };
