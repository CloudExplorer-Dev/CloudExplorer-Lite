import type { Role } from "@commons/api/role/type";
import type { CreateRoleRequest, UpdateRoleRequest } from "./type";
import BaseRoleApi from "@commons/api/role";
import { get, put, post, del } from "@commons/request";
import type { Ref } from "vue";
import type { Result } from "@commons/request/Result";
import type { ModulePermission } from "./type";
import type { SimpleMap } from "@commons/api/base/type";

export function addRole(
  role: CreateRoleRequest,
  loading?: Ref<boolean>
): Promise<Result<Role>> {
  return post("/api/role", undefined, role, loading);
}

export function updateRole(
  role: UpdateRoleRequest,
  loading?: Ref<boolean>
): Promise<Result<Role>> {
  return put("/api/role", undefined, role, loading);
}

export function deleteRole(
  id: string,
  loading?: Ref<boolean>
): Promise<Result<boolean>> {
  return del("/api/role", { id: id }, undefined, loading);
}

export function batchDeleteRoles(
  ids: Array<string>,
  loading?: Ref<boolean>
): Promise<Result<boolean>> {
  return del("/api/role/batch", null, ids, loading);
}

export function getModulePermissions(
  role: string,
  loading?: Ref<boolean>
): Promise<Result<SimpleMap<ModulePermission>>> {
  return get("/api/role/module-permission", { role: role }, loading);
}

export function getRolePermissions(
  roleId: string,
  loading?: Ref<boolean>
): Promise<Result<any>> {
  return get("/api/role/permission", { id: roleId }, loading);
}

export function updateRolePermissions(
  roleId: string,
  permissionIds: Array<string>,
  loading?: Ref<boolean>
): Promise<Result<any>> {
  return post("/api/role/permission", { id: roleId }, permissionIds, loading);
}

const RoleApi = {
  ...BaseRoleApi,
  addRole,
  updateRole,
  deleteRole,
  batchDeleteRoles,
  getModulePermissions,
  getRolePermissions,
  updateRolePermissions,
};

export default RoleApi;
