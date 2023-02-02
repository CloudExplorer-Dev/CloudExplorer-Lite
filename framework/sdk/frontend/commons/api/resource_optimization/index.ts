import { get } from "@commons/request";
import type Result from "@commons/request/Result";
import type { Page } from "@commons/request/Result";
import type { ListOptimizationRequest, VmCloudServerVO } from "./type";
import type { Ref } from "vue";

export function listServer(
  req: ListOptimizationRequest | Record<string, never>,
  loading?: Ref<boolean>
): Promise<Result<Page<VmCloudServerVO>>> {
  return get(
    (import.meta.env.VITE_APP_NAME === "operation-analytics"
      ? ""
      : "/operation-analytics/") + "api/optimize_analysis/server/page",
    req,
    loading
  );
}

const ResourceOptimizationViewApi = {
  listServer,
};

export default ResourceOptimizationViewApi;
