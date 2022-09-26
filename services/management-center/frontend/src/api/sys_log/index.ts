import { post, del, get, put } from "@commons/request";
import type Result from "@commons/request/Result";
import type { Page } from "@commons/request/Result";
import type { SystemLogVO, ListSystemLogRequest } from "./type";
import type { Ref } from "vue";

export const ListSystemLog: (
  req: ListSystemLogRequest,
  loading?: Ref<boolean>
) => Promise<Result<Page<SystemLogVO>>> = (req) => {
  return get("api/log/system/list", req);
};

export type { SystemLogVO };
