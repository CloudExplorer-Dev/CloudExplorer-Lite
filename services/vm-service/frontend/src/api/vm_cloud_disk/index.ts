import { get, put, del, post } from "@commons/request";
import type Result from "@commons/request/Result";
import type { Page } from "@commons/request/Result";
import type {
  VmCloudDiskVO,
  ListVmCloudDiskRequest,
  AttachDiskRequest,
  EnlargeDiskRequest,
  BatchAttachDiskRequest,
  ListVmRequest,
} from "./type";
import type { Ref } from "vue";
import type { VmCloudServerVO } from "@/api/vm_cloud_server/type";
import type { GrantRequest } from "@/api/vm_cloud_server/type";

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
 * 根据云账号查询创建磁盘表单数据
 * @param req
 * @param loading
 */
export function getCreateDiskForm(
  accountId: string,
  loading?: Ref<boolean>
): Promise<Result<any>> {
  return get(`/api/disk/createDiskForm/${accountId}`, null, loading);
}

/**
 * 创建磁盘
 */
export function createDisk(
  dataInfo: any,
  loading?: Ref<boolean>
): Promise<Result<null>> {
  return post("api/disk/createDisk", null, dataInfo, loading);
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
  return put(`api/disk/detach/${id}`, null, null, loading);
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
  return del(`api/disk/delete/${id}`, null, null, loading);
}

/**
 * 将磁盘放入
 * @param id
 * @param loading
 */
export function recycleDisk(
  id: string,
  loading?: Ref<boolean>
): Promise<Result<boolean>> {
  return put(`api/disk/recycleDisk/${id}`, null, null, loading);
}

/**
 * 批量将磁盘放入
 * @param ids
 * @param loading
 */
export function batchRecycleDisks(
  ids: string[],
  loading?: Ref<boolean>
): Promise<Result<boolean>> {
  return put("api/disk/batchRecycleDisks", null, ids, loading);
}

/**
 * 批量挂载磁盘
 * @param attachDiskRequest
 * @param loading
 */
export function batchAttach(
  attachDiskRequest: BatchAttachDiskRequest,
  loading?: Ref<boolean>
): Promise<Result<boolean>> {
  return put("api/disk/batchAttach", null, attachDiskRequest, loading);
}

/**
 * 批量卸载磁盘
 * @param ids
 * @param loading
 */
export function batchDetach(
  ids: string[],
  loading?: Ref<boolean>
): Promise<Result<boolean>> {
  return put("api/disk/batchDetach", null, ids, loading);
}

/**
 * 批量删除磁盘
 * @param ids
 * @param loading
 */
export function batchDeleteDisk(
  ids: string[],
  loading?: Ref<boolean>
): Promise<Result<boolean>> {
  return del("api/disk/batchDelete", null, ids, loading);
}

/**
 * 查询可以挂载的云主机列表
 */
export function listVm(
  req: ListVmRequest,
  loading?: Ref<boolean>
): Promise<Result<VmCloudServerVO[]>> {
  return get("api/disk/listVm", req, loading);
}

/**
 * 根据ID获取磁盘信息
 */
export function showCloudDiskById(
  id: string,
  loading?: Ref<boolean>
): Promise<Result<VmCloudDiskVO>> {
  return get(`api/disk/showCloudDiskById/${id}`, null, loading);
}

/**
 * 云磁盘授权
 * @param req
 * @param loading
 */
export function grantVmCloudDisk(req: GrantRequest, loading?: Ref<boolean>) {
  return post("/api/disk/grant", null, req, loading);
}

const VmCloudDiskApi = {
  listVmCloudDisk,
  getCreateDiskForm,
  createDisk,
  enlarge,
  attach,
  detach,
  deleteDisk,
  listVm,
  showCloudDiskById,
  batchAttach,
  batchDetach,
  batchDeleteDisk,
  grantVmCloudDisk,
  batchRecycleDisks,
  recycleDisk,
};
export default VmCloudDiskApi;
