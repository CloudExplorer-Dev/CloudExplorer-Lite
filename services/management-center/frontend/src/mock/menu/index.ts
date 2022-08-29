import type { MockMethod } from "vite-plugin-mock";
import menus from "./data";
import { runingModulesMenu } from "./data";
import Result from "@commons/request/Result";
export default [
  {
    url: "/api/menu", // 注意，这里只能是string格式
    method: "get",
    response: () => {
      return Result.success(menus);
    },
  },
  {
    url: "/api/runingModulesMenu", // 注意，这里只能是string格式
    method: "get",
    response: () => {
      return Result.success(runingModulesMenu);
    },
  },
] as MockMethod[];
