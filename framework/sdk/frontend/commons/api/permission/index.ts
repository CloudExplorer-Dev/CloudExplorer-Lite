import { get } from "@commons/request";
import type Result from "@commons/request/Result";
import type { Ref } from "vue";

export function getCurrentUserPlainPermissions(
  loading?: Ref<boolean>
): Promise<Result<Array<string>>> {
  return get("/api/permission/current", null, loading);
}
