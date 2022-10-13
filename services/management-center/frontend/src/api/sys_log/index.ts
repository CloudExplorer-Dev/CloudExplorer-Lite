import { get } from "@commons/request";
import type Result from "@commons/request/Result";
import type { Page } from "@commons/request/Result";
import type { SystemLogVO, ListSystemLogRequest } from "./type";
import type { Ref } from "vue";

export function listSystemLog(
  req: ListSystemLogRequest,
  loading?: Ref<boolean>
): Promise<Result<Page<SystemLogVO>>> {
  return get("api/log/system/list", req,loading);
};

const SysLogApi = {
  listSystemLog
}

export default SysLogApi;
