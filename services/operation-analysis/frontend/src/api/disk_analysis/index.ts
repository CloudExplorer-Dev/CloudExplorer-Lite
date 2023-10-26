import { exportExcel, get } from "@commons/request";
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
  req: Ref<ResourceAnalysisRequest | undefined>,
  loading?: Ref<boolean>
): Promise<Result<any>> {
  return get("api/disk_analysis/spread", req, loading);
}

export function getIncreaseTrend(
  req: Ref<ResourceAnalysisRequest | undefined>,
  loading?: Ref<boolean>
): Promise<Result<any>> {
  return get("api/disk_analysis/increase_trend", req, loading);
}
export function getAnalysisOrgDiskCount(
  req: Ref<ResourceAnalysisRequest | undefined>,
  loading?: Ref<boolean>
): Promise<Result<any>> {
  return get("api/disk_analysis/org_workspace_disk_count_bar", req, loading);
}
export function getAnalysisWorkspaceDiskCount(
  req: ResourceAnalysisRequest,
  loading?: Ref<boolean>
): Promise<Result<any>> {
  return get("api/disk_analysis/org_workspace_disk_count_bar", req, loading);
}

export function getAnalysisOrgWorkspaceDiskCount(
  req: Ref<ResourceAnalysisRequest | undefined>,
  loading?: Ref<boolean>
): Promise<Result<any>> {
  return get("api/disk_analysis/org_workspace_disk_count_bar", req, loading);
}
/**
 * 导出磁盘明细
 * @param req 请求参数
 * @param loading 加载器
 * @returns void
 */
const exportData = (req: any, loading: Ref<boolean>) => {
  return exportExcel(
    "磁盘明细",
    "/api/disk_analysis/disk/download",
    req,
    loading
  );
};

const ResourceSpreadViewApi = {
  listDisk,
  listAccounts,
  getSpreadData,
  getIncreaseTrend,
  getAnalysisOrgDiskCount,
  getAnalysisWorkspaceDiskCount,
  getAnalysisOrgWorkspaceDiskCount,
  exportData,
};

export default ResourceSpreadViewApi;
