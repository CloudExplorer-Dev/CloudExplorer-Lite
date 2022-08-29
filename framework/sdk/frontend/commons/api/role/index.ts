import type { Role } from "./type";
import { get } from "@commons/request";
import type Result from "@commons/request/Result";

/**
 * 获取当前的角色
 * @returns
 */
export const getCurrentRole = () => {
  const roleData: Promise<Result<Role>> = get("/api/currentUser");
  return roleData;
};

/**
 * 获取用户下所有角色
 * @returns
 */
export const listRole = () => {
  const roleData: Promise<Result<Array<Role>>> = get("/api/listRoles");
  return roleData;
};
export type { Role };
