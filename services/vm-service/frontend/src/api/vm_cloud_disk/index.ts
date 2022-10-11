import { get } from "@commons/request";
import type Result from "@commons/request/Result";
import type { Page } from "@commons/request/Result";
import type { VmCloudDiskVO, ListVmCloudDiskRequest } from "./type";
import type { Ref } from "vue";

/**
 * 磁盘列表
 * @param req
 * @param loading
 */
export function listVmCloudDisk(
  req: ListVmCloudDiskRequest,
  loading?: Ref<boolean>
): Promise<Result<Page<VmCloudDiskVO>>> {
  return get("api/disk/page", req, loading);
}

const VmCloudDiskApi = {
  listVmCloudDisk,
};
export default VmCloudDiskApi;
