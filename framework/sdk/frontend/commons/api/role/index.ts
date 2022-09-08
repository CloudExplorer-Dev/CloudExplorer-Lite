import type { Role } from "./type";
import { get } from "@commons/request";
import type { Ref } from "vue";
import type { Result, Page } from "@commons/request/Result";
import type { RoleRequest, RolePageRequest } from "./type";

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

const BaseRoleApi = {
  listRoles,
  pageRoles,
};

export default BaseRoleApi;
