import { exportExcel, get, post } from "@commons/request";
import type Result from "@commons/request/Result";
import type { Page } from "@commons/request/Result";
import type {
  CreateRequest,
  UpdateRequest,
  ListOptimizationStrategyRequest,
  OptimizationStrategy,
  UpdateStatusRequest,
  ResourceTypeDTO,
  VmCloudServerVO,
  PageOptimizationStrategyResourceRequest,
  OptimizationStrategyIgnoreResourceRequest,
} from "./type";
import type { Ref } from "vue";

export function pageOptimizationStrategy(
  req: ListOptimizationStrategyRequest,
  loading?: Ref<boolean>
): Promise<Result<Page<OptimizationStrategy>>> {
  return get("api/optimization_strategy/page", req, loading);
}

export function listOptimizationStrategy(
  resourceType: string,
  loading?: Ref<boolean>
): Promise<Result<Array<OptimizationStrategy>>> {
  return get(
    (import.meta.env.VITE_APP_NAME === "operation-analysis"
      ? ""
      : "/operation-analysis/") +
      `api/optimization_strategy/${resourceType}/list`,
    undefined,
    loading
  );
}

export const create = (req: CreateRequest, loading?: Ref<boolean>) => {
  return post("api/optimization_strategy/add", "", req, loading);
};

export const update = (
  req: UpdateRequest | CreateRequest,
  loading?: Ref<boolean>
) => {
  return post("api/optimization_strategy/update", "", req, loading);
};

export const getOptimizationStrategy = (id: string, loading?: Ref<boolean>) => {
  return get(`api/optimization_strategy/${id}`, undefined, loading);
};

export function getResourceTypeList(
  loading?: Ref<boolean>
): Promise<Result<Array<ResourceTypeDTO>>> {
  return get(
    (import.meta.env.VITE_APP_NAME === "operation-analysis"
      ? ""
      : "/operation-analysis/") +
      `api/optimization_strategy/resource_type/list`,
    undefined,
    loading
  );
}

/**
 * 根据优化策略查询云主机列表
 * @param req
 * @param loading
 */
export function pageOptimizationStrategyServerResourceList(
  req: PageOptimizationStrategyResourceRequest,
  loading?: Ref<boolean>
): Promise<Result<Page<VmCloudServerVO>>> {
  return get(
    (import.meta.env.VITE_APP_NAME === "operation-analysis"
      ? ""
      : "/operation-analysis/") +
      "api/optimization_strategy_resource/server/page",
    req,
    loading
  );
}

/**
 * 导出云主机优化数据
 * @param fileName 文件名称
 * @param req 查询参数
 * @param loading 表格loading
 */
export function exportServerData(
  fileName: string,
  req: PageOptimizationStrategyResourceRequest,
  loading?: Ref<boolean>
) {
  exportExcel(
    fileName,
    "api/optimization_strategy_resource/server/download/" + req.version,
    req,
    loading
  );
}

/**
 * 获取云主机数据
 * @param req
 * @param loading
 */
export function getServerResourceList(
  req: any,
  loading?: Ref<boolean>
): Promise<Result<Array<VmCloudServerVO>>> {
  return get("api/optimization_strategy/server/list", req, loading);
}

/**
 * 启停
 */
export const changeStatus = (
  req: UpdateStatusRequest,
  loading?: Ref<boolean>
) => {
  return post("api/optimization_strategy/changeStatus", "", req, loading);
};

export function cancelIgnore(
  optimizationStrategyId: string,
  req: OptimizationStrategyIgnoreResourceRequest,
  loading?: Ref<boolean>
): Promise<Result<boolean>> {
  return post(`api/optimization_strategy/cancel_ignore`, null, req, loading);
}

export function addIgnore(
  optimizationStrategyId: string,
  req: OptimizationStrategyIgnoreResourceRequest,
  loading?: Ref<boolean>
): Promise<Result<boolean>> {
  return post("api/optimization_strategy/add_ignore", null, req, loading);
}

const OptimizationStrategyViewApi = {
  listOptimizationStrategy,
  create,
  update,
  changeStatus,
  getOptimizationStrategy,
  getResourceTypeList,
  getServerResourceList,
  pageOptimizationStrategyServerResourceList,
  pageOptimizationStrategy,
  cancelIgnore,
  addIgnore,
  exportServerData,
};

export default OptimizationStrategyViewApi;
