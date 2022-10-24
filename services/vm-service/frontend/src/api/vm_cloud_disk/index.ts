import { get, put, del } from "@commons/request";
import type Result from "@commons/request/Result";
import type { Page } from "@commons/request/Result";
import type {
  VmCloudDiskVO,
  ListVmCloudDiskRequest,
  AttachDiskRequest,
  EnlargeDiskRequest,
} from "./type";
import type { Ref } from "vue";
import type { VmCloudServerVO } from "@/api/vm_cloud_server/type";

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

/**
 * 扩容磁盘
 * @param enlargeDiskRequest
 * @param loading
 */
export function enlarge(
  enlargeDiskRequest: EnlargeDiskRequest,
  loading?: Ref<boolean>
): Promise<Result<boolean>> {
  return put("api/disk/enlarge", null, enlargeDiskRequest, loading);
}

/**
 * 挂载磁盘
 * @param id
 * @param instanceUuid
 * @param loading
 */
export function attach(
  attachDiskRequest: AttachDiskRequest,
  loading?: Ref<boolean>
): Promise<Result<boolean>> {
  return put("api/disk/attach", null, attachDiskRequest, loading);
}

/**
 * 卸载磁盘
 * @param id
 * @param instanceUuid
 * @param loading
 */
export function detach(
  id: string,
  loading?: Ref<boolean>
): Promise<Result<boolean>> {
  return put("api/disk/detach/" + id, null, null, loading);
}

/**
 * 删除磁盘
 * @param id
 * @param instanceUuid
 * @param loading
 */
export function deleteDisk(
  id: string,
  loading?: Ref<boolean>
): Promise<Result<boolean>> {
  return del("api/disk/delete/" + id, null, null, loading);
}

/**
 * 根据云账号获取虚拟机列表
 */
export function listVmByAccountId(
  accountId: string,
  loading?: Ref<boolean>
): Promise<Result<VmCloudServerVO[]>> {
  return get("api/disk/listVmByAccountId/" + accountId, null, loading);
}

/**
 * 根据ID获取磁盘信息
 */
export function showCloudDiskById(
  id: string,
  loading?: Ref<boolean>
): Promise<Result<VmCloudDiskVO>> {
  return get("api/disk/showCloudDiskById/" + id, null, loading);
}

const VmCloudDiskApi = {
  listVmCloudDisk,
  enlarge,
  attach,
  detach,
  deleteDisk,
  listVmByAccountId,
  showCloudDiskById,
};
export default VmCloudDiskApi;
