import type { SimpleMap } from "@commons/api/base/type";

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
  deleteTime?: string;
  expiredTime?: string;
  platform?: string;
  vmToolsStatus?: string;
  accountName?: string;
  workspaceName?: string;
  organizationName?: string;
  showLoading?: boolean;
  securityGroupIds?: [];
  instanceChargeType?: string;
  applyUser: string;
}

/**
 *任务记录
 */
interface CloudServerJobRecord {
  /**
   *云主机id
   */
  resourceId: string;
  /**
   *任务记录id
   */
  jobRecordId: string;
  /**
   *任务记录类型
   */
  type: string;
  /**
   * 任务状态
   */
  status: string;
  /**
   * 任务描述
   */
  description: string;
  /**
   * 创建时间
   */
  createTime: string;
  /**
   * 更新时间
   */
  updateTime: string;
  /**
   * 结果
   */
  result: string;
  /**
   *任务参数
   */
  params: SimpleMap<Array<{ size: number; region: string } | any>>;
}

/**
 * 监控数据
 */
interface PerfMonitorData {
  resourceId: string;
  values: number[];
  timestamps: number[];
  metricName: string;
  unit: string;
}

/**
 * 查询监控参数
 */
interface GetPerfMonitorRequest {
  cloudAccountId: string;
  entityType: string;
  instanceId: string;
  metricName: string;
  startTime: number;
  endTime: number;
}

interface ListVmCloudServerRequest {
  pageSize: number;
  currentPage: number;
}

export interface CreateServerRequest {
  accountId: string;

  createRequest: string;

  fromInfo: string;
}

interface ChangeServerConfigRequest {
  id?: string;
  newInstanceType: string;
}

interface GrantRequest {
  ids: string[];
  sourceId: string;
  grant: boolean;
}

export enum IpType {
  IPv4,
  IPv6,
}

interface Ip {
  ip: string;
  type: IpType;
  isPublicIp: boolean;
}

export type {
  VmCloudServerVO,
  ListVmCloudServerRequest,
  CloudServerJobRecord,
  PerfMonitorData,
  GetPerfMonitorRequest,
  ChangeServerConfigRequest,
  GrantRequest,
  Ip,
};
