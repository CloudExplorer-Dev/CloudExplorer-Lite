import { exportExcel, get } from "@commons/request";
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
/**
 * 导出存储器列表明细
 * @param request 请求对象
 * @param loading 加载器
 * @returns void
 */
const exportData = (request: any, loading?: Ref<boolean>) => {
  return exportExcel(
    "存储器明细列表",
    "api/base_resource_analysis/datastore/download",
    request,
    loading
  );
};

const VmCloudDatastoreApi = {
  listVmCloudDatastore,
  exportData,
};

export default VmCloudDatastoreApi;
