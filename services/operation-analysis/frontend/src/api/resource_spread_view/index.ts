import { get } from "@commons/request";
import type Result from "@commons/request/Result";
import type { ResourceAnalysisRequest } from "@commons/api/resource_spread_view/type";
import type { CloudAccount } from "@commons/api/cloud_account/type";
import type { VmCloudHostVO } from "@/api/vm_cloud_host/type";
import type { VmCloudDatastoreVO } from "@/api/vm_cloud_datastore/type";
import type { Ref } from "vue";

/**
 * 私有云账号
 * @param req
 * @param loading
 */
export function listAccounts(loading?: Ref<boolean>): Promise<Result<any>> {
  return get("api/base_resource_analysis/private_cloud_accounts", loading);
}

/**
 * 私有云账号
 */
export function listPrivateAccounts(
  loading?: Ref<boolean>
): Promise<Result<Array<CloudAccount>>> {
  return get("api/base_resource_analysis/private_cloud_accounts", loading);
}

export function listClusters(
  req: Ref<ResourceAnalysisRequest | undefined>,
  loading?: Ref<boolean>
): Promise<Result<any>> {
  return get("api/base_resource_analysis/clusters", req, loading);
}

export function listHost(
  req: Ref<ResourceAnalysisRequest | undefined>,
  loading?: Ref<boolean>
): Promise<Result<Array<VmCloudHostVO>>> {
  return get("api/base_resource_analysis/hosts", req, loading);
}

export function listDatastores(
  req: Ref<ResourceAnalysisRequest | undefined>,
  loading?: Ref<boolean>
): Promise<Result<Array<VmCloudDatastoreVO>>> {
  return get("api/base_resource_analysis/datastores", req, loading);
}

export function getAllocatedInfo(
  req: Ref<ResourceAnalysisRequest | undefined>,
  loading?: Ref<boolean>
): Promise<Result<any>> {
  return get("api/base_resource_analysis/allocated_info", req, loading);
}

export function getSpreadInfo(
  req: Ref<ResourceAnalysisRequest | undefined>,
  loading?: Ref<boolean>
): Promise<Result<any>> {
  return get("api/base_resource_analysis/spread_info", req, loading);
}

export function getResourceTrendData(
  req: Ref<ResourceAnalysisRequest | undefined>,
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
  listPrivateAccounts,
};

export default ResourceSpreadViewApi;
