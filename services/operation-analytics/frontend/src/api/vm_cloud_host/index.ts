import { get } from "@commons/request";
import type Result from "@commons/request/Result";
import type { Page } from "@commons/request/Result";
import type {
  VmCloudHostVO,
  ListVmCloudHostRequest
} from "./type";
import type { Ref } from "vue";

/**
 * 宿主机列表
 * @param req
 * @param loading
 */
export function listVmCloudHost(
  req: ListVmCloudHostRequest,
  loading?: Ref<boolean>
): Promise<Result<Page<VmCloudHostVO>>> {
  return get("api/baseResourceAnalysis/host/page", req, loading);
}


const VmCloudHostApi = {
  listVmCloudHost
};

export default VmCloudHostApi;
