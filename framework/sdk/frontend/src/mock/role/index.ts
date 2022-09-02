import type { MockMethod } from "vite-plugin-mock";
import { userRoles, roles } from "./data";
import Result from "@commons/request/Result";
import { authMockHander } from "@commons/mock/utils/mock";
/**
 *获取当前用户的所有角色
 * @param username 用户名
 * @returns        当前用户的角色
 */
const getCurrentUserRole = (username: string) => {
  const currentUserRole = userRoles
    .filter((userRole) => {
      return userRole.username === username;
    })
    .map((userRole) => {
      const role = roles.find((role) => {
        return role.id === userRole.roleId;
      });
      return role;
    });
  return currentUserRole;
};
export default [
  {
    // 获得当前权限
    url: "/api/currentRole",
    method: "get",
    response: ({ headers }: any) => {
      const result = authMockHander(headers);
      if (typeof result !== "string") {
        return result;
      }
      const tokenObj = JSON.parse(result);
      // 获取当前用户的角色
      const currentUserRole = getCurrentUserRole(tokenObj.username);
      // 后端存储一份当前的角色
      localStorage.setItem("currentRole", JSON.stringify(currentUserRole[0]));
      // 默认是呀第一个当作当前角色
      return Result.success(currentUserRole[0]);
    },
  },
  {
    // 获得当前权限
    url: "/api/listRoles",
    method: "get",
    response: ({ headers }: any) => {
      const result = authMockHander(headers);
      if (typeof result !== "string") {
        return result;
      }
      const tokenObj = JSON.parse(result);
      // 获取当前用户的角色
      const currentUserRole = getCurrentUserRole(tokenObj.username);
      // 默认是呀第一个当作当前角色
      return Result.success(currentUserRole);
    },
  },
] as MockMethod[];
