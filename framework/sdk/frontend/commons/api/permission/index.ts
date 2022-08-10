import { get } from "../../request";
import { Permission } from "./type";
import Result from "../../request/Result";
/**
 * 获取当前权限
 * @returns
 */
export const getPermission = () => {
  const permissions: Promise<Result<Array<Permission>>> =
    get("/api/permission");
  return permissions;
};
export type { Permission };
