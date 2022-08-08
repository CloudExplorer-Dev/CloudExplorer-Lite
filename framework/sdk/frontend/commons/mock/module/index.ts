import type { MockMethod } from "vite-plugin-mock";
import modules from "./data";
import Result from "../../request/Result";
export default [
  {
    url: "/api/list/modules", // 注意，这里只能是string格式
    method: "get",
    response: ({ query }: any) => {
      return Result.success(modules);
    },
  },
  {
    url: "/api/getCurrentModule",
    method: "get",
    response: ({ query }: any) => {
      return Result.success(
        modules.find((m) => {
          return m.name === "management-center";
        })
      );
    },
  },
] as MockMethod[];
