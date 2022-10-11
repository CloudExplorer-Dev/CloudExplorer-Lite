import { get } from "@commons/request";
import type Result from "@commons/request/Result";
import type { Page } from "@commons/request/Result";
import type { VmCloudImageVO, ListVmCloudImageRequest } from "./type";
import type { Ref } from "vue";

/**
 * 镜像列表
 * @param req
 * @param loading
 */
export function listVmCloudImage(
  req: ListVmCloudImageRequest,
  loading?: Ref<boolean>
): Promise<Result<Page<VmCloudImageVO>>> {
  return get("api/image/page", req, loading);
}
const VmCloudImageApi = {
  listVmCloudImage,
};
export default VmCloudImageApi;
