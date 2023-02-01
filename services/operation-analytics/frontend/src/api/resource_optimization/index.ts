import { get } from "@commons/request";
import type Result from "@commons/request/Result";
import type { Page } from "@commons/request/Result";
import type {
  OptimizationRequest,
  ListOptimizationRequest,
  VmCloudServerVO,
} from "./type";
import type { Ref } from "vue";

export function listServer(
  req: ListOptimizationRequest | {},
  loading?: Ref<boolean>
): Promise<Result<Page<VmCloudServerVO>>> {
  return get("api/optimize_analysis/server/page", req, loading);
}
/**
 * 云账号
 * @param req
 * @param loading
 */
export function listAccounts(loading?: Ref<boolean>): Promise<Result<any>> {
  return get("api/server_analysis/cloud_accounts", loading);
}

const ResourceOptimizationViewApi = {
  listServer,
  listAccounts,
};

export default ResourceOptimizationViewApi;
