interface OptimizeSuggest {
  name: string;
  optimizeSuggestCode: string;
  value: number;
  index: number;
  [propName: string]: any;
}

/**
 * 修改策略请求参数
 */
interface OptimizeStrategyBaseReq {
  optimizeSuggestCode: string;
  /**
   * 资源使用率策略升降配参数
   */
  usedRateRequest?: any;
  /**
   * 资源状态策略参数
   */
  statusRequest?: any;
}

interface VmCloudServerVO {
  id: string;
  instanceUuid?: string;
  accountId?: string;
  instanceId?: string;
  instanceName?: string;
  instanceType?: string;
  instanceTypeDescription?: string;
  remoteIp?: string;
  ipArray?: [];
  platform?: string;
  accountName?: string;
  cpuMinimum?: number;
  cpuMaximum?: number;
  cpuAverage?: number;
  memoryMinimum?: number;
  memoryMaximum?: number;
  memoryAverage?: number;
  diskAverage?: number;
  optimizeSuggest?: string;
  optimizeSuggestCode?: string;
  content?: string;
}

interface PageOptimizeBaseRequest {
  pageSize: number;
  currentPage: number;
  [propName: string]: any;
}
interface OptimizeBaseRequest {
  [propName: string]: any;
}
export type {
  OptimizeSuggest,
  VmCloudServerVO,
  PageOptimizeBaseRequest,
  OptimizeBaseRequest,
  OptimizeStrategyBaseReq,
};
