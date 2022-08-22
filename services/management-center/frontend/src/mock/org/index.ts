import type { MockMethod } from "vite-plugin-mock";
import org from "./data";
import Result from "ce-base/commons/request/Result";
import { Page } from "ce-base/commons/request/Result";
import { Organization } from "@/api/organization/type";
const searchChildData = (
  rootDatas: Array<Organization>,
  rootData: Organization
) => {
  const find = org.find((item) => {
    return item.pid === rootData.id;
  });
  if (find) {
    rootDatas.push(find);
    searchChildData(rootDatas, find);
  }
};
export default [
  {
    url: "/api/list/org", // 注意，这里只能是string格式
    method: "get",
    response: ({ query }: any) => {
      console.log(query);
      const newArr = org
        .filter((item) => {
          return item.pid === null;
        })
        .splice(
          query.currentPage * query.pageSize,
          Number.parseInt(query.pageSize)
        );
      const childrenData: Array<Organization> = [];
      newArr.forEach((root) => {
        searchChildData(childrenData, root);
      });
      childrenData.forEach((children) => {
        newArr.push(children);
      });
      const pageData: Page<Organization> = {
        records: newArr,
        current: Number.parseInt(query.currentPage),
        size: Number.parseInt(query.pageSize),
        total: org.length,
        hasNext: true,
      };
      return Result.success(pageData);
    },
  },
] as MockMethod[];
