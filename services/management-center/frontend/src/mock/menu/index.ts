import type { MockMethod } from "vite-plugin-mock";
import menus from "./data";
import Result from "ce-base/commons/request/Result";
export default [
  {
    url: "/api/menu", // 注意，这里只能是string格式
    method: "get",
    response: ({ query }: any) => {
      const menusType: any = menus;
      return Result.success(menusType[query.moduleName]);
    },
  },
] as MockMethod[];
