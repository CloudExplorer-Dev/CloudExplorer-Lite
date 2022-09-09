import type { Role, RoleRequest, RolePageRequest } from "./type";
import { get } from "@commons/request";
import type { Ref } from "vue";
import type { Result, Page } from "@commons/request/Result";

export function listRoles(
  request?: RoleRequest,
  loading?: Ref<boolean>
): Promise<Result<Array<Role>>> {
  return get("/api/roles", request, loading);
}

export function pageRoles(
  request: RolePageRequest,
  loading?: Ref<boolean>
): Promise<Result<Page<Role>>> {
  return get("/api/role/pages", request, loading);
}

export function getRoleById(
  id: string,
  loading?: Ref<boolean>
): Promise<Result<Role>> {
  return get("/api/role", { id: id }, loading);
}

const BaseRoleApi = {
  listRoles,
  pageRoles,
  getRoleById,
};

export default BaseRoleApi;
