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
  req: ListVmCloudDiskRequest | {},
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
  req: ResourceAnalysisRequest | {},
  loading?: Ref<boolean>
): Promise<Result<any>> {
  return get("api/disk_analysis/spread", req, loading);
}

export function getIncreaseTrend(
  req: ResourceAnalysisRequest | {},
  loading?: Ref<boolean>
): Promise<Result<any>> {
  return get("api/disk_analysis/increase_trend", req, loading);
}

const ResourceSpreadViewApi = {
  listDisk,
  listAccounts,
  getSpreadData,
  getIncreaseTrend,
};

export default ResourceSpreadViewApi;
