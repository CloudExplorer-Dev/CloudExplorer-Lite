import { get, post } from "@commons/request";
import type Result from "@commons/request/Result";
import type { Page } from "@commons/request/Result";
import type {
  OptimizeStrategyBaseReq,
  PageOptimizeBaseRequest,
  VmCloudServerVO,
} from "@commons/api/optimize/type";
import type { Ref } from "vue";
import type { OptimizeSuggest } from "@commons/api/optimize/type";
/**
 * 云主机优化列表
 * @param req
 * @param loading
 */
export function listOptimizeServer(
  req: PageOptimizeBaseRequest,
  loading?: Ref<boolean>
): Promise<Result<Page<VmCloudServerVO>>> {
  return get(
    (import.meta.env.VITE_APP_NAME === "operation-analysis"
      ? ""
      : "/operation-analysis/") + "api/optimize/server/page",
    req,
    loading
  );
}

/**
 * 优化建议
 * @param req
 * @param loading
 */
export function getOptimizeSuggestList(
  loading?: Ref<boolean>
): Promise<Result<Array<OptimizeSuggest>>> {
  return get(
    (import.meta.env.VITE_APP_NAME === "operation-analysis"
      ? ""
      : "/operation-analysis/") + "api/optimize/server/optimize_suggest",
    loading
  );
}

/**
 * 优化建议
 * @param req
 * @param loading
 */
export function getOptimizeStrategy(
  req: string,
  loading?: Ref<boolean>
): Promise<Result<any>> {
  return get(
    (import.meta.env.VITE_APP_NAME === "operation-analysis"
      ? ""
      : "/operation-analysis/") +
      "api/optimize/server/strategy/" +
      req,
    loading
  );
}

/**
 * 修改优化策略
 * @param req
 * @param loading
 */
export function modifyOptimizeStrategy(
  req: OptimizeStrategyBaseReq,
  loading?: Ref<boolean>
): Promise<Result<any>> {
  return post(
    (import.meta.env.VITE_APP_NAME === "operation-analysis"
      ? ""
      : "/operation-analysis/") + "api/optimize/server/strategy/modify/",
    null,
    req,
    loading
  );
}

const OptimizeViewApi = {
  listOptimizeServer,
  getOptimizeSuggestList,
  getOptimizeStrategy,
  modifyOptimizeStrategy,
};

export default OptimizeViewApi;
