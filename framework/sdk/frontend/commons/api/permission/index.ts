import { get } from "../../request";
import { Permission, ModulePermission } from "./type";
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
/**
 *
 * @returns
 */
export const getRuningPermissions: () => Promise<
  Result<ModulePermission>
> = () => {
  return get("/api/runingPermission");
};
export type { Permission, ModulePermission };
