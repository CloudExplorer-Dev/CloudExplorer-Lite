import { get } from "@commons/request";
import type Result from "@commons/request/Result";
import type { ResourceAnalysisRequest } from "@commons/api/resource_spread_view/type";
import type { Ref } from "vue";

/**
 * 私有云账号
 * @param req
 * @param loading
 */
export function listAccounts(loading?: Ref<boolean>): Promise<Result<any>> {
  return get("api/base_resource_analysis/private_cloud_accounts", loading);
}

export function listClusters(
  req: ResourceAnalysisRequest | {},
  loading?: Ref<boolean>
): Promise<Result<any>> {
  return get("api/base_resource_analysis/clusters", req, loading);
}

export function listHost(
  req: ResourceAnalysisRequest | {},
  loading?: Ref<boolean>
): Promise<Result<any>> {
  return get("api/base_resource_analysis/hosts", req, loading);
}

export function listDatastores(
  req: ResourceAnalysisRequest | {},
  loading?: Ref<boolean>
): Promise<Result<any>> {
  return get("api/base_resource_analysis/datastores", req, loading);
}

export function getAllocatedInfo(
  req: ResourceAnalysisRequest | {},
  loading?: Ref<boolean>
): Promise<Result<any>> {
  return get("api/base_resource_analysis/allocated_info", req, loading);
}

export function getSpreadInfo(
  req: ResourceAnalysisRequest | {},
  loading?: Ref<boolean>
): Promise<Result<any>> {
  return get("api/base_resource_analysis/spread_info", req, loading);
}

export function getResourceTrendData(
  req: ResourceAnalysisRequest | {},
  loading?: Ref<boolean>
): Promise<Result<any>> {
  return get("api/base_resource_analysis/resource_trend", req, loading);
}

const ResourceSpreadViewApi = {
  listAccounts,
  listClusters,
  listHost,
  listDatastores,
  getAllocatedInfo,
  getSpreadInfo,
  getResourceTrendData,
};

export default ResourceSpreadViewApi;
