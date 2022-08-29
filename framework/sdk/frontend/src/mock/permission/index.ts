import type { MockMethod } from "vite-plugin-mock";
import {
  rolePermissions,
  permissions,
  runingPermissions,
  runingRolePermission,
} from "./data";
import Result from "@commons/request/Result";
import { authMockHander } from "@commons/mock/utils/mock";
const getPermission = (moduleName: string, roleId: string) => {
  const runingRolePermissionType: any = runingRolePermission;
  const runingPermissionsType: any = runingPermissions;
  const permissionsData = runingRolePermissionType[moduleName]
    .filter((item: any) => {
      return item.roleId === roleId;
    })
    .map((item: any) => {
      return runingPermissionsType[moduleName].find((permission: any) => {
        return item.permissionId === permission.id;
      });
    });
  return permissionsData;
};
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
      const runingPermissionData = Object.keys(runingPermissions)
        .map((moduleName) => {
          return { [moduleName]: getPermission(moduleName, currentRoleObj.id) };
        })
        .reduce((pre, next) => {
          return { ...pre, ...next };
        }, {});
      return Result.success(runingPermissionData);
    },
  },
] as MockMethod[];
