import type { Ref } from "vue";
import type Result from "@commons/request/Result";
import { get } from "@commons/request";

export function findHelpLink(loading?: Ref<boolean>): Promise<Result<string>> {
  return get("/api/system_parameter/findHelpLink", {}, loading);
}
