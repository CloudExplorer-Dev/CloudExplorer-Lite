import type { MockMethod } from "vite-plugin-mock";
import user from "./data";
import Result from "@commons/request/Result";
import type { Page } from "@commons/request/Result";
import type { User } from "@/api/user/type";

export default [
  {
    url: "/api/list/user",
    method: "post",
    response: ({ query }: any) => {
      const newArr = user.splice(
        query.currentPage * query.pageSize,
        Number.parseInt(query.pageSize)
      );
      const pageData: Page<User> = {
        records: newArr,
        current: Number.parseInt(query.currentPage),
        size: Number.parseInt(query.pageSize),
        total: user.length,
        hasNext: true,
      };
      return Result.success(pageData);
    },
  },
] as MockMethod[];
