import type { Ref } from "vue";
import type Result from "@commons/request/Result";
import { get } from "@commons/request";
import type { SimpleMap } from "@commons/api/base/type";

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
    "/operation-analysis/api/server_analysis/increase_trend",
    req,
    loading
  );
}

export function getSpreadData(
  req: any,
  loading?: Ref<boolean>
): Promise<Result<any>> {
  return get("/operation-analysis/api/server_analysis/spread", req, loading);
}

export function listVmCloudServer(
  req: any,
  loading?: Ref<boolean>
): Promise<Result<Array<any>>> {
  return get("/vm-service/api/server/list", req, loading);
}

export function listPerfMetricMonitor(
  req: any,
  loading?: Ref<boolean>
): Promise<Result<SimpleMap<Array<any>>>> {
  return get("/vm-service/api/base/monitor/list", req, loading);
}

export function getComplianceViewResourceCount(
  req: any,
  loading?: Ref<boolean>
): Promise<Result<SimpleMap<any>>> {
  return get(
    "/security-compliance/api/compliance_view/resource/count",
    req,
    loading
  );
}
export function getComplianceViewRuleCount(
  req: any,
  loading?: Ref<boolean>
): Promise<Result<SimpleMap<any>>> {
  return get(
    "/security-compliance/api/compliance_view/rule/count",
    req,
    loading
  );
}

const api = {
  getHistoryTrend,
  getIncreaseTrend,
  getSpreadData,
  listVmCloudServer,
  listPerfMetricMonitor,
  getComplianceViewResourceCount,
  getComplianceViewRuleCount,
};

export default api;
