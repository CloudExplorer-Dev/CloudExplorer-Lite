import type { Role } from "./type";
import { get } from "@commons/request";
import type Result from "@commons/request/Result";
import type { Ref } from "vue";

/**
 * 获取所有角色
 * @returns
 */
export function listRoles(
  loading?: Ref<boolean>
): Promise<Result<Array<Role>>> {
  return get("/api/roles", null, loading);
}
