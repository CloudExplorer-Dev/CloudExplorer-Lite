import {get} from "../../request";
import type {Menu, MenuRequest, ModuleMenu, RequiredPermissions,} from "./type";
import type {Result} from "../../request/Result";
import packageJson from "@/../package.json";

/**
 * 获取当前模块的菜单
 * @returns
 */
export const getCurrentMenus: () => Promise<Result<Array<Menu>>> = () => {
  const moduleRequest: MenuRequest = { moduleName: packageJson.name };
  return get("/api/menu", moduleRequest);
};

/**
 * 获取正在运行的所有模块
 */
export const getRuningModuleMenu: () => Promise<Result<ModuleMenu>> = () => {
  return get("/api/runingModulesMenu");
};

export type { Menu, RequiredPermissions, ModuleMenu };
