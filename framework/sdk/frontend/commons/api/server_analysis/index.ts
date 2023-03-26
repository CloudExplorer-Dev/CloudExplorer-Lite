import { get } from "@commons/request";
import type Result from "@commons/request/Result";
import type { ResourceAnalysisRequest } from "@commons/api/server_analysis/type";
import type { Ref } from "vue";

function getIncreaseTrend(
  req: ResourceAnalysisRequest,
  loading?: Ref<boolean>
): Promise<Result<any>> {
  return get(
    (import.meta.env.VITE_APP_NAME === "operation-analysis"
      ? ""
      : "/operation-analysis/") + "api/server_analysis/increase_trend",
    req,
    loading
  );
}

const API = {
  getIncreaseTrend,
};

export default API;
