import { get } from "../../request";
import type { Menu, MenuRequest } from "./type";
import Result from "../../request/Result";
export const getMenuByModule = (module: string) => {
  const moduleRequest: MenuRequest = { moduleName: module };
  const modules: Promise<Result<Array<Menu>>> = get("/api/menu", moduleRequest);
  return modules;
};
