import type { Ref } from "vue";
import type Result from "@commons/request/Result";
import { get } from "@commons/request";

export function getHistoryTrend(
  type: "MONTH" | "YEAR",
  historyNum: number,
  loading?: Ref<boolean>
): Promise<Result<Array<any>>> {
  return get(
    `/finance-management/api/bill_view/history_trend/${type}/${historyNum}`,
    {},
    loading
  );
}

export function getIncreaseTrend(
  req: any,
  loading?: Ref<boolean>
): Promise<Result<any>> {
  return get(
    "/operation-analytics/api/server_analysis/increase_trend",
    req,
    loading
  );
}

export function getSpreadData(
  req: any,
  loading?: Ref<boolean>
): Promise<Result<any>> {
  return get("/operation-analytics/api/server_analysis/spread", req, loading);
}

const api = {
  getHistoryTrend,
  getIncreaseTrend,
  getSpreadData,
};

export default api;
