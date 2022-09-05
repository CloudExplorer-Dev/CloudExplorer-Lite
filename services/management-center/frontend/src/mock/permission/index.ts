import type { MockMethod } from "vite-plugin-mock";
import { rolePermissions, permissions } from "./data";
import Result from "@commons/request/Result";
import { authMockHander } from "@commons/mock/utils/mock";
export default [
  {
    url: "/api/permission", // 注意，这里只能是string格式
    method: "get",
    response: ({ headers }: any) => {
      const result = authMockHander(headers);
      if (typeof result !== "string") {
        return result;
      }
      const currentRole = localStorage.getItem("currentRole");
      if (!currentRole) {
        return Result.error("角色不存在", 1002);
      }
      const currentRoleObj = JSON.parse(currentRole);
      const permissionsData = rolePermissions
        .filter((item) => {
          return item.roleId === currentRoleObj.id;
        })
        .map((item) => {
          return permissions.find((permission) => {
            return item.permissionId === permission.id;
          });
        });
      return Result.success(permissionsData);
    },
  },
  {
    url: "/api/runingPermission", // 注意，这里只能是string格式
    method: "get",
    response: () => {
      return Result.success({});
    },
  },
] as MockMethod[];
