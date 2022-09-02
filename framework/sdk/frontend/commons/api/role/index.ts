import type { Role } from "./type";
import { get } from "@commons/request";
import type Result from "@commons/request/Result";

/**
 * 获取当前的角色
 * @returns
 */
export const getCurrentRole = () => {
  const roleData: Promise<Result<Role>> = get("/api/currentRole");
  return roleData;
};

/**
 * 获取所有角色
 * @returns
 */
export const listAllRole = () => {
  const roleData: Promise<Result<Array<Role>>> = get("/api/role/list");
  return roleData;
};
