import { get } from "@commons/request";
import type Result from "@commons/request/Result";
import type { Page } from "@commons/request/Result";
import type {
  ResourceAnalysisRequest,
  ListVmCloudServerRequest,
  VmCloudServerVO,
} from "./type";
import type { Ref } from "vue";

export function listServer(
  req: ListVmCloudServerRequest | {},
  loading?: Ref<boolean>
): Promise<Result<Page<VmCloudServerVO>>> {
  return get("api/server_analysis/server/page", req, loading);
}
/**
 * 云账号
 * @param req
 * @param loading
 */
export function listAccounts(loading?: Ref<boolean>): Promise<Result<any>> {
  return get("api/server_analysis/cloud_accounts", loading);
}

export function listHost(
  req: ResourceAnalysisRequest | {},
  loading?: Ref<boolean>
): Promise<Result<any>> {
  return get("api/server_analysis/hosts", req, loading);
}

export function getSpreadData(
  req: ResourceAnalysisRequest | {},
  loading?: Ref<boolean>
): Promise<Result<any>> {
  return get("api/server_analysis/spread", req, loading);
}

export function getIncreaseTrend(
  req: ResourceAnalysisRequest | {},
  loading?: Ref<boolean>
): Promise<Result<any>> {
  return get("api/server_analysis/increase_trend", req, loading);
}
export function getResourceTrendData(
  req: ResourceAnalysisRequest | {},
  loading?: Ref<boolean>
): Promise<Result<any>> {
  return get("api/server_analysis/resource_used_trend", req, loading);
}
export function getAnalyticsOrgWorkspaceVmCount(
  req: ResourceAnalysisRequest | {},
  loading?: Ref<boolean>
): Promise<Result<any>> {
  return get("api/server_analysis/org_workspace_vm_count_bar", req, loading);
}

const ResourceSpreadViewApi = {
  listServer,
  listAccounts,
  listHost,
  getSpreadData,
  getIncreaseTrend,
  getResourceTrendData,
  getAnalyticsOrgWorkspaceVmCount,
};

export default ResourceSpreadViewApi;
