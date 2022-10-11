import { get } from "@commons/request";
import type Result from "@commons/request/Result";
import type { Page } from "@commons/request/Result";
import type { OperatedLogVO, ListOperatedLogRequest } from "./type";
import type { Ref } from "vue";

export function listOperatedLog(
  req: ListOperatedLogRequest,
  loading?: Ref<boolean>
): Promise<Result<Page<OperatedLogVO>>> {
  return get("api/log/operated/list", req,loading);
};

const OperatedLogApi = {
  listOperatedLog
}

export default OperatedLogApi;
