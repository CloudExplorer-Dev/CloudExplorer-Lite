import { get, post } from "@commons/request";
import type Result from "@commons/request/Result";
import type { Page } from "@commons/request/Result";
import type { Ref } from "vue";
import type {
  ListRecycleBinRequest,
  RecycleBinInfo,
} from "@/api/recycle_bin/type";

export function listRecycleBins(
  req: ListRecycleBinRequest,
  loading?: Ref<boolean>
): Promise<Result<Page<RecycleBinInfo>>> {
  return get("api/vm/recycleBin/page", req, loading);
}

export function getRecycleBinById(
  id: string,
  loading?: Ref<boolean>
): Promise<Result<RecycleBinInfo>> {
  return get("/api/vm/recycleBin/findOne", { id: id }, loading);
}

/**
 * 删除单个资源
 * @param deleteResource
 * @param loading
 */
export function deleteResource(
  recycleId: string,
  loading?: Ref<boolean>
): Promise<Result<boolean>> {
  return post(
    "/api/vm/recycleBin/deleteResource/" + recycleId,
    null,
    null,
    loading
  );
}

/**
 * 批量删除资源
 * @param batchDeleteResource
 * @param loading
 */
export function batchDeleteResource(
  recycleIds: Array<string>,
  loading?: Ref<boolean>
): Promise<Result<boolean>> {
  return post(
    "/api/vm/recycleBin/batchDeleteResource",
    null,
    { recycleIds: recycleIds },
    loading
  );
}

/**
 * 恢复单个资源
 * @param recoverResource
 * @param loading
 */
export function recoverResource(
  recycleId: string,
  loading?: Ref<boolean>
): Promise<Result<boolean>> {
  return post(
    "/api/vm/recycleBin/recoverResource/" + recycleId,
    null,
    null,
    loading
  );
}

/**
 * 批量恢复资源
 * @param recycleBinId
 * @param loading
 */
export function batchRecoverResource(
  recycleIds: Array<string>,
  loading?: Ref<boolean>
): Promise<Result<boolean>> {
  return post(
    "/api/vm/recycleBin/batchRecoverResource",
    null,
    { recycleIds: recycleIds },
    loading
  );
}

/**
 * 获取当前回收站的开启状态
 */
export function getRecycleEnableStatus(
  loading?: Ref<boolean>
): Promise<Result<boolean>> {
  return get("api/vm/recycleBin/getRecycleEnableStatus", null, loading);
}

const RecycleBinsApi = {
  listRecycleBins,
  getRecycleBinById,
  batchRecoverResource,
  recoverResource,
  batchDeleteResource,
  deleteResource,
  getRecycleEnableStatus,
};

export default RecycleBinsApi;
