import { get, post } from "@commons/request";
import type Result from "@commons/request/Result";
import type { Page } from "@commons/request/Result";
import type { OperatedLogVO, ListOperatedLogRequest } from "./type";
import type { Ref } from "vue";

export function listOperatedLog(
  req: ListOperatedLogRequest,
  loading?: Ref<boolean>
): Promise<Result<Page<OperatedLogVO>>> {
  return get("api/log/operated/list", req, loading);
}
export function getLogClearConfig(
  req: any,
  loading?: Ref<boolean>
): Promise<Result<string>> {
  return get("api/log/keep/months", req, loading);
}

export function saveLogClearConfig(
  req: any,
  loading?: Ref<boolean>
): Promise<Result<null>> {
  return post("api/log/keep/months", undefined, req, loading);
}
export function batchSaveLogClearConfig(
  req: any,
  loading?: Ref<boolean>
): Promise<Result<null>> {
  return post("api/log/keep/months/batch", undefined, req, loading);
}

const OperatedLogApi = {
  listOperatedLog,
  getLogClearConfig,
  saveLogClearConfig,
  batchSaveLogClearConfig,
};

export default OperatedLogApi;
