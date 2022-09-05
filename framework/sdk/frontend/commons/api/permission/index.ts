import { get } from "@commons/request";
import type { ModulePermission, Permission } from "./type";
import type Result from "@commons/request/Result";
import type { Ref } from "vue";

export const getCurrentUserPlainPermissions = (loading?: Ref<boolean>) => {
  const permissions: Promise<Result<Array<string>>> = get(
    "/api/permission/current"
  );
  return permissions;
};
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
