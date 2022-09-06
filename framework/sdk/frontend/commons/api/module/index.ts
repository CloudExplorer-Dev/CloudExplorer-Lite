import { get } from "@commons/request";
import type { Module } from "./type";
import type Result from "@commons/request/Result";
import type { Ref } from "vue";

export function listModules(
  loading?: Ref<boolean>
): Promise<Result<Array<Module>>> {
  return get("/api/modules", null, loading);
}
