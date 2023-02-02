import { get } from "@commons/request";
import type Result from "@commons/request/Result";
import BaseResourceOptimizationViewApi from "@commons/api/resource_optimization";
import type { Ref } from "vue";

/**
 * 云账号
 * @param req
 * @param loading
 */
export function listAccounts(loading?: Ref<boolean>): Promise<Result<any>> {
  return get("api/server_analysis/cloud_accounts", loading);
}

const ResourceOptimizationViewApi = {
  ...BaseResourceOptimizationViewApi,
  listAccounts,
};

export default ResourceOptimizationViewApi;
