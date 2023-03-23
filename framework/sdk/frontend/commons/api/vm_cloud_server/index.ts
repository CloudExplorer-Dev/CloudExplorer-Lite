import { get } from "@commons/request";
import type { Result } from "@commons/request/Result";
import type { Ref } from "vue";

export interface StatusCount {
  count: number;
  status: string;
}

function getCountsGroupByStatus(
  loading?: Ref<boolean>
): Promise<Result<Array<StatusCount>>> {
  return get(
    (import.meta.env.VITE_APP_NAME === "vm-service" ? "" : "/vm-service/") +
      `api/server/status/count`,
    {},
    loading
  );
}

export default { getCountsGroupByStatus };
