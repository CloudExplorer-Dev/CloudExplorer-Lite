import { get } from "../../request";
import type { Menu, MenuRequest, RequiredPermissions } from "./type";
import Result from "../../request/Result";
import packageJson from "@/../package.json";
export const getMenuByModule = () => {
  const moduleRequest: MenuRequest = { moduleName: packageJson.name };
  const modules: Promise<Result<Array<Menu>>> = get("/api/menu", moduleRequest);
  return modules;
};
export type { Menu, RequiredPermissions };
