import type { SimpleMap } from "@commons/api/base/type";
export class OptimizationRequest {
  days: number;
  cpuRate: number;
  /**
   * 是否是最大使用率，否则平均值
   */
  cpuMaxRate: boolean;
  memoryRate: number;
  memoryMaxRate: boolean;
  cycleContinuedRunning: boolean;
  cycleContinuedDays: number;
  volumeContinuedRunning: boolean;
  volumeContinuedDays: number;
  continuedRunning: boolean;
  continuedDays: number;

  accountId: string;
  instanceName: string;
  optimizeSuggest: string;

  conditionOr: string;
  constructor(
    days: number,
    cpuRate: number,
    cpuMaxRate: boolean,
    memoryRate: number,
    memoryMaxRate: boolean,
    cycleContinuedRunning: boolean,
    cycleContinuedDays: number,
    volumeContinuedRunning: boolean,
    volumeContinuedDays: number,
    continuedRunning: boolean,
    continuedDays: number,
    accountId: string,
    instanceName: string,
    optimizeSuggest: string,
    conditionOr: string
  ) {
    this.days = days;
    this.cpuRate = cpuRate;
    this.cpuMaxRate = cpuMaxRate;
    this.memoryRate = memoryRate;
    this.memoryMaxRate = memoryMaxRate;
    this.cycleContinuedRunning = cycleContinuedRunning;
    this.cycleContinuedDays = cycleContinuedDays;
    this.volumeContinuedRunning = volumeContinuedRunning;
    this.volumeContinuedDays = volumeContinuedDays;
    this.continuedRunning = continuedRunning;
    this.continuedDays = continuedDays;
    this.accountId = accountId;
    this.instanceName = instanceName;
    this.optimizeSuggest = optimizeSuggest;
    this.conditionOr = conditionOr;
  }
}

interface OptimizeSuggest {
  checked: boolean;
  color: string;
  name: string;
  code: string;
  value: number;
  data: object;
}

interface VmCloudServerVO {
  id: string;
  instanceUuid?: string;
  workspaceId?: string;
  projectId?: string;
  accountId?: string;
  instanceId?: string;
  instanceName?: string;
  imageId?: string;
  instanceStatus?: string;
  instanceType?: string;
  instanceTypeDescription?: string;
  region?: string;
  zone?: string;
  host?: string;
  remoteIp?: string;
  ipArray?: [];
  os?: string;
  osVersion?: string;
  cpu?: number;
  memory?: number;
  disk?: number;
  hostname?: string;
  managementIp?: string;
  managementPort?: number;
  osInfo?: string;
  remark?: string;
  network?: string;
  vpcId?: string;
  subnetId?: string;
  networkInterfaceId?: string;
  managementIpv6?: string;
  remoteIpv6?: string;
  localIpv6?: string;
  ipType?: string;
  snapShot?: number;
  createTime?: string;
  updateTime?: string;
  platform?: string;
  vmToolsStatus?: string;
  accountName?: string;
  workspaceName?: string;
  organizationName?: string;
  showLoading?: boolean;
  securityGroupIds?: [];
  instanceChargeType?: string;
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

interface ListOptimizationRequest {
  pageSize: number;
  currentPage: number;
}

export type { VmCloudServerVO, ListOptimizationRequest, OptimizeSuggest };
