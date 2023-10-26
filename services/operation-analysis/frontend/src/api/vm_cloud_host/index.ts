import { exportExcel, get } from "@commons/request";
import type Result from "@commons/request/Result";
import type { Page } from "@commons/request/Result";
import type { VmCloudHostVO, ListVmCloudHostRequest } from "./type";
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
  return get("api/base_resource_analysis/host/page", req, loading);
}
/**
 * 导出宿主机列表数据
 * @param req 请求对象
 * @param loading 加载器
 * @returns void
 */
const exportVmCloudHost = (req: any, loading?: Ref<boolean>) => {
  return exportExcel(
    "宿主机明细列表",
    "api/base_resource_analysis/host/download",
    req,
    loading
  );
};

const VmCloudHostApi = {
  listVmCloudHost,
  exportVmCloudHost,
};

export default VmCloudHostApi;
