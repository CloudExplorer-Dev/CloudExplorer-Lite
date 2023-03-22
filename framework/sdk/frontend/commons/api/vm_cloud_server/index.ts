import { get } from "@commons/request";
import type { Result } from "@commons/request/Result";
import type { Ref } from "vue";

function getCountsGroupByStatus(loading?: Ref<boolean>): Promise<Result<any>> {
  return get(
    (import.meta.env.VITE_APP_NAME === "vm-service" ? "" : "/vm-service/") +
      `api/server/status/count`,
    {},
    loading
  );
}

export default { getCountsGroupByStatus };
