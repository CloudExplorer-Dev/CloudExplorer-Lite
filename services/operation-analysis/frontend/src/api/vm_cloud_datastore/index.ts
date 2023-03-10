import { get } from "@commons/request";
import type Result from "@commons/request/Result";
import type { Page } from "@commons/request/Result";
import type { VmCloudDatastoreVO, ListVmCloudDatastoreRequest } from "./type";
import type { Ref } from "vue";

/**
 * 存储器列表
 * @param req
 * @param loading
 */
export function listVmCloudDatastore(
  req: ListVmCloudDatastoreRequest,
  loading?: Ref<boolean>
): Promise<Result<Page<VmCloudDatastoreVO>>> {
  return get("api/base_resource_analysis/datastore/page", req, loading);
}

const VmCloudDatastoreApi = {
  listVmCloudDatastore,
};

export default VmCloudDatastoreApi;
