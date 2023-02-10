import { get } from "@commons/request";
import type Result from "@commons/request/Result";
import type { Page } from "@commons/request/Result";
import type {
  ResourceAnalysisRequest,
  ListVmCloudDiskRequest,
  VmCloudDiskVO,
} from "./type";
import type { Ref } from "vue";

export function listDisk(
  req: ListVmCloudDiskRequest,
  loading?: Ref<boolean>
): Promise<Result<Page<VmCloudDiskVO>>> {
  return get("api/disk_analysis/disk/page", req, loading);
}
/**
 * 云账号
 * @param req
 * @param loading
 */
export function listAccounts(loading?: Ref<boolean>): Promise<Result<any>> {
  return get("api/disk_analysis/cloud_accounts", loading);
}

export function getSpreadData(
  req: ResourceAnalysisRequest,
  loading?: Ref<boolean>
): Promise<Result<any>> {
  return get("api/disk_analysis/spread", req, loading);
}

export function getIncreaseTrend(
  req: ResourceAnalysisRequest,
  loading?: Ref<boolean>
): Promise<Result<any>> {
  return get("api/disk_analysis/increase_trend", req, loading);
}
export function getAnalyticsOrgDiskCount(
  req: ResourceAnalysisRequest,
  loading?: Ref<boolean>
): Promise<Result<any>> {
  return get("api/disk_analysis/org_workspace_disk_count_bar", req, loading);
}
export function getAnalyticsWorkspaceDiskCount(
  req: ResourceAnalysisRequest,
  loading?: Ref<boolean>
): Promise<Result<any>> {
  return get("api/disk_analysis/org_workspace_disk_count_bar", req, loading);
}

const ResourceSpreadViewApi = {
  listDisk,
  listAccounts,
  getSpreadData,
  getIncreaseTrend,
  getAnalyticsOrgDiskCount,
  getAnalyticsWorkspaceDiskCount,
};

export default ResourceSpreadViewApi;
